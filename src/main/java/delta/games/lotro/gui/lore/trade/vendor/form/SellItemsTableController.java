package delta.games.lotro.gui.lore.trade.vendor.form;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.money.comparator.MoneyComparator;
import delta.games.lotro.gui.lore.items.ItemColumnIds;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.lore.items.chooser.ItemsTableBuilder;
import delta.games.lotro.gui.utils.MoneyCellRenderer;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.trade.vendor.ValuedItem;

/**
 * Controller for a table that shows items to sell.
 * @author DAM
 */
public class SellItemsTableController
{
  // Data
  private TypedProperties _prefs;
  private List<ValuedItem> _items;
  // GUI
  private JTable _table;
  private GenericTableController<ValuedItem> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   * @param items Items to show.
   */
  public SellItemsTableController(TypedProperties prefs, Filter<ValuedItem> filter, List<ValuedItem> items)
  {
    _prefs=prefs;
    _items=new ArrayList<ValuedItem>(items);
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  private GenericTableController<ValuedItem> buildTable()
  {
    ListDataProvider<ValuedItem> provider=new ListDataProvider<ValuedItem>(_items);
    GenericTableController<ValuedItem> table=new GenericTableController<ValuedItem>(provider);
    List<TableColumnController<ValuedItem,?>> columns=buildColumns();
    for(TableColumnController<ValuedItem,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<ValuedItem> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a valued items table.
   * @return A list of columns for a valued items table.
   */
  public static List<TableColumnController<ValuedItem,?>> buildColumns()
  {
    List<TableColumnController<ValuedItem,?>> ret=new ArrayList<TableColumnController<ValuedItem,?>>();
    List<DefaultTableColumnController<Item,?>> itemColumns=new ArrayList<DefaultTableColumnController<Item,?>>();
    // Icon column
    itemColumns.add(ItemsTableBuilder.buildIconColumn());
    // Identifier column
    itemColumns.add(ItemsTableBuilder.buildIdColumn());
    // Name column
    itemColumns.add(ItemsTableBuilder.buildNameColumn());
    // Item level column
    itemColumns.add(ItemsTableBuilder.buildItemLevelColumn());
    // Category column
    itemColumns.add(ItemsTableBuilder.buildCategoryColumn());
    // Build result
    for(DefaultTableColumnController<Item,?> itemColumn : itemColumns)
    {
      CellDataProvider<ValuedItem,Item> provider=new CellDataProvider<ValuedItem,Item>()
      {
        public Item getData(ValuedItem source)
        {
          return source.getItem();
        }
      };
      @SuppressWarnings("unchecked")
      TableColumnController<Item,Object> c=(TableColumnController<Item,Object>)itemColumn;
      ProxiedTableColumnController<ValuedItem,Item,Object> proxiedColumn=new ProxiedTableColumnController<ValuedItem,Item,Object>(c,provider);
      ret.add(proxiedColumn);
    }
    // Value column
    ret.add(buildValueColumn());
    return ret;
  }

  /**
   * Build a column for the value of an item.
   * @return a column.
   */
  private static DefaultTableColumnController<ValuedItem,Money> buildValueColumn()
  {
    CellDataProvider<ValuedItem,Money> valueCell=new CellDataProvider<ValuedItem,Money>()
    {
      @Override
      public Money getData(ValuedItem item)
      {
        return item.getValue();
      }
    };
    DefaultTableColumnController<ValuedItem,Money> valueColumn=new DefaultTableColumnController<ValuedItem,Money>(ItemColumnIds.VALUE.name(),"Sell Price",Money.class,valueCell);
    valueColumn.setWidthSpecs(120,120,120);
    valueColumn.setCellRenderer(new MoneyCellRenderer());
    valueColumn.setComparator(new MoneyComparator());
    return valueColumn;
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
      columnIds.add(ItemColumnIds.NAME.name());
      columnIds.add(ItemColumnIds.ITEM_LEVEL.name());
      columnIds.add(ItemColumnIds.CATEGORY.name());
      columnIds.add(ItemColumnIds.VALUE.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<ValuedItem> getTableController()
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
   * Refresh table.
   */
  public void refresh()
  {
    if (_table!=null)
    {
      _tableController.refresh();
    }
  }

  private void configureTable()
  {
    JTable table=getTable();
    // Adjust table row height for icons (32 pixels)
    table.setRowHeight(32);
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
