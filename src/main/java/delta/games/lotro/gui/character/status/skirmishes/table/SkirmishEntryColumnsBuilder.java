package delta.games.lotro.gui.character.status.skirmishes.table;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.status.skirmishes.SkirmishEntry;
import delta.games.lotro.character.status.skirmishes.SkirmishLevel;
import delta.games.lotro.character.status.skirmishes.SkirmishStats;
import delta.games.lotro.common.enums.GroupSize;
import delta.games.lotro.gui.utils.DurationCellRenderer;
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;

/**
 * Builds column definitions for {@link SkirmishEntry} data.
 * @author DAM
 */
public class SkirmishEntryColumnsBuilder
{
  /**
   * Build the columns to show the attributes of {@link SkirmishEntry}.
   * @return a list of columns.
   */
  public static List<TableColumnController<SkirmishEntry,?>> buildSkirmishEntryColumns()
  {
    List<TableColumnController<SkirmishEntry,?>> ret=new ArrayList<TableColumnController<SkirmishEntry,?>>();
    // Name
    {
      CellDataProvider<SkirmishEntry,String> cell=new CellDataProvider<SkirmishEntry,String>()
      {
        @Override
        public String getData(SkirmishEntry entry)
        {
          return entry.getSkirmish().getName();
        }
      };
      DefaultTableColumnController<SkirmishEntry,String> column=new DefaultTableColumnController<SkirmishEntry,String>(SkirmishEntryColumnIds.NAME.name(),"Name",String.class,cell); // I18n
      column.setWidthSpecs(220,300,220);
      ret.add(column);
    }
    // Group size
    {
      CellDataProvider<SkirmishEntry,String> cell=new CellDataProvider<SkirmishEntry,String>()
      {
        @Override
        public String getData(SkirmishEntry entry)
        {
          GroupSize size=entry.getSize();
          return (size!=null)?size.toString():"Mixed"; // I18n
        }
      };
      DefaultTableColumnController<SkirmishEntry,String> column=new DefaultTableColumnController<SkirmishEntry,String>(SkirmishEntryColumnIds.GROUP_SIZE.name(),"Size",String.class,cell); // I18n
      column.setWidthSpecs(100,100,100);
      ret.add(column);
    }
    // Level
    {
      CellDataProvider<SkirmishEntry,String> cell=new CellDataProvider<SkirmishEntry,String>()
      {
        @Override
        public String getData(SkirmishEntry stats)
        {
          SkirmishLevel level=stats.getLevel();
          return (level!=null)?level.toString():"Mixed"; // I18n
        }
      };
      DefaultTableColumnController<SkirmishEntry,String> column=new DefaultTableColumnController<SkirmishEntry,String>(SkirmishEntryColumnIds.LEVEL.name(),"Level",String.class,cell); // I18n
      column.setWidthSpecs(65,65,65);
      ret.add(column);
    }
    // Statistics
    {
      CellDataProvider<SkirmishEntry,SkirmishStats> provider=new CellDataProvider<SkirmishEntry,SkirmishStats>()
      {
        @Override
        public SkirmishStats getData(SkirmishEntry entry)
        {
          return entry.getStats();
        }
      };
      for(TableColumnController<SkirmishStats,?> skirmishStatsColumn : buildSkirmishStatsColumns())
      {
        @SuppressWarnings("unchecked")
        TableColumnController<SkirmishStats,Object> c=(TableColumnController<SkirmishStats,Object>)skirmishStatsColumn;
        ProxiedTableColumnController<SkirmishEntry,SkirmishStats,Object> column=new ProxiedTableColumnController<SkirmishEntry,SkirmishStats,Object>(c,provider);
        ret.add(column);
      }
    }
    return ret;
  }

