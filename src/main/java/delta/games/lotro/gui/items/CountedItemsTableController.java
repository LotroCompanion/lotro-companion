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
import delta.games.lotro.gui.items.chooser.ItemChoiceTableController;
import delta.games.lotro.gui.items.chooser.ItemChoiceWindowController;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for a table that shows counted items.
 * @author DAM
 */
public class CountedItemsTableController
{
  /**
   * Identifier of the "Count" column.
   */
  public static final String COUNT_COLUMN="COUNT";

  // Preferences
  private TypedProperties _prefs;
  // Data
  protected List<CountedItem> _items;
  // GUI
  private GenericTableController<CountedItem> _tableController;

  /**
   * Constructor.
   * @param prefs User preferences.
   * @param items Items to show.
   * @param filter Items filter.
   */
  public CountedItemsTableController(TypedProperties prefs, List<CountedItem> items, final Filter<Item> filter)
  {
    _prefs=prefs;
    _items=items;
    _tableController=buildTable();
    if (filter!=null)
    {
      Filter<CountedItem> f=new Filter<CountedItem>()
      {
        public boolean accept(CountedItem item)
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
  public GenericTableController<CountedItem> getTableController()
  {
    return _tableController;
  }

  private GenericTableController<CountedItem> buildTable()
  {
    DataProvider<CountedItem> provider=new ListDataProvider<CountedItem>(_items);
    GenericTableController<CountedItem> table=new GenericTableController<CountedItem>(provider);
    List<TableColumnController<CountedItem,?>> columns=initColumns();
    TableColumnsManager<CountedItem> columnsManager=table.getColumnsManager();
    for(TableColumnController<CountedItem,?> column : columns)
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

  protected List<TableColumnController<CountedItem,?>> initColumns()
  {
    List<TableColumnController<CountedItem,?>> ret=new ArrayList<TableColumnController<CountedItem,?>>();

    List<DefaultTableColumnController<Item,?>> columns=ItemChoiceTableController.initColumns();
    for(TableColumnController<Item,?> column : columns)
    {
      CellDataProvider<CountedItem,Item> dataProvider=new CellDataProvider<CountedItem,Item>()
      {
        @Override
        public Item getData(CountedItem p)
        {
          return p.getItem();
        }
      };
      @SuppressWarnings("unchecked")
      TableColumnController<Item,Object> c=(TableColumnController<Item,Object>)column;
      TableColumnController<CountedItem,Object> proxiedColumn=new ProxiedTableColumnController<CountedItem,Item,Object>(c,dataProvider);
      ret.add(proxiedColumn);
    }
    // Count column
    {
      CellDataProvider<CountedItem,Integer> countCell=new CellDataProvider<CountedItem,Integer>()
      {
        @Override
        public Integer getData(CountedItem item)
        {
          return Integer.valueOf(item.getCount());
        }
      };
      DefaultTableColumnController<CountedItem,Integer> countColumn=new DefaultTableColumnController<CountedItem,Integer>(COUNT_COLUMN,"Count",Integer.class,countCell);
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
