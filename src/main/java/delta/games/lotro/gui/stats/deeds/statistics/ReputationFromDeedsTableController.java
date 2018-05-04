package delta.games.lotro.gui.stats.deeds.statistics;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.stats.deeds.DeedsStatistics;
import delta.games.lotro.stats.deeds.FactionStatsFromDeeds;

/**
 * Controller for a table that shows the title events for a single character.
 * @author DAM
 */
public class ReputationFromDeedsTableController
{
  private static final String FACTION="FACTION";
  private static final String AMOUNT="AMOUNT";
  private static final String DEEDS_COUNT="DEEDS_COUNT";

  // Data
  private DeedsStatistics _stats;
  private List<FactionStatsFromDeeds> _factionStats;
  // GUI
  private JTable _table;
  private GenericTableController<FactionStatsFromDeeds> _tableController;

  /**
   * Constructor.
   * @param stats Stats to show.
   */
  public ReputationFromDeedsTableController(DeedsStatistics stats)
  {
    _stats=stats;
    _tableController=buildTable();
  }

  private GenericTableController<FactionStatsFromDeeds> buildTable()
  {
    _factionStats=new ArrayList<FactionStatsFromDeeds>();
    ListDataProvider<FactionStatsFromDeeds> provider=new ListDataProvider<FactionStatsFromDeeds>(_factionStats);
    GenericTableController<FactionStatsFromDeeds> table=new GenericTableController<FactionStatsFromDeeds>(provider);

    // Faction
    {
      CellDataProvider<FactionStatsFromDeeds,String> factionCell=new CellDataProvider<FactionStatsFromDeeds,String>()
      {
        @Override
        public String getData(FactionStatsFromDeeds item)
        {
          return item.getFaction().getName();
        }
      };
      DefaultTableColumnController<FactionStatsFromDeeds,String> factionColumn=new DefaultTableColumnController<FactionStatsFromDeeds,String>(FACTION,"Faction",String.class,factionCell);
      factionColumn.setWidthSpecs(100,300,200);
      table.addColumnController(factionColumn);
    }
    // Amount column
    {
      CellDataProvider<FactionStatsFromDeeds,Integer> amountCell=new CellDataProvider<FactionStatsFromDeeds,Integer>()
      {
        @Override
        public Integer getData(FactionStatsFromDeeds item)
        {
          Integer amount=Integer.valueOf(item.getPoints());
          return amount;
        }
      };
      DefaultTableColumnController<FactionStatsFromDeeds,Integer> amountColumn=new DefaultTableColumnController<FactionStatsFromDeeds,Integer>(AMOUNT,"Amount",Integer.class,amountCell);
      amountColumn.setWidthSpecs(120,120,120);
      table.addColumnController(amountColumn);
    }
    // Deeds count column
    {
      CellDataProvider<FactionStatsFromDeeds,Integer> deedsCountCell=new CellDataProvider<FactionStatsFromDeeds,Integer>()
      {
        @Override
        public Integer getData(FactionStatsFromDeeds item)
        {
          Integer count=Integer.valueOf(item.getDeedsCount());
          return count;
        }
      };
      DefaultTableColumnController<FactionStatsFromDeeds,Integer> deedsCountColumn=new DefaultTableColumnController<FactionStatsFromDeeds,Integer>(DEEDS_COUNT,"Deeds",Integer.class,deedsCountCell);
      deedsCountColumn.setWidthSpecs(100,300,200);
      table.addColumnController(deedsCountColumn);
    }

    TableColumnsManager<FactionStatsFromDeeds> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=new ArrayList<String>();
    columnIds.add(FACTION);
    columnIds.add(AMOUNT);
    columnIds.add(DEEDS_COUNT);
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<FactionStatsFromDeeds> getTableController()
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
    if (_table==null)
    {
      _table=_tableController.getTable();
    }
    return _table;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    _table=null;
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _stats=null;
  }
}
