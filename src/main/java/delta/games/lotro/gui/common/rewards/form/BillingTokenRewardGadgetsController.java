package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.enums.BillingGroup;
import delta.games.lotro.gui.lore.billingGroups.BillingGroupsUiUtils;

/**
 * Controller for the UI gadgets of a billing token reward.
 * @author DAM
 */
public class BillingTokenRewardGadgetsController extends RewardGadgetsController
{
  private HyperLinkController _billingGroupLink;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param billingGroup Billing group to show.
   */
  public BillingTokenRewardGadgetsController(WindowController parent, BillingGroup billingGroup)
  {
    super(parent);
    // Label
    _label=new LabelWithHalo();
    _label.setForeground(Color.WHITE);
    // Link
    _billingGroupLink=BillingGroupsUiUtils.buildBillingGroupLink(parent,billingGroup,_label);
    // Icon
    Icon lpIcon=IconsManager.getIcon("/resources/gui/icons/LP.png");
    _icon=GuiFactory.buildIconLabel(lpIcon);
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_billingGroupLink!=null)
    {
      _billingGroupLink.dispose();
      _billingGroupLink=null;
    }
  }
}
