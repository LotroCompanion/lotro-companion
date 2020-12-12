package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.LabelWithHalo;

/**
 * Controller for the UI gadgets of a virtue XP reward.
 * @author DAM
 */
public class VirtueXpRewardGadgetsController extends RewardGadgetsController
{
  /**
   * Constructor.
   * @param virtueXP Virtue XP.
   */
  public VirtueXpRewardGadgetsController(int virtueXP)
  {
    // Label
    String text=virtueXP+" Virtue Experience";
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    Icon icon=IconsManager.getIcon("/resources/gui/rewards/virtue-xp.png");
    _icon=GuiFactory.buildIconLabel(icon);
  }
}
