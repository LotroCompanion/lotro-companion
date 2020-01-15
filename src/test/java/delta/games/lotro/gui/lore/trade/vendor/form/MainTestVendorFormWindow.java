package delta.games.lotro.gui.lore.trade.vendor.form;

import java.util.List;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.trade.vendor.VendorNpc;
import delta.games.lotro.lore.trade.vendor.VendorsManager;

/**
 * Simple test class to show some vendor windows.
 * @author DAM
 */
public class MainTestVendorFormWindow
{
  private void doIt()
  {
    VendorsManager vendorsMgr=VendorsManager.getInstance();
    List<VendorNpc> vendors=vendorsMgr.getAll();
    int index=0;
    for(VendorNpc vendor : vendors)
    {
      if (index%20==0)
      {
        showVendorWindow(vendor);
      }
      index++;
    }
  }

  private void showVendorWindow(VendorNpc vendor)
  {
    NavigatorWindowController window=NavigatorFactory.buildNavigator(null,1);
    PageIdentifier ref=ReferenceConstants.getVendorReference(vendor.getIdentifier());
    window.navigateTo(ref);
    window.show(false);
  }

  /**
   * Main method for this test class.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestVendorFormWindow().doIt();
  }
}
