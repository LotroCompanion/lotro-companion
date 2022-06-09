package delta.games.lotro.gui.kinship;

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
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.toon.ToonsTableColumnIds;
import delta.games.lotro.kinship.Kinship;
import delta.games.lotro.kinship.KinshipMember;
import delta.games.lotro.kinship.events.KinshipEvent;
import delta.games.lotro.kinship.events.KinshipEventType;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a table that kinship members.
 * @author DAM
 */
public class KinshipMembersTableController implements GenericEventsListener<KinshipEvent>
{
  // Data
  private TypedProperties _prefs;
  private List<KinshipMember> _members;
  // GUI
  private JTable _table;
  private GenericTableController<KinshipMember> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Member filter.
   */
  public KinshipMembersTableController(TypedProperties prefs, Filter<KinshipMember> filter)
  {
    _prefs=prefs;
    _members=new ArrayList<KinshipMember>();
    _tableController=buildTable();
    _tableController.setFilter(filter);
    EventsManager.addListener(KinshipEvent.class,this);
  }

  private GenericTableController<KinshipMember> buildTable()
  {
    ListDataProvider<KinshipMember> provider=new ListDataProvider<KinshipMember>(_members);
    GenericTableController<KinshipMember> table=new GenericTableController<KinshipMember>(provider);

    List<TableColumnController<KinshipMember,?>> columns=KinshipMemberColumnsBuilder.build();
    for(TableColumnController<KinshipMember,?> column:columns)
    {
      // Skip some tables
      String id=column.getId();
      if ((ToonsTableColumnIds.SERVER.name().equals(id)) || (ToonsTableColumnIds.ACCOUNT.name().equals(id)))
      {
        continue;
      }
      table.addColumnController(column);
    }
    String sort=Sort.SORT_ASCENDING+ToonsTableColumnIds.NAME+Sort.SORT_ITEM_SEPARATOR+Sort.SORT_ASCENDING+KinshipMembersColumnIds.RANK;
    table.setSort(Sort.buildFromString(sort));

    TableColumnsManager<KinshipMember> columnsManager=table.getColumnsManager();
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
      columnIds.add(KinshipMembersColumnIds.RANK.name());
      columnIds.add(ToonsTableColumnIds.LEVEL.name());
      columnIds.add(ToonsTableColumnIds.RACE.name());
      columnIds.add(ToonsTableColumnIds.CLASS.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<KinshipMember> getTableController()
  {
    return _tableController;
  }

  /**
   * Handle kinship events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(KinshipEvent event)
  {
    KinshipEventType type=event.getType();
    if (type==KinshipEventType.KINSHIP_ROSTER_UPDATED)
    {
      Kinship kinship=event.getKinship();
      setMembers(kinship.getRoster().getAllMembers());
    }
  }

  /**
   * Set the members to show.
   * @param members List of members to show.
   */
  public void setMembers(List<KinshipMember> members)
  {
    _members.clear();
    _members.addAll(members);
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
    return _members.size();
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
    // Listeners
    EventsManager.removeListener(KinshipEvent.class,this);
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
    _members=null;
  }
}
