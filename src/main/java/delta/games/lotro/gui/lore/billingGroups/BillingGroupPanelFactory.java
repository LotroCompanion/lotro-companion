package delta.games.lotro.gui.lore.billingGroups;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.billingGroups.form.BillingGroupDisplayPanelController;
import delta.games.lotro.lore.billingGroups.BillingGroupDescription;
import delta.games.lotro.lore.billingGroups.BillingGroupsManager;

/**
 * Factory for billing groups-related panels.
 * @author DAM
 */
public class BillingGroupPanelFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public BillingGroupPanelFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.BILLING_GROUP_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildBillingGroupPanel(id);
    }
    return ret;
  }

  private BillingGroupDisplayPanelController buildBillingGroupPanel(int billingGroupID)
  {
    BillingGroupsManager billingGroupsMgr=BillingGroupsManager.getInstance();
    BillingGroupDescription billingGroup=billingGroupsMgr.getBillingGroupDescription(billingGroupID);
    if (billingGroup!=null)
    {
      BillingGroupDisplayPanelController billingGroupPanel=new BillingGroupDisplayPanelController(_parent,billingGroup);
      return billingGroupPanel;
    }
    return null;
  }
}
