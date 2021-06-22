package delta.games.lotro.gui.stats.achievables.statistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.GenericTableController.DateRenderer;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.stats.achievables.AchievablesStatistics;
import delta.games.lotro.stats.achievables.statistics.TitleEvent;
import delta.games.lotro.utils.Formats;

/**
 * Controller for a table that shows the title events for a single character.
 * @author DAM
 */
public class TitleEventsTableController
{
  private static final String TITLE="TITLE";
  private static final String DATE="COMPLETION_DATE";
  private static final String DEED_NAME="DEED";

  // Data
  private AchievablesStatistics _stats;
  // GUI
  private JTable _table;
  private GenericTableController<TitleEvent> _tableController;

  /**
   * Constructor.
   * @param stats Stats to show.
   */
  public TitleEventsTableController(AchievablesStatistics stats)
  {
    _stats=stats;
    _tableController=buildTable();
  }

  private GenericTableController<TitleEvent> buildTable()
  {
    ListDataProvider<TitleEvent> provider=new ListDataProvider<TitleEvent>(_stats.getTitles());
    GenericTableController<TitleEvent> table=new GenericTableController<TitleEvent>(provider);

    // Title
    {
      CellDataProvider<TitleEvent,String> titleCell=new CellDataProvider<TitleEvent,String>()
      {
        @Override
        public String getData(TitleEvent item)
        {
          return item.getTitle();
        }
      };
      DefaultTableColumnController<TitleEvent,String> titleColumn=new DefaultTableColumnController<TitleEvent,String>(TITLE,"Title",String.class,titleCell);
      titleColumn.setWidthSpecs(100,300,200);
      table.addColumnController(titleColumn);
    }
    // Date column
    {
      CellDataProvider<TitleEvent,Date> completionDateCell=new CellDataProvider<TitleEvent,Date>()
      {
        @Override
        public Date getData(TitleEvent item)
        {
          Long timestamp=item.getDate();
          return (timestamp!=null)?new Date(timestamp.longValue()):null;
        }
      };
      DefaultTableColumnController<TitleEvent,Date> completionDateColumn=new DefaultTableColumnController<TitleEvent,Date>(DATE,"Date",Date.class,completionDateCell);
      completionDateColumn.setWidthSpecs(120,120,120);
      completionDateColumn.setCellRenderer(new DateRenderer(Formats.DATE_TIME_PATTERN));
      table.addColumnController(completionDateColumn);
    }
    // Achievable name column
    {
      CellDataProvider<TitleEvent,String> deedCell=new CellDataProvider<TitleEvent,String>()
      {
        @Override
        public String getData(TitleEvent item)
        {
          return item.getAchievable().getName();
        }
      };
      DefaultTableColumnController<TitleEvent,String> deedColumn=new DefaultTableColumnController<TitleEvent,String>(DEED_NAME,"Deed",String.class,deedCell);
      deedColumn.setWidthSpecs(100,300,200);
      table.addColumnController(deedColumn);
    }

    TableColumnsManager<TitleEvent> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=new ArrayList<String>();
    columnIds.add(TITLE);
    columnIds.add(DATE);
    columnIds.add(DEED_NAME);
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<TitleEvent> getTableController()
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
