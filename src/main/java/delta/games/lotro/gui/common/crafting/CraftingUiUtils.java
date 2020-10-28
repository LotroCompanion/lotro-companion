package delta.games.lotro.gui.common.crafting;

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
 * @author dm
 */
public class CraftingUiUtils
{
  /**
   * Build a combo-box controller to choose a profession.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Profession> buildProfessionCombo()
  {
    CraftingData crafting=CraftingSystem.getInstance().getData();
    Professions professionsRegistry=crafting.getProfessionsRegistry();
    List<Profession> professions=professionsRegistry.getAll();
    return buildProfessionCombo(professions);
  }

  /**
   * Build a combo-box controller to choose a profession.
   * @param professions Professions to show.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Profession> buildProfessionCombo(List<Profession> professions)
  {
    ComboBoxController<Profession> ctrl=new ComboBoxController<Profession>();
    ctrl.addEmptyItem("");

    Collections.sort(professions,new ProfessionComparator());
    for(Profession profession : professions)
    {
      String label=profession.getName();
      ctrl.addItem(profession,label);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a crafting tier.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Integer> buildTierCombo()
  {
    CraftingData crafting=CraftingSystem.getInstance().getData();
    Professions professionsRegistry=crafting.getProfessionsRegistry();
    Profession profession=professionsRegistry.getAll().get(0);
    List<CraftingLevel> levels=profession.getLevels();
    return buildTierCombo(levels);
  }

  /**
   * Build a combo-box controller to choose a crafting tier.
   * @param levels Tiers to show.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Integer> buildTierCombo(List<CraftingLevel> levels)
  {
    ComboBoxController<Integer> ctrl=new ComboBoxController<Integer>();
    ctrl.addEmptyItem("");

    for(CraftingLevel level : levels)
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
}
