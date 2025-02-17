package delta.games.lotro.gui.house;

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
import delta.common.ui.swing.tables.Sort;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.enums.HouseType;
import delta.games.lotro.gui.character.status.housing.HouseDisplayWindowController;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.lore.items.table.ItemColumnIds;
import delta.games.lotro.house.HouseEntry;

/**
 * Controller for a table that shows houses.
 * @author DAM
 */
public class HousesTableController
{
  /**
   * Identifier of the "Server" column.
   */
  public static final String SERVER="SERVER";
  /**
   * Identifier of the "Homestead" column.
   */
  public static final String HOMESTEAD="HOMESTEAD";
  /**
   * Identifier of the "Neighborhood" column.
   */
  public static final String NEIGHBORHOOD="NEIGHBORHOOD";
  /**
   * Identifier of the "Address" column.
   */
  public static final String HOUSE_ADDRESS="HOUSE_ADDRESS";
  /**
   * Identifier of the "Type" column.
   */
  public static final String HOUSE_TYPE="HOUSE_TYPE";

  // Parent controller
  private WindowController _parent;
  // Preferences
  private TypedProperties _prefs;
  // Data
  protected List<HouseEntry> _items;
  // GUI
  private GenericTableController<HouseEntry> _tableController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param prefs User preferences.
   * @param items Items to show.
   * @param filter Managed filter.
   */
  public HousesTableController(WindowController parent, TypedProperties prefs, List<HouseEntry> items, Filter<HouseEntry> filter)
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
  public GenericTableController<HouseEntry> getTableController()
  {
    return _tableController;
  }

  private GenericTableController<HouseEntry> buildTable()
  {
    // Build table
    DataProvider<HouseEntry> provider=new ListDataProvider<HouseEntry>(_items);
    GenericTableController<HouseEntry> table=new GenericTableController<HouseEntry>(provider);
    List<TableColumnController<HouseEntry,?>> columns=initColumns();
    TableColumnsManager<HouseEntry> columnsManager=table.getColumnsManager();
    for(TableColumnController<HouseEntry,?> column : columns)
    {
      columnsManager.addColumnController(column,false);
    }
    List<String> columnsIds=getColumnsId();
    columnsManager.setColumns(columnsIds);

    // Sort
    String sort=Sort.SORT_ASCENDING+SERVER+Sort.SORT_ITEM_SEPARATOR+Sort.SORT_ASCENDING+HOMESTEAD+Sort.SORT_ITEM_SEPARATOR+Sort.SORT_ASCENDING+NEIGHBORHOOD+Sort.SORT_ITEM_SEPARATOR+Sort.SORT_ASCENDING+HOUSE_ADDRESS;
    table.setSort(Sort.buildFromString(sort));
    return table;
  }

  /**
   * Build a list of all managed columns.
   * @return A list of column controllers.
   */
  private List<TableColumnController<HouseEntry,?>> initColumns()
  {
    List<TableColumnController<HouseEntry,?>> ret=new ArrayList<TableColumnController<HouseEntry,?>>();

    // Server column
    ret.add(buildServerColumn());
    // Homestead column
    ret.add(buildHomesteadColumn());
    // Neighborhood column
    ret.add(buildNeighborhoodColumn());
    // Address column
    ret.add(buildAddressColumn());
    // Type column
    ret.add(buildTypeColumn());
    return ret;
  }

  private TableColumnController<HouseEntry,?> buildServerColumn()
  {
    CellDataProvider<HouseEntry,String> cell=new CellDataProvider<HouseEntry,String>()
    {
      @Override
      public String getData(HouseEntry item)
      {
        return item.getServer();
      }
    };
    DefaultTableColumnController<HouseEntry,String> column=new DefaultTableColumnController<HouseEntry,String>(SERVER,"Server",String.class,cell); // I18n
    column.setWidthSpecs(100,100,100);
    return column;
  }

  private TableColumnController<HouseEntry,?> buildHomesteadColumn()
  {
    CellDataProvider<HouseEntry,String> cell=new CellDataProvider<HouseEntry,String>()
    {
      @Override
      public String getData(HouseEntry item)
      {
        return item.getNeighborhoodTemplate();
      }
    };
    DefaultTableColumnController<HouseEntry,String> column=new DefaultTableColumnController<HouseEntry,String>(HOMESTEAD,"Homestead",String.class,cell); // I18n
    column.setWidthSpecs(100,150,150);
    return column;
  }

  private TableColumnController<HouseEntry,?> buildNeighborhoodColumn()
  {
    CellDataProvider<HouseEntry,String> cell=new CellDataProvider<HouseEntry,String>()
    {
      @Override
      public String getData(HouseEntry item)
      {
        return item.getNeighborhood();
      }
    };
    DefaultTableColumnController<HouseEntry,String> column=new DefaultTableColumnController<HouseEntry,String>(NEIGHBORHOOD,"Neighborhood",String.class,cell); // I18n
    column.setWidthSpecs(100,100,100);
    return column;
  }

  private TableColumnController<HouseEntry,?> buildAddressColumn()
  {
    CellDataProvider<HouseEntry,String> cell=new CellDataProvider<HouseEntry,String>()
    {
      @Override
      public String getData(HouseEntry item)
      {
        return item.getAddress();
      }
    };
    DefaultTableColumnController<HouseEntry,String> column=new DefaultTableColumnController<HouseEntry,String>(HOUSE_ADDRESS,"Address",String.class,cell); // I18n
    column.setWidthSpecs(100,150,150);
    return column;
  }

  private TableColumnController<HouseEntry,?> buildTypeColumn()
  {
    CellDataProvider<HouseEntry,HouseType> cell=new CellDataProvider<HouseEntry,HouseType>()
    {
      @Override
      public HouseType getData(HouseEntry item)
      {
        return item.getType();
      }
    };
    DefaultTableColumnController<HouseEntry,HouseType> column=new DefaultTableColumnController<HouseEntry,HouseType>(HOUSE_TYPE,"Type",HouseType.class,cell); // I18n
    column.setWidthSpecs(100,100,100);
    return column;
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
    columnsIds.add(SERVER);
    columnsIds.add(HOMESTEAD);
    columnsIds.add(NEIGHBORHOOD);
    columnsIds.add(HOUSE_ADDRESS);
    columnsIds.add(HOUSE_TYPE);
    return columnsIds;
  }

  private void configureTable()
  {
    // Action listener
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          HouseEntry house=(HouseEntry)event.getSource();
          showHouse(house);
        }
      }
    };
    _tableController.addActionListener(al);
  }

  private void showHouse(HouseEntry house)
  {
    String id=HouseDisplayWindowController.getWindowIdentifier(house.getHouse().getIdentifier());
    WindowsManager mgr=_parent.getWindowsManager();
    WindowController ctrl=mgr.getWindow(id);
    if (ctrl==null)
    {
      ctrl=new HouseDisplayWindowController(_parent,house.getHouse());
      mgr.registerWindow(ctrl);
    }
    ctrl.bringToFront();
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
