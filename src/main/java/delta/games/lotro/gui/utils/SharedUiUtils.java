package delta.games.lotro.gui.utils;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.common.VirtueId;
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
  public static ComboBoxController<VirtueId> buildVirtueCombo()
  {
    ComboBoxController<VirtueId> ctrl=new ComboBoxController<VirtueId>();
    ctrl.addEmptyItem("");
    for(VirtueId virtue : VirtueId.values())
    {
      ctrl.addItem(virtue,virtue.getLabel());
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
