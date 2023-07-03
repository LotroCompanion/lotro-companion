package delta.games.lotro.gui.character.status.titles.table;

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
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.Sort;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.status.titles.TitleStatus;
import delta.games.lotro.character.status.titles.TitlesStatusManager;
import delta.games.lotro.character.status.titles.comparators.TitleAcquisitionTimestampComparator;
import delta.games.lotro.character.status.titles.filter.TitleStatusFilter;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.lore.titles.TitleColumnIds;
import delta.games.lotro.gui.lore.titles.TitlesTableController;
import delta.games.lotro.lore.titles.TitleDescription;

/**
 * Controller for a table that shows the status of all titles for a single character.
 * @author DAM
 */
public class TitlesStatusTableController extends AbstractAreaController
{
  // Data
  private TypedProperties _prefs;
  private List<TitleStatus> _statuses;
  // GUI
  private JTable _table;
  private GenericTableController<TitleStatus> _tableController;

  /**
   * Constructor.
   * @param titlesStatus Status to show.
   * @param parent Parent controller.
   * @param prefs Preferences.
   * @param filter Managed filter.
   * @param titles Titles to use.
   * @param listener Listener for updates.
   */
  public TitlesStatusTableController(TitlesStatusManager titlesStatus, AreaController parent, TypedProperties prefs, TitleStatusFilter filter, List<TitleDescription> titles, FilterUpdateListener listener)
  {
    super(parent);
    _prefs=prefs;
    _statuses=new ArrayList<TitleStatus>();
    for(TitleDescription title : titles)
    {
      TitleStatus status=titlesStatus.get(title,true);
      _statuses.add(status);
    }
    Collections.sort(_statuses,new TitleAcquisitionTimestampComparator());
    int index=0;
    for(TitleStatus status : _statuses)
    {
      if (status.getAcquisitionTimeStamp()!=null)
      {
        index++;
        status.setAcquisitionOrder(Integer.valueOf(index));
      }
    }
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  private GenericTableController<TitleStatus> buildTable()
  {
    ListDataProvider<TitleStatus> provider=new ListDataProvider<TitleStatus>(_statuses);
    GenericTableController<TitleStatus> table=new GenericTableController<TitleStatus>(provider);
    // Title columns
    List<DefaultTableColumnController<TitleDescription,?>> titleColumns=TitlesTableController.buildColumns(this);
    CellDataProvider<TitleStatus,TitleDescription> dataProvider=new CellDataProvider<TitleStatus,TitleDescription>()
    {
      @Override
      public TitleDescription getData(TitleStatus status)
      {
        return status.getTitle();
      }
    };
    for(TableColumnController<TitleDescription,?> titleColumn : titleColumns)
    {
      @SuppressWarnings("unchecked")
      TableColumnController<TitleDescription,Object> c=(TableColumnController<TitleDescription,Object>)titleColumn;
      TableColumnController<TitleStatus,Object> proxiedColumn=new ProxiedTableColumnController<TitleStatus,TitleDescription,Object>(c,dataProvider);
      table.addColumnController(proxiedColumn);
    }
    // Title status columns
    for(TableColumnController<TitleStatus,?> column : TitleStatusColumnsBuilder.buildTitleStatusColumns())
    {
      table.addColumnController(column);
    }

    TableColumnsManager<TitleStatus> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);

    // Sort
    Sort sort=Sort.buildFromString(Sort.SORT_DESCENDING+TitleStatusColumnIds.ORDER);
    table.setSort(sort);
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
      columnIds.add(TitleStatusColumnIds.ACQUIRED.name());
      columnIds.add(TitleStatusColumnIds.ORDER.name());
      //columnIds.add(TitleStatusColumnIds.ACQUISITION_DATE.name());
      columnIds.add(TitleColumnIds.ICON.name());
      columnIds.add(TitleColumnIds.NAME.name());
      columnIds.add(TitleColumnIds.CATEGORY.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<TitleStatus> getTableController()
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
   * Get the total number of title statuses.
   * @return A number of title statuses.
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
    super.dispose();
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
