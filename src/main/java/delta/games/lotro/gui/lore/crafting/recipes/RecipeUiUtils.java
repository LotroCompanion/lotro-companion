package delta.games.lotro.gui.lore.crafting.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.common.enums.CraftingUICategory;
import delta.games.lotro.common.enums.comparator.LotroEnumEntryNameComparator;
import delta.games.lotro.lore.crafting.recipes.CraftingResult;
import delta.games.lotro.lore.crafting.recipes.Ingredient;
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.crafting.recipes.RecipeVersion;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.comparators.ItemNameComparator;

/**
 * Utility methods for recipe-related UIs.
 * @author DAM
 */
public class RecipeUiUtils
{
  /**
   * Build a combo-box controller to choose a recipe category.
   * @param recipes Recipes to use.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<CraftingUICategory> buildCategoryCombo(List<Recipe> recipes)
  {
    ComboBoxController<CraftingUICategory> ctrl=new ComboBoxController<CraftingUICategory>();
    ctrl.addEmptyItem("");
    for(CraftingUICategory category : getCategories(recipes))
    {
      ctrl.addItem(category,category.getLabel());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  private static List<CraftingUICategory> getCategories(List<Recipe> recipes)
  {
    Set<CraftingUICategory> categories=new HashSet<CraftingUICategory>();
    for(Recipe recipe : recipes)
    {
      CraftingUICategory category=recipe.getCategory();
      categories.add(category);
    }
    ArrayList<CraftingUICategory> ret=new ArrayList<CraftingUICategory>(categories);
    Collections.sort(ret,new LotroEnumEntryNameComparator<CraftingUICategory>());
    return ret;
  }

  /**
   * Get a list of all ingredients.
   * @param recipe To use.
   * @return a list of all ingredients.
   */
  public static List<CountedItem<Item>> getIngredientItems(Recipe recipe)
  {
    List<CountedItem<Item>> items=new ArrayList<CountedItem<Item>>();
    RecipeVersion version=recipe.getVersions().get(0);
    List<Ingredient> ingredients=version.getIngredients();
    int nbIngredients=ingredients.size();
    for(int i=0;i<nbIngredients;i++)
    {
      Ingredient ingredient=ingredients.get(i);
      Item item=ingredient.getItem();
      if (item!=null)
      {
        int quantity=ingredient.getQuantity();
        items.add(new CountedItem<Item>(item,quantity));
      }
    }
    return items;
  }

  /**
   * Get a list of all result items (first version).
   * @param recipe To use.
   * @return a list of all ingredients.
   */
  public static List<CountedItem<Item>> getResultItems(Recipe recipe)
  {
    List<CountedItem<Item>> items=new ArrayList<CountedItem<Item>>();
    RecipeVersion version=recipe.getVersions().get(0);
    CraftingResult regular=version.getRegular();
    {
    	Item regularItem=regular.getItem();
      int regularQuantity=regular.getQuantity();
      items.add(new CountedItem<Item>(regularItem,regularQuantity));
    }
    CraftingResult critical=version.getCritical();
    if (critical!=null)
    {
    	Item criticalItem=critical.getItem();
      int criticalQuantity=critical.getQuantity();
      items.add(new CountedItem<Item>(criticalItem,criticalQuantity));
    }
    return items;
  }

  /**
   * Build a combo-box controller to choose an ingredient name.
   * @param recipes Recipes to use.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Integer> buildIngredientsCombo(List<Recipe> recipes)
  {
    ComboBoxController<Integer> ctrl=new ComboBoxController<Integer>();
    ctrl.addEmptyItem("");
    List<Item> items=getIngredients(recipes);
    for(Item item : items)
    {
      ctrl.addItem(Integer.valueOf(item.getIdentifier()),item.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  private static List<Item> getIngredients(List<Recipe> recipes)
  {
    Set<Item> items=new HashSet<Item>();
    for(Recipe recipe : recipes)
    {
      RecipeVersion version=recipe.getVersions().get(0);
      List<Ingredient> ingredients=version.getIngredients();
      for(Ingredient ingredient : ingredients)
      {
        Item item=ingredient.getItem();
        if (item!=null)
        {
          items.add(item);
        }
      }
    }
    ArrayList<Item> ret=new ArrayList<Item>(items);
    Collections.sort(ret,new ItemNameComparator());
    return ret;
  }
}
