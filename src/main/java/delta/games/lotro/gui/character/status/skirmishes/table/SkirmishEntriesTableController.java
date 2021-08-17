package delta.games.lotro.gui.character.status.skirmishes.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.status.skirmishes.SkirmishEntriesUtils;
import delta.games.lotro.character.status.skirmishes.SkirmishEntry;
import delta.games.lotro.character.status.skirmishes.SkirmishStatsManager;
import delta.games.lotro.character.status.skirmishes.cfg.SkirmishEntriesPolicy;
import delta.games.lotro.character.status.skirmishes.filter.SkirmishEntryFilter;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;

/**
 * Controller for a table that shows the skirmish entries for a single character.
 * @author DAM
 */
public class SkirmishEntriesTableController
{
  // Data
  private TypedProperties _prefs;
  private SkirmishStatsManager _stats;
  private SkirmishEntryFilter _filter;
  private List<SkirmishEntry> _entries;
  // GUI
  private JTable _table;
  private GenericTableController<SkirmishEntry> _tableController;

  /**
   * Constructor.
   * @param stats Stats.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public SkirmishEntriesTableController(SkirmishStatsManager stats, TypedProperties prefs, SkirmishEntryFilter filter)
  {
    _prefs=prefs;
    _stats=stats;
    _entries=new ArrayList<SkirmishEntry>();
    _tableController=buildTable();
    _filter=filter;
    configureTable();
  }

  private GenericTableController<SkirmishEntry> buildTable()
  {
    ListDataProvider<SkirmishEntry> provider=new ListDataProvider<SkirmishEntry>(_entries);
    GenericTableController<SkirmishEntry> table=new GenericTableController<SkirmishEntry>(provider);
    // Columns
    List<TableColumnController<SkirmishEntry,?>> columns=SkirmishEntryColumnsBuilder.buildSkirmishEntryColumns();
    for(TableColumnController<SkirmishEntry,?> column : columns)
    {
      table.addColumnController(column);
    }
    List<String> columnsIds=getColumnIds();
    TableColumnsManager<SkirmishEntry> columnsManager=table.getColumnsManager();
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
      columnIds.add(SkirmishEntryColumnIds.NAME.name());
      columnIds.add(SkirmishEntryColumnIds.GROUP_SIZE.name());
      columnIds.add(SkirmishEntryColumnIds.LEVEL.name());
      columnIds.add(SkirmishEntryColumnIds.MONSTER_KILLS.name());
      columnIds.add(SkirmishEntryColumnIds.LIEUTENANT_KILLS.name());
      columnIds.add(SkirmishEntryColumnIds.BOSS_KILLS.name());
      columnIds.add(SkirmishEntryColumnIds.BOSS_RESETS.name());
      columnIds.add(SkirmishEntryColumnIds.DEFENDERS_LOST.name());
      columnIds.add(SkirmishEntryColumnIds.DEFENDERS_SAVED.name());
      columnIds.add(SkirmishEntryColumnIds.SOLDIER_DEATHS.name());
      columnIds.add(SkirmishEntryColumnIds.CONTROL_POINTS_TAKEN.name());
      columnIds.add(SkirmishEntryColumnIds.ENCOUNTERS_COMPLETED.name());
      columnIds.add(SkirmishEntryColumnIds.PLAY_TIME.name());
      columnIds.add(SkirmishEntryColumnIds.SKIRMISHES_COMPLETED.name());
      columnIds.add(SkirmishEntryColumnIds.SKIRMISHES_ATTEMPTED.name());
      columnIds.add(SkirmishEntryColumnIds.BEST_TIME.name());
      columnIds.add(SkirmishEntryColumnIds.TOTAL_MARKS_EARNED.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<SkirmishEntry> getTableController()
  {
    return _tableController;
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    SkirmishEntriesPolicy policy=new SkirmishEntriesPolicy(true,true);
    List<SkirmishEntry> entries=SkirmishEntriesUtils.getEntries(_stats,_filter,policy);
    _entries.clear();
    _entries.addAll(entries);
    _tableController.refresh();
  }

  /**
   * Get the total number of quests.
   * @return A number of quests.
   */
  public int getNbItems()
  {
    return _entries.size();
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
    _entries=null;
  }
}
