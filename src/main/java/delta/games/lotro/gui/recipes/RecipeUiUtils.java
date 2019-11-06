package delta.games.lotro.gui.recipes;

import java.util.Collections;
import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.lore.crafting.CraftingData;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.crafting.CraftingSystem;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.ProfessionComparator;
import delta.games.lotro.lore.crafting.Professions;

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
}
