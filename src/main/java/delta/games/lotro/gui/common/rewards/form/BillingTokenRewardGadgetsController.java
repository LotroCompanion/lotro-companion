package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.games.lotro.common.enums.BillingGroup;

/**
 * Controller for the UI gadgets of a billing token reward.
 * @author DAM
 */
public class BillingTokenRewardGadgetsController extends RewardGadgetsController
{
  /**
   * Constructor.
   * @param billingGroup Billing group to show.
   */
  public BillingTokenRewardGadgetsController(BillingGroup billingGroup)
  {
    // Label
    String text=billingGroup.getLabel();
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    Icon lpIcon=IconsManager.getIcon("/resources/gui/icons/LP.png");
    _icon=GuiFactory.buildIconLabel(lpIcon);
  }
}
