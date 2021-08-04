package delta.games.lotro.gui.character.status.tasks.table;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.character.status.tasks.TaskStatus;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.gui.character.status.achievables.table.AchievableStatusColumnsBuilder;
import delta.games.lotro.gui.common.rewards.table.RewardsColumnsBuilder;
import delta.games.lotro.gui.lore.items.chooser.ItemsTableBuilder;
import delta.games.lotro.gui.lore.quests.table.QuestsColumnsBuilder;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.tasks.Task;

/**
 * Builds column definitions for TaskStatus data.
 * @author DAM
 */
public class TaskStatusColumnsBuilder
{
  /**
   * Identifier of the "Consumed item name" column.
   */
  public static final String CONSUMED_ITEM_NAME_COLUMN="CONSUMED_ITEM_NAME";
  /**
   * Identifier of the "Count" column.
   */
  public static final String COUNT_COLUMN="COUNT";

  /**
   * Build the columns to show the attributes of a task status.
   * @return a list of columns.
   */
  public static List<TableColumnController<TaskStatus,?>> buildTaskStatusColumns()
  {
    List<TableColumnController<TaskStatus,?>> ret=new ArrayList<TableColumnController<TaskStatus,?>>();
    // Task
    {
      CellDataProvider<TaskStatus,Task> provider=new CellDataProvider<TaskStatus,Task>()
      {
        @Override
        public Task getData(TaskStatus taskStatus)
        {
          return taskStatus.getTask();
        }
      };
      for(TableColumnController<Task,?> taskColumn : buildTaskColumns())
      {
        @SuppressWarnings("unchecked")
        TableColumnController<Task,Object> c=(TableColumnController<Task,Object>)taskColumn;
        ProxiedTableColumnController<TaskStatus,Task,Object> column=new ProxiedTableColumnController<TaskStatus,Task,Object>(c,provider);
        ret.add(column);
      }
    }
    // Status
    {
      CellDataProvider<TaskStatus,AchievableStatus> provider=new CellDataProvider<TaskStatus,AchievableStatus>()
      {
        @Override
        public AchievableStatus getData(TaskStatus taskStatus)
        {
          return taskStatus.getStatus();
        }
      };
      for(TableColumnController<AchievableStatus,?> questStatusColumn : AchievableStatusColumnsBuilder.buildQuestStateColumns())
      {
        @SuppressWarnings("unchecked")
        TableColumnController<AchievableStatus,Object> c=(TableColumnController<AchievableStatus,Object>)questStatusColumn;
        ProxiedTableColumnController<TaskStatus,AchievableStatus,Object> column=new ProxiedTableColumnController<TaskStatus,AchievableStatus,Object>(c,provider);
        ret.add(column);
      }
    }
    return ret;
  }

  private static List<TableColumnController<Task,?>> buildTaskColumns()
  {
    List<TableColumnController<Task,?>> ret=new ArrayList<TableColumnController<Task,?>>();
    // Quest
    CellDataProvider<Task,QuestDescription> provider=new CellDataProvider<Task,QuestDescription>()
    {
      @Override
      public QuestDescription getData(Task task)
      {
        return task.getQuest();
      }
    };
    for(TableColumnController<QuestDescription,?> questColumn : buildQuestColumns())
    {
      @SuppressWarnings("unchecked")
      TableColumnController<QuestDescription,Object> c=(TableColumnController<QuestDescription,Object>)questColumn;
      ProxiedTableColumnController<Task,QuestDescription,Object> column=new ProxiedTableColumnController<Task,QuestDescription,Object>(c,provider);
      ret.add(column);
    }
    // Consumed items
    List<DefaultTableColumnController<Item,?>> itemColumns=new ArrayList<DefaultTableColumnController<Item,?>>();
    DefaultTableColumnController<Item,?> itemNameColumn=ItemsTableBuilder.buildNameColumn(CONSUMED_ITEM_NAME_COLUMN,"Consumed item");
    itemColumns.add(itemNameColumn);
    itemColumns.add(ItemsTableBuilder.buildIconColumn());
    CellDataProvider<Task,Item> itemProvider=new CellDataProvider<Task,Item>()
    {
      @Override
      public Item getData(Task task)
      {
        return task.getItem();
      }
    };
    for(TableColumnController<Item,?> itemColumn : itemColumns)
    {
      @SuppressWarnings("unchecked")
      TableColumnController<Item,Object> c=(TableColumnController<Item,Object>)itemColumn;
      ProxiedTableColumnController<Task,Item,Object> column=new ProxiedTableColumnController<Task,Item,Object>(c,itemProvider);
      ret.add(column);
    }
    // Consumed items count
    {
      CellDataProvider<Task,Integer> countCell=new CellDataProvider<Task,Integer>()
      {
        @Override
        public Integer getData(Task item)
        {
          return Integer.valueOf(item.getItemCount());
        }
      };
      DefaultTableColumnController<Task,Integer> countColumn=new DefaultTableColumnController<Task,Integer>(COUNT_COLUMN,"Items Count",Integer.class,countCell);
      countColumn.setWidthSpecs(55,55,50);
      ret.add(countColumn);
    }
    return ret;
  }

  private static List<TableColumnController<QuestDescription,?>> buildQuestColumns()
  {
    List<TableColumnController<QuestDescription,?>> ret=new ArrayList<TableColumnController<QuestDescription,?>>();
    // Name
    ret.add(QuestsColumnsBuilder.buildQuestNameColumn());
    // Rewards
    CellDataProvider<QuestDescription,Rewards> provider=new CellDataProvider<QuestDescription,Rewards>()
    {
      @Override
      public Rewards getData(QuestDescription quest)
      {
        return quest.getRewards();
      }
    };
    for(TableColumnController<Rewards,?> rewardColumn : buildRewardsColumns())
    {
      @SuppressWarnings("unchecked")
      TableColumnController<Rewards,Object> c=(TableColumnController<Rewards,Object>)rewardColumn;
      ProxiedTableColumnController<QuestDescription,Rewards,Object> column=new ProxiedTableColumnController<QuestDescription,Rewards,Object>(c,provider);
      ret.add(column);
    }
    return ret;
  }

  private static List<TableColumnController<Rewards,?>> buildRewardsColumns()
  {
    List<TableColumnController<Rewards,?>> ret=new ArrayList<TableColumnController<Rewards,?>>();
    ret.add(RewardsColumnsBuilder.buildXPColumn());
    ret.add(RewardsColumnsBuilder.buildItemXPColumn());
    ret.add(RewardsColumnsBuilder.buildMountXPColumn());
    ret.add(RewardsColumnsBuilder.buildFactionNameColumn());
    ret.add(RewardsColumnsBuilder.buildFactionAmountColumn());
    return ret;
  }
}
