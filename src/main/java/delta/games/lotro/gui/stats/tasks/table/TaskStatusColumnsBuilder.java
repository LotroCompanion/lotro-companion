package delta.games.lotro.gui.stats.tasks.table;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.character.tasks.TaskStatus;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.gui.common.rewards.table.RewardsColumnsBuilder;
import delta.games.lotro.gui.quests.table.QuestsColumnsBuilder;
import delta.games.lotro.gui.stats.achievables.table.AchievableStatusColumnsBuilder;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.tasks.Task;

/**
 * Builds column definitions for TaskStatus data.
 * @author DAM
 */
public class TaskStatusColumnsBuilder
{
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
    return ret;
  }
}
