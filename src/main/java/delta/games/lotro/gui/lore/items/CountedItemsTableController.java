package delta.games.lotro.gui.lore.items;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
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
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.lore.items.chooser.ItemsTableBuilder;
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemProvider;

/**
 * Controller for a table that shows counted items.
 * @author DAM
 * @param <T> Type of managed items.
 */
public class CountedItemsTableController<T extends ItemProvider>
{
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
    List<String> columnsIds=getColumnsId();
    columnsManager.setColumns(columnsIds);
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
    columnsIds.add(ItemColumnIds.NAME.name());
    return columnsIds;
  }

  /**
   * Build a list of all managed columns.
   * @return A list of column controllers.
   */
  public static <T extends ItemProvider> List<TableColumnController<CountedItem<T>,?>> initColumns()
  {
    List<TableColumnController<CountedItem<T>,?>> ret=new ArrayList<TableColumnController<CountedItem<T>,?>>();

    // Icon column
    {
      CellDataProvider<CountedItem<T>,Icon> iconCell=new CellDataProvider<CountedItem<T>,Icon>()
      {
        @Override
        public Icon getData(CountedItem<T> countedItem)
        {
          Item item=countedItem.getItem();
          if (item!=null)
          {
            return ItemUiTools.buildItemIcon(item);
          }
          String icon=countedItem.getIcon();
          return LotroIconsManager.getItemIcon(icon);
        }
      };
      DefaultTableColumnController<CountedItem<T>,Icon> iconColumn=new DefaultTableColumnController<CountedItem<T>,Icon>(ItemColumnIds.ICON.name(),"Icon",Icon.class,iconCell);
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      ret.add(iconColumn);
    }
    // ID column
    {
      CellDataProvider<CountedItem<T>,Long> idCell=new CellDataProvider<CountedItem<T>,Long>()
      {
        @Override
        public Long getData(CountedItem<T> countedItem)
        {
          Item item=countedItem.getItem();
          if (item!=null)
          {
            return Long.valueOf(item.getIdentifier());
          }
          return Long.valueOf(countedItem.getId());
        }
      };
      DefaultTableColumnController<CountedItem<T>,Long> idColumn=new DefaultTableColumnController<CountedItem<T>,Long>(ItemColumnIds.ID.name(),"ID",Long.class,idCell);
      idColumn.setWidthSpecs(90,90,50);
      ret.add(idColumn);
    }
    // Name column
    {
      CellDataProvider<CountedItem<T>,String> nameCell=new CellDataProvider<CountedItem<T>,String>()
      {
        @Override
        public String getData(CountedItem<T> countedItem)
        {
          Item item=countedItem.getItem();
          if (item!=null)
          {
            return item.getName();
          }
          return countedItem.getName();
        }
      };
      DefaultTableColumnController<CountedItem<T>,String> nameColumn=new DefaultTableColumnController<CountedItem<T>,String>(ItemColumnIds.NAME.name(),"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(150,-1,150);
      ret.add(nameColumn);
    }

    List<DefaultTableColumnController<Item,?>> columns=ItemsTableBuilder.initColumns();
    for(TableColumnController<Item,?> column : columns)
    {
      String id=column.getId();
      // Ignore ID, icon and name
      if ((ItemColumnIds.ID.name().equals(id)) || (ItemColumnIds.ICON.name().equals(id)) || (ItemColumnIds.NAME.name().equals(id)))
      {
        continue;
      }
      CellDataProvider<CountedItem<T>,Item> dataProvider=new CellDataProvider<CountedItem<T>,Item>()
      {
        @Override
        public Item getData(CountedItem<T> p)
        {
          return p.getItem();
        }
      };
      @SuppressWarnings("unchecked")
      TableColumnController<Item,Object> c=(TableColumnController<Item,Object>)column;
      TableColumnController<CountedItem<T>,Object> proxiedColumn=new ProxiedTableColumnController<CountedItem<T>,Item,Object>(c,dataProvider);
      ret.add(proxiedColumn);
    }
    // Count column
    {
      CellDataProvider<CountedItem<T>,Integer> countCell=new CellDataProvider<CountedItem<T>,Integer>()
      {
        @Override
        public Integer getData(CountedItem<T> item)
        {
          return Integer.valueOf(item.getQuantity());
        }
      };
      DefaultTableColumnController<CountedItem<T>,Integer> countColumn=new DefaultTableColumnController<CountedItem<T>,Integer>(COUNT_COLUMN,"Count",Integer.class,countCell);
      StatColumnsUtils.configureIntegerColumn(countColumn,55);
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
