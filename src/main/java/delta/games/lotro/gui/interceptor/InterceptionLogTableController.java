package delta.games.lotro.gui.interceptor;

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
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.gui.items.chooser.ItemChooser;
import delta.games.lotro.interceptor.data.monitoring.InterceptionLog;
import delta.games.lotro.interceptor.data.monitoring.InterceptionLogEntry;
import delta.games.lotro.interceptor.data.monitoring.LogLevel;
import delta.games.lotro.utils.Formats;

/**
 * Controller for a table that shows interception log entries.
 * @author DAM
 */
public class InterceptionLogTableController
{
  // Data
  private TypedProperties _prefs;
  private InterceptionLog _log;
  private List<InterceptionLogEntry> _logEntries;
  // GUI
  private JTable _table;
  private GenericTableController<InterceptionLogEntry> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   * @param log Log to show.
   */
  public InterceptionLogTableController(TypedProperties prefs, Filter<InterceptionLogEntry> filter, InterceptionLog log)
  {
    _prefs=prefs;
    _log=log;
    _logEntries=new ArrayList<InterceptionLogEntry>();
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<InterceptionLogEntry> buildTable()
  {
    ListDataProvider<InterceptionLogEntry> provider=new ListDataProvider<InterceptionLogEntry>(_logEntries);
    GenericTableController<InterceptionLogEntry> table=new GenericTableController<InterceptionLogEntry>(provider);
    List<DefaultTableColumnController<InterceptionLogEntry,?>> columns=buildColumns();
    for(DefaultTableColumnController<InterceptionLogEntry,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<InterceptionLogEntry> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a mounts table.
   * @return A list of columns for a mounts table.
   */
  public static List<DefaultTableColumnController<InterceptionLogEntry,?>> buildColumns()
  {
    List<DefaultTableColumnController<InterceptionLogEntry,?>> ret=new ArrayList<DefaultTableColumnController<InterceptionLogEntry,?>>();

    // Date column
    {
      CellDataProvider<InterceptionLogEntry,Date> dateCell=new CellDataProvider<InterceptionLogEntry,Date>()
      {
        @Override
        public Date getData(InterceptionLogEntry entry)
        {
          return entry.getDate();
        }
      };
      DefaultTableColumnController<InterceptionLogEntry,Date> dateColumn=new DefaultTableColumnController<InterceptionLogEntry,Date>(InterceptionLogColumnIds.DATE.name(),"Date",Date.class,dateCell);
      dateColumn.setWidthSpecs(120,120,120);
      dateColumn.setCellRenderer(new DateRenderer(Formats.DATE_TIME_PATTERN));
      ret.add(dateColumn);
    }
    // Level column
    {
      CellDataProvider<InterceptionLogEntry,LogLevel> levelCell=new CellDataProvider<InterceptionLogEntry,LogLevel>()
      {
        @Override
        public LogLevel getData(InterceptionLogEntry entry)
        {
          return entry.getLevel();
        }
      };
      DefaultTableColumnController<InterceptionLogEntry,LogLevel> levelColumn=new DefaultTableColumnController<InterceptionLogEntry,LogLevel>(InterceptionLogColumnIds.LEVEL.name(),"Level",LogLevel.class,levelCell);
      levelColumn.setWidthSpecs(100,100,50);
      ret.add(levelColumn);
    }
    // Character column
    {
      CellDataProvider<InterceptionLogEntry,String> toonCell=new CellDataProvider<InterceptionLogEntry,String>()
      {
        @Override
        public String getData(InterceptionLogEntry entry)
        {
          CharacterFile toon=entry.getToon();
          if (toon!=null)
          {
            return toon.getName()+"@"+toon.getServerName();
          }
          return "";
        }
      };
      DefaultTableColumnController<InterceptionLogEntry,String> toonColumn=new DefaultTableColumnController<InterceptionLogEntry,String>(InterceptionLogColumnIds.CHARACTER.name(),"Character",String.class,toonCell);
      toonColumn.setWidthSpecs(100,140,140);
      ret.add(toonColumn);
    }
    // Label column
    {
      CellDataProvider<InterceptionLogEntry,String> labelCell=new CellDataProvider<InterceptionLogEntry,String>()
      {
        @Override
        public String getData(InterceptionLogEntry entry)
        {
          return entry.getMessage();
        }
      };
      DefaultTableColumnController<InterceptionLogEntry,String> labelColumn=new DefaultTableColumnController<InterceptionLogEntry,String>(InterceptionLogColumnIds.MESSAGE.name(),"Label",String.class,labelCell);
      labelColumn.setWidthSpecs(150,-1,150);
      ret.add(labelColumn);
    }
    return ret;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=null;
    if (_prefs!=null)
    {
      columnIds=_prefs.getStringList(ItemChooser.COLUMNS_PROPERTY);
    }
    if (columnIds==null)
    {
      columnIds=new ArrayList<String>();
      columnIds.add(InterceptionLogColumnIds.DATE.name());
      columnIds.add(InterceptionLogColumnIds.LEVEL.name());
      columnIds.add(InterceptionLogColumnIds.CHARACTER.name());
      columnIds.add(InterceptionLogColumnIds.MESSAGE.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<InterceptionLogEntry> getTableController()
  {
    return _tableController;
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _tableController.filterUpdated();
  }

  /**
   * Get the total number of mounts.
   * @return A number of mounts.
   */
  public int getNbItems()
  {
    return _logEntries.size();
  }

  /**
   * Get the number of filtered items in the managed table.
   * @return A number of items.
   */
  public int getNbFilteredItems()
  {
    int ret=_tableController.getNbFilteredItems();
    return ret;
  }

  /**
   * Refresh table.
   */
  public void refresh()
  {
    init();
    if (_table!=null)
    {
      _tableController.refresh();
    }
  }

  private void init()
  {
    synchronized (_log)
    {
      _logEntries.clear();
      _logEntries.addAll(_log.getEntries());
    }
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
    // Preferences
    if (_prefs!=null)
    {
      List<String> columnIds=_tableController.getColumnsManager().getSelectedColumnsIds();
      _prefs.setStringList(ItemChooser.COLUMNS_PROPERTY,columnIds);
      _prefs=null;
    }
    // GUI
    _table=null;
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _logEntries=null;
  }
}
