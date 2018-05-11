package delta.games.lotro.gui.stats.deeds.statistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.GenericTableController.DateRenderer;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.stats.deeds.DeedsStatistics;
import delta.games.lotro.stats.deeds.statistics.EmoteEvent;
import delta.games.lotro.utils.Formats;

/**
 * Controller for a table that shows the emote events for a single character.
 * @author DAM
 */
public class EmoteEventsTableController
{
  private static final String EMOTE="EMOTE";
  private static final String DATE="COMPLETION_DATE";
  private static final String DEED_NAME="DEED";

  // Data
  private DeedsStatistics _stats;
  // GUI
  private JTable _table;
  private GenericTableController<EmoteEvent> _tableController;

  /**
   * Constructor.
   * @param stats Stats to show.
   */
  public EmoteEventsTableController(DeedsStatistics stats)
  {
    _stats=stats;
    _tableController=buildTable();
  }

  private GenericTableController<EmoteEvent> buildTable()
  {
    ListDataProvider<EmoteEvent> provider=new ListDataProvider<EmoteEvent>(_stats.getEmotes());
    GenericTableController<EmoteEvent> table=new GenericTableController<EmoteEvent>(provider);

    // Title
    {
      CellDataProvider<EmoteEvent,String> titleCell=new CellDataProvider<EmoteEvent,String>()
      {
        @Override
        public String getData(EmoteEvent item)
        {
          return item.getEmote();
        }
      };
      DefaultTableColumnController<EmoteEvent,String> titleColumn=new DefaultTableColumnController<EmoteEvent,String>(EMOTE,"Emote",String.class,titleCell);
      titleColumn.setWidthSpecs(100,300,200);
      table.addColumnController(titleColumn);
    }
    // Date column
    {
      CellDataProvider<EmoteEvent,Date> completionDateCell=new CellDataProvider<EmoteEvent,Date>()
      {
        @Override
        public Date getData(EmoteEvent item)
        {
          Long timestamp=item.getDate();
          return (timestamp!=null)?new Date(timestamp.longValue()):null;
        }
      };
      DefaultTableColumnController<EmoteEvent,Date> completionDateColumn=new DefaultTableColumnController<EmoteEvent,Date>(DATE,"Date",Date.class,completionDateCell);
      completionDateColumn.setWidthSpecs(120,120,120);
      completionDateColumn.setCellRenderer(new DateRenderer(Formats.DATE_TIME_PATTERN));
      table.addColumnController(completionDateColumn);
    }
    // Deed name column
    {
      CellDataProvider<EmoteEvent,String> deedCell=new CellDataProvider<EmoteEvent,String>()
      {
        @Override
        public String getData(EmoteEvent item)
        {
          return item.getDeed().getName();
        }
      };
      DefaultTableColumnController<EmoteEvent,String> deedColumn=new DefaultTableColumnController<EmoteEvent,String>(DEED_NAME,"Deed",String.class,deedCell);
      deedColumn.setWidthSpecs(100,300,200);
      table.addColumnController(deedColumn);
    }

    TableColumnsManager<EmoteEvent> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=new ArrayList<String>();
    columnIds.add(EMOTE);
    columnIds.add(DATE);
    columnIds.add(DEED_NAME);
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<EmoteEvent> getTableController()
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