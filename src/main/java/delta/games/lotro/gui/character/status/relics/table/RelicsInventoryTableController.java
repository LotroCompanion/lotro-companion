package delta.games.lotro.gui.character.status.relics.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.status.relics.RelicsInventoryEntry;
import delta.games.lotro.character.status.relics.RelicsInventoryManager;
import delta.games.lotro.character.status.relics.filter.RelicInventoryEntryFilter;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.lore.items.legendary.relics.RelicColumnIds;
import delta.games.lotro.lore.items.legendary.relics.Relic;

/**
 * Controller for a table that shows the relics inventory for a single character.
 * @author DAM
 */
public class RelicsInventoryTableController
{
  // Data
  private TypedProperties _prefs;
  private List<RelicsInventoryEntry> _entries;
  // GUI
  private JTable _table;
  private GenericTableController<RelicsInventoryEntry> _tableController;

  /**
   * Constructor.
   * @param statusMgr Status to show.
   * @param prefs Preferences.
   * @param filter Managed filter.
   * @param relics Relics to use.
   */
  public RelicsInventoryTableController(RelicsInventoryManager statusMgr, TypedProperties prefs, RelicInventoryEntryFilter filter, List<Relic> relics)
  {
    _prefs=prefs;
    _entries=new ArrayList<RelicsInventoryEntry>(statusMgr.getRelicEntries());
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  private GenericTableController<RelicsInventoryEntry> buildTable()
  {
    ListDataProvider<RelicsInventoryEntry> provider=new ListDataProvider<RelicsInventoryEntry>(_entries);
    GenericTableController<RelicsInventoryEntry> table=new GenericTableController<RelicsInventoryEntry>(provider);
    // Columns
    List<TableColumnController<RelicsInventoryEntry,?>> columns=RelicsInventoryColumnsBuilder.buildRelicInventoryEntryColumns();
    for(TableColumnController<RelicsInventoryEntry,?> column : columns)
    {
      table.addColumnController(column);
    }
    List<String> columnsIds=getColumnIds();
    TableColumnsManager<RelicsInventoryEntry> columnsManager=table.getColumnsManager();
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
      columnIds.add(RelicColumnIds.ICON.name());
      columnIds.add(RelicColumnIds.NAME.name());
      columnIds.add(RelicColumnIds.CATEGORY.name());
      columnIds.add(RelicsInventoryColumnsBuilder.COUNT_COLUMN);
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<RelicsInventoryEntry> getTableController()
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
   * Get the total number of items.
   * @return A number of items.
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
