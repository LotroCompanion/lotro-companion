package delta.games.lotro.gui.toon;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.Sort;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.ui.swing.tables.TablePreferencesProperties;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a table that shows all available toons.
 * @author DAM
 */
public class ToonsTableController implements GenericEventsListener<CharacterEvent>
{
  // Data
  private TypedProperties _prefs;
  private List<CharacterFile> _toons;
  // GUI
  private JTable _table;
  private GenericTableController<CharacterFile> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Character filter.
   */
  public ToonsTableController(TypedProperties prefs, Filter<CharacterFile> filter)
  {
    _prefs=prefs;
    _toons=new ArrayList<CharacterFile>();
    _tableController=buildTable();
    _tableController.setFilter(filter);
    EventsManager.addListener(CharacterEvent.class,this);
  }

  private GenericTableController<CharacterFile> buildTable()
  {
    ListDataProvider<CharacterFile> provider=new ListDataProvider<CharacterFile>(_toons);
    GenericTableController<CharacterFile> table=new GenericTableController<CharacterFile>(provider);

    // Columns
    // - build
    List<TableColumnController<CharacterFile,?>> columns=CharacterFileColumnsBuilder.build();
    for(TableColumnController<CharacterFile,?> column:columns)
    {
      table.addColumnController(column);
    }
    // - select
    TableColumnsManager<CharacterFile> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);

    // Sort
    Sort sort=getSort();
    table.setSort(sort);

    return table;
  }

  private Sort getSort()
  {
    String sortString=null;
    if (_prefs!=null)
    {
      sortString=_prefs.getStringProperty(TablePreferencesProperties.SORT_PROPERTY,null);
    }
    if (sortString==null)
    {
      sortString=Sort.SORT_ASCENDING+ToonsTableColumnIds.NAME+Sort.SORT_ITEM_SEPARATOR+Sort.SORT_ASCENDING+ToonsTableColumnIds.SERVER;
    }
    Sort sort=Sort.buildFromString(sortString);
    return sort;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=null;
    if (_prefs!=null)
    {
      columnIds=_prefs.getStringList(TablePreferencesProperties.COLUMNS_PROPERTY);
    }
    if (columnIds==null)
    {
      columnIds=getDefaultColumns();
    }
    return columnIds;
  }

  private List<String> getDefaultColumns()
  {
    List<String> columnIds=new ArrayList<String>();
    columnIds.add(ToonsTableColumnIds.NAME.name());
    columnIds.add(ToonsTableColumnIds.CLASS.name());
    columnIds.add(ToonsTableColumnIds.RACE.name());
    columnIds.add(ToonsTableColumnIds.LEVEL.name());
    columnIds.add(ToonsTableColumnIds.SERVER.name());
    columnIds.add(ToonsTableColumnIds.MONEY.name());
    columnIds.add(ToonsTableColumnIds.INGAME_TIME.name());
    columnIds.add(ToonsTableColumnIds.TITLE.name());
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<CharacterFile> getTableController()
  {
    return _tableController;
  }

  /**
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
    if ((type==CharacterEventType.CHARACTER_SUMMARY_UPDATED)
        || (type==CharacterEventType.CHARACTER_DETAILS_UPDATED))
    {
      CharacterFile toon=event.getToonFile();
      _tableController.refresh(toon);
    }
  }

  /**
   * Set the characters to show.
   * @param toons List of characters to show.
   */
  public void setToons(List<CharacterFile> toons)
  {
    _toons.clear();
    for(CharacterFile toon : toons)
    {
      CharacterSummary summary=toon.getSummary();
      if (summary!=null)
      {
        _toons.add(toon);
      }
    }
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
   * Add an action listener.
   * @param al Action listener to add.
   */
  public void addActionListener(ActionListener al)
  {
    _tableController.addActionListener(al);
  }

  /**
   * Remove an action listener.
   * @param al Action listener to remove.
   */
  public void removeActionListener(ActionListener al)
  {
    _tableController.removeActionListener(al);
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _tableController.filterUpdated();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Listeners
    EventsManager.removeListener(CharacterEvent.class,this);
    // Preferences
    if (_prefs!=null)
    {
      _tableController.savePreferences(_prefs);
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
    _toons=null;
  }
}
