package delta.games.lotro.gui.character.status.recipes.table;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.status.recipes.RecipeState;
import delta.games.lotro.character.status.recipes.RecipeStatus;
import delta.games.lotro.gui.lore.crafting.recipes.RecipesTableController;
import delta.games.lotro.lore.crafting.recipes.Recipe;

/**
 * Builds column definitions for RecipeStatus data.
 * @author DAM
 */
public class RecipesStatusColumnsBuilder
{
  /**
   * Build the columns to show the attributes of a recipe status.
   * @return a list of columns.
   */
  public static List<TableColumnController<RecipeStatus,?>> buildRecipeStatusColumns()
  {
    List<TableColumnController<RecipeStatus,?>> ret=new ArrayList<TableColumnController<RecipeStatus,?>>();
    // Recipe
    {
      CellDataProvider<RecipeStatus,Recipe> provider=new CellDataProvider<RecipeStatus,Recipe>()
      {
        @Override
        public Recipe getData(RecipeStatus recipeStatus)
        {
          return recipeStatus.getRecipe();
        }
      };
      for(TableColumnController<Recipe,?> recipeColumn : RecipesTableController.buildColumns())
      {
        @SuppressWarnings("unchecked")
        TableColumnController<Recipe,Object> c=(TableColumnController<Recipe,Object>)recipeColumn;
        ProxiedTableColumnController<RecipeStatus,Recipe,Object> column=new ProxiedTableColumnController<RecipeStatus,Recipe,Object>(c,provider);
        ret.add(column);
      }
    }
    // State
    {
      CellDataProvider<RecipeStatus,RecipeState> stateCell=new CellDataProvider<RecipeStatus,RecipeState>()
      {
        @Override
        public RecipeState getData(RecipeStatus status)
        {
          return status.getState();
        }
      };
      DefaultTableColumnController<RecipeStatus,RecipeState> stateColumn=new DefaultTableColumnController<RecipeStatus,RecipeState>(RecipeStatusColumnIds.STATE.name(),"State",RecipeState.class,stateCell); // I18n
      stateColumn.setWidthSpecs(80,80,80);
      ret.add(stateColumn);
    }
    return ret;
  }
}
