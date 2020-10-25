package delta.games.lotro.gui.lore.instances;

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
import delta.games.lotro.lore.instances.InstancesTree;
import delta.games.lotro.lore.instances.SkirmishPrivateEncounter;

/**
 * Controller for a table that shows instances.
 * @author DAM
 */
public class InstancesTableController
{
  // Data
  private TypedProperties _prefs;
  private List<SkirmishPrivateEncounter> _instances;
  // GUI
  private JTable _table;
  private GenericTableController<SkirmishPrivateEncounter> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public InstancesTableController(TypedProperties prefs, Filter<SkirmishPrivateEncounter> filter)
  {
    _prefs=prefs;
    _instances=new ArrayList<SkirmishPrivateEncounter>();
    init();
    _tableController=buildTable();
    _tableController.setFilter(filter);
    getTable();
  }

  private GenericTableController<SkirmishPrivateEncounter> buildTable()
  {
    ListDataProvider<SkirmishPrivateEncounter> provider=new ListDataProvider<SkirmishPrivateEncounter>(_instances);
    GenericTableController<SkirmishPrivateEncounter> table=new GenericTableController<SkirmishPrivateEncounter>(provider);
    List<DefaultTableColumnController<SkirmishPrivateEncounter,?>> columns=buildColumns();
    for(DefaultTableColumnController<SkirmishPrivateEncounter,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<SkirmishPrivateEncounter> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for an instances table.
   * @return A list of columns for an instances table.
   */
  public static List<DefaultTableColumnController<SkirmishPrivateEncounter,?>> buildColumns()
  {
    List<DefaultTableColumnController<SkirmishPrivateEncounter,?>> ret=new ArrayList<DefaultTableColumnController<SkirmishPrivateEncounter,?>>();
    // Identifier column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<SkirmishPrivateEncounter,Integer> idCell=new CellDataProvider<SkirmishPrivateEncounter,Integer>()
      {
        @Override
        public Integer getData(SkirmishPrivateEncounter instance)
        {
          return Integer.valueOf(instance.getIdentifier());
        }
      };
      DefaultTableColumnController<SkirmishPrivateEncounter,Integer> idColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,Integer>(InstanceColumnIds.ID.name(),"ID",Integer.class,idCell);
      idColumn.setWidthSpecs(80,80,80);
      ret.add(idColumn);
    }
    // Name column
    {
      CellDataProvider<SkirmishPrivateEncounter,String> nameCell=new CellDataProvider<SkirmishPrivateEncounter,String>()
      {
        @Override
        public String getData(SkirmishPrivateEncounter instance)
        {
          return instance.getName();
        }
      };
      DefaultTableColumnController<SkirmishPrivateEncounter,String> nameColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,String>(InstanceColumnIds.NAME.name(),"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,-1,250);
      ret.add(nameColumn);
    }
    // Type column
    {
      CellDataProvider<SkirmishPrivateEncounter,String> typeCell=new CellDataProvider<SkirmishPrivateEncounter,String>()
      {
        @Override
        public String getData(SkirmishPrivateEncounter instance)
        {
          return instance.getType();
        }
      };
      DefaultTableColumnController<SkirmishPrivateEncounter,String> typeColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,String>(InstanceColumnIds.TYPE.name(),"Type",String.class,typeCell);
      typeColumn.setWidthSpecs(100,140,140);
      ret.add(typeColumn);
    }
    // Category column
    {
      CellDataProvider<SkirmishPrivateEncounter,String> categoryCell=new CellDataProvider<SkirmishPrivateEncounter,String>()
      {
        @Override
        public String getData(SkirmishPrivateEncounter instance)
        {
          return instance.getCategory();
        }
      };
      DefaultTableColumnController<SkirmishPrivateEncounter,String> categoryColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,String>(InstanceColumnIds.CATEGORY.name(),"Category",String.class,categoryCell);
      categoryColumn.setWidthSpecs(100,220,220);
      ret.add(categoryColumn);
    }
    // Maximum number of players
    {
      CellDataProvider<SkirmishPrivateEncounter,Integer> maxPlayersCell=new CellDataProvider<SkirmishPrivateEncounter,Integer>()
      {
        @Override
        public Integer getData(SkirmishPrivateEncounter instance)
        {
          return instance.getMaxPlayers();
        }
      };
      DefaultTableColumnController<SkirmishPrivateEncounter,Integer> maxPlayersColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,Integer>(InstanceColumnIds.MAX_PLAYERS.name(),"Max players",Integer.class,maxPlayersCell);
      maxPlayersColumn.setWidthSpecs(50,50,50);
      ret.add(maxPlayersColumn);
    }
    // Minimum scale level
    {
      CellDataProvider<SkirmishPrivateEncounter,Integer> minLevelCell=new CellDataProvider<SkirmishPrivateEncounter,Integer>()
      {
        @Override
        public Integer getData(SkirmishPrivateEncounter instance)
        {
          return Integer.valueOf(instance.getMinLevelScale());
        }
      };
      DefaultTableColumnController<SkirmishPrivateEncounter,Integer> minLevelColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,Integer>(InstanceColumnIds.MIN_LEVEL.name(),"Min scale",Integer.class,minLevelCell);
      minLevelColumn.setWidthSpecs(50,50,50);
      ret.add(minLevelColumn);
    }
    // Maximum scale level
    {
      CellDataProvider<SkirmishPrivateEncounter,Integer> maxLevelCell=new CellDataProvider<SkirmishPrivateEncounter,Integer>()
      {
        @Override
        public Integer getData(SkirmishPrivateEncounter instance)
        {
          return Integer.valueOf(instance.getMaxLevelScale());
        }
      };
      DefaultTableColumnController<SkirmishPrivateEncounter,Integer> maxLevelColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,Integer>(InstanceColumnIds.MAX_LEVEL.name(),"Max scale",Integer.class,maxLevelCell);
      maxLevelColumn.setWidthSpecs(50,50,50);
      ret.add(maxLevelColumn);
    }
    // Level scaling
    {
      CellDataProvider<SkirmishPrivateEncounter,Integer> scalingCell=new CellDataProvider<SkirmishPrivateEncounter,Integer>()
      {
        @Override
        public Integer getData(SkirmishPrivateEncounter instance)
        {
          return instance.getLevelScaling();
        }
      };
      DefaultTableColumnController<SkirmishPrivateEncounter,Integer> scalingColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,Integer>(InstanceColumnIds.LEVEL_SCALING.name(),"Scaling",Integer.class,scalingCell);
      scalingColumn.setWidthSpecs(50,50,50);
      ret.add(scalingColumn);
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
        columnIds.add(InstanceColumnIds.ID.name());
      }
      columnIds.add(InstanceColumnIds.NAME.name());
      columnIds.add(InstanceColumnIds.TYPE.name());
      columnIds.add(InstanceColumnIds.CATEGORY.name());
      columnIds.add(InstanceColumnIds.MAX_PLAYERS.name());
      columnIds.add(InstanceColumnIds.MIN_LEVEL.name());
      columnIds.add(InstanceColumnIds.MAX_LEVEL.name());
      columnIds.add(InstanceColumnIds.LEVEL_SCALING.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<SkirmishPrivateEncounter> getTableController()
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
   * Get the total number of mounts.
   * @return A number of mounts.
   */
  public int getNbItems()
  {
    return _instances.size();
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
    init();
    if (_table!=null)
    {
      _tableController.refresh();
    }
  }

  private void init()
  {
    _instances.clear();
    List<SkirmishPrivateEncounter> instances=InstancesTree.getInstance().getInstances();
    _instances.addAll(instances);
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
    _instances=null;
  }
}
