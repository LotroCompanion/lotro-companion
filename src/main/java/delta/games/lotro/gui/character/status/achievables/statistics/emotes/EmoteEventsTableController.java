package delta.games.lotro.gui.character.status.achievables.statistics.emotes;

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
import delta.games.lotro.character.status.achievables.statistics.emotes.EmoteEvent;
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;

/**
 * Controller for a table that shows the emote events for a single character.
 * @author DAM
 */
public class EmoteEventsTableController
{
  private static final String EMOTE="EMOTE";
  private static final String DATE="COMPLETION_DATE";
  private static final String ACHIEVABLE_NAME="ACHIEVABLE";

  // Data
  private AchievablesStatistics _stats;
  // GUI
  private JTable _table;
  private GenericTableController<EmoteEvent> _tableController;

  /**
   * Constructor.
   * @param stats Stats to show.
   */
  public EmoteEventsTableController(AchievablesStatistics stats)
  {
    _stats=stats;
    _tableController=buildTable();
  }

  private GenericTableController<EmoteEvent> buildTable()
  {
    ListDataProvider<EmoteEvent> provider=new ListDataProvider<EmoteEvent>(_stats.getEmotes());
    GenericTableController<EmoteEvent> table=new GenericTableController<EmoteEvent>(provider);

    // Emote
    {
      CellDataProvider<EmoteEvent,String> emoteCell=new CellDataProvider<EmoteEvent,String>()
      {
        @Override
        public String getData(EmoteEvent item)
        {
          return item.getEmote();
        }
      };
      DefaultTableColumnController<EmoteEvent,String> emoteColumn=new DefaultTableColumnController<EmoteEvent,String>(EMOTE,"Emote",String.class,emoteCell); // I18n
      emoteColumn.setWidthSpecs(100,300,200);
      table.addColumnController(emoteColumn);
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
      DefaultTableColumnController<EmoteEvent,Date> completionDateColumn=new DefaultTableColumnController<EmoteEvent,Date>(DATE,"Date",Date.class,completionDateCell); // I18n
      StatColumnsUtils.configureDateTimeColumn(completionDateColumn);
      table.addColumnController(completionDateColumn);
    }
    // Achievable name column
    {
      CellDataProvider<EmoteEvent,String> achievableCell=new CellDataProvider<EmoteEvent,String>()
      {
        @Override
        public String getData(EmoteEvent item)
        {
          return item.getAchievable().getName();
        }
      };
      DefaultTableColumnController<EmoteEvent,String> achievableColumn=new DefaultTableColumnController<EmoteEvent,String>(ACHIEVABLE_NAME,"Source",String.class,achievableCell); // I18n
      achievableColumn.setWidthSpecs(100,300,200);
      table.addColumnController(achievableColumn);
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
    columnIds.add(ACHIEVABLE_NAME);
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
