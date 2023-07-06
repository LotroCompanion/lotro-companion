package delta.games.lotro.gui.character.storage;

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
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.location.StorageLocation;
import delta.games.lotro.character.storage.location.comparators.LocationComparator;
import delta.games.lotro.common.owner.Owner;
import delta.games.lotro.common.owner.comparators.OwnerComparator;
import delta.games.lotro.gui.lore.items.CountedItemsTableController;
import delta.games.lotro.gui.lore.items.ItemColumnIds;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.ItemProvider;

/**
 * Controller for a table that shows stored items.
 * @author DAM
 */
public class StoredItemsTableController
{
  /**
   * Identifier of the "Owner" column.
   */
  public static final String OWNER_COLUMN="OWNER";
  /**
   * Identifier of the "Location" column.
   */
  public static final String LOCATION_COLUMN="LOCATION";

  // Parent controller
  private WindowController _parent;
  // Preferences
  private TypedProperties _prefs;
  // Data
  protected List<StoredItem> _items;
  // GUI
  private GenericTableController<StoredItem> _tableController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param prefs User preferences.
   * @param items Items to show.
   * @param filter Managed filter.
   */
  public StoredItemsTableController(WindowController parent, TypedProperties prefs, List<StoredItem> items, Filter<StoredItem> filter)
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
  public GenericTableController<StoredItem> getTableController()
  {
    return _tableController;
  }

  private GenericTableController<StoredItem> buildTable()
  {
    // Build table
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

    // Sort
    String sort=Sort.SORT_ASCENDING+ItemColumnIds.NAME+Sort.SORT_ITEM_SEPARATOR+Sort.SORT_ASCENDING+OWNER_COLUMN;
    table.setSort(Sort.buildFromString(sort));
    return table;
  }

  /**
   * Build a list of all managed columns.
   * @return A list of column controllers.
   */
  public static List<TableColumnController<StoredItem,?>> initColumns()
  {
    List<TableColumnController<StoredItem,?>> ret=new ArrayList<TableColumnController<StoredItem,?>>();

    List<TableColumnController<CountedItem<ItemProvider>,?>> columns=CountedItemsTableController.initColumns();
    for(TableColumnController<CountedItem<ItemProvider>,?> column : columns)
    {
      CellDataProvider<StoredItem,CountedItem<ItemProvider>> dataProvider=new CellDataProvider<StoredItem,CountedItem<ItemProvider>>()
      {
        @Override
        public CountedItem<ItemProvider> getData(StoredItem p)
        {
          return p.getItem();
        }
      };
      @SuppressWarnings("unchecked")
      TableColumnController<CountedItem<ItemProvider>,Object> c=(TableColumnController<CountedItem<ItemProvider>,Object>)column;
      TableColumnController<StoredItem,Object> proxiedColumn=new ProxiedTableColumnController<StoredItem,CountedItem<ItemProvider>,Object>(c,dataProvider);
      ret.add(proxiedColumn);
    }
    // Owner column
    {
      CellDataProvider<StoredItem,Owner> ownerCell=new CellDataProvider<StoredItem,Owner>()
      {
        @Override
        public Owner getData(StoredItem countedItem)
        {
          return countedItem.getOwner();
        }
      };
      DefaultTableColumnController<StoredItem,Owner> ownerColumn=new DefaultTableColumnController<StoredItem,Owner>(OWNER_COLUMN,"Owner",Owner.class,ownerCell); // I18n
      ownerColumn.setWidthSpecs(150,-1,150);
      ownerColumn.setComparator(new OwnerComparator());
      ownerColumn.setCellRenderer(new OwnerRenderer());
      ret.add(ownerColumn);
    }
    // Location column
    {
      CellDataProvider<StoredItem,StorageLocation> locationCell=new CellDataProvider<StoredItem,StorageLocation>()
      {
        @Override
        public StorageLocation getData(StoredItem countedItem)
        {
          return countedItem.getLocation();
        }
      };
      DefaultTableColumnController<StoredItem,StorageLocation> locationColumn=new DefaultTableColumnController<StoredItem,StorageLocation>(LOCATION_COLUMN,"Location",StorageLocation.class,locationCell); // I18n
      locationColumn.setWidthSpecs(150,-1,150);
      locationColumn.setComparator(new LocationComparator());
      ret.add(locationColumn);
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
    columnsIds.add(CountedItemsTableController.COUNT_COLUMN);
    columnsIds.add(OWNER_COLUMN);
    columnsIds.add(LOCATION_COLUMN);
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
