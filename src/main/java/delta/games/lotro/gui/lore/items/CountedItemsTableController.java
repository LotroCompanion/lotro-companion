package delta.games.lotro.gui.lore.items;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.ColumnsUtils;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.Sort;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.lore.items.table.ItemColumnIds;
import delta.games.lotro.gui.lore.items.table.ItemsTableBuilder;
import delta.games.lotro.gui.utils.tables.renderers.MoneyCellRenderer;
import delta.games.lotro.lore.items.BaseItem;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.ItemProvider;

/**
 * Controller for a table that shows counted items.
 * @author DAM
 * @param <T> Type of managed items.
 */
public class CountedItemsTableController<T extends BaseItem>
{
  /**
   * Identifier of the "Total Value" column.
   */
  public static final String TOTAL_VALUE_COLUMN="TOTAL_VALUE";
  /**
   * Identifier of the "Count" column.
   */
  public static final String COUNT_COLUMN="COUNT";

  // Preferences
  private TypedProperties _prefs;
  // Data
  protected List<CountedItem<T>> _items;
  // GUI
  private GenericTableController<CountedItem<T>> _tableController;

  /**
   * Constructor.
   * @param prefs User preferences.
   * @param items Items to show.
   * @param filter Items filter.
   */
  public CountedItemsTableController(TypedProperties prefs, List<CountedItem<T>> items, final Filter<Item> filter)
  {
    _prefs=prefs;
    _items=items;
    _tableController=buildTable();
    if (filter!=null)
    {
      Filter<CountedItem<T>> f=new Filter<CountedItem<T>>()
      {
        @Override
        public boolean accept(CountedItem<T> item)
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
  public GenericTableController<CountedItem<T>> getTableController()
  {
    return _tableController;
  }

  private GenericTableController<CountedItem<T>> buildTable()
  {
    DataProvider<CountedItem<T>> provider=new ListDataProvider<CountedItem<T>>(_items);
    GenericTableController<CountedItem<T>> table=new GenericTableController<CountedItem<T>>(provider);
    List<TableColumnController<CountedItem<T>,?>> columns=initColumns();
    TableColumnsManager<CountedItem<T>> columnsManager=table.getColumnsManager();
    for(TableColumnController<CountedItem<T>,?> column : columns)
    {
      columnsManager.addColumnController(column,false);
    }
    // Columns
    List<String> columnsIds=getColumnsId();
    columnsManager.setColumns(columnsIds);
    // Sort
    String sort=Sort.SORT_DESCENDING+COUNT_COLUMN;
    table.setSort(Sort.buildFromString(sort));
    return table;
  }

  protected List<String> getColumnsId()
  {
    List<String> columnsIds;
    if (_prefs!=null)
    {
      columnsIds=_prefs.getStringList(ItemChooser.COLUMNS_PROPERTY);
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
    columnsIds.add(TOTAL_VALUE_COLUMN);
    columnsIds.add(ItemColumnIds.NAME.name());
    return columnsIds;
  }

  /**
   * Build a list of all managed columns.
   * @return A list of column controllers.
   */
  public static <T extends BaseItem> List<TableColumnController<CountedItem<T>,?>> initColumns()
  {
    List<TableColumnController<CountedItem<T>,?>> ret=new ArrayList<TableColumnController<CountedItem<T>,?>>();

    CellDataProvider<CountedItem<T>,T> managedItemProvider=new CellDataProvider<CountedItem<T>,T>()
    {
      @Override
      public T getData(CountedItem<T> p)
      {
        return p.getManagedItem();
      }
    };

    // 1) Columns that use a BaseItem (item level, stats, value)
    // Value column
    TableColumnController<T,Money> valueColumn=ItemsTableBuilder.buildValueColumn();
    TableColumnController<CountedItem<T>,Money> valueProxiedColumn=new ProxiedTableColumnController<CountedItem<T>,T,Money>(valueColumn,managedItemProvider);
    ret.add(valueProxiedColumn);
    // Item level column
    TableColumnController<T,Integer> itemLevelColumn=ItemsTableBuilder.buildItemLevelColumn();
    TableColumnController<CountedItem<T>,Integer> itemLevelProxiedColumn=new ProxiedTableColumnController<CountedItem<T>,T,Integer>(itemLevelColumn,managedItemProvider);
    ret.add(itemLevelProxiedColumn);
    // Stat columns
    List<TableColumnController<T,Number>> statColumns=ItemsTableBuilder.buildStatColumns();
    for(TableColumnController<T,Number> statColumn : statColumns)
    {
      TableColumnController<CountedItem<T>,Number> proxiedColumn=new ProxiedTableColumnController<CountedItem<T>,T,Number>(statColumn,managedItemProvider);
      ret.add(proxiedColumn);
    }
    // Gather known column names (to avoid building them again using Item)
    Set<String> columnNames=new HashSet<String>();
    for(TableColumnController<CountedItem<T>,?> column : ret)
    {
      columnNames.add(column.getId());
    }

    // 2) Build columns base on Item
    List<TableColumnController<Item,?>> columns=ItemsTableBuilder.initColumns();
    CellDataProvider<CountedItem<T>,Item> itemProvider=new CellDataProvider<CountedItem<T>,Item>()
    {
      @Override
      public Item getData(CountedItem<T> p)
      {
        return p.getItem();
      }
    };
    for(TableColumnController<Item,?> column : columns)
    {
      if (!columnNames.contains(column.getId()))
      {
        @SuppressWarnings("unchecked")
        TableColumnController<Item,Object> c=(TableColumnController<Item,Object>)column;
        TableColumnController<CountedItem<T>,Object> proxiedColumn=new ProxiedTableColumnController<CountedItem<T>,Item,Object>(c,itemProvider);
        ret.add(proxiedColumn);
      }
    }

    // 3) Build columns that use count
    // Total value column
    ret.add(buildTotalValueColumn());
    // Count column
    ret.add(buildCountColumn());
    return ret;
  }

  private static <T extends ItemProvider> TableColumnController<CountedItem<T>,Money> buildTotalValueColumn()
  {
    CellDataProvider<CountedItem<T>,Money> valueCell=new CellDataProvider<CountedItem<T>,Money>()
    {
      @Override
      public Money getData(CountedItem<T> item)
      {
        int quantity=item.getQuantity();
        T managedItem=item.getManagedItem();
        Money oneValue=getItemValue(managedItem);
        if ((oneValue==null) || (quantity==1))
        {
          return oneValue;
        }
        int newInternalValue=oneValue.getInternalValue()*quantity;
        Money money=new Money();
        money.setRawValue(newInternalValue);
        return money;
      }
    };
    DefaultTableColumnController<CountedItem<T>,Money> valueColumn=new DefaultTableColumnController<CountedItem<T>,Money>(TOTAL_VALUE_COLUMN,"Total Value",Money.class,valueCell); // 18n
    MoneyCellRenderer.configureColumn(valueColumn);
    return valueColumn;
  }

  private static <T extends ItemProvider> TableColumnController<CountedItem<T>,Integer> buildCountColumn()
  {
    CellDataProvider<CountedItem<T>,Integer> countCell=new CellDataProvider<CountedItem<T>,Integer>()
    {
      @Override
      public Integer getData(CountedItem<T> item)
      {
        return Integer.valueOf(item.getQuantity());
      }
    };
    DefaultTableColumnController<CountedItem<T>,Integer> countColumn=new DefaultTableColumnController<CountedItem<T>,Integer>(COUNT_COLUMN,"Count",Integer.class,countCell); // 18n
    ColumnsUtils.configureIntegerColumn(countColumn,55);
    return countColumn;
  }

  private static <T extends ItemProvider> Money getItemValue(T managedItem)
  {
    if (managedItem instanceof ItemInstance)
    {
      return ((ItemInstance<?>)managedItem).getEffectiveValue();
    }
    return managedItem.getItem().getValueAsMoney();
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
      _prefs.setStringList(ItemChooser.COLUMNS_PROPERTY,columnIds);
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
