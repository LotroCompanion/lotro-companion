package delta.games.lotro.gui.lore.crafting.recipes;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.enums.CraftingUICategory;
import delta.games.lotro.gui.lore.items.ItemsSummaryPanelController;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.utils.DurationCellRenderer;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.crafting.recipes.RecipesManager;

/**
 * Controller for a table that shows recipes.
 * @author DAM
 */
public class RecipesTableController
{
  // Data
  private TypedProperties _prefs;
  private List<Recipe> _recipes;
  // GUI
  private JTable _table;
  private GenericTableController<Recipe> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public RecipesTableController(TypedProperties prefs, Filter<Recipe> filter)
  {
    _prefs=prefs;
    _recipes=new ArrayList<Recipe>();
    init();
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  private GenericTableController<Recipe> buildTable()
  {
    ListDataProvider<Recipe> provider=new ListDataProvider<Recipe>(_recipes);
    GenericTableController<Recipe> table=new GenericTableController<Recipe>(provider);
    List<DefaultTableColumnController<Recipe,?>> columns=buildColumns();
    for(DefaultTableColumnController<Recipe,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<Recipe> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a recipes table.
   * @return A list of columns for a recipes table.
   */
  @SuppressWarnings("rawtypes")
  public static List<DefaultTableColumnController<Recipe,?>> buildColumns()
  {
    List<DefaultTableColumnController<Recipe,?>> ret=new ArrayList<DefaultTableColumnController<Recipe,?>>();
    // Identifier column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<Recipe,Integer> idCell=new CellDataProvider<Recipe,Integer>()
      {
        @Override
        public Integer getData(Recipe recipe)
        {
          return Integer.valueOf(recipe.getIdentifier());
        }
      };
      DefaultTableColumnController<Recipe,Integer> idColumn=new DefaultTableColumnController<Recipe,Integer>(RecipeColumnIds.ID.name(),"ID",Integer.class,idCell); // 18n
      idColumn.setWidthSpecs(80,80,80);
      ret.add(idColumn);
    }
    // Name column
    {
      CellDataProvider<Recipe,String> nameCell=new CellDataProvider<Recipe,String>()
      {
        @Override
        public String getData(Recipe recipe)
        {
          return recipe.getName();
        }
      };
      DefaultTableColumnController<Recipe,String> nameColumn=new DefaultTableColumnController<Recipe,String>(RecipeColumnIds.NAME.name(),"Name",String.class,nameCell); // 18n
      nameColumn.setWidthSpecs(100,300,200);
      ret.add(nameColumn);
    }
    // Profession column
    {
      CellDataProvider<Recipe,Profession> professionCell=new CellDataProvider<Recipe,Profession>()
      {
        @Override
        public Profession getData(Recipe recipe)
        {
          return recipe.getProfession();
        }
      };
      DefaultTableColumnController<Recipe,Profession> professionColumn=new DefaultTableColumnController<Recipe,Profession>(RecipeColumnIds.PROFESSION.name(),"Profession",Profession.class,professionCell); // 18n
      professionColumn.setWidthSpecs(100,100,100);
      ret.add(professionColumn);
    }
    // Tier column
    {
      CellDataProvider<Recipe,Integer> tierCell=new CellDataProvider<Recipe,Integer>()
      {
        @Override
        public Integer getData(Recipe recipe)
        {
          return Integer.valueOf(recipe.getTier());
        }
      };
      DefaultTableColumnController<Recipe,Integer> tierColumn=new DefaultTableColumnController<Recipe,Integer>(RecipeColumnIds.TIER.name(),"Tier",Integer.class,tierCell); // 18n
      tierColumn.setWidthSpecs(50,50,50);
      ret.add(tierColumn);
    }
    // Category column
    {
      CellDataProvider<Recipe,CraftingUICategory> categoryCell=new CellDataProvider<Recipe,CraftingUICategory>()
      {
        @Override
        public CraftingUICategory getData(Recipe recipe)
        {
          return recipe.getCategory();
        }
      };
      DefaultTableColumnController<Recipe,CraftingUICategory> categoryColumn=new DefaultTableColumnController<Recipe,CraftingUICategory>(RecipeColumnIds.CATEGORY.name(),"Category",CraftingUICategory.class,categoryCell); // 18n
      categoryColumn.setWidthSpecs(80,270,80);
      ret.add(categoryColumn);
    }
    // XP column
    {
      CellDataProvider<Recipe,Integer> xpCell=new CellDataProvider<Recipe,Integer>()
      {
        @Override
        public Integer getData(Recipe recipe)
        {
          return Integer.valueOf(recipe.getXP());
        }
      };
      DefaultTableColumnController<Recipe,Integer> xpColumn=new DefaultTableColumnController<Recipe,Integer>(RecipeColumnIds.XP.name(),"XP",Integer.class,xpCell); // 18n
      xpColumn.setWidthSpecs(30,30,30);
      ret.add(xpColumn);
    }
    // Cooldown column
    {
      CellDataProvider<Recipe,Integer> cooldownCell=new CellDataProvider<Recipe,Integer>()
      {
        @Override
        public Integer getData(Recipe recipe)
        {
          int cooldown=recipe.getCooldown();
          return (cooldown!=-1)?Integer.valueOf(cooldown):null;
        }
      };
      DefaultTableColumnController<Recipe,Integer> cooldownColumn=new DefaultTableColumnController<Recipe,Integer>(RecipeColumnIds.COOLDOWN.name(),"Cooldown",Integer.class,cooldownCell); // 18n
      cooldownColumn.setWidthSpecs(60,60,60);
      cooldownColumn.setCellRenderer(new DurationCellRenderer());
      ret.add(cooldownColumn);
    }
    // 'One time use' column
    {
      CellDataProvider<Recipe,Boolean> cooldownCell=new CellDataProvider<Recipe,Boolean>()
      {
        @Override
        public Boolean getData(Recipe recipe)
        {
          boolean oneTimeUse=recipe.isOneTimeUse();
          return Boolean.valueOf(oneTimeUse);
        }
      };
      DefaultTableColumnController<Recipe,Boolean> cooldownColumn=new DefaultTableColumnController<Recipe,Boolean>(RecipeColumnIds.ONE_TIME_USE.name(),"Single use",Boolean.class,cooldownCell); // 18n
      cooldownColumn.setWidthSpecs(30,30,30);
      ret.add(cooldownColumn);
    }
    // 'Guild' column
    {
      CellDataProvider<Recipe,Boolean> guildCell=new CellDataProvider<Recipe,Boolean>()
      {
        @Override
        public Boolean getData(Recipe recipe)
        {
          boolean guildRequired=recipe.isGuildRequired();
          return Boolean.valueOf(guildRequired);
        }
      };
      DefaultTableColumnController<Recipe,Boolean> guildColumn=new DefaultTableColumnController<Recipe,Boolean>(RecipeColumnIds.GUILD.name(),"Guild",Boolean.class,guildCell); // 18n
      guildColumn.setWidthSpecs(30,30,30);
      ret.add(guildColumn);
    }
    // Ingredients
    {
      CellDataProvider<Recipe,List> ingredientsCell=new CellDataProvider<Recipe,List>()
      {
        @Override
        public List getData(Recipe recipe)
        {
          return RecipeUiUtils.getIngredientItems(recipe);
        }
      };
      DefaultTableColumnController<Recipe,List> ingredientsColumn=new DefaultTableColumnController<Recipe,List>(RecipeColumnIds.INGREDIENTS.name(),"Ingredients",List.class,ingredientsCell); // 18n
      ingredientsColumn.setWidthSpecs(150,230,150);
      ItemsSummaryPanelController panelController=new ItemsSummaryPanelController();
      TableCellRenderer renderer=panelController.buildRenderer();
      ingredientsColumn.setCellRenderer(renderer);
      ret.add(ingredientsColumn);
    }
    // Results
    {
      CellDataProvider<Recipe,List> resultsCell=new CellDataProvider<Recipe,List>()
      {
        @Override
        public List getData(Recipe recipe)
        {
          return RecipeUiUtils.getResultItems(recipe);
        }
      };
      DefaultTableColumnController<Recipe,List> resultsColumn=new DefaultTableColumnController<Recipe,List>(RecipeColumnIds.RESULT.name(),"Result",List.class,resultsCell); // 18n
      resultsColumn.setWidthSpecs(80,80,80);
      ItemsSummaryPanelController panelController=new ItemsSummaryPanelController();
      TableCellRenderer renderer=panelController.buildRenderer();
      resultsColumn.setCellRenderer(renderer);
      ret.add(resultsColumn);
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
      columnIds.add(RecipeColumnIds.NAME.name());
      columnIds.add(RecipeColumnIds.PROFESSION.name());
      columnIds.add(RecipeColumnIds.TIER.name());
      columnIds.add(RecipeColumnIds.CATEGORY.name());
      columnIds.add(RecipeColumnIds.INGREDIENTS.name());
      columnIds.add(RecipeColumnIds.RESULT.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<Recipe> getTableController()
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
   * Get the total number of recipes.
   * @return A number of recipes.
   */
  public int getNbItems()
  {
    return _recipes.size();
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

  private void reset()
  {
    _recipes.clear();
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
    reset();
    RecipesManager recipesManager=RecipesManager.getInstance();
    List<Recipe> recipes=recipesManager.getAll();
    for(Recipe recipe : recipes)
    {
      _recipes.add(recipe);
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
    _recipes=null;
  }
}
