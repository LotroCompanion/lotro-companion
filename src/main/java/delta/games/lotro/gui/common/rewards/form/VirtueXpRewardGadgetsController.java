package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.common.utils.l10n.L10n;

/**
 * Controller for the UI gadgets of a virtue XP reward.
 * @author DAM
 */
public class VirtueXpRewardGadgetsController extends RewardGadgetsController
{
  /**
   * Constructor.
   * @param parent Parent controller.
   * @param virtueXP Virtue XP.
   */
  public VirtueXpRewardGadgetsController(AreaController parent, int virtueXP)
  {
    super(parent);
    // Label
    String text=L10n.getString(virtueXP)+" Virtue Experience"; // I18n
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
