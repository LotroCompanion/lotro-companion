package delta.games.lotro.gui.stats.deeds.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.CellDataUpdater;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.GenericTableController.DateRenderer;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.achievables.AchievableElementState;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.character.achievables.AchievablesStatusManager;
import delta.games.lotro.character.achievables.filter.DeedStatusFilter;
import delta.games.lotro.gui.deed.table.DeedColumnIds;
import delta.games.lotro.gui.deed.table.DeedsTableController;
import delta.games.lotro.gui.items.chooser.ItemChooser;
import delta.games.lotro.gui.stats.achievables.table.AchievableElementStateTableCellEditor;
import delta.games.lotro.gui.stats.achievables.table.AchievableElementStateTableCellRenderer;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.utils.Formats;

/**
 * Controller for a table that shows the status of all deeds for a single character.
 * @author DAM
 */
public class DeedStatusTableController
{
  private static final String COMPLETED="COMPLETED";
  private static final String COMPLETION_DATE="COMPLETION_DATE";

  // Data
  private TypedProperties _prefs;
  private List<AchievableStatus> _statuses;
  // GUI
  private JTable _table;
  private GenericTableController<AchievableStatus> _tableController;

  /**
   * Constructor.
   * @param deedsStatus Status to show.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public DeedStatusTableController(AchievablesStatusManager deedsStatus, TypedProperties prefs, DeedStatusFilter filter)
  {
    _prefs=prefs;
    List<DeedDescription> deeds=DeedsManager.getInstance().getAll();
    _statuses=new ArrayList<AchievableStatus>();
    for(DeedDescription deed : deeds)
    {
      AchievableStatus status=deedsStatus.get(deed,true);
      _statuses.add(status);
    }
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<AchievableStatus> buildTable()
  {
    ListDataProvider<AchievableStatus> provider=new ListDataProvider<AchievableStatus>(_statuses);
    GenericTableController<AchievableStatus> table=new GenericTableController<AchievableStatus>(provider);
    List<TableColumnController<DeedDescription,?>> deedColumns=DeedsTableController.buildColumns();
    CellDataProvider<AchievableStatus,DeedDescription> dataProvider=new CellDataProvider<AchievableStatus,DeedDescription>()
    {
      @Override
      public DeedDescription getData(AchievableStatus deed)
      {
        return (DeedDescription)deed.getAchievable();
      }
    };
    for(TableColumnController<DeedDescription,?> deedColumn : deedColumns)
    {
      @SuppressWarnings("unchecked")
      TableColumnController<DeedDescription,Object> c=(TableColumnController<DeedDescription,Object>)deedColumn;
      TableColumnController<AchievableStatus,Object> proxiedColumn=new ProxiedTableColumnController<AchievableStatus,DeedDescription,Object>(c,dataProvider);
      table.addColumnController(proxiedColumn);
    }

    // State
    {
      CellDataProvider<AchievableStatus,AchievableElementState> completedCell=new CellDataProvider<AchievableStatus,AchievableElementState>()
      {
        @Override
        public AchievableElementState getData(AchievableStatus status)
        {
          return status.getState();
        }
      };
      DefaultTableColumnController<AchievableStatus,AchievableElementState> completedColumn=new DefaultTableColumnController<AchievableStatus,AchievableElementState>(COMPLETED,"Completed",AchievableElementState.class,completedCell);
      completedColumn.setWidthSpecs(30,30,30);
      completedColumn.setEditable(true);
      // Renderer
      completedColumn.setCellRenderer(new AchievableElementStateTableCellRenderer());
      completedColumn.setCellEditor(new AchievableElementStateTableCellEditor());
      // Updater
      CellDataUpdater<AchievableStatus> updater=new CellDataUpdater<AchievableStatus>()
      {
        @Override
        public void setData(AchievableStatus status, Object value)
        {
          status.setState((AchievableElementState)value);
        }
      };
      completedColumn.setValueUpdater(updater);
      table.addColumnController(completedColumn);
    }
    // Completion date column
    {
      CellDataProvider<AchievableStatus,Date> completionDateCell=new CellDataProvider<AchievableStatus,Date>()
      {
        @Override
        public Date getData(AchievableStatus status)
        {
          Long timestamp=status.getCompletionDate();
          return (timestamp!=null)?new Date(timestamp.longValue()):null;
        }
      };
      DefaultTableColumnController<AchievableStatus,Date> completionDateColumn=new DefaultTableColumnController<AchievableStatus,Date>(COMPLETION_DATE,"Completion Date",Date.class,completionDateCell);
      completionDateColumn.setWidthSpecs(120,120,120);
      completionDateColumn.setCellRenderer(new DateRenderer(Formats.DATE_TIME_PATTERN));
      table.addColumnController(completionDateColumn);
    }

    TableColumnsManager<AchievableStatus> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);

    return table;
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
      columnIds.add(COMPLETED);
      columnIds.add(DeedColumnIds.NAME.name());
      columnIds.add(DeedColumnIds.CATEGORY.name());
      columnIds.add(DeedColumnIds.LEVEL.name());
      columnIds.add(COMPLETION_DATE);
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<AchievableStatus> getTableController()
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
   * Get the total number of deeds.
   * @return A number of deeds.
   */
  public int getNbItems()
  {
    return _statuses.size();
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
    _statuses=null;
  }
}
