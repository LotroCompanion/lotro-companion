package delta.games.lotro.gui.lore.trade.vendor;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.lore.trade.vendor.VendorNpc;
import delta.games.lotro.lore.trade.vendor.VendorsManager;

/**
 * Controller for a table that shows vendors.
 * @author DAM
 */
public class VendorsTableController
{
  // Data
  private TypedProperties _prefs;
  private List<VendorNpc> _vendors;
  // GUI
  private JTable _table;
  private GenericTableController<VendorNpc> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public VendorsTableController(TypedProperties prefs, Filter<VendorNpc> filter)
  {
    _prefs=prefs;
    _vendors=new ArrayList<VendorNpc>();
    init();
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<VendorNpc> buildTable()
  {
    ListDataProvider<VendorNpc> provider=new ListDataProvider<VendorNpc>(_vendors);
    GenericTableController<VendorNpc> table=new GenericTableController<VendorNpc>(provider);
    List<DefaultTableColumnController<VendorNpc,?>> columns=buildColumns();
    for(DefaultTableColumnController<VendorNpc,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<VendorNpc> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a recipes table.
   * @return A list of columns for a recipes table.
   */
  public static List<DefaultTableColumnController<VendorNpc,?>> buildColumns()
  {
    List<DefaultTableColumnController<VendorNpc,?>> ret=new ArrayList<DefaultTableColumnController<VendorNpc,?>>();
    // Identifier column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<VendorNpc,Integer> idCell=new CellDataProvider<VendorNpc,Integer>()
      {
        @Override
        public Integer getData(VendorNpc vendor)
        {
          return Integer.valueOf(vendor.getIdentifier());
        }
      };
      DefaultTableColumnController<VendorNpc,Integer> idColumn=new DefaultTableColumnController<VendorNpc,Integer>(VendorColumnIds.ID.name(),"ID",Integer.class,idCell);
      idColumn.setWidthSpecs(80,80,80);
      ret.add(idColumn);
    }
    // Name column
    {
      CellDataProvider<VendorNpc,String> nameCell=new CellDataProvider<VendorNpc,String>()
      {
        @Override
        public String getData(VendorNpc vendor)
        {
          return vendor.getNpc().getName();
        }
      };
      DefaultTableColumnController<VendorNpc,String> nameColumn=new DefaultTableColumnController<VendorNpc,String>(VendorColumnIds.NAME.name(),"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(150,-1,150);
      ret.add(nameColumn);
    }
    // Title column
    {
      CellDataProvider<VendorNpc,String> titleCell=new CellDataProvider<VendorNpc,String>()
      {
        @Override
        public String getData(VendorNpc vendor)
        {
          return vendor.getNpc().getTitle();
        }
      };
      DefaultTableColumnController<VendorNpc,String> titleColumn=new DefaultTableColumnController<VendorNpc,String>(VendorColumnIds.TITLE.name(),"Title",String.class,titleCell);
      titleColumn.setWidthSpecs(150,-1,150);
      ret.add(titleColumn);
    }
    return ret;
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
      if (UiConfiguration.showTechnicalColumns())
      {
        columnIds.add(VendorColumnIds.ID.name());
      }
      columnIds.add(VendorColumnIds.NAME.name());
      columnIds.add(VendorColumnIds.TITLE.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<VendorNpc> getTableController()
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
   * Get the total number of recipes.
   * @return A number of recipes.
   */
  public int getNbItems()
  {
    return _vendors.size();
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

  private void reset()
  {
    _vendors.clear();
  }

  /**
   * Refresh table.
   */
  public void refresh()
  {
    init();
    if (_table!=null)
    {
      _tableController.refresh();
    }
  }

  private void init()
  {
    reset();
    List<VendorNpc> vendors=VendorsManager.getInstance().getAll();
    for(VendorNpc vendor : vendors)
    {
      _vendors.add(vendor);
    }
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
    _vendors=null;
  }
}
