package delta.games.lotro.gui.lore.quests.table;

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
import delta.games.lotro.common.LockType;
import delta.games.lotro.common.Repeatability;
import delta.games.lotro.common.Size;
import delta.games.lotro.common.enums.QuestCategory;
import delta.games.lotro.common.requirements.UsageRequirement;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.gui.common.requirements.table.RequirementsColumnsBuilder;
import delta.games.lotro.gui.common.rewards.table.RewardsColumnIds;
import delta.games.lotro.gui.common.rewards.table.RewardsColumnsBuilder;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for a table that shows quests.
 * @author DAM
 */
public class QuestsTableController extends AbstractAreaController
{
  // Data
  private TypedProperties _prefs;
  private List<QuestDescription> _quests;
  // GUI
  private JTable _table;
  private GenericTableController<QuestDescription> _tableController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public QuestsTableController(AreaController parent, TypedProperties prefs, Filter<QuestDescription> filter)
  {
    super(parent);
    _prefs=prefs;
    _quests=new ArrayList<QuestDescription>();
    init();
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<QuestDescription> buildTable()
  {
    ListDataProvider<QuestDescription> provider=new ListDataProvider<QuestDescription>(_quests);
    GenericTableController<QuestDescription> table=new GenericTableController<QuestDescription>(provider);
    List<TableColumnController<QuestDescription,?>> columns=buildColumns(this);
    for(TableColumnController<QuestDescription,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<QuestDescription> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build a 'ID' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,Integer> buildQuestIDColumn()
  {
    CellDataProvider<QuestDescription,Integer> cell=new CellDataProvider<QuestDescription,Integer>()
    {
      @Override
      public Integer getData(QuestDescription quest)
      {
        return Integer.valueOf(quest.getIdentifier());
      }
    };
    String columnName=Labels.getLabel("quests.table.column.id");
    DefaultTableColumnController<QuestDescription,Integer> column=new DefaultTableColumnController<QuestDescription,Integer>(QuestColumnIds.ID.name(),columnName,Integer.class,cell);
    column.setWidthSpecs(100,100,100);
    return column;
  }

  /**
   * Build a 'category' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,QuestCategory> buildQuestCategoryColumn()
  {
    CellDataProvider<QuestDescription,QuestCategory> cell=new CellDataProvider<QuestDescription,QuestCategory>()
    {
      @Override
      public QuestCategory getData(QuestDescription quest)
      {
        return quest.getCategory();
      }
    };
    String columnName=Labels.getLabel("quests.table.column.category");
    DefaultTableColumnController<QuestDescription,QuestCategory> column=new DefaultTableColumnController<QuestDescription,QuestCategory>(QuestColumnIds.CATEGORY.name(),columnName,QuestCategory.class,cell);
    column.setWidthSpecs(80,350,80);
    return column;
  }

  /**
   * Build a 'quest arc' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,String> buildQuestArcColumn()
  {
    CellDataProvider<QuestDescription,String> cell=new CellDataProvider<QuestDescription,String>()
    {
      @Override
      public String getData(QuestDescription quest)
      {
        return quest.getQuestArc();
      }
    };
    String columnName=Labels.getLabel("quests.table.column.questArc");
    DefaultTableColumnController<QuestDescription,String> column=new DefaultTableColumnController<QuestDescription,String>(QuestColumnIds.QUEST_ARC.name(),columnName,String.class,cell);
    column.setWidthSpecs(80,350,80);
    return column;
  }

  /**
   * Build a 'quest size' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,Size> buildQuestSizeColumn()
  {
    CellDataProvider<QuestDescription,Size> sizeCell=new CellDataProvider<QuestDescription,Size>()
    {
      @Override
      public Size getData(QuestDescription quest)
      {
        return quest.getSize();
      }
    };
    String columnName=Labels.getLabel("quests.table.column.size");
    DefaultTableColumnController<QuestDescription,Size> sizeColumn=new DefaultTableColumnController<QuestDescription,Size>(QuestColumnIds.SIZE.name(),columnName,Size.class,sizeCell);
    sizeColumn.setWidthSpecs(100,100,100);
    return sizeColumn;
  }

  /**
   * Build a 'monster play' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,Boolean> buildMonsterPlayColumn()
  {
    CellDataProvider<QuestDescription,Boolean> cell=new CellDataProvider<QuestDescription,Boolean>()
    {
      @Override
      public Boolean getData(QuestDescription quest)
      {
        return Boolean.valueOf(quest.isMonsterPlay());
      }
    };
    String columnName=Labels.getLabel("quests.table.column.monsterPlay");
    DefaultTableColumnController<QuestDescription,Boolean> column=new DefaultTableColumnController<QuestDescription,Boolean>(QuestColumnIds.MONSTER_PLAY.name(),columnName,Boolean.class,cell);
    column.setWidthSpecs(100,100,100);
    return column;
  }

  /**
   * Build a 'repeatability' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,Repeatability> buildRepeatabilityColumn()
  {
    CellDataProvider<QuestDescription,Repeatability> cell=new CellDataProvider<QuestDescription,Repeatability>()
    {
      @Override
      public Repeatability getData(QuestDescription quest)
      {
        return quest.getRepeatability();
      }
    };
    String columnName=Labels.getLabel("quests.table.column.repeatability");
    DefaultTableColumnController<QuestDescription,Repeatability> column=new DefaultTableColumnController<QuestDescription,Repeatability>(QuestColumnIds.REPEATABLE.name(),columnName,Repeatability.class,cell);
    column.setWidthSpecs(100,100,100);
    return column;
  }

  /**
   * Build a 'lock type' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,LockType> buildLockTypeColumn()
  {
    CellDataProvider<QuestDescription,LockType> cell=new CellDataProvider<QuestDescription,LockType>()
    {
      @Override
      public LockType getData(QuestDescription quest)
      {
        return quest.getLockType();
      }
    };
    String columnName=Labels.getLabel("quests.table.column.lock");
    DefaultTableColumnController<QuestDescription,LockType> column=new DefaultTableColumnController<QuestDescription,LockType>(QuestColumnIds.LOCK_TYPE.name(),columnName,LockType.class,cell);
    column.setWidthSpecs(60,60,60);
    return column;
  }

  /**
   * Build a 'instanced' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,Boolean> buildInstancedColumn()
  {
    CellDataProvider<QuestDescription,Boolean> cell=new CellDataProvider<QuestDescription,Boolean>()
    {
      @Override
      public Boolean getData(QuestDescription quest)
      {
        return Boolean.valueOf(quest.isInstanced());
      }
    };
    String columnName=Labels.getLabel("quests.table.column.instanced");
    DefaultTableColumnController<QuestDescription,Boolean> column=new DefaultTableColumnController<QuestDescription,Boolean>(QuestColumnIds.INSTANCED.name(),columnName,Boolean.class,cell);
    column.setWidthSpecs(100,100,100);
    return column;
  }

  /**
   * Build a 'shareable' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,Boolean> buildShareableColumn()
  {
    CellDataProvider<QuestDescription,Boolean> cell=new CellDataProvider<QuestDescription,Boolean>()
    {
      @Override
      public Boolean getData(QuestDescription quest)
      {
        return Boolean.valueOf(quest.isShareable());
      }
    };
    String columnName=Labels.getLabel("quests.table.column.shareable");
    DefaultTableColumnController<QuestDescription,Boolean> column=new DefaultTableColumnController<QuestDescription,Boolean>(QuestColumnIds.SHAREABLE.name(),columnName,Boolean.class,cell);
    column.setWidthSpecs(100,100,100);
    return column;
  }

  /**
   * Build a 'session play' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,Boolean> buildSessionPlayColumn()
  {
    CellDataProvider<QuestDescription,Boolean> cell=new CellDataProvider<QuestDescription,Boolean>()
    {
      @Override
      public Boolean getData(QuestDescription quest)
      {
        return Boolean.valueOf(quest.isSessionPlay());
      }
    };
    String columnName=Labels.getLabel("quests.table.column.sessionPlay");
    DefaultTableColumnController<QuestDescription,Boolean> column=new DefaultTableColumnController<QuestDescription,Boolean>(QuestColumnIds.SESSION_PLAY.name(),columnName,Boolean.class,cell);
    column.setWidthSpecs(100,100,100);
    return column;
  }

  /**
   * Build a 'autobestowed' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,Boolean> buildAutobestowedColumn()
  {
    CellDataProvider<QuestDescription,Boolean> cell=new CellDataProvider<QuestDescription,Boolean>()
    {
      @Override
      public Boolean getData(QuestDescription quest)
      {
        return Boolean.valueOf(quest.isAutoBestowed());
      }
    };
    String columnName=Labels.getLabel("quests.table.column.autoBestowed");
    DefaultTableColumnController<QuestDescription,Boolean> column=new DefaultTableColumnController<QuestDescription,Boolean>(QuestColumnIds.AUTO_BESTOWED.name(),columnName,Boolean.class,cell);
    column.setWidthSpecs(100,100,100);
    return column;
  }

  /**
   * Build a 'hidden' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,Boolean> buildHiddenColumn()
  {
    CellDataProvider<QuestDescription,Boolean> cell=new CellDataProvider<QuestDescription,Boolean>()
    {
      @Override
      public Boolean getData(QuestDescription quest)
      {
        return Boolean.valueOf(quest.isHidden());
      }
    };
    String columnName=Labels.getLabel("quests.table.column.hidden");
    DefaultTableColumnController<QuestDescription,Boolean> column=new DefaultTableColumnController<QuestDescription,Boolean>(QuestColumnIds.OBSOLETE.name(),columnName,Boolean.class,cell);
    column.setWidthSpecs(100,100,100);
    return column;
  }

  /**
   * Build a 'description' column.
   * @param parent Parent controller.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,String> buildDescriptionColumn(AreaController parent)
  {
    CellDataProvider<QuestDescription,String> descriptionCell=new CellDataProvider<QuestDescription,String>()
    {
      @Override
      public String getData(QuestDescription quest)
      {
        String description=quest.getDescription();
        description=ContextRendering.render(parent,description);
        return description;
      }
    };
    String columnName=Labels.getLabel("quests.table.column.description");
    DefaultTableColumnController<QuestDescription,String> descriptionColumn=new DefaultTableColumnController<QuestDescription,String>(QuestColumnIds.DESCRIPTION.name(),columnName,String.class,descriptionCell);
    descriptionColumn.setWidthSpecs(100,-1,100);
    return descriptionColumn;
  }

  /**
   * Build a 'infamy' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,Integer> buildInfamyColumn()
  {
    CellDataProvider<QuestDescription,Integer> cell=new CellDataProvider<QuestDescription,Integer>()
    {
      @Override
      public Integer getData(QuestDescription quest)
      {
        if (quest.isMonsterPlay())
        {
          int glory=quest.getRewards().getGlory();
          return glory>0?Integer.valueOf(glory):null;
        }
        return null;
      }
    };
    String columnName=Labels.getLabel("quests.table.column.infamy");
    DefaultTableColumnController<QuestDescription,Integer> column=new DefaultTableColumnController<QuestDescription,Integer>(RewardsColumnIds.INFAMY.name(),columnName,Integer.class,cell);
    column.setWidthSpecs(60,60,60);
    return column;
  }

  /**
   * Build a 'renown' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,Integer> buildRenownColumn()
  {
    CellDataProvider<QuestDescription,Integer> cell=new CellDataProvider<QuestDescription,Integer>()
    {
      @Override
      public Integer getData(QuestDescription quest)
      {
        if (quest.isMonsterPlay())
        {
          return null;
        }
        int glory=quest.getRewards().getGlory();
        return glory>0?Integer.valueOf(glory):null;
      }
    };
    String columnName=Labels.getLabel("quests.table.column.renown");
    DefaultTableColumnController<QuestDescription,Integer> column=new DefaultTableColumnController<QuestDescription,Integer>(RewardsColumnIds.RENOWN.name(),columnName,Integer.class,cell);
    column.setWidthSpecs(60,60,60);
    return column;
  }

  /**
   * Build the columns for a quests table.
   * @param parent Parent controller.
   * @return A list of columns for a quests table.
   */
  public static List<TableColumnController<QuestDescription,?>> buildColumns(AreaController parent)
  {
    List<TableColumnController<QuestDescription,?>> ret=new ArrayList<TableColumnController<QuestDescription,?>>();
    // Identifier column
    if (UiConfiguration.showTechnicalColumns())
    {
      ret.add(buildQuestIDColumn());
    }
    // Name column
    ret.add(QuestsColumnsBuilder.buildQuestNameColumn());
    // Category column
    ret.add(buildQuestCategoryColumn());
    // Quest arc column
    ret.add(buildQuestArcColumn());
    // Challenge level column
    ret.add(QuestsColumnsBuilder.buildQuestChallengeColumn());
    // Recommended size column
    ret.add(buildQuestSizeColumn());
    // Monster Play column
    ret.add(buildMonsterPlayColumn());
    // Repeatable column
    ret.add(buildRepeatabilityColumn());
    // Lock type column
    ret.add(buildLockTypeColumn());
    // Instanced column
    ret.add(buildInstancedColumn());
    // Shareable column
    ret.add(buildShareableColumn());
    // Session play column
    ret.add(buildSessionPlayColumn());
    // Auto-bestowed column
    ret.add(buildAutobestowedColumn());
    // Hidden column
    ret.add(buildHiddenColumn());
    // Description column
    ret.add(buildDescriptionColumn(parent));
    // Requirements
    {
      List<DefaultTableColumnController<UsageRequirement,?>> requirementColumns=RequirementsColumnsBuilder.buildRequirementsColumns();
      CellDataProvider<QuestDescription,UsageRequirement> dataProvider=new CellDataProvider<QuestDescription,UsageRequirement>()
      {
        @Override
        public UsageRequirement getData(QuestDescription quest)
        {
          return quest.getUsageRequirement();
        }
      };
      for(DefaultTableColumnController<UsageRequirement,?> requirementColumn : requirementColumns)
      {
        @SuppressWarnings("unchecked")
        TableColumnController<UsageRequirement,Object> c=(TableColumnController<UsageRequirement,Object>)requirementColumn;
        TableColumnController<QuestDescription,Object> proxiedColumn=new ProxiedTableColumnController<QuestDescription,UsageRequirement,Object>(c,dataProvider);
        ret.add(proxiedColumn);
      }
    }
    // Rewards
    {
      List<DefaultTableColumnController<Rewards,?>> rewardColumns=RewardsColumnsBuilder.buildRewardColumns(parent);
      CellDataProvider<QuestDescription,Rewards> dataProvider=new CellDataProvider<QuestDescription,Rewards>()
      {
        @Override
        public Rewards getData(QuestDescription quest)
        {
          return quest.getRewards();
        }
      };
      for(DefaultTableColumnController<Rewards,?> rewardColumn : rewardColumns)
      {
        @SuppressWarnings("unchecked")
        TableColumnController<Rewards,Object> c=(TableColumnController<Rewards,Object>)rewardColumn;
        TableColumnController<QuestDescription,Object> proxiedColumn=new ProxiedTableColumnController<QuestDescription,Rewards,Object>(c,dataProvider);
        ret.add(proxiedColumn);
      }
    }
    // Infamy
    ret.add(buildInfamyColumn());
    // Renown
    ret.add(buildRenownColumn());
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
      columnIds.add(QuestColumnIds.NAME.name());
      columnIds.add(QuestColumnIds.CATEGORY.name());
      columnIds.add(QuestColumnIds.LEVEL.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<QuestDescription> getTableController()
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
   * Get the total number of quests.
   * @return A number of quests.
   */
  public int getNbItems()
  {
    return _quests.size();
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
    _quests.clear();
  }

  private void init()
  {
    reset();
    QuestsManager manager=QuestsManager.getInstance();
    List<QuestDescription> quests=manager.getAll();
    for(QuestDescription quest : quests)
    {
      _quests.add(quest);
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

  @Override
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
    _quests=null;
  }
}
