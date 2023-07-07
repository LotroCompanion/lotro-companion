package delta.games.lotro.gui.common.statistics.reputation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.area.AbstractAreaController;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.common.Named;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.common.statistics.FactionStats;
import delta.games.lotro.common.statistics.ReputationStats;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.utils.DataProvider;
import delta.games.lotro.utils.comparators.DelegatingComparator;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for a table that shows some reputations.
 * @author DAM
 * @param <T> Type of managed faction stats entries.
 */
public abstract class ReputationTableController<T extends FactionStats> extends AbstractAreaController
{
  private static final String FACTION="FACTION";
  private static final String AMOUNT="AMOUNT";

  // Data
  private ReputationStats<T> _stats;
  private List<T> _factionStats;
  // GUI
  private GenericTableController<T> _tableController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param stats Stats to show.
   */
  public ReputationTableController(AreaController parent, ReputationStats<T> stats)
  {
    super(parent);
    _stats=stats;
    _tableController=buildTable();
  }

  private GenericTableController<T> buildTable()
  {
    _factionStats=new ArrayList<T>();
    ListDataProvider<T> provider=new ListDataProvider<T>(_factionStats);
    GenericTableController<T> table=new GenericTableController<T>(provider);
    // Define columns
    defineColumns(table);
    // Set columns to use
    TableColumnsManager<T> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  protected void defineColumns(GenericTableController<T> table)
  {
    // Faction
    {
      CellDataProvider<T,String> factionCell=new CellDataProvider<T,String>()
      {
        @Override
        public String getData(T item)
        {
          Faction faction=item.getFaction();
          String rawFactionName=faction.getName();
          String factionName=ContextRendering.render(ReputationTableController.this,rawFactionName);
          return factionName;
        }
      };
      DefaultTableColumnController<T,String> factionColumn=new DefaultTableColumnController<T,String>(FACTION,"Faction",String.class,factionCell); // I18n
      factionColumn.setWidthSpecs(200,-1,200);
      table.addColumnController(factionColumn);
    }
    // Amount column
    {
      CellDataProvider<T,Integer> amountCell=new CellDataProvider<T,Integer>()
      {
        @Override
        public Integer getData(T item)
        {
          Integer amount=Integer.valueOf(item.getPoints());
          return amount;
        }
      };
      DefaultTableColumnController<T,Integer> amountColumn=new DefaultTableColumnController<T,Integer>(AMOUNT,"Points",Integer.class,amountCell); // I18n
      amountColumn.setWidthSpecs(60,60,60);
      table.addColumnController(amountColumn);
    }
  }

  protected List<String> getColumnIds()
  {
    List<String> columnIds=new ArrayList<String>();
    columnIds.add(FACTION);
    columnIds.add(AMOUNT);
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<T> getTableController()
  {
    return _tableController;
  }

  /**
   * Update display.
   */
  public void update()
  {
    List<T> factionStats=_stats.getFactionStats();
    sortFactionStatsByName(factionStats);
    _factionStats.clear();
    _factionStats.addAll(factionStats);
    _tableController.refresh();
  }

  private void sortFactionStatsByName(List<T> factionStats)
  {
    DataProvider<T,Named> provider=new DataProvider<T,Named>()
    {
      public Named getData(T p)
      {
        return p.getFaction();
      }
    };
    DelegatingComparator<T,Named> c=new DelegatingComparator<T,Named>(provider,new NamedComparator());
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
    super.dispose();
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
