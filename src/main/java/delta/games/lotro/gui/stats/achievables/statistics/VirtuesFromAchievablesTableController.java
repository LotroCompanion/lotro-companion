package delta.games.lotro.gui.stats.achievables.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.stats.achievables.AchievablesStatistics;
import delta.games.lotro.stats.achievables.VirtueStatsComparator;
import delta.games.lotro.stats.achievables.VirtueStatsFromAchievables;

/**
 * Controller for a table that shows the virtue points acquired from achievables.
 * @author DAM
 */
public class VirtuesFromAchievablesTableController
{
  private static final String ICON="ICON";
  private static final String VIRTUE="VIRTUE";
  private static final String POINTS="POINTS";
  private static final String COUNT="DEEDS_COUNT";

  // Data
  private AchievablesStatistics _stats;
  private List<VirtueStatsFromAchievables> _virtueStats;
  // GUI
  private GenericTableController<VirtueStatsFromAchievables> _tableController;

  /**
   * Constructor.
   * @param stats Stats to show.
   */
  public VirtuesFromAchievablesTableController(AchievablesStatistics stats)
  {
    _stats=stats;
    _tableController=buildTable();
    configureTable();
  }

  private GenericTableController<VirtueStatsFromAchievables> buildTable()
  {
    _virtueStats=new ArrayList<VirtueStatsFromAchievables>();
    ListDataProvider<VirtueStatsFromAchievables> provider=new ListDataProvider<VirtueStatsFromAchievables>(_virtueStats);
    GenericTableController<VirtueStatsFromAchievables> table=new GenericTableController<VirtueStatsFromAchievables>(provider);

    // Virtue icon
    {
      CellDataProvider<VirtueStatsFromAchievables,Icon> iconCell=new CellDataProvider<VirtueStatsFromAchievables,Icon>()
      {
        @Override
        public Icon getData(VirtueStatsFromAchievables item)
        {
          VirtueDescription virtue=item.getVirtue();
          Icon icon=LotroIconsManager.getVirtueIcon(virtue);
          return icon;
        }
      };
      DefaultTableColumnController<VirtueStatsFromAchievables,Icon> iconColumn=new DefaultTableColumnController<VirtueStatsFromAchievables,Icon>(ICON,"Icon",Icon.class,iconCell);
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      table.addColumnController(iconColumn);
    }
    // Virtue name
    {
      CellDataProvider<VirtueStatsFromAchievables,String> virtueCell=new CellDataProvider<VirtueStatsFromAchievables,String>()
      {
        @Override
        public String getData(VirtueStatsFromAchievables item)
        {
          return item.getVirtue().getName();
        }
      };
      DefaultTableColumnController<VirtueStatsFromAchievables,String> virtueColumn=new DefaultTableColumnController<VirtueStatsFromAchievables,String>(VIRTUE,"Virtue",String.class,virtueCell);
      virtueColumn.setWidthSpecs(200,-1,200);
      table.addColumnController(virtueColumn);
    }
    // Virtue points column
    {
      CellDataProvider<VirtueStatsFromAchievables,Integer> amountCell=new CellDataProvider<VirtueStatsFromAchievables,Integer>()
      {
        @Override
        public Integer getData(VirtueStatsFromAchievables item)
        {
          Integer amount=Integer.valueOf(item.getPoints());
          return amount;
        }
      };
      DefaultTableColumnController<VirtueStatsFromAchievables,Integer> amountColumn=new DefaultTableColumnController<VirtueStatsFromAchievables,Integer>(POINTS,"Points",Integer.class,amountCell);
      amountColumn.setWidthSpecs(60,60,60);
      table.addColumnController(amountColumn);
    }
    // Count column
    {
      CellDataProvider<VirtueStatsFromAchievables,Integer> countCell=new CellDataProvider<VirtueStatsFromAchievables,Integer>()
      {
        @Override
        public Integer getData(VirtueStatsFromAchievables item)
        {
          Integer count=Integer.valueOf(item.getAchievablesCount());
          return count;
        }
      };
      DefaultTableColumnController<VirtueStatsFromAchievables,Integer> countColumn=new DefaultTableColumnController<VirtueStatsFromAchievables,Integer>(COUNT,"Achievables",Integer.class,countCell);
      countColumn.setWidthSpecs(60,60,60);
      table.addColumnController(countColumn);
    }

    TableColumnsManager<VirtueStatsFromAchievables> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=new ArrayList<String>();
    columnIds.add(ICON);
    columnIds.add(VIRTUE);
    columnIds.add(POINTS);
    columnIds.add(COUNT);
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<VirtueStatsFromAchievables> getTableController()
  {
    return _tableController;
  }

  /**
   * Update display.
   */
  public void update()
  {
    _virtueStats.clear();
    _virtueStats.addAll(_stats.getVirtues().values());
    Collections.sort(_virtueStats,new VirtueStatsComparator());
    _tableController.refresh();
  }

  private void configureTable()
  {
    JTable table=getTable();
    // Adjust table row height for icons (32 pixels)
    table.setRowHeight(32);
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
