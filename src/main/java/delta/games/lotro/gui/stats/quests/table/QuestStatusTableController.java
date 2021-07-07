package delta.games.lotro.gui.stats.quests.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.character.achievables.AchievablesStatusManager;
import delta.games.lotro.character.achievables.filter.QuestStatusFilter;
import delta.games.lotro.gui.items.chooser.ItemChooser;
import delta.games.lotro.gui.quests.table.QuestColumnIds;
import delta.games.lotro.gui.quests.table.QuestsTableController;
import delta.games.lotro.gui.stats.achievables.table.AchievableStatusColumnIds;
import delta.games.lotro.gui.stats.achievables.table.AchievableStatusColumnsBuilder;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;

/**
 * Controller for a table that shows the status of all quests for a single character.
 * @author DAM
 */
public class QuestStatusTableController
{
  // Data
  private TypedProperties _prefs;
  private List<AchievableStatus> _statuses;
  // GUI
  private JTable _table;
  private GenericTableController<AchievableStatus> _tableController;

  /**
   * Constructor.
   * @param questsStatus Status to show.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public QuestStatusTableController(AchievablesStatusManager questsStatus, TypedProperties prefs, QuestStatusFilter filter)
  {
    _prefs=prefs;
    List<QuestDescription> quests=QuestsManager.getInstance().getAll();
    _statuses=new ArrayList<AchievableStatus>();
    for(QuestDescription quest : quests)
    {
      AchievableStatus status=questsStatus.get(quest,true);
      _statuses.add(status);
    }
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<AchievableStatus> buildTable()
  {
    ListDataProvider<AchievableStatus> provider=new ListDataProvider<AchievableStatus>(_statuses);
    GenericTableController<AchievableStatus> table=new GenericTableController<AchievableStatus>(provider);
    // Quest columns
    List<TableColumnController<QuestDescription,?>> questColumns=QuestsTableController.buildColumns();
    CellDataProvider<AchievableStatus,QuestDescription> dataProvider=new CellDataProvider<AchievableStatus,QuestDescription>()
    {
      @Override
      public QuestDescription getData(AchievableStatus status)
      {
        return (QuestDescription)status.getAchievable();
      }
    };
    for(TableColumnController<QuestDescription,?> questColumn : questColumns)
    {
      @SuppressWarnings("unchecked")
      TableColumnController<QuestDescription,Object> c=(TableColumnController<QuestDescription,Object>)questColumn;
      TableColumnController<AchievableStatus,Object> proxiedColumn=new ProxiedTableColumnController<AchievableStatus,QuestDescription,Object>(c,dataProvider);
      table.addColumnController(proxiedColumn);
    }
    // Achievable status columns
    for(TableColumnController<AchievableStatus,?> column : AchievableStatusColumnsBuilder.buildQuestStateColumns())
    {
      table.addColumnController(column);
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
      columnIds.add(AchievableStatusColumnIds.COMPLETED.name());
      columnIds.add(QuestColumnIds.NAME.name());
      columnIds.add(QuestColumnIds.CATEGORY.name());
      columnIds.add(QuestColumnIds.LEVEL.name());
      columnIds.add(AchievableStatusColumnIds.COMPLETION_COUNT.name());
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
   * Get the total number of quests.
   * @return A number of quests.
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
