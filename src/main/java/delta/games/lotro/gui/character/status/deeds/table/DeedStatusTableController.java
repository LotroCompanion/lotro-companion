package delta.games.lotro.gui.character.status.deeds.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.area.AbstractAreaController;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.filter.DeedStatusFilter;
import delta.games.lotro.common.blacklist.Blacklist;
import delta.games.lotro.gui.character.status.achievables.table.AchievableStatusColumnIds;
import delta.games.lotro.gui.character.status.achievables.table.AchievableStatusColumnsBuilder;
import delta.games.lotro.gui.lore.deeds.table.DeedColumnIds;
import delta.games.lotro.gui.lore.deeds.table.DeedsTableController;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.lore.deeds.DeedDescription;

/**
 * Controller for a table that shows the status of all deeds for a single character.
 * @author DAM
 */
public class DeedStatusTableController extends AbstractAreaController
{
  // Data
  private TypedProperties _prefs;
  private List<AchievableStatus> _statuses;
  private Blacklist _blacklist;
  // GUI
  private JTable _table;
  private GenericTableController<AchievableStatus> _tableController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param deedsStatus Status to show.
   * @param prefs Preferences.
   * @param filter Managed filter.
   * @param deeds Deeds to use.
   * @param listener Listener for updates.
   * @param blacklist Blacklist.
   */
  public DeedStatusTableController(AreaController parent, AchievablesStatusManager deedsStatus, TypedProperties prefs, DeedStatusFilter filter, List<DeedDescription> deeds, FilterUpdateListener listener, Blacklist blacklist)
  {
    super(parent);
    _prefs=prefs;
    _blacklist=blacklist;
    _statuses=new ArrayList<AchievableStatus>();
    for(DeedDescription deed : deeds)
    {
      AchievableStatus status=deedsStatus.get(deed,true);
      _statuses.add(status);
    }
    _tableController=buildTable(listener);
    _tableController.setFilter(filter);
  }

  private GenericTableController<AchievableStatus> buildTable(FilterUpdateListener listener)
  {
    ListDataProvider<AchievableStatus> provider=new ListDataProvider<AchievableStatus>(_statuses);
    GenericTableController<AchievableStatus> table=new GenericTableController<AchievableStatus>(provider);
    // Deed columns
    List<TableColumnController<DeedDescription,?>> deedColumns=DeedsTableController.buildColumns(this);
    CellDataProvider<AchievableStatus,DeedDescription> dataProvider=new CellDataProvider<AchievableStatus,DeedDescription>()
    {
      @Override
      public DeedDescription getData(AchievableStatus status)
      {
        return (DeedDescription)status.getAchievable();
      }
    };
    for(TableColumnController<DeedDescription,?> deedColumn : deedColumns)
    {
      @SuppressWarnings("unchecked")
      TableColumnController<DeedDescription,Object> c=(TableColumnController<DeedDescription,Object>)deedColumn;
      TableColumnController<AchievableStatus,Object> proxiedColumn=new ProxiedTableColumnController<AchievableStatus,DeedDescription,Object>(c,dataProvider);
      table.addColumnController(proxiedColumn);
    }
    // Achievable status columns
    for(TableColumnController<AchievableStatus,?> column : AchievableStatusColumnsBuilder.buildDeedStateColumns(listener))
    {
      table.addColumnController(column);
    }
    // Blacklisted column
    table.addColumnController(buildBlacklistedColumn());
    TableColumnsManager<AchievableStatus> columnsManager=table.getColumnsManager();

    // Setup
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);

    return table;
  }

  private TableColumnController<AchievableStatus,String> buildBlacklistedColumn()
  {
    CellDataProvider<AchievableStatus,String> countCell=new CellDataProvider<AchievableStatus,String>()
    {
      @Override
      public String getData(AchievableStatus status)
      {
        return (_blacklist.isBlacklisted(status.getIdentifier()))?"Yes":"No";
      }
    };
    DefaultTableColumnController<AchievableStatus,String> countColumn=new DefaultTableColumnController<AchievableStatus,String>(AchievableStatusColumnIds.BLACKLISTED.name(),"Blacklisted",String.class,countCell);
    countColumn.setWidthSpecs(50,50,50);
    return countColumn;
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
      columnIds.add(AchievableStatusColumnIds.COMPLETED.name());
      columnIds.add(DeedColumnIds.NAME.name());
      columnIds.add(DeedColumnIds.CATEGORY.name());
      columnIds.add(DeedColumnIds.LEVEL.name());
      columnIds.add(AchievableStatusColumnIds.COMPLETION_DATE.name());
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
    super.dispose();
    // Preferences
    if (_prefs!=null)
    {
      List<String> columnIds=_tableController.getColumnsManager().getSelectedColumnsIds();
      _prefs.setStringList(ItemChooser.COLUMNS_PROPERTY,columnIds);
      _prefs=null;
    }
    _blacklist=null;
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
