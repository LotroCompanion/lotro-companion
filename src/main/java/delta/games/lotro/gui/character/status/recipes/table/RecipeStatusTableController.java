package delta.games.lotro.gui.character.status.recipes.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.status.recipes.RecipeStatus;
import delta.games.lotro.character.status.recipes.RecipesStatusManager;
import delta.games.lotro.character.status.recipes.filter.RecipeStatusFilter;
import delta.games.lotro.common.blacklist.Blacklist;
import delta.games.lotro.gui.character.status.achievables.table.AchievableStatusColumnIds;
import delta.games.lotro.gui.lore.crafting.recipes.RecipeColumnIds;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;

/**
 * Controller for a table that shows the status of recipes for a single character.
 * @author DAM
 */
public class RecipeStatusTableController
{
  // Data
  private TypedProperties _prefs;
  private List<RecipeStatus> _statuses;
  private Blacklist _blacklist;
  // GUI
  private JTable _table;
  private GenericTableController<RecipeStatus> _tableController;

  /**
   * Constructor.
   * @param statusMgr Status to show.
   * @param prefs Preferences.
   * @param filter Managed filter.
   * @param blacklist Blacklist.
   */
  public RecipeStatusTableController(RecipesStatusManager statusMgr, TypedProperties prefs, RecipeStatusFilter filter, Blacklist blacklist)
  {
    _prefs=prefs;
    _statuses=new ArrayList<RecipeStatus>(statusMgr.getRecipeStatuses());
    _blacklist=blacklist;
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  private GenericTableController<RecipeStatus> buildTable()
  {
    ListDataProvider<RecipeStatus> provider=new ListDataProvider<RecipeStatus>(_statuses);
    GenericTableController<RecipeStatus> table=new GenericTableController<RecipeStatus>(provider);
    // Columns
    List<TableColumnController<RecipeStatus,?>> columns=RecipesStatusColumnsBuilder.buildRecipeStatusColumns();
    for(TableColumnController<RecipeStatus,?> column : columns)
    {
      table.addColumnController(column);
    }
    // Blacklisted column
    table.addColumnController(buildBlacklistedColumn());
    List<String> columnsIds=getColumnIds();
    TableColumnsManager<RecipeStatus> columnsManager=table.getColumnsManager();
    columnsManager.setColumns(columnsIds);

    return table;
  }

  private TableColumnController<RecipeStatus,String> buildBlacklistedColumn()
  {
    CellDataProvider<RecipeStatus,String> countCell=new CellDataProvider<RecipeStatus,String>()
    {
      @Override
      public String getData(RecipeStatus status)
      {
        return (_blacklist.isBlacklisted(status.getIdentifier()))?"Yes":"No"; // I18n
      }
    };
    DefaultTableColumnController<RecipeStatus,String> countColumn=new DefaultTableColumnController<RecipeStatus,String>(AchievableStatusColumnIds.BLACKLISTED.name(),"Blacklisted",String.class,countCell);
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
      columnIds.add(RecipeColumnIds.NAME.name());
      columnIds.add(RecipeColumnIds.PROFESSION.name());
      columnIds.add(RecipeColumnIds.TIER.name());
      columnIds.add(RecipeColumnIds.CATEGORY.name());
      columnIds.add(RecipeStatusColumnIds.STATE.name());
      columnIds.add(AchievableStatusColumnIds.BLACKLISTED.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<RecipeStatus> getTableController()
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
   * Get the total number of elements.
   * @return A number of elements.
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
