package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.LotroIconsManager;

/**
 * Controller for the UI gadgets of a trait reward.
 * @author DAM
 */
public class TraitRewardGadgetsController extends RewardGadgetsController
{
  /**
   * Constructor.
   * @param trait Trait.
   */
  public TraitRewardGadgetsController(TraitDescription trait)
  {
    // Label
    String text=trait.getName();
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    int id=trait.getIconId();
    Icon icon=LotroIconsManager.getTraitIcon(id);
    _icon=GuiFactory.buildIconLabel(icon);
  }
}
