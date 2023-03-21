package delta.games.lotro.gui.lore.billingGroups;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.enums.BillingGroup;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.NavigationUtils;

/**
 * Utility methods for billing groups-related UIs.
 * @author DAM
 */
public class BillingGroupsUiUtils
{
  /**
   * Build a billing group link controller.
   * @param parent Parent window.
   * @param billingGroup Billing group to use.
   * @param label Label to use.
   * @return a new controller.
   */
  public static HyperLinkController buildBillingGroupLink(final WindowController parent, final BillingGroup billingGroup, JLabel label)
  {
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showBillingGroupWindow(parent,billingGroup.getCode());
      }
    };
    String text=(billingGroup!=null)?billingGroup.getLabel():"???";
    LocalHyperlinkAction action=new LocalHyperlinkAction(text,al);
    HyperLinkController controller=new HyperLinkController(action,label);
    return controller;
  }

  /**
   * Show a billing group display window.
   * @param parent Parent window.
   * @param billingGroupID Billing group identifier.
   */
  public static void showBillingGroupWindow(WindowController parent, int billingGroupID)
  {
    PageIdentifier ref=ReferenceConstants.getBillingGroupReference(billingGroupID);
    NavigationUtils.navigateTo(ref,parent);
  }
}
