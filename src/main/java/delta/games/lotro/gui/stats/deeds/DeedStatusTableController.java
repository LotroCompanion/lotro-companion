package delta.games.lotro.gui.stats.deeds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.GenericTableController.DateRenderer;
import delta.common.ui.swing.tables.CellDataUpdater;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.deed.DeedColumnIds;
import delta.games.lotro.gui.deed.DeedsTableController;
import delta.games.lotro.gui.items.chooser.ItemChooser;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.stats.deeds.DeedStatus;
import delta.games.lotro.stats.deeds.DeedsStatusManager;
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
  private DeedsStatusManager _deedsStatus;
  private List<DeedDescription> _deeds;
  // GUI
  private JTable _table;
  private GenericTableController<DeedDescription> _tableController;

  /**
   * Constructor.
   * @param deedsStatus Status to show.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public DeedStatusTableController(DeedsStatusManager deedsStatus, TypedProperties prefs, Filter<DeedDescription> filter)
  {
    _prefs=prefs;
    _deeds=DeedsManager.getInstance().getAll();
    _deedsStatus=deedsStatus;
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<DeedDescription> buildTable()
  {
    ListDataProvider<DeedDescription> provider=new ListDataProvider<DeedDescription>(_deeds);
    GenericTableController<DeedDescription> table=new GenericTableController<DeedDescription>(provider);
    List<DefaultTableColumnController<DeedDescription,?>> deedColumns=DeedsTableController.buildColumns();
    for(DefaultTableColumnController<DeedDescription,?> deedColumn : deedColumns)
    {
      table.addColumnController(deedColumn);
    }

    // Status
    {
      CellDataProvider<DeedDescription,Boolean> completedCell=new CellDataProvider<DeedDescription,Boolean>()
      {
        @Override
        public Boolean getData(DeedDescription item)
        {
          DeedStatus status=_deedsStatus.get(item.getKey(),false);
          if (status!=null)
          {
            return status.isCompleted();
          }
          return Boolean.FALSE;
        }
      };
      DefaultTableColumnController<DeedDescription,Boolean> completedColumn=new DefaultTableColumnController<DeedDescription,Boolean>(COMPLETED,"Completed",Boolean.class,completedCell);
      completedColumn.setWidthSpecs(30,30,30);

      completedColumn.setEditable(true);
      CellDataUpdater<DeedDescription> updater=new CellDataUpdater<DeedDescription>()
      {
        @Override
        public void setData(DeedDescription item, Object value)
        {
          DeedStatus status=_deedsStatus.get(item.getKey(),true);
          Boolean completed=(Boolean)value;
          status.setCompleted(completed);
        }
      };
      completedColumn.setValueUpdater(updater);
      table.addColumnController(completedColumn);
    }
    // Completion date column
    {
      CellDataProvider<DeedDescription,Date> completionDateCell=new CellDataProvider<DeedDescription,Date>()
      {
        @Override
        public Date getData(DeedDescription item)
        {
          Long timestamp=null;
          DeedStatus status=_deedsStatus.get(item.getKey(),false);
          if (status!=null)
          {
            timestamp=status.getCompletionDate();
          }
          return (timestamp!=null)?new Date(timestamp.longValue()):null;
        }
      };
      DefaultTableColumnController<DeedDescription,Date> completionDateColumn=new DefaultTableColumnController<DeedDescription,Date>(COMPLETION_DATE,"Completion Date",Date.class,completionDateCell);
      completionDateColumn.setWidthSpecs(120,120,120);
      completionDateColumn.setCellRenderer(new DateRenderer(Formats.DATE_TIME_PATTERN));
      table.addColumnController(completionDateColumn);
    }

    TableColumnsManager<DeedDescription> columnsManager=table.getColumnsManager();
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
      columnIds.add(COMPLETION_DATE);
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<DeedDescription> getTableController()
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
    return _deeds.size();
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
    _deeds=null;
    _deedsStatus=null;
  }
}
