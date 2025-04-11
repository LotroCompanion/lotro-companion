package delta.games.lotro.gui.friends;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.Sort;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.account.status.friends.Friend;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.toon.ToonsTableColumnIds;

/**
 * Controller for a table that friends.
 * @author DAM
 */
public class FriendsTableController
{
  // Data
  private TypedProperties _prefs;
  private List<Friend> _friends;
  // GUI
  private JTable _table;
  private GenericTableController<Friend> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Member filter.
   */
  public FriendsTableController(TypedProperties prefs, Filter<Friend> filter)
  {
    _prefs=prefs;
    _friends=new ArrayList<Friend>();
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<Friend> buildTable()
  {
    ListDataProvider<Friend> provider=new ListDataProvider<Friend>(_friends);
    GenericTableController<Friend> table=new GenericTableController<Friend>(provider);

    List<TableColumnController<Friend,?>> columns=FriendColumnsBuilder.build();
    for(TableColumnController<Friend,?> column : columns)
    {
      table.addColumnController(column);
    }
    String sort=Sort.SORT_ASCENDING+ToonsTableColumnIds.NAME;
    table.setSort(Sort.buildFromString(sort));

    TableColumnsManager<Friend> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=null;
    if (_prefs!=null)
    {
      columnIds=_prefs.getStringList(ItemChooser.COLUMNS_PROPERTY);
    }
    if (columnIds==null)
    {
      columnIds=new ArrayList<String>();
      columnIds.add(ToonsTableColumnIds.NAME.name());
      columnIds.add(ToonsTableColumnIds.LEVEL.name());
      columnIds.add(ToonsTableColumnIds.CLASS.name());
      columnIds.add(FriendsColumnIds.AREA.name());
      columnIds.add(FriendsColumnIds.VOCATION.name());
      columnIds.add(FriendsColumnIds.LAST_LOGOUT_DATE.name());
      columnIds.add(FriendsColumnIds.NOTES.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<Friend> getTableController()
  {
    return _tableController;
  }

  /**
   * Set the members to show.
   * @param members List of members to show.
   */
  public void setFriends(List<Friend> members)
  {
    _friends.clear();
    _friends.addAll(members);
    _tableController.refresh();
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    if (_table==null)
    {
      _table=_tableController.getTable();
    }
    return _table;
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _tableController.filterUpdated();
  }

  /**
   * Get the total number of members.
   * @return A number of members.
   */
  public int getNbItems()
  {
    return _friends.size();
  }

  /**
   * Get the number of filtered items in the managed table.
   * @return A number of items.
   */
  public int getNbFilteredItems()
  {
    int ret=_tableController.getNbFilteredItems();
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Preferences
    if (_prefs!=null)
    {
      List<String> columnIds=_tableController.getColumnsManager().getSelectedColumnsIds();
      _prefs.setStringList(ItemChooser.COLUMNS_PROPERTY,columnIds);
      _prefs=null;
    }
    // GUI
    _table=null;
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _friends=null;
  }
}
