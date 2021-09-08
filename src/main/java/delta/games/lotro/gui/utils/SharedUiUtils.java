package delta.games.lotro.gui.utils;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.character.virtues.VirtuesManager;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionsRegistry;

/**
 * Shared UI utilities.
 * @author DAM
 */
public class SharedUiUtils
{
  /**
   * Build a combo-box controller to choose a faction.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Faction> buildFactionCombo()
  {
    ComboBoxController<Faction> ctrl=new ComboBoxController<Faction>();
    ctrl.addEmptyItem("");
    List<Faction> factions=FactionsRegistry.getInstance().getAll();
    for(Faction faction : factions)
    {
      ctrl.addItem(faction,faction.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a virtue.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<VirtueDescription> buildVirtueCombo()
  {
    ComboBoxController<VirtueDescription> ctrl=new ComboBoxController<VirtueDescription>();
    ctrl.addEmptyItem("");
    List<VirtueDescription> virtues=VirtuesManager.getInstance().getAll();
    for(VirtueDescription virtue : virtues)
    {
      ctrl.addItem(virtue,virtue.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Get the path for a toolbar icon.
   * @param iconName Icon name.
   * @return A path.
   */
  public static String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  /**
   * Build a combo-box controller to choose from null, true or false.
   * @param nullLabel Label for null case.
   * @param trueLabel Label for true case.
   * @param falseLabel Label for false case.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Boolean> build3StatesBooleanCombobox(String nullLabel, String trueLabel, String falseLabel)
  {
    ComboBoxController<Boolean> ctrl=new ComboBoxController<Boolean>();
    ctrl.addEmptyItem(nullLabel);
    ctrl.addItem(Boolean.TRUE,trueLabel);
    ctrl.addItem(Boolean.FALSE,falseLabel);
    ctrl.selectItem(null);
    return ctrl;
  }
}
