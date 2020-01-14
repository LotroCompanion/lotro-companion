package delta.games.lotro.gui.lore.trade.vendor;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.trade.vendor.form.VendorDisplayPanelController;
import delta.games.lotro.lore.trade.vendor.VendorNpc;
import delta.games.lotro.lore.trade.vendor.VendorsManager;

/**
 * Factory for vendor panels.
 * @author DAM
 */
public class VendorPanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public VendorPanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.VENDOR_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildVendorPanel(id);
    }
    return ret;
  }

  private VendorDisplayPanelController buildVendorPanel(int vendorId)
  {
    VendorsManager vendorsMgr=VendorsManager.getInstance();
    VendorNpc vendor=vendorsMgr.getVendor(vendorId);
    if (vendor!=null)
    {
      VendorDisplayPanelController panel=new VendorDisplayPanelController(_parent,vendor);
      return panel;
    }
    return null;
  }
}
