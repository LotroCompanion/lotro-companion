package delta.games.lotro.gui.recipes;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.crafting.Profession;

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
    for(Profession profession : Profession.getAll())
    {
      String label=profession.getLabel();
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
    for(CraftingLevel level : CraftingLevel.ALL_TIERS)
    {
      int tier=level.getTier();
      if (tier!=0)
      {
        ctrl.addItem(Integer.valueOf(tier),level.getLabel());
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
}
