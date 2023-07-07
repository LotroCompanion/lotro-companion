package delta.games.lotro.gui.lore.deeds.table;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.area.AbstractAreaController;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.ChallengeLevel;
import delta.games.lotro.common.ChallengeLevelComparator;
import delta.games.lotro.common.enums.DeedCategory;
import delta.games.lotro.common.requirements.UsageRequirement;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.gui.common.requirements.table.RequirementsColumnsBuilder;
import delta.games.lotro.gui.common.rewards.table.RewardsColumnsBuilder;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.deeds.DeedsManager;

/**
 * Controller for a table that shows deeds.
 * @author DAM
 */
public class DeedsTableController extends AbstractAreaController
{
  // Data
  private TypedProperties _prefs;
  private List<DeedDescription> _deeds;
  // GUI
  private JTable _table;
  private GenericTableController<DeedDescription> _tableController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public DeedsTableController(AreaController parent,TypedProperties prefs, Filter<DeedDescription> filter)
  {
    super(parent);
    _prefs=prefs;
    _deeds=new ArrayList<DeedDescription>();
    init();
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<DeedDescription> buildTable()
  {
    ListDataProvider<DeedDescription> provider=new ListDataProvider<DeedDescription>(_deeds);
    GenericTableController<DeedDescription> table=new GenericTableController<DeedDescription>(provider);
    List<TableColumnController<DeedDescription,?>> columns=buildColumns(this);
    for(TableColumnController<DeedDescription,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<DeedDescription> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a deeds table.
   * @param parent Parent controller.
   * @return A list of columns for a deeds table.
   */
  public static List<TableColumnController<DeedDescription,?>> buildColumns(AreaController parent)
  {
    List<TableColumnController<DeedDescription,?>> ret=new ArrayList<TableColumnController<DeedDescription,?>>();
    // Identifier column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<DeedDescription,Integer> idCell=new CellDataProvider<DeedDescription,Integer>()
      {
        @Override
        public Integer getData(DeedDescription deed)
        {
          return Integer.valueOf(deed.getIdentifier());
        }
      };
      DefaultTableColumnController<DeedDescription,Integer> idColumn=new DefaultTableColumnController<DeedDescription,Integer>(DeedColumnIds.ID.name(),"ID",Integer.class,idCell); // 18n
      idColumn.setWidthSpecs(100,100,100);
      ret.add(idColumn);
    }
    // Name column
    {
      CellDataProvider<DeedDescription,String> nameCell=new CellDataProvider<DeedDescription,String>()
      {
        @Override
        public String getData(DeedDescription deed)
        {
          return deed.getName();
        }
      };
      DefaultTableColumnController<DeedDescription,String> nameColumn=new DefaultTableColumnController<DeedDescription,String>(DeedColumnIds.NAME.name(),"Name",String.class,nameCell); // 18n
      nameColumn.setWidthSpecs(100,300,200);
      ret.add(nameColumn);
    }
    // Type column
    {
      CellDataProvider<DeedDescription,DeedType> typeCell=new CellDataProvider<DeedDescription,DeedType>()
      {
        @Override
        public DeedType getData(DeedDescription deed)
        {
          return deed.getType();
        }
      };
      DefaultTableColumnController<DeedDescription,DeedType> typeColumn=new DefaultTableColumnController<DeedDescription,DeedType>(DeedColumnIds.TYPE.name(),"Type",DeedType.class,typeCell); // 18n
      typeColumn.setWidthSpecs(80,100,80);
      ret.add(typeColumn);
    }
    // Category column
    {
      CellDataProvider<DeedDescription,DeedCategory> categoryCell=new CellDataProvider<DeedDescription,DeedCategory>()
      {
        @Override
        public DeedCategory getData(DeedDescription deed)
        {
          return deed.getCategory();
        }
      };
      DefaultTableColumnController<DeedDescription,DeedCategory> categoryColumn=new DefaultTableColumnController<DeedDescription,DeedCategory>(DeedColumnIds.CATEGORY.name(),"Category",DeedCategory.class,categoryCell); // 18n
      categoryColumn.setWidthSpecs(80,350,80);
      ret.add(categoryColumn);
    }
    // Challenge level column
    {
      CellDataProvider<DeedDescription,ChallengeLevel> levelCell=new CellDataProvider<DeedDescription,ChallengeLevel>()
      {
        @Override
        public ChallengeLevel getData(DeedDescription deed)
        {
          return deed.getChallengeLevel();
        }
      };
      DefaultTableColumnController<DeedDescription,ChallengeLevel> levelColumn=new DefaultTableColumnController<DeedDescription,ChallengeLevel>(DeedColumnIds.LEVEL.name(),"Level",ChallengeLevel.class,levelCell); // 18n
      levelColumn.setWidthSpecs(100,100,100);
      levelColumn.setComparator(new ChallengeLevelComparator());
      ret.add(levelColumn);
    }
    // Hidden column
    {
      CellDataProvider<DeedDescription,Boolean> hiddenCell=new CellDataProvider<DeedDescription,Boolean>()
      {
        @Override
        public Boolean getData(DeedDescription deed)
        {
          return Boolean.valueOf(deed.isHidden());
        }
      };
      DefaultTableColumnController<DeedDescription,Boolean> hiddenColumn=new DefaultTableColumnController<DeedDescription,Boolean>(DeedColumnIds.OBSOLETE.name(),"Hidden",Boolean.class,hiddenCell); // 18n
      hiddenColumn.setWidthSpecs(100,100,100);
      ret.add(hiddenColumn);
    }
    // Monster Play column
    {
      CellDataProvider<DeedDescription,Boolean> monsterPlayCell=new CellDataProvider<DeedDescription,Boolean>()
      {
        @Override
        public Boolean getData(DeedDescription deed)
        {
          return Boolean.valueOf(deed.isMonsterPlay());
        }
      };
      DefaultTableColumnController<DeedDescription,Boolean> monsterPlayColumn=new DefaultTableColumnController<DeedDescription,Boolean>(DeedColumnIds.MONSTER_PLAY.name(),"Monster Play",Boolean.class,monsterPlayCell); // 18n
      monsterPlayColumn.setWidthSpecs(100,100,100);
      ret.add(monsterPlayColumn);
    }
    // Requirements
    {
      List<DefaultTableColumnController<UsageRequirement,?>> requirementColumns=RequirementsColumnsBuilder.buildRequirementsColumns();
      CellDataProvider<DeedDescription,UsageRequirement> dataProvider=new CellDataProvider<DeedDescription,UsageRequirement>()
      {
        @Override
        public UsageRequirement getData(DeedDescription deed)
        {
          return deed.getUsageRequirement();
        }
      };
      for(DefaultTableColumnController<UsageRequirement,?> requirementColumn : requirementColumns)
      {
        @SuppressWarnings("unchecked")
        TableColumnController<UsageRequirement,Object> c=(TableColumnController<UsageRequirement,Object>)requirementColumn;
        TableColumnController<DeedDescription,Object> proxiedColumn=new ProxiedTableColumnController<DeedDescription,UsageRequirement,Object>(c,dataProvider);
        ret.add(proxiedColumn);
      }
    }
    // Rewards
    {
      List<DefaultTableColumnController<Rewards,?>> rewardColumns=RewardsColumnsBuilder.buildRewardColumns(parent);
      CellDataProvider<DeedDescription,Rewards> dataProvider=new CellDataProvider<DeedDescription,Rewards>()
      {
        @Override
        public Rewards getData(DeedDescription deed)
        {
          return deed.getRewards();
        }
      };
      for(DefaultTableColumnController<Rewards,?> rewardColumn : rewardColumns)
      {
        @SuppressWarnings("unchecked")
        TableColumnController<Rewards,Object> c=(TableColumnController<Rewards,Object>)rewardColumn;
        TableColumnController<DeedDescription,Object> proxiedColumn=new ProxiedTableColumnController<DeedDescription,Rewards,Object>(c,dataProvider);
        ret.add(proxiedColumn);
      }
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
      columnIds.add(DeedColumnIds.NAME.name());
      columnIds.add(DeedColumnIds.TYPE.name());
      columnIds.add(DeedColumnIds.CATEGORY.name());
      columnIds.add(DeedColumnIds.LEVEL.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<DeedDescription> getTableController()
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
   * Get the total number of deeds.
   * @return A number of deeds.
   */
  public int getNbItems()
  {
    return _deeds.size();
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
    _deeds.clear();
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

  /**
   * Refresh table.
   * @param deed Deed to refresh.
   */
  public void refresh(DeedDescription deed)
  {
    if (_table!=null)
    {
      _tableController.refresh(deed);
    }
  }

  private void init()
  {
    reset();
    DeedsManager manager=DeedsManager.getInstance();
    List<DeedDescription> deeds=manager.getAll();
    for(DeedDescription deed : deeds)
    {
      _deeds.add(deed);
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
    super.dispose();
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
    _deeds=null;
  }
}
