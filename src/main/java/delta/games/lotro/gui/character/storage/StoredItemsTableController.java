package delta.games.lotro.gui.character.storage;

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
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.location.StorageLocation;
import delta.games.lotro.character.storage.location.comparators.LocationComparator;
import delta.games.lotro.common.owner.Owner;
import delta.games.lotro.common.owner.comparators.OwnerComparator;
import delta.games.lotro.gui.items.CountedItemsTableController;
import delta.games.lotro.gui.items.ItemColumnIds;
import delta.games.lotro.gui.items.chooser.ItemChoiceWindowController;
import delta.games.lotro.lore.items.CountedItem;

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

  // Preferences
  private TypedProperties _prefs;
  // Data
  protected List<? extends StoredItem> _items;
  // GUI
  private GenericTableController<StoredItem> _tableController;

  /**
   * Constructor.
   * @param prefs User preferences.
   * @param items Items to show.
   * @param filter Managed filter.
   */
  public StoredItemsTableController(TypedProperties prefs, List<? extends StoredItem> items, Filter<StoredItem> filter)
  {
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

    List<TableColumnController<CountedItem,?>> columns=CountedItemsTableController.initColumns();
    for(TableColumnController<CountedItem,?> column : columns)
    {
      CellDataProvider<StoredItem,CountedItem> dataProvider=new CellDataProvider<StoredItem,CountedItem>()
      {
        @Override
        public CountedItem getData(StoredItem p)
        {
          return p;
        }
      };
      @SuppressWarnings("unchecked")
      TableColumnController<CountedItem,Object> c=(TableColumnController<CountedItem,Object>)column;
      TableColumnController<StoredItem,Object> proxiedColumn=new ProxiedTableColumnController<StoredItem,CountedItem,Object>(c,dataProvider);
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
      DefaultTableColumnController<StoredItem,Owner> ownerColumn=new DefaultTableColumnController<StoredItem,Owner>(OWNER_COLUMN,"Owner",Owner.class,ownerCell);
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
      DefaultTableColumnController<StoredItem,StorageLocation> locationColumn=new DefaultTableColumnController<StoredItem,StorageLocation>(LOCATION_COLUMN,"Location",StorageLocation.class,locationCell);
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
      columnsIds=_prefs.getStringList(ItemChoiceWindowController.COLUMNS_PROPERTY);
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
