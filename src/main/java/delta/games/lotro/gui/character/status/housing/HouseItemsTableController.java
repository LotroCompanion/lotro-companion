package delta.games.lotro.gui.character.status.housing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

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
import delta.games.lotro.character.status.housing.HousingItem;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.common.enums.HousingHookID;
import delta.games.lotro.common.owner.Owner;
import delta.games.lotro.gui.character.storage.StorageFilterController;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.lore.items.table.ItemColumnIds;
import delta.games.lotro.gui.lore.items.table.ItemsTableBuilder;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.ItemProvider;
import delta.games.lotro.lore.items.ItemsManager;

/**
 * Controller for a table that shows house items.
 * @author DAM
 */
public class HouseItemsTableController
{
  /**
   * Identifier of the "Hook" column.
   */
  public static final String HOOK_COLUMN="HOOK";
  /**
   * Identifier of the "Position" column.
   */
  public static final String POSITION_COLUMN="POSITION";
  /**
   * Identifier of the "Rotation" column.
   */
  public static final String ROTATION_COLUMN="ROTATION";
  /**
   * Identifier of the "Hook" column.
   */
  public static final String HOOK_ROTATION_COLUMN="HOOK_ROTATION";
  /**
   * Identifier of the "Position offset" column.
   */
  public static final String POSITION_OFFSET_COLUMN="POSITION_OFFSET";
  /**
   * Identifier of the "Bound to" column.
   */
  public static final String BOUND_TO_COLUMN="BOUND_TO";

  // Parent controller
  private WindowController _parent;
  // Preferences
  private TypedProperties _prefs;
  // Data
  protected List<HousingItem> _items;
  // GUI
  private GenericTableController<HousingItem> _tableController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param prefs User preferences.
   * @param items Items to show.
   * @param filter Managed filter.
   */
  public HouseItemsTableController(WindowController parent, TypedProperties prefs, List<HousingItem> items, Filter<HousingItem> filter)
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
  public GenericTableController<HousingItem> getTableController()
  {
    return _tableController;
  }

  private GenericTableController<HousingItem> buildTable()
  {
    // Build table
    DataProvider<HousingItem> provider=new ListDataProvider<HousingItem>(_items);
    GenericTableController<HousingItem> table=new GenericTableController<HousingItem>(provider);
    List<TableColumnController<HousingItem,?>> columns=initColumns();
    TableColumnsManager<HousingItem> columnsManager=table.getColumnsManager();
    for(TableColumnController<HousingItem,?> column : columns)
    {
      columnsManager.addColumnController(column,false);
    }
    List<String> columnsIds=getColumnsId();
    columnsManager.setColumns(columnsIds);

    // Sort
    String sort=Sort.SORT_ASCENDING+ItemColumnIds.NAME+Sort.SORT_ITEM_SEPARATOR+Sort.SORT_ASCENDING+HOOK_COLUMN;
    table.setSort(Sort.buildFromString(sort));
    return table;
  }

  /**
   * Build a list of all managed columns.
   * @return A list of column controllers.
   */
  public static List<TableColumnController<HousingItem,?>> initColumns()
  {
    List<TableColumnController<HousingItem,?>> ret=new ArrayList<TableColumnController<HousingItem,?>>();

    List<TableColumnController<Item,?>> columns=ItemsTableBuilder.initColumns();
    for(TableColumnController<Item,?> column : columns)
    {
      CellDataProvider<HousingItem,Item> dataProvider=new CellDataProvider<HousingItem,Item>()
      {
        @Override
        public Item getData(HousingItem hi)
        {
          int itemID=hi.getItemID();
          // TODO : put the Item inside the HousingItem
          Item item=ItemsManager.getInstance().getItem(itemID);
          return item;
        }
      };
      @SuppressWarnings("unchecked")
      TableColumnController<Item,Object> c=(TableColumnController<Item,Object>)column;
      TableColumnController<HousingItem,Object> proxiedColumn=new ProxiedTableColumnController<HousingItem,Item,Object>(c,dataProvider);
      ret.add(proxiedColumn);
    }
    // Hook column
    {
      CellDataProvider<HousingItem,HousingHookID> cell=new CellDataProvider<HousingItem,HousingHookID>()
      {
        @Override
        public HousingHookID getData(HousingItem item)
        {
          return item.getHookID();
        }
      };
      DefaultTableColumnController<HousingItem,HousingHookID> column=new DefaultTableColumnController<HousingItem,HousingHookID>(HOOK_COLUMN,"Hook",HousingHookID.class,cell); // I18n
      column.setWidthSpecs(150,-1,150);
      ret.add(column);
    }
    // Position column
    // Rotation column
    // Hook rotation column
    // Position offset column
    // Bound to column
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
    columnsIds.add(HOOK_COLUMN);
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

  @SuppressWarnings("unchecked")
  private void showItem(Object sourceItem)
  {
    StoredItem storedItem=(StoredItem)sourceItem;
    CountedItem<ItemProvider> countedItem=storedItem.getItem();
    ItemProvider managedItem=countedItem.getManagedItem();
    if (managedItem instanceof ItemInstance)
    {
      ItemInstance<? extends Item> item=(ItemInstance<? extends Item>)managedItem;
      ItemUiTools.showItemInstanceWindow(_parent,item);
    }
    else if (managedItem instanceof Item)
    {
      Item item=(Item)managedItem;
      ItemUiTools.showItemForm(_parent,item);
    }
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

  /**
   * Owner renderer.
   * @author DAM
   */
  public static class OwnerRenderer extends DefaultTableCellRenderer
  {
    @Override
    public void setValue(Object value)
    {
      String text=StorageFilterController.getLabelForOwner((Owner)value);
      setText(text);
    }
  }
}
