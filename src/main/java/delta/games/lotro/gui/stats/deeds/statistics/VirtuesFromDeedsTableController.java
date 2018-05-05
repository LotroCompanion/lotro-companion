package delta.games.lotro.gui.stats.deeds.statistics;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.stats.deeds.DeedsStatistics;
import delta.games.lotro.stats.deeds.VirtueStatsFromDeeds;

/**
 * Controller for a table that shows the virtue points acquired from deeds.
 * @author DAM
 */
public class VirtuesFromDeedsTableController
{
  private static final String ICON="ICON";
  private static final String VIRTUE="VIRTUE";
  private static final String POINTS="POINTS";
  private static final String DEEDS_COUNT="DEEDS_COUNT";

  // Data
  private DeedsStatistics _stats;
  private List<VirtueStatsFromDeeds> _factionStats;
  // GUI
  private GenericTableController<VirtueStatsFromDeeds> _tableController;

  /**
   * Constructor.
   * @param stats Stats to show.
   */
  public VirtuesFromDeedsTableController(DeedsStatistics stats)
  {
    _stats=stats;
    _tableController=buildTable();
    configureTable();
  }

  private GenericTableController<VirtueStatsFromDeeds> buildTable()
  {
    _factionStats=new ArrayList<VirtueStatsFromDeeds>();
    ListDataProvider<VirtueStatsFromDeeds> provider=new ListDataProvider<VirtueStatsFromDeeds>(_factionStats);
    GenericTableController<VirtueStatsFromDeeds> table=new GenericTableController<VirtueStatsFromDeeds>(provider);

    // Virtue icon
    {
      CellDataProvider<VirtueStatsFromDeeds,Icon> iconCell=new CellDataProvider<VirtueStatsFromDeeds,Icon>()
      {
        @Override
        public Icon getData(VirtueStatsFromDeeds item)
        {
          VirtueId virtueId=item.getVirtueId();
          Icon icon=LotroIconsManager.getVirtueIcon(virtueId.name());
          return icon;
        }
      };
      DefaultTableColumnController<VirtueStatsFromDeeds,Icon> iconColumn=new DefaultTableColumnController<VirtueStatsFromDeeds,Icon>(ICON,"Icon",Icon.class,iconCell);
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      table.addColumnController(iconColumn);
    }
    // Virtue name
    {
      CellDataProvider<VirtueStatsFromDeeds,String> factionCell=new CellDataProvider<VirtueStatsFromDeeds,String>()
      {
        @Override
        public String getData(VirtueStatsFromDeeds item)
        {
          return item.getVirtueId().getLabel();
        }
      };
      DefaultTableColumnController<VirtueStatsFromDeeds,String> factionColumn=new DefaultTableColumnController<VirtueStatsFromDeeds,String>(VIRTUE,"Virtue",String.class,factionCell);
      factionColumn.setWidthSpecs(200,-1,200);
      table.addColumnController(factionColumn);
    }
    // Virtue points column
    {
      CellDataProvider<VirtueStatsFromDeeds,Integer> amountCell=new CellDataProvider<VirtueStatsFromDeeds,Integer>()
      {
        @Override
        public Integer getData(VirtueStatsFromDeeds item)
        {
          Integer amount=Integer.valueOf(item.getPoints());
          return amount;
        }
      };
      DefaultTableColumnController<VirtueStatsFromDeeds,Integer> amountColumn=new DefaultTableColumnController<VirtueStatsFromDeeds,Integer>(POINTS,"Points",Integer.class,amountCell);
      amountColumn.setWidthSpecs(60,60,60);
      table.addColumnController(amountColumn);
    }
    // Deeds count column
    {
      CellDataProvider<VirtueStatsFromDeeds,Integer> deedsCountCell=new CellDataProvider<VirtueStatsFromDeeds,Integer>()
      {
        @Override
        public Integer getData(VirtueStatsFromDeeds item)
        {
          Integer count=Integer.valueOf(item.getDeedsCount());
          return count;
        }
      };
      DefaultTableColumnController<VirtueStatsFromDeeds,Integer> deedsCountColumn=new DefaultTableColumnController<VirtueStatsFromDeeds,Integer>(DEEDS_COUNT,"Deeds",Integer.class,deedsCountCell);
      deedsCountColumn.setWidthSpecs(60,60,60);
      table.addColumnController(deedsCountColumn);
    }

    TableColumnsManager<VirtueStatsFromDeeds> columnsManager=table.getColumnsManager();
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
    columnIds.add(DEEDS_COUNT);
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<VirtueStatsFromDeeds> getTableController()
  {
    return _tableController;
  }

  /**
   * Update display.
   */
  public void update()
  {
    _factionStats.clear();
    _factionStats.addAll(_stats.getVirtues().values());
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
