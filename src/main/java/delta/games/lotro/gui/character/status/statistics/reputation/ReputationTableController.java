package delta.games.lotro.gui.character.status.statistics.reputation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.character.status.achievables.statistics.reputation.AchievablesFactionStats;
import delta.games.lotro.character.status.achievables.statistics.reputation.AchievablesReputationStats;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionNameComparator;
import delta.games.lotro.utils.DataProvider;
import delta.games.lotro.utils.comparators.DelegatingComparator;

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
  private AchievablesReputationStats _stats;
  private List<AchievablesFactionStats> _factionStats;
  private AchievableUIMode _mode;
  // GUI
  private GenericTableController<AchievablesFactionStats> _tableController;

  /**
   * Constructor.
   * @param stats Stats to show.
   * @param mode UI mode.
   */
  public ReputationTableController(AchievablesReputationStats stats, AchievableUIMode mode)
  {
    _stats=stats;
    _mode=mode;
    _tableController=buildTable();
  }

  private GenericTableController<AchievablesFactionStats> buildTable()
  {
    _factionStats=new ArrayList<AchievablesFactionStats>();
    ListDataProvider<AchievablesFactionStats> provider=new ListDataProvider<AchievablesFactionStats>(_factionStats);
    GenericTableController<AchievablesFactionStats> table=new GenericTableController<AchievablesFactionStats>(provider);

    // Faction
    {
      CellDataProvider<AchievablesFactionStats,String> factionCell=new CellDataProvider<AchievablesFactionStats,String>()
      {
        @Override
        public String getData(AchievablesFactionStats item)
        {
          return item.getFaction().getName();
        }
      };
      DefaultTableColumnController<AchievablesFactionStats,String> factionColumn=new DefaultTableColumnController<AchievablesFactionStats,String>(FACTION,"Faction",String.class,factionCell);
      factionColumn.setWidthSpecs(200,-1,200);
      table.addColumnController(factionColumn);
    }
    // Amount column
    {
      CellDataProvider<AchievablesFactionStats,Integer> amountCell=new CellDataProvider<AchievablesFactionStats,Integer>()
      {
        @Override
        public Integer getData(AchievablesFactionStats item)
        {
          Integer amount=Integer.valueOf(item.getPoints());
          return amount;
        }
      };
      DefaultTableColumnController<AchievablesFactionStats,Integer> amountColumn=new DefaultTableColumnController<AchievablesFactionStats,Integer>(AMOUNT,"Points",Integer.class,amountCell);
      amountColumn.setWidthSpecs(60,60,60);
      table.addColumnController(amountColumn);
    }
    // Achievables count column
    {
      String name=(_mode==AchievableUIMode.DEED)?"Deeds":"Quests";
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
      DefaultTableColumnController<AchievablesFactionStats,Integer> completionsCountColumn=new DefaultTableColumnController<AchievablesFactionStats,Integer>(COMPLETIONS_COUNT,"Completions",Integer.class,completionsCountCell);
      completionsCountColumn.setWidthSpecs(60,60,60);
      table.addColumnController(completionsCountColumn);
    }

    TableColumnsManager<AchievablesFactionStats> columnsManager=table.getColumnsManager();
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
  public GenericTableController<AchievablesFactionStats> getTableController()
  {
    return _tableController;
  }

  /**
   * Update display.
   */
  public void update()
  {
    List<AchievablesFactionStats> factionStats=_stats.getFactionStats();
    sortFactionStatsByName(factionStats);
    _factionStats.clear();
    _factionStats.addAll(factionStats);
    _tableController.refresh();
  }

  private void sortFactionStatsByName(List<AchievablesFactionStats> factionStats)
  {
    DataProvider<AchievablesFactionStats,Faction> provider=new DataProvider<AchievablesFactionStats,Faction>()
    {
      public Faction getData(AchievablesFactionStats p)
      {
        return p.getFaction();
      }
    };
    DelegatingComparator<AchievablesFactionStats,Faction> c=new DelegatingComparator<AchievablesFactionStats,Faction>(provider,new FactionNameComparator());
    Collections.sort(factionStats,c);
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
