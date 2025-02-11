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
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.enums.HouseType;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.lore.items.table.ItemColumnIds;

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
  protected List<HouseTableEntry> _items;
  // GUI
  private GenericTableController<HouseTableEntry> _tableController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param prefs User preferences.
   * @param items Items to show.
   * @param filter Managed filter.
   */
  public HousesTableController(WindowController parent, TypedProperties prefs, List<HouseTableEntry> items, Filter<HouseTableEntry> filter)
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
  public GenericTableController<HouseTableEntry> getTableController()
  {
    return _tableController;
  }

  private GenericTableController<HouseTableEntry> buildTable()
  {
    // Build table
    DataProvider<HouseTableEntry> provider=new ListDataProvider<HouseTableEntry>(_items);
    GenericTableController<HouseTableEntry> table=new GenericTableController<HouseTableEntry>(provider);
    List<TableColumnController<HouseTableEntry,?>> columns=initColumns();
    TableColumnsManager<HouseTableEntry> columnsManager=table.getColumnsManager();
    for(TableColumnController<HouseTableEntry,?> column : columns)
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
  private List<TableColumnController<HouseTableEntry,?>> initColumns()
  {
    List<TableColumnController<HouseTableEntry,?>> ret=new ArrayList<TableColumnController<HouseTableEntry,?>>();

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

  private TableColumnController<HouseTableEntry,?> buildServerColumn()
  {
    CellDataProvider<HouseTableEntry,String> cell=new CellDataProvider<HouseTableEntry,String>()
    {
      @Override
      public String getData(HouseTableEntry item)
      {
        return item.getServer();
      }
    };
    DefaultTableColumnController<HouseTableEntry,String> column=new DefaultTableColumnController<HouseTableEntry,String>(SERVER,"Server",String.class,cell); // I18n
    column.setWidthSpecs(100,100,100);
    return column;
  }

  private TableColumnController<HouseTableEntry,?> buildHomesteadColumn()
  {
    CellDataProvider<HouseTableEntry,String> cell=new CellDataProvider<HouseTableEntry,String>()
    {
      @Override
      public String getData(HouseTableEntry item)
      {
        return item.getNeighborhoodTemplate();
      }
    };
    DefaultTableColumnController<HouseTableEntry,String> column=new DefaultTableColumnController<HouseTableEntry,String>(HOMESTEAD,"Homestead",String.class,cell); // I18n
    column.setWidthSpecs(100,150,150);
    return column;
  }

  private TableColumnController<HouseTableEntry,?> buildNeighborhoodColumn()
  {
    CellDataProvider<HouseTableEntry,String> cell=new CellDataProvider<HouseTableEntry,String>()
    {
      @Override
      public String getData(HouseTableEntry item)
      {
        return item.getNeighborhood();
      }
    };
    DefaultTableColumnController<HouseTableEntry,String> column=new DefaultTableColumnController<HouseTableEntry,String>(NEIGHBORHOOD,"Neighborhood",String.class,cell); // I18n
    column.setWidthSpecs(100,100,100);
    return column;
  }

  private TableColumnController<HouseTableEntry,?> buildAddressColumn()
  {
    CellDataProvider<HouseTableEntry,String> cell=new CellDataProvider<HouseTableEntry,String>()
    {
      @Override
      public String getData(HouseTableEntry item)
      {
        return item.getAddress();
      }
    };
    DefaultTableColumnController<HouseTableEntry,String> column=new DefaultTableColumnController<HouseTableEntry,String>(HOUSE_ADDRESS,"Address",String.class,cell); // I18n
    column.setWidthSpecs(100,150,150);
    return column;
  }

  private TableColumnController<HouseTableEntry,?> buildTypeColumn()
  {
    CellDataProvider<HouseTableEntry,HouseType> cell=new CellDataProvider<HouseTableEntry,HouseType>()
    {
      @Override
      public HouseType getData(HouseTableEntry item)
      {
        return item.getType();
      }
    };
    DefaultTableColumnController<HouseTableEntry,HouseType> column=new DefaultTableColumnController<HouseTableEntry,HouseType>(HOUSE_TYPE,"Type",HouseType.class,cell); // I18n
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
          HouseTableEntry house=(HouseTableEntry)event.getSource();
          showHouse(house);
        }
      }
    };
    _tableController.addActionListener(al);
  }

  private void showHouse(HouseTableEntry house)
  {
    // TODO
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
