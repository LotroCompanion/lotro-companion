package delta.games.lotro.gui.recipes;

import java.awt.event.ActionListener;
import java.io.File;
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
import delta.games.lotro.gui.items.chooser.ItemChoiceWindowController;
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
  public static List<DefaultTableColumnController<Recipe,?>> buildColumns()
  {
    List<DefaultTableColumnController<Recipe,?>> ret=new ArrayList<DefaultTableColumnController<Recipe,?>>();
    // Identifier column
    {
      CellDataProvider<Recipe,Integer> idCell=new CellDataProvider<Recipe,Integer>()
      {
        @Override
        public Integer getData(Recipe recipe)
        {
          return Integer.valueOf(recipe.getIdentifier());
        }
      };
      DefaultTableColumnController<Recipe,Integer> idColumn=new DefaultTableColumnController<Recipe,Integer>(RecipeColumnIds.ID.name(),"ID",Integer.class,idCell);
      idColumn.setWidthSpecs(100,100,100);
      ret.add(idColumn);
    }
    // Key column
    {
      CellDataProvider<Recipe,String> keyCell=new CellDataProvider<Recipe,String>()
      {
        @Override
        public String getData(Recipe recipe)
        {
          return recipe.getKey();
        }
      };
      DefaultTableColumnController<Recipe,String> keyColumn=new DefaultTableColumnController<Recipe,String>(RecipeColumnIds.KEY.name(),"Key",String.class,keyCell);
      keyColumn.setWidthSpecs(100,200,200);
      ret.add(keyColumn);
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
      DefaultTableColumnController<Recipe,String> nameColumn=new DefaultTableColumnController<Recipe,String>(RecipeColumnIds.NAME.name(),"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,300,200);
      ret.add(nameColumn);
    }
    // Profession column
    {
      CellDataProvider<Recipe,String> professionCell=new CellDataProvider<Recipe,String>()
      {
        @Override
        public String getData(Recipe recipe)
        {
          return recipe.getProfession();
        }
      };
      DefaultTableColumnController<Recipe,String> professionColumn=new DefaultTableColumnController<Recipe,String>(RecipeColumnIds.PROFESSION.name(),"Profession",String.class,professionCell);
      professionColumn.setWidthSpecs(80,350,80);
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
      DefaultTableColumnController<Recipe,Integer> tierColumn=new DefaultTableColumnController<Recipe,Integer>(RecipeColumnIds.TIER.name(),"Tier",Integer.class,tierCell);
      tierColumn.setWidthSpecs(100,100,100);
      ret.add(tierColumn);
    }
    // Category column
    {
      CellDataProvider<Recipe,String> categoryCell=new CellDataProvider<Recipe,String>()
      {
        @Override
        public String getData(Recipe recipe)
        {
          return recipe.getCategory();
        }
      };
      DefaultTableColumnController<Recipe,String> categoryColumn=new DefaultTableColumnController<Recipe,String>(RecipeColumnIds.CATEGORY.name(),"Category",String.class,categoryCell);
      categoryColumn.setWidthSpecs(80,350,80);
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
      DefaultTableColumnController<Recipe,Integer> xpColumn=new DefaultTableColumnController<Recipe,Integer>(RecipeColumnIds.XP.name(),"XP",Integer.class,xpCell);
      xpColumn.setWidthSpecs(100,100,100);
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
      DefaultTableColumnController<Recipe,Integer> cooldownColumn=new DefaultTableColumnController<Recipe,Integer>(RecipeColumnIds.COOLDOWN.name(),"Cooldown",Integer.class,cooldownCell);
      cooldownColumn.setWidthSpecs(100,100,100);
      // TODO add a formatter to display a duration
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
      DefaultTableColumnController<Recipe,Boolean> cooldownColumn=new DefaultTableColumnController<Recipe,Boolean>(RecipeColumnIds.ONE_TIME_USE.name(),"Single use",Boolean.class,cooldownCell);
      cooldownColumn.setWidthSpecs(30,30,30);
      ret.add(cooldownColumn);
    }
    // Ingredients
    {
      CellDataProvider<Recipe,Recipe> ingredientsCell=new CellDataProvider<Recipe,Recipe>()
      {
        @Override
        public Recipe getData(Recipe recipe)
        {
          return recipe;
        }
      };
      DefaultTableColumnController<Recipe,Recipe> ingredientsColumn=new DefaultTableColumnController<Recipe,Recipe>(RecipeColumnIds.INGREDIENTS.name(),"Ingredients",Recipe.class,ingredientsCell);
      ingredientsColumn.setWidthSpecs(150,260,150);
      IngredientsSummaryPanelController panelController=new IngredientsSummaryPanelController();
      TableCellRenderer renderer=panelController.buildRenderer();
      ingredientsColumn.setCellRenderer(renderer);
      ret.add(ingredientsColumn);
    }
    // TODO: Graphical panels for:
    //REGULAR_RESULT
    //CRITICAL_RESULT
    return ret;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=null;
    if (_prefs!=null)
    {
      columnIds=_prefs.getStringList(ItemChoiceWindowController.COLUMNS_PROPERTY);
    }
    if (columnIds==null)
    {
      columnIds=new ArrayList<String>();
      columnIds.add(RecipeColumnIds.NAME.name());
      columnIds.add(RecipeColumnIds.PROFESSION.name());
      columnIds.add(RecipeColumnIds.TIER.name());
      columnIds.add(RecipeColumnIds.CATEGORY.name());
      columnIds.add(RecipeColumnIds.INGREDIENTS.name());
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
    RecipesManager recipesManager=new RecipesManager();
    recipesManager.loadRecipesFromFile(new File("data/lore/recipes.xml").getAbsoluteFile());
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
      _prefs.setStringList(ItemChoiceWindowController.COLUMNS_PROPERTY,columnIds);
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
