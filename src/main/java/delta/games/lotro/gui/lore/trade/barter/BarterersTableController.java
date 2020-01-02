package delta.games.lotro.gui.lore.trade.barter;

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
import delta.games.lotro.gui.items.chooser.ItemChooser;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.lore.trade.barter.BarterNpc;
import delta.games.lotro.lore.trade.barter.BarterersManager;

/**
 * Controller for a table that shows barterers.
 * @author DAM
 */
public class BarterersTableController
{
  // Data
  private TypedProperties _prefs;
  private List<BarterNpc> _barterers;
  // GUI
  private JTable _table;
  private GenericTableController<BarterNpc> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public BarterersTableController(TypedProperties prefs, Filter<BarterNpc> filter)
  {
    _prefs=prefs;
    _barterers=new ArrayList<BarterNpc>();
    init();
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<BarterNpc> buildTable()
  {
    ListDataProvider<BarterNpc> provider=new ListDataProvider<BarterNpc>(_barterers);
    GenericTableController<BarterNpc> table=new GenericTableController<BarterNpc>(provider);
    List<DefaultTableColumnController<BarterNpc,?>> columns=buildColumns();
    for(DefaultTableColumnController<BarterNpc,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<BarterNpc> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a recipes table.
   * @return A list of columns for a recipes table.
   */
  public static List<DefaultTableColumnController<BarterNpc,?>> buildColumns()
  {
    List<DefaultTableColumnController<BarterNpc,?>> ret=new ArrayList<DefaultTableColumnController<BarterNpc,?>>();
    // Identifier column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<BarterNpc,Integer> idCell=new CellDataProvider<BarterNpc,Integer>()
      {
        @Override
        public Integer getData(BarterNpc barterer)
        {
          return Integer.valueOf(barterer.getIdentifier());
        }
      };
      DefaultTableColumnController<BarterNpc,Integer> idColumn=new DefaultTableColumnController<BarterNpc,Integer>(BartererColumnIds.ID.name(),"ID",Integer.class,idCell);
      idColumn.setWidthSpecs(80,80,80);
      ret.add(idColumn);
    }
    // Name column
    {
      CellDataProvider<BarterNpc,String> nameCell=new CellDataProvider<BarterNpc,String>()
      {
        @Override
        public String getData(BarterNpc barterer)
        {
          return barterer.getNpc().getName();
        }
      };
      DefaultTableColumnController<BarterNpc,String> nameColumn=new DefaultTableColumnController<BarterNpc,String>(BartererColumnIds.NAME.name(),"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(150,-1,150);
      ret.add(nameColumn);
    }
    // Title column
    {
      CellDataProvider<BarterNpc,String> titleCell=new CellDataProvider<BarterNpc,String>()
      {
        @Override
        public String getData(BarterNpc barterer)
        {
          return barterer.getNpc().getTitle();
        }
      };
      DefaultTableColumnController<BarterNpc,String> titleColumn=new DefaultTableColumnController<BarterNpc,String>(BartererColumnIds.TITLE.name(),"Title",String.class,titleCell);
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
        columnIds.add(BartererColumnIds.ID.name());
      }
      columnIds.add(BartererColumnIds.NAME.name());
      columnIds.add(BartererColumnIds.TITLE.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<BarterNpc> getTableController()
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
    return _barterers.size();
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
    _barterers.clear();
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
    List<BarterNpc> barterers=BarterersManager.getInstance().getAll();
    for(BarterNpc barterer : barterers)
    {
      _barterers.add(barterer);
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
    _barterers=null;
  }
}
