package delta.games.lotro.gui.character.status.achievables.statistics.traits;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.character.status.achievables.statistics.AchievablesStatistics;
import delta.games.lotro.character.status.achievables.statistics.traits.TraitEvent;
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;

/**
 * Controller for a table that shows the trait events for a single character.
 * @author DAM
 */
public class TraitEventsTableController
{
  private static final String TRAIT="TRAIT";
  private static final String DATE="COMPLETION_DATE";
  private static final String ACHIEVABLE_NAME="ACHIEVABLE";

  // Data
  private AchievablesStatistics _stats;
  // GUI
  private JTable _table;
  private GenericTableController<TraitEvent> _tableController;

  /**
   * Constructor.
   * @param stats Stats to show.
   */
  public TraitEventsTableController(AchievablesStatistics stats)
  {
    _stats=stats;
    _tableController=buildTable();
  }

  private GenericTableController<TraitEvent> buildTable()
  {
    ListDataProvider<TraitEvent> provider=new ListDataProvider<TraitEvent>(_stats.getTraits());
    GenericTableController<TraitEvent> table=new GenericTableController<TraitEvent>(provider);

    // Trait name
    {
      CellDataProvider<TraitEvent,String> traitCell=new CellDataProvider<TraitEvent,String>()
      {
        @Override
        public String getData(TraitEvent item)
        {
          return item.getTrait();
        }
      };
      DefaultTableColumnController<TraitEvent,String> traitColumn=new DefaultTableColumnController<TraitEvent,String>(TRAIT,"Trait",String.class,traitCell); // I18n
      traitColumn.setWidthSpecs(100,300,200);
      table.addColumnController(traitColumn);
    }
    // Date column
    {
      CellDataProvider<TraitEvent,Date> completionDateCell=new CellDataProvider<TraitEvent,Date>()
      {
        @Override
        public Date getData(TraitEvent item)
        {
          Long timestamp=item.getDate();
          return (timestamp!=null)?new Date(timestamp.longValue()):null;
        }
      };
      DefaultTableColumnController<TraitEvent,Date> completionDateColumn=new DefaultTableColumnController<TraitEvent,Date>(DATE,"Date",Date.class,completionDateCell); // I18n
      StatColumnsUtils.configureDateTimeColumn(completionDateColumn);
      table.addColumnController(completionDateColumn);
    }
    // Achievable name column
    {
      CellDataProvider<TraitEvent,String> achievableCell=new CellDataProvider<TraitEvent,String>()
      {
        @Override
        public String getData(TraitEvent item)
        {
          return item.getAchievable().getName();
        }
      };
      DefaultTableColumnController<TraitEvent,String> achievableColumn=new DefaultTableColumnController<TraitEvent,String>(ACHIEVABLE_NAME,"Source",String.class,achievableCell); // I18n
      achievableColumn.setWidthSpecs(100,300,200);
      table.addColumnController(achievableColumn);
    }

    TableColumnsManager<TraitEvent> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=new ArrayList<String>();
    columnIds.add(TRAIT);
    columnIds.add(DATE);
    columnIds.add(ACHIEVABLE_NAME);
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<TraitEvent> getTableController()
  {
    return _tableController;
  }

  /**
   * Update display.
   */
  public void update()
  {
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
