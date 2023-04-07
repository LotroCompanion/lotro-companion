package delta.games.lotro.gui.lore.items.table;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.lore.items.ItemColumnIds;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.lore.items.chooser.ItemsTableBuilder;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for a table that shows items.
 * @author DAM
 */
public class ItemsTableController
{
  // Data
  private TypedProperties _prefs;
  private List<Item> _items;
  // GUI
  private JTable _table;
  private GenericTableController<Item> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   * @param items Items to use.
   */
  public ItemsTableController(TypedProperties prefs, Filter<Item> filter, List<Item> items)
  {
    _prefs=prefs;
    _items=new ArrayList<Item>(items);
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<Item> buildTable()
  {
    GenericTableController<Item> table=ItemsTableBuilder.buildTable(_items);
    TableColumnsManager<Item> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    table.updateColumns();
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
      columnIds.add(ItemColumnIds.ICON.name());
      columnIds.add(ItemColumnIds.ID.name());
      columnIds.add(ItemColumnIds.NAME.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<Item> getTableController()
  {
    return _tableController;
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _tableController.filterUpdated();
  }

  /**
   * Get the total number of items.
   * @return A number of items.
   */
  public int getNbItems()
  {
    return _items.size();
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
   * Add an action listener.
   * @param al Action listener to add.
   */
  public void addActionListener(ActionListener al)
  {
    _tableController.addActionListener(al);
  }

  /**
   * Remove an action listener.
   * @param al Action listener to remove.
   */
  public void removeActionListener(ActionListener al)
  {
    _tableController.removeActionListener(al);
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
    _items=null;
  }
}
