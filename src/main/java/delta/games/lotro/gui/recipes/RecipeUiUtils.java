package delta.games.lotro.gui.recipes;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.lore.crafting.recipes.CraftingResult;
import delta.games.lotro.lore.crafting.recipes.Ingredient;
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.crafting.recipes.RecipeVersion;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.ItemProxy;

/**
 * Utility methods for recipe-related UIs.
 * @author DAM
 */
public class RecipeUiUtils
{
  /**
   * Build a combo-box controller to choose a recipe category.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildCategoryCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    // TODO
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Get a list of all ingredients.
   * @param recipe To use.
   * @return a list of all ingredients.
   */
  public static List<CountedItem<ItemProxy>> getIngredientItems(Recipe recipe)
  {
    List<CountedItem<ItemProxy>> items=new ArrayList<CountedItem<ItemProxy>>();
    RecipeVersion version=recipe.getVersions().get(0);
    List<Ingredient> ingredients=version.getIngredients();
    int nbIngredients=ingredients.size();
    for(int i=0;i<nbIngredients;i++)
    {
      Ingredient ingredient=ingredients.get(i);
      ItemProxy item=ingredient.getItem();
      int quantity=ingredient.getQuantity();
      items.add(new CountedItem<ItemProxy>(item,quantity));
    }
    return items;
  }

  /**
   * Get a list of all result items (first version).
   * @param recipe To use.
   * @return a list of all ingredients.
   */
  public static List<CountedItem<ItemProxy>> getResultItems(Recipe recipe)
  {
    List<CountedItem<ItemProxy>> items=new ArrayList<CountedItem<ItemProxy>>();
    RecipeVersion version=recipe.getVersions().get(0);
    CraftingResult regular=version.getRegular();
    {
      ItemProxy regularItem=regular.getItem();
      int regularQuantity=regular.getQuantity();
      items.add(new CountedItem<ItemProxy>(regularItem,regularQuantity));
    }
    CraftingResult critical=version.getCritical();
    if (critical!=null)
    {
      ItemProxy criticalItem=critical.getItem();
      int criticalQuantity=critical.getQuantity();
      items.add(new CountedItem<ItemProxy>(criticalItem,criticalQuantity));
    }
    return items;
  }

}