  /**
   * Build the columns to show the attributes of {@link SkirmishStats}.
   * @return a list of columns.
   */
  public static List<TableColumnController<SkirmishStats,?>> buildSkirmishStatsColumns()
  {
    List<TableColumnController<SkirmishStats,?>> ret=new ArrayList<TableColumnController<SkirmishStats,?>>();
    // Monster kills
    {
      CellDataProvider<SkirmishStats,Integer> cell=new CellDataProvider<SkirmishStats,Integer>()
      {
        @Override
        public Integer getData(SkirmishStats stats)
        {
          return Integer.valueOf(stats.getMonsterKills());
        }
      };
      DefaultTableColumnController<SkirmishStats,Integer> column=new DefaultTableColumnController<SkirmishStats,Integer>(SkirmishEntryColumnIds.MONSTER_KILLS.name(),"Mob Kills",Integer.class,cell); // I18n
      StatColumnsUtils.configureIntegerColumn(column,50);
      ret.add(column);
    }
    // Lieutenant kills
    {
      CellDataProvider<SkirmishStats,Integer> cell=new CellDataProvider<SkirmishStats,Integer>()
      {
        @Override
        public Integer getData(SkirmishStats stats)
        {
          return Integer.valueOf(stats.getLieutenantKills());
        }
      };
      DefaultTableColumnController<SkirmishStats,Integer> column=new DefaultTableColumnController<SkirmishStats,Integer>(SkirmishEntryColumnIds.LIEUTENANT_KILLS.name(),"Lt Kills",Integer.class,cell); // I18n
      StatColumnsUtils.configureIntegerColumn(column,50);
      ret.add(column);
    }
    // Boss kills
    {
      CellDataProvider<SkirmishStats,Integer> cell=new CellDataProvider<SkirmishStats,Integer>()
      {
        @Override
        public Integer getData(SkirmishStats stats)
        {
          return Integer.valueOf(stats.getBossKills());
        }
      };
      DefaultTableColumnController<SkirmishStats,Integer> column=new DefaultTableColumnController<SkirmishStats,Integer>(SkirmishEntryColumnIds.BOSS_KILLS.name(),"Boss Kills",Integer.class,cell); // I18n
      StatColumnsUtils.configureIntegerColumn(column,50);
      ret.add(column);
    }
    // Boss resets
    {
      CellDataProvider<SkirmishStats,Integer> cell=new CellDataProvider<SkirmishStats,Integer>()
      {
        @Override
        public Integer getData(SkirmishStats stats)
        {
          return Integer.valueOf(stats.getBossResets());
        }
      };
      DefaultTableColumnController<SkirmishStats,Integer> column=new DefaultTableColumnController<SkirmishStats,Integer>(SkirmishEntryColumnIds.BOSS_RESETS.name(),"Boss Resets",Integer.class,cell); // I18n
      StatColumnsUtils.configureIntegerColumn(column,50);
      ret.add(column);
    }
    // Defenders lost
    {
      CellDataProvider<SkirmishStats,Integer> cell=new CellDataProvider<SkirmishStats,Integer>()
      {
        @Override
        public Integer getData(SkirmishStats stats)
        {
          return Integer.valueOf(stats.getDefendersLost());
        }
      };
      DefaultTableColumnController<SkirmishStats,Integer> column=new DefaultTableColumnController<SkirmishStats,Integer>(SkirmishEntryColumnIds.DEFENDERS_LOST.name(),"Def.Lost",Integer.class,cell); // I18n
      StatColumnsUtils.configureIntegerColumn(column,50);
      ret.add(column);
    }
    // Defenders saved
    {
      CellDataProvider<SkirmishStats,Integer> cell=new CellDataProvider<SkirmishStats,Integer>()
      {
        @Override
        public Integer getData(SkirmishStats stats)
        {
          return Integer.valueOf(stats.getDefendersSaved());
        }
      };
      DefaultTableColumnController<SkirmishStats,Integer> column=new DefaultTableColumnController<SkirmishStats,Integer>(SkirmishEntryColumnIds.DEFENDERS_SAVED.name(),"Def.Saved",Integer.class,cell); // I18n
      StatColumnsUtils.configureIntegerColumn(column,50);
      ret.add(column);
    }
    // Soldier deaths
    {
      CellDataProvider<SkirmishStats,Integer> cell=new CellDataProvider<SkirmishStats,Integer>()
      {
        @Override
        public Integer getData(SkirmishStats stats)
        {
          return Integer.valueOf(stats.getSoldiersDeaths());
        }
      };
      DefaultTableColumnController<SkirmishStats,Integer> column=new DefaultTableColumnController<SkirmishStats,Integer>(SkirmishEntryColumnIds.SOLDIER_DEATHS.name(),"Soldier Deaths",Integer.class,cell); // I18n
      StatColumnsUtils.configureIntegerColumn(column,50);
      ret.add(column);
    }
    // Control Points taken
    {
      CellDataProvider<SkirmishStats,Integer> cell=new CellDataProvider<SkirmishStats,Integer>()
      {
        @Override
        public Integer getData(SkirmishStats stats)
        {
          return Integer.valueOf(stats.getControlPointsTaken());
        }
      };
      DefaultTableColumnController<SkirmishStats,Integer> column=new DefaultTableColumnController<SkirmishStats,Integer>(SkirmishEntryColumnIds.CONTROL_POINTS_TAKEN.name(),"CP Taken",Integer.class,cell); // I18n
      StatColumnsUtils.configureIntegerColumn(column,50);
      ret.add(column);
    }
    // Encounters completed
    {
      CellDataProvider<SkirmishStats,Integer> cell=new CellDataProvider<SkirmishStats,Integer>()
      {
        @Override
        public Integer getData(SkirmishStats stats)
        {
          return Integer.valueOf(stats.getEncountersCompleted());
        }
      };
      DefaultTableColumnController<SkirmishStats,Integer> column=new DefaultTableColumnController<SkirmishStats,Integer>(SkirmishEntryColumnIds.ENCOUNTERS_COMPLETED.name(),"Encounters Completed",Integer.class,cell); // I18n
      StatColumnsUtils.configureIntegerColumn(column,50);
      ret.add(column);
    }
    // Play time
    {
      CellDataProvider<SkirmishStats,Integer> cell=new CellDataProvider<SkirmishStats,Integer>()
      {
        @Override
        public Integer getData(SkirmishStats stats)
        {
          float playTime=stats.getPlayTime();
          return (playTime!=-1)?Integer.valueOf((int)playTime):null;
        }
      };
      DefaultTableColumnController<SkirmishStats,Integer> column=new DefaultTableColumnController<SkirmishStats,Integer>(SkirmishEntryColumnIds.PLAY_TIME.name(),"Play Time",Integer.class,cell); // I18n
      column.setWidthSpecs(80,100,80);
      column.setCellRenderer(new DurationCellRenderer());
      ret.add(column);
    }
    // Skirmishes completed
    {
      CellDataProvider<SkirmishStats,Integer> cell=new CellDataProvider<SkirmishStats,Integer>()
      {
        @Override
        public Integer getData(SkirmishStats stats)
        {
          return Integer.valueOf(stats.getSkirmishesCompleted());
        }
      };
      DefaultTableColumnController<SkirmishStats,Integer> column=new DefaultTableColumnController<SkirmishStats,Integer>(SkirmishEntryColumnIds.SKIRMISHES_COMPLETED.name(),"Completed",Integer.class,cell); // I18n
      StatColumnsUtils.configureIntegerColumn(column,50);
      ret.add(column);
    }
    // Skirmishes attempted
    {
      CellDataProvider<SkirmishStats,Integer> cell=new CellDataProvider<SkirmishStats,Integer>()
      {
        @Override
        public Integer getData(SkirmishStats stats)
        {
          return Integer.valueOf(stats.getSkirmishesAttempted());
        }
      };
      DefaultTableColumnController<SkirmishStats,Integer> column=new DefaultTableColumnController<SkirmishStats,Integer>(SkirmishEntryColumnIds.SKIRMISHES_ATTEMPTED.name(),"Attempted",Integer.class,cell); // I18n
      StatColumnsUtils.configureIntegerColumn(column,50);
      ret.add(column);
    }
    // Best time
    {
      CellDataProvider<SkirmishStats,Integer> cell=new CellDataProvider<SkirmishStats,Integer>()
      {
        @Override
        public Integer getData(SkirmishStats stats)
        {
          float bestTime=stats.getBestTime();
          return (bestTime!=-1)?Integer.valueOf((int)bestTime):null;
        }
      };
      DefaultTableColumnController<SkirmishStats,Integer> column=new DefaultTableColumnController<SkirmishStats,Integer>(SkirmishEntryColumnIds.BEST_TIME.name(),"Best Time",Integer.class,cell); // I18n
      column.setWidthSpecs(80,100,80);
      column.setCellRenderer(new DurationCellRenderer());
      ret.add(column);
    }
    // Total marks earned
    {
      CellDataProvider<SkirmishStats,Integer> cell=new CellDataProvider<SkirmishStats,Integer>()
      {
        @Override
        public Integer getData(SkirmishStats stats)
        {
          return Integer.valueOf(stats.getTotalMarksEarned());
        }
      };
      DefaultTableColumnController<SkirmishStats,Integer> column=new DefaultTableColumnController<SkirmishStats,Integer>(SkirmishEntryColumnIds.TOTAL_MARKS_EARNED.name(),"Marks",Integer.class,cell); // I18n
      StatColumnsUtils.configureIntegerColumn(column,50);
      ret.add(column);
    }
    return ret;
  }
}
