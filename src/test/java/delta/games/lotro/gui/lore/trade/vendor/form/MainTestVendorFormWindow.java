package delta.games.lotro.gui.lore.trade.vendor.form;

import java.util.List;

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
    VendorDisplayWindowController window=new VendorDisplayWindowController(null,vendor);
    window.show();
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
