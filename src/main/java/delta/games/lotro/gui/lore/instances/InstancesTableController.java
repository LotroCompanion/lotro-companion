package delta.games.lotro.gui.lore.instances;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.enums.WJEncounterCategory;
import delta.games.lotro.common.enums.WJEncounterType;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.lore.instances.InstanceTreeEntry;
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
  private List<InstanceTreeEntry> _instances;
  // GUI
  private JTable _table;
  private GenericTableController<InstanceTreeEntry> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public InstancesTableController(TypedProperties prefs, InstanceEntriesFilter filter)
  {
    _prefs=prefs;
    _instances=new ArrayList<InstanceTreeEntry>();
    init();
    _tableController=buildTable();
    _tableController.setFilter(filter);
    getTable();
  }

  private GenericTableController<InstanceTreeEntry> buildTable()
  {
    ListDataProvider<InstanceTreeEntry> provider=new ListDataProvider<InstanceTreeEntry>(_instances);
    GenericTableController<InstanceTreeEntry> table=new GenericTableController<InstanceTreeEntry>(provider);
    // Private encounter columns
    CellDataProvider<InstanceTreeEntry,SkirmishPrivateEncounter> peProvider=new CellDataProvider<InstanceTreeEntry,SkirmishPrivateEncounter>()
    {
      public SkirmishPrivateEncounter getData(InstanceTreeEntry entry)
      {
        return entry.getInstance();
      }
    };
    List<DefaultTableColumnController<SkirmishPrivateEncounter,?>> columns=buildColumns();
    for(DefaultTableColumnController<SkirmishPrivateEncounter,?> column : columns)
    {
      ProxiedTableColumnController<InstanceTreeEntry,SkirmishPrivateEncounter,?> proxyColumn=new ProxiedTableColumnController<>(column,peProvider);
      table.addColumnController(proxyColumn);
    }
    // Instance category column
    table.addColumnController(buildCategoryColumn());
    TableColumnsManager<InstanceTreeEntry> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  private TableColumnController<InstanceTreeEntry,String> buildCategoryColumn()
  {
    CellDataProvider<InstanceTreeEntry,String> groupNameCell=new CellDataProvider<InstanceTreeEntry,String>()
    {
      @Override
      public String getData(InstanceTreeEntry instance)
      {
        return instance.getName();
      }
    };
    DefaultTableColumnController<InstanceTreeEntry,String> groupNameColumn=new DefaultTableColumnController<InstanceTreeEntry,String>(InstanceColumnIds.GROUP_NAME.name(),"Group",String.class,groupNameCell); // I18n
    groupNameColumn.setWidthSpecs(100,150,150);
    return groupNameColumn;
  }

  /**
   * Build the columns for an instances table.
   * @return A list of columns for an instances table.
   */
  private static List<DefaultTableColumnController<SkirmishPrivateEncounter,?>> buildColumns()
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
      DefaultTableColumnController<SkirmishPrivateEncounter,Integer> idColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,Integer>(InstanceColumnIds.ID.name(),"ID",Integer.class,idCell); // I18n
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
      DefaultTableColumnController<SkirmishPrivateEncounter,String> nameColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,String>(InstanceColumnIds.NAME.name(),"Name",String.class,nameCell); // I18n
      nameColumn.setWidthSpecs(100,-1,250);
      ret.add(nameColumn);
    }
    // Type column
    {
      CellDataProvider<SkirmishPrivateEncounter,WJEncounterType> typeCell=new CellDataProvider<SkirmishPrivateEncounter,WJEncounterType>()
      {
        @Override
        public WJEncounterType getData(SkirmishPrivateEncounter instance)
        {
          return instance.getType();
        }
      };
      DefaultTableColumnController<SkirmishPrivateEncounter,WJEncounterType> typeColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,WJEncounterType>(InstanceColumnIds.TYPE.name(),"Type",WJEncounterType.class,typeCell); // I18n
      typeColumn.setWidthSpecs(100,140,140);
      ret.add(typeColumn);
    }
    // Category column
    {
      CellDataProvider<SkirmishPrivateEncounter,WJEncounterCategory> categoryCell=new CellDataProvider<SkirmishPrivateEncounter,WJEncounterCategory>()
      {
        @Override
        public WJEncounterCategory getData(SkirmishPrivateEncounter instance)
        {
          return instance.getCategory();
        }
      };
      DefaultTableColumnController<SkirmishPrivateEncounter,WJEncounterCategory> categoryColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,WJEncounterCategory>(InstanceColumnIds.CATEGORY.name(),"Category",WJEncounterCategory.class,categoryCell); // I18n
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
      DefaultTableColumnController<SkirmishPrivateEncounter,Integer> maxPlayersColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,Integer>(InstanceColumnIds.MAX_PLAYERS.name(),"Max players",Integer.class,maxPlayersCell); // I18n
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
      DefaultTableColumnController<SkirmishPrivateEncounter,Integer> minLevelColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,Integer>(InstanceColumnIds.MIN_LEVEL.name(),"Min scale",Integer.class,minLevelCell); // I18n
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
      DefaultTableColumnController<SkirmishPrivateEncounter,Integer> maxLevelColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,Integer>(InstanceColumnIds.MAX_LEVEL.name(),"Max scale",Integer.class,maxLevelCell); // I18n
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
      DefaultTableColumnController<SkirmishPrivateEncounter,Integer> scalingColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,Integer>(InstanceColumnIds.LEVEL_SCALING.name(),"Scaling",Integer.class,scalingCell); // I18n
      scalingColumn.setWidthSpecs(50,50,50);
      ret.add(scalingColumn);
    }
    // Scalable
    {
      CellDataProvider<SkirmishPrivateEncounter,Boolean> scalableCell=new CellDataProvider<SkirmishPrivateEncounter,Boolean>()
      {
        @Override
        public Boolean getData(SkirmishPrivateEncounter instance)
        {
          return Boolean.valueOf(instance.isScalable());
        }
      };
      DefaultTableColumnController<SkirmishPrivateEncounter,Boolean> scalableColumn=new DefaultTableColumnController<SkirmishPrivateEncounter,Boolean>(InstanceColumnIds.SCALABLE.name(),"Scalable",Boolean.class,scalableCell); // I18n
      scalableColumn.setWidthSpecs(30,30,30);
      ret.add(scalableColumn);
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
      columnIds.add(InstanceColumnIds.GROUP_NAME.name());
      columnIds.add(InstanceColumnIds.CATEGORY.name());
      columnIds.add(InstanceColumnIds.MAX_PLAYERS.name());
      columnIds.add(InstanceColumnIds.MIN_LEVEL.name());
      columnIds.add(InstanceColumnIds.MAX_LEVEL.name());
      columnIds.add(InstanceColumnIds.LEVEL_SCALING.name());
      columnIds.add(InstanceColumnIds.SCALABLE.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<InstanceTreeEntry> getTableController()
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
    List<InstanceTreeEntry> instances=InstancesTree.getInstance().getEntries();
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
