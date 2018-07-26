package delta.games.lotro.gui.items;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.gui.items.chooser.ItemChoiceTableController;
import delta.games.lotro.gui.items.chooser.ItemChoiceWindowController;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for a table that shows stored items.
 * @author DAM
 */
public class StoredItemsTableController
{
  /**
   * Identifier of the "Count" column.
   */
  public static final String COUNT_COLUMN="COUNT";

  // Preferences
  private TypedProperties _prefs;
  // Data
  protected List<StoredItem> _items;
  // GUI
  private GenericTableController<StoredItem> _tableController;

  /**
   * Constructor.
   * @param prefs User preferences.
   * @param items Items to show.
   * @param filter Items filter.
   */
  public StoredItemsTableController(TypedProperties prefs, List<StoredItem> items, final Filter<Item> filter)
  {
    _prefs=prefs;
    _items=items;
    _tableController=buildTable();
    if (filter!=null)
    {
      Filter<StoredItem> f=new Filter<StoredItem>()
      {
        public boolean accept(StoredItem item)
        {
          return filter.accept(item.getItem());
        }
      };
      _tableController.setFilter(f);
    }
    configureTable();
  }

  /**
   * Get the managed generic table controller.
   * @return the managed generic table controller.
   */
  public GenericTableController<StoredItem> getTableController()
  {
    return _tableController;
  }

  private GenericTableController<StoredItem> buildTable()
  {
    DataProvider<StoredItem> provider=new ListDataProvider<StoredItem>(_items);
    GenericTableController<StoredItem> table=new GenericTableController<StoredItem>(provider);
    List<TableColumnController<StoredItem,?>> columns=initColumns();
    TableColumnsManager<StoredItem> columnsManager=table.getColumnsManager();
    for(TableColumnController<StoredItem,?> column : columns)
    {
      columnsManager.addColumnController(column,false);
    }
    List<String> columnsIds=getColumnsId();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  protected List<String> getColumnsId()
  {
    List<String> columnsIds;
    if (_prefs!=null)
    {
      columnsIds=_prefs.getStringList(ItemChoiceWindowController.COLUMNS_PROPERTY);
    }
    else
    {
      columnsIds=getDefaultColumnIds();
    }
    return columnsIds;
  }

  protected List<String> getDefaultColumnIds()
  {
    List<String> columnsIds=new ArrayList<String>();
    columnsIds.add(ItemColumnIds.ICON.name());
    columnsIds.add(COUNT_COLUMN);
    columnsIds.add(ItemColumnIds.NAME.name());
    return columnsIds;
  }

  protected List<TableColumnController<StoredItem,?>> initColumns()
  {
    List<TableColumnController<StoredItem,?>> ret=new ArrayList<TableColumnController<StoredItem,?>>();

    List<DefaultTableColumnController<Item,?>> columns=ItemChoiceTableController.initColumns();
    for(TableColumnController<Item,?> column : columns)
    {
      CellDataProvider<StoredItem,Item> dataProvider=new CellDataProvider<StoredItem,Item>()
      {
        @Override
        public Item getData(StoredItem p)
        {
          return p.getItem();
        }
      };
      @SuppressWarnings("unchecked")
      TableColumnController<Item,Object> c=(TableColumnController<Item,Object>)column;
      TableColumnController<StoredItem,Object> proxiedColumn=new ProxiedTableColumnController<StoredItem,Item,Object>(c,dataProvider);
      ret.add(proxiedColumn);
    }
    // Count column
    {
      CellDataProvider<StoredItem,Integer> countCell=new CellDataProvider<StoredItem,Integer>()
      {
        @Override
        public Integer getData(StoredItem item)
        {
          return Integer.valueOf(item.getQuantity());
        }
      };
      DefaultTableColumnController<StoredItem,Integer> countColumn=new DefaultTableColumnController<StoredItem,Integer>(COUNT_COLUMN,"Count",Integer.class,countCell);
      countColumn.setWidthSpecs(55,55,50);
      ret.add(countColumn);
    }
    return ret;
  }

  private void configureTable()
  {
    JTable table=getTable();
    // Adjust table row height for icons (32 pixels)
    table.setRowHeight(32);
  }

  /**
   * Update display.
   */
  public void update()
  {
    _tableController.refresh();
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    return _tableController.getTable();
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _tableController.filterUpdated();
  }

  /**
   * Get the total number of items in the managed table.
   * @return A number of items.
   */
  public int getNbItems()
  {
    return (_items!=null)?_items.size():0;
  }

  /**
   * Get the number of filtered items.
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
      _prefs.setStringList(ItemChoiceWindowController.COLUMNS_PROPERTY,columnIds);
    }
    // GUI
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _items=null;
  }
}
