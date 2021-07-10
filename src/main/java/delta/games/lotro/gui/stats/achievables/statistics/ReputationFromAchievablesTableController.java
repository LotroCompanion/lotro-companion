package delta.games.lotro.gui.stats.achievables.statistics;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.gui.stats.achievables.AchievableUIMode;
import delta.games.lotro.stats.achievables.AchievablesStatistics;
import delta.games.lotro.stats.achievables.FactionStatsFromAchievables;

/**
 * Controller for a table that shows the reputations for a single character.
 * @author DAM
 */
public class ReputationFromAchievablesTableController
{
  private static final String FACTION="FACTION";
  private static final String AMOUNT="AMOUNT";
  private static final String COUNT="ACHIEVABLES_COUNT";

  // Data
  private AchievablesStatistics _stats;
  private List<FactionStatsFromAchievables> _factionStats;
  private AchievableUIMode _mode;
  // GUI
  private GenericTableController<FactionStatsFromAchievables> _tableController;

  /**
   * Constructor.
   * @param stats Stats to show.
   * @param mode UI mode.
   */
  public ReputationFromAchievablesTableController(AchievablesStatistics stats, AchievableUIMode mode)
  {
    _stats=stats;
    _mode=mode;
    _tableController=buildTable();
  }

  private GenericTableController<FactionStatsFromAchievables> buildTable()
  {
    _factionStats=new ArrayList<FactionStatsFromAchievables>();
    ListDataProvider<FactionStatsFromAchievables> provider=new ListDataProvider<FactionStatsFromAchievables>(_factionStats);
    GenericTableController<FactionStatsFromAchievables> table=new GenericTableController<FactionStatsFromAchievables>(provider);

    // Faction
    {
      CellDataProvider<FactionStatsFromAchievables,String> factionCell=new CellDataProvider<FactionStatsFromAchievables,String>()
      {
        @Override
        public String getData(FactionStatsFromAchievables item)
        {
          return item.getFaction().getName();
        }
      };
      DefaultTableColumnController<FactionStatsFromAchievables,String> factionColumn=new DefaultTableColumnController<FactionStatsFromAchievables,String>(FACTION,"Faction",String.class,factionCell);
      factionColumn.setWidthSpecs(200,-1,200);
      table.addColumnController(factionColumn);
    }
    // Amount column
    {
      CellDataProvider<FactionStatsFromAchievables,Integer> amountCell=new CellDataProvider<FactionStatsFromAchievables,Integer>()
      {
        @Override
        public Integer getData(FactionStatsFromAchievables item)
        {
          Integer amount=Integer.valueOf(item.getPoints());
          return amount;
        }
      };
      DefaultTableColumnController<FactionStatsFromAchievables,Integer> amountColumn=new DefaultTableColumnController<FactionStatsFromAchievables,Integer>(AMOUNT,"Points",Integer.class,amountCell);
      amountColumn.setWidthSpecs(60,60,60);
      table.addColumnController(amountColumn);
    }
    // Achievables count column
    {
      String name=(_mode==AchievableUIMode.DEED)?"Deeds":"Quests";
      CellDataProvider<FactionStatsFromAchievables,Integer> countCell=new CellDataProvider<FactionStatsFromAchievables,Integer>()
      {
        @Override
        public Integer getData(FactionStatsFromAchievables item)
        {
          Integer count=Integer.valueOf(item.getAchievablesCount());
          return count;
        }
      };
      DefaultTableColumnController<FactionStatsFromAchievables,Integer> countColumn=new DefaultTableColumnController<FactionStatsFromAchievables,Integer>(COUNT,name,Integer.class,countCell);
      countColumn.setWidthSpecs(60,60,60);
      table.addColumnController(countColumn);
    }

    TableColumnsManager<FactionStatsFromAchievables> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=new ArrayList<String>();
    columnIds.add(FACTION);
    columnIds.add(AMOUNT);
    columnIds.add(COUNT);
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<FactionStatsFromAchievables> getTableController()
  {
    return _tableController;
  }

  /**
   * Update display.
   */
  public void update()
  {
    _factionStats.clear();
    _factionStats.addAll(_stats.getReputation().values());
    _tableController.refresh();
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    return _tableController.getTable();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _stats=null;
  }
}
