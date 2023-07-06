package delta.games.lotro.gui.character.storage.wardrobe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.Sort;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.storage.wardrobe.WardrobeItem;
import delta.games.lotro.gui.lore.items.ItemColumnIds;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.lore.items.chooser.ItemsTableBuilder;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for a table that shows wardrobe items.
 * @author DAM
 */
public class WardrobeItemsTableController
{
  /**
   * Identifier of the "Color" column.
   */
  public static final String COLOR_COLUMN="COLOR";

  // Parent controller
  private WindowController _parent;
  // Preferences
  private TypedProperties _prefs;
  // Data
  protected List<WardrobeItem> _items;
  // GUI
  private GenericTableController<WardrobeItem> _tableController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param prefs User preferences.
   * @param items Items to show.
   * @param filter Managed filter.
   */
  public WardrobeItemsTableController(WindowController parent, TypedProperties prefs, List<WardrobeItem> items, Filter<WardrobeItem> filter)
  {
    _parent=parent;
    _prefs=prefs;
    _items=items;
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  /**
   * Get the managed generic table controller.
   * @return the managed generic table controller.
   */
  public GenericTableController<WardrobeItem> getTableController()
  {
    return _tableController;
  }

  private GenericTableController<WardrobeItem> buildTable()
  {
    // Build table
    DataProvider<WardrobeItem> provider=new ListDataProvider<WardrobeItem>(_items);
    GenericTableController<WardrobeItem> table=new GenericTableController<WardrobeItem>(provider);
    List<TableColumnController<WardrobeItem,?>> columns=initColumns();
    TableColumnsManager<WardrobeItem> columnsManager=table.getColumnsManager();
    for(TableColumnController<WardrobeItem,?> column : columns)
    {
      columnsManager.addColumnController(column,false);
    }
    List<String> columnsIds=getColumnsId();
    columnsManager.setColumns(columnsIds);

    // Sort
    String sort=Sort.SORT_ASCENDING+ItemColumnIds.NAME+Sort.SORT_ITEM_SEPARATOR+Sort.SORT_ASCENDING+COLOR_COLUMN;
    table.setSort(Sort.buildFromString(sort));
    return table;
  }

  /**
   * Build a list of all managed columns.
   * @return A list of column controllers.
   */
  public static List<TableColumnController<WardrobeItem,?>> initColumns()
  {
    List<TableColumnController<WardrobeItem,?>> ret=new ArrayList<TableColumnController<WardrobeItem,?>>();

    List<DefaultTableColumnController<Item,?>> columns=ItemsTableBuilder.initColumns();
    for(TableColumnController<Item,?> column : columns)
    {
      CellDataProvider<WardrobeItem,Item> dataProvider=new CellDataProvider<WardrobeItem,Item>()
      {
        @Override
        public Item getData(WardrobeItem p)
        {
          return p.getItem();
        }
      };
      @SuppressWarnings("unchecked")
      TableColumnController<Item,Object> c=(TableColumnController<Item,Object>)column;
      TableColumnController<WardrobeItem,Object> proxiedColumn=new ProxiedTableColumnController<WardrobeItem,Item,Object>(c,dataProvider);
      ret.add(proxiedColumn);
    }
    // Color column
    {
      CellDataProvider<WardrobeItem,String> colorCell=new CellDataProvider<WardrobeItem,String>()
      {
        @Override
        public String getData(WardrobeItem wardrobeItem)
        {
          return wardrobeItem.getColorsLabel();
        }
      };
      DefaultTableColumnController<WardrobeItem,String> colorColumn=new DefaultTableColumnController<WardrobeItem,String>(COLOR_COLUMN,"Color",String.class,colorCell); // I18n
      colorColumn.setWidthSpecs(100,100,100);
      ret.add(colorColumn);
    }
    return ret;
  }

  protected List<String> getColumnsId()
  {
    List<String> columnsIds=null;
    if (_prefs!=null)
    {
      columnsIds=_prefs.getStringList(ItemChooser.COLUMNS_PROPERTY);
    }
    if (columnsIds==null)
    {
      columnsIds=getDefaultColumnIds();
    }
    return columnsIds;
  }

  protected List<String> getDefaultColumnIds()
  {
    List<String> columnsIds=new ArrayList<String>();
    columnsIds.add(ItemColumnIds.ICON.name());
    columnsIds.add(ItemColumnIds.NAME.name());
    columnsIds.add(COLOR_COLUMN);
    return columnsIds;
  }

  private void configureTable()
  {
    JTable table=getTable();
    // Adjust table row height for icons (32 pixels)
    table.setRowHeight(32);
    // Action listener
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          Object sourceItem=event.getSource();
          showItem(sourceItem);
        }
      }
    };
    _tableController.addActionListener(al);
  }

  private void showItem(Object sourceItem)
  {
    WardrobeItem wardrobeItem=(WardrobeItem)sourceItem;
    Item item=wardrobeItem.getItem();
    ItemUiTools.showItemForm(_parent,item);
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
    // Parent controller
    _parent=null;
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
