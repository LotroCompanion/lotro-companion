package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.LotroIconsManager;

/**
 * Controller for the UI gadgets of a trait reward.
 * @author DAM
 */
public class TraitRewardGadgetsController
{
  private JLabel _labelIcon;
  private JLabel _label;

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
    _labelIcon=GuiFactory.buildIconLabel(icon);
  }

  /**
   * Get the managed (label) icon.
   * @return a icon label.
   */
  public JLabel getLabelIcon()
  {
    return _labelIcon;
  }

  /**
   * Get the managed label.
   * @return a halo label.
   */
  public JLabel getLabel()
  {
    return _label;
  }
}
