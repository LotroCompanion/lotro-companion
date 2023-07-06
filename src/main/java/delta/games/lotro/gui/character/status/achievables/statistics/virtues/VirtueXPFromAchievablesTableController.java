package delta.games.lotro.gui.character.status.achievables.statistics.virtues;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.character.status.achievables.statistics.AchievablesStatistics;
import delta.games.lotro.character.status.achievables.statistics.virtues.VirtueXPStatsFromAchievable;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;

/**
 * Controller for a table that shows the virtue XP points acquired from achievables.
 * @author DAM
 */
public class VirtueXPFromAchievablesTableController
{
  private static final String ACHIEVABLE_NAME="ACHIEVABLE_NAME";
  private static final String POINTS="POINTS";
  private static final String COUNT="COMPLETION_COUNT";

  // Data
  private AchievablesStatistics _stats;
  private List<VirtueXPStatsFromAchievable> _virtueStats;
  private AchievableUIMode _mode;
  // GUI
  private GenericTableController<VirtueXPStatsFromAchievable> _tableController;

  /**
   * Constructor.
   * @param stats Stats to show.
   * @param mode UI mode.
   */
  public VirtueXPFromAchievablesTableController(AchievablesStatistics stats, AchievableUIMode mode)
  {
    _stats=stats;
    _mode=mode;
    _tableController=buildTable();
  }

  private GenericTableController<VirtueXPStatsFromAchievable> buildTable()
  {
    _virtueStats=new ArrayList<VirtueXPStatsFromAchievable>();
    ListDataProvider<VirtueXPStatsFromAchievable> provider=new ListDataProvider<VirtueXPStatsFromAchievable>(_virtueStats);
    GenericTableController<VirtueXPStatsFromAchievable> table=new GenericTableController<VirtueXPStatsFromAchievable>(provider);

    // Achievable name
    {
      CellDataProvider<VirtueXPStatsFromAchievable,String> nameCell=new CellDataProvider<VirtueXPStatsFromAchievable,String>()
      {
        @Override
        public String getData(VirtueXPStatsFromAchievable item)
        {
          return item.getAchievable().getName();
        }
      };
      String name=(_mode==AchievableUIMode.DEED)?"Deed":"Quest"; // I18n
      DefaultTableColumnController<VirtueXPStatsFromAchievable,String> nameColumn=new DefaultTableColumnController<VirtueXPStatsFromAchievable,String>(ACHIEVABLE_NAME,name,String.class,nameCell);
      nameColumn.setWidthSpecs(200,-1,200);
      table.addColumnController(nameColumn);
    }
    // Virtue XP points column
    {
      CellDataProvider<VirtueXPStatsFromAchievable,Integer> amountCell=new CellDataProvider<VirtueXPStatsFromAchievable,Integer>()
      {
        @Override
        public Integer getData(VirtueXPStatsFromAchievable item)
        {
          Integer amount=Integer.valueOf(item.getPoints());
          return amount;
        }
      };
      DefaultTableColumnController<VirtueXPStatsFromAchievable,Integer> amountColumn=new DefaultTableColumnController<VirtueXPStatsFromAchievable,Integer>(POINTS,"Points",Integer.class,amountCell); // I18n
      amountColumn.setWidthSpecs(60,60,60);
      table.addColumnController(amountColumn);
    }
    // Achievables count column
    if (_mode==AchievableUIMode.QUEST)
    {
      CellDataProvider<VirtueXPStatsFromAchievable,Integer> completionsCell=new CellDataProvider<VirtueXPStatsFromAchievable,Integer>()
      {
        @Override
        public Integer getData(VirtueXPStatsFromAchievable item)
        {
          Integer count=Integer.valueOf(item.getCompletionsCount());
          return count;
        }
      };
      DefaultTableColumnController<VirtueXPStatsFromAchievable,Integer> completionsColumn=new DefaultTableColumnController<VirtueXPStatsFromAchievable,Integer>(COUNT,"Completions",Integer.class,completionsCell); // I18n
      completionsColumn.setWidthSpecs(60,60,60);
      table.addColumnController(completionsColumn);
    }

    TableColumnsManager<VirtueXPStatsFromAchievable> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=new ArrayList<String>();
    columnIds.add(ACHIEVABLE_NAME);
    columnIds.add(POINTS);
    columnIds.add(COUNT);
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<VirtueXPStatsFromAchievable> getTableController()
  {
    return _tableController;
  }

  /**
   * Update display.
   */
  public void update()
  {
    _virtueStats.clear();
    _virtueStats.addAll(_stats.getVirtueXPStats().getEntries());
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
