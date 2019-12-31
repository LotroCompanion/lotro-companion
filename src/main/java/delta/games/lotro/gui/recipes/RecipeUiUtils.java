package delta.games.lotro.gui.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.lore.crafting.CraftingData;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.crafting.CraftingSystem;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.ProfessionComparator;
import delta.games.lotro.lore.crafting.Professions;
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
   * Build a combo-box controller to choose a recipe profession.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildProfessionCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");

    CraftingData crafting=CraftingSystem.getInstance().getData();
    Professions professionsRegistry=crafting.getProfessionsRegistry();
    List<Profession> professions=professionsRegistry.getAll();
    Collections.sort(professions,new ProfessionComparator());
    for(Profession profession : professions)
    {
      String label=profession.getName();
      ctrl.addItem(label,label);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a recipe category.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Integer> buildTierCombo()
  {
    ComboBoxController<Integer> ctrl=new ComboBoxController<Integer>();
    ctrl.addEmptyItem("");

    CraftingData crafting=CraftingSystem.getInstance().getData();
    Professions professionsRegistry=crafting.getProfessionsRegistry();
    Profession profession=professionsRegistry.getAll().get(0);

    for(CraftingLevel level : profession.getLevels())
    {
      int tier=level.getTier();
      if (tier!=0)
      {
        ctrl.addItem(Integer.valueOf(tier),level.getName());
      }
    }
    ctrl.selectItem(null);
    return ctrl;
  }

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
  public static List<CountedItem> getIngredientItems(Recipe recipe)
  {
    List<CountedItem> items=new ArrayList<CountedItem>();
    RecipeVersion version=recipe.getVersions().get(0);
    List<Ingredient> ingredients=version.getIngredients();
    int nbIngredients=ingredients.size();
    for(int i=0;i<nbIngredients;i++)
    {
      Ingredient ingredient=ingredients.get(i);
      ItemProxy item=ingredient.getItem();
      int quantity=ingredient.getQuantity();
      items.add(new CountedItem(item,quantity));
    }
    return items;
  }

  /**
   * Get a list of all result items (first version).
   * @param recipe To use.
   * @return a list of all ingredients.
   */
  public static List<CountedItem> getResultItems(Recipe recipe)
  {
    List<CountedItem> items=new ArrayList<CountedItem>();
    RecipeVersion version=recipe.getVersions().get(0);
    CraftingResult regular=version.getRegular();
    {
      ItemProxy regularItem=regular.getItem();
      int regularQuantity=regular.getQuantity();
      items.add(new CountedItem(regularItem,regularQuantity));
    }
    CraftingResult critical=version.getCritical();
    if (critical!=null)
    {
      ItemProxy criticalItem=critical.getItem();
      int criticalQuantity=critical.getQuantity();
      items.add(new CountedItem(criticalItem,criticalQuantity));
    }
    return items;
  }

}
