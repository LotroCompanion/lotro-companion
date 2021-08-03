package delta.games.lotro.gui.character.status.statistics.reputation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.character.status.statistics.reputation.FactionStats;
import delta.games.lotro.character.status.statistics.reputation.ReputationStats;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;

/**
 * Controller for a table that shows the reputations for a single character.
 * @author DAM
 */
public class ReputationTableController
{
  private static final String FACTION="FACTION";
  private static final String AMOUNT="AMOUNT";
  private static final String COUNT="ACHIEVABLES_COUNT";
  private static final String COMPLETIONS_COUNT="COMPLETIONS_COUNT";

  // Data
  private ReputationStats _stats;
  private List<FactionStats> _factionStats;
  private AchievableUIMode _mode;
  // GUI
  private GenericTableController<FactionStats> _tableController;

  /**
   * Constructor.
   * @param stats Stats to show.
   * @param mode UI mode.
   */
  public ReputationTableController(ReputationStats stats, AchievableUIMode mode)
  {
    _stats=stats;
    _mode=mode;
    _tableController=buildTable();
  }

  private GenericTableController<FactionStats> buildTable()
  {
    _factionStats=new ArrayList<FactionStats>();
    ListDataProvider<FactionStats> provider=new ListDataProvider<FactionStats>(_factionStats);
    GenericTableController<FactionStats> table=new GenericTableController<FactionStats>(provider);

    // Faction
    {
      CellDataProvider<FactionStats,String> factionCell=new CellDataProvider<FactionStats,String>()
      {
        @Override
        public String getData(FactionStats item)
        {
          return item.getFaction().getName();
        }
      };
      DefaultTableColumnController<FactionStats,String> factionColumn=new DefaultTableColumnController<FactionStats,String>(FACTION,"Faction",String.class,factionCell);
      factionColumn.setWidthSpecs(200,-1,200);
      table.addColumnController(factionColumn);
    }
    // Amount column
    {
      CellDataProvider<FactionStats,Integer> amountCell=new CellDataProvider<FactionStats,Integer>()
      {
        @Override
        public Integer getData(FactionStats item)
        {
          Integer amount=Integer.valueOf(item.getPoints());
          return amount;
        }
      };
      DefaultTableColumnController<FactionStats,Integer> amountColumn=new DefaultTableColumnController<FactionStats,Integer>(AMOUNT,"Points",Integer.class,amountCell);
      amountColumn.setWidthSpecs(60,60,60);
      table.addColumnController(amountColumn);
    }
    // Achievables count column
    {
      String name=(_mode==AchievableUIMode.DEED)?"Deeds":"Quests";
      CellDataProvider<FactionStats,Integer> countCell=new CellDataProvider<FactionStats,Integer>()
      {
        @Override
        public Integer getData(FactionStats item)
        {
          Integer count=Integer.valueOf(item.getAchievablesCount());
          return count;
        }
      };
      DefaultTableColumnController<FactionStats,Integer> countColumn=new DefaultTableColumnController<FactionStats,Integer>(COUNT,name,Integer.class,countCell);
      countColumn.setWidthSpecs(60,60,60);
      table.addColumnController(countColumn);
    }
    // Achievables count column (for quests only)
    if (_mode==AchievableUIMode.QUEST)
    {
      CellDataProvider<FactionStats,Integer> completionsCountCell=new CellDataProvider<FactionStats,Integer>()
      {
        @Override
        public Integer getData(FactionStats item)
        {
          Integer count=Integer.valueOf(item.getCompletionsCount());
          return count;
        }
      };
      DefaultTableColumnController<FactionStats,Integer> completionsCountColumn=new DefaultTableColumnController<FactionStats,Integer>(COMPLETIONS_COUNT,"Completions",Integer.class,completionsCountCell);
      completionsCountColumn.setWidthSpecs(60,60,60);
      table.addColumnController(completionsCountColumn);
    }

    TableColumnsManager<FactionStats> columnsManager=table.getColumnsManager();
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
    columnIds.add(COMPLETIONS_COUNT);
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<FactionStats> getTableController()
  {
    return _tableController;
  }

  /**
   * Update display.
   */
  public void update()
  {
    _factionStats.clear();
    _factionStats.addAll(_stats.getFactionStats());
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
