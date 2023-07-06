package delta.games.lotro.gui.character.status.achievables.statistics.reputation;

import java.util.List;

import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.games.lotro.character.status.achievables.statistics.reputation.AchievablesFactionStats;
import delta.games.lotro.character.status.achievables.statistics.reputation.AchievablesReputationStats;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;
import delta.games.lotro.gui.common.statistics.reputation.ReputationTableController;

/**
 * Controller for a table that shows the reputations for a single character.
 * @author DAM
 */
public class AchievablesReputationTableController extends ReputationTableController<AchievablesFactionStats>
{
  private static final String COUNT="ACHIEVABLES_COUNT";
  private static final String COMPLETIONS_COUNT="COMPLETIONS_COUNT";

  // Data
  private AchievableUIMode _mode;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param stats Stats to show.
   * @param mode UI mode.
   */
  public AchievablesReputationTableController(AreaController parent, AchievablesReputationStats stats, AchievableUIMode mode)
  {
    super(parent,stats);
    _mode=mode;
  }

  protected void defineColumns(GenericTableController<AchievablesFactionStats> table)
  {
    super.defineColumns(table);
    // Achievables count column
    {
      String name=(_mode==AchievableUIMode.DEED)?"Deeds":"Quests"; // I18n
      CellDataProvider<AchievablesFactionStats,Integer> countCell=new CellDataProvider<AchievablesFactionStats,Integer>()
      {
        @Override
        public Integer getData(AchievablesFactionStats item)
        {
          Integer count=Integer.valueOf(item.getAchievablesCount());
          return count;
        }
      };
      DefaultTableColumnController<AchievablesFactionStats,Integer> countColumn=new DefaultTableColumnController<AchievablesFactionStats,Integer>(COUNT,name,Integer.class,countCell);
      countColumn.setWidthSpecs(60,60,60);
      table.addColumnController(countColumn);
    }
    // Achievables count column (for quests only)
    if (_mode==AchievableUIMode.QUEST)
    {
      CellDataProvider<AchievablesFactionStats,Integer> completionsCountCell=new CellDataProvider<AchievablesFactionStats,Integer>()
      {
        @Override
        public Integer getData(AchievablesFactionStats item)
        {
          Integer count=Integer.valueOf(item.getCompletionsCount());
          return count;
        }
      };
      DefaultTableColumnController<AchievablesFactionStats,Integer> completionsCountColumn=new DefaultTableColumnController<AchievablesFactionStats,Integer>(COMPLETIONS_COUNT,"Completions",Integer.class,completionsCountCell); // I18n
      completionsCountColumn.setWidthSpecs(60,60,60);
      table.addColumnController(completionsCountColumn);
    }
  }

  protected List<String> getColumnIds()
  {
    List<String> columnIds=super.getColumnIds();
    columnIds.add(COUNT);
    columnIds.add(COMPLETIONS_COUNT);
    return columnIds;
  }
}
