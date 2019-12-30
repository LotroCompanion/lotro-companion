package delta.games.lotro.gui.lore.trade.vendor.form;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.trade.vendor.VendorNpc;

/**
 * Controller for a "vendor display" window.
 * @author DAM
 */
public class VendorDisplayWindowController extends DefaultDialogController
{
  @Override
  public void configureWindow()
  {
    super.configureWindow();
    getWindow().setPreferredSize(new Dimension(750,480));
  }

  // Controllers
  private VendorDisplayPanelController _controller;
  // Data
  private VendorNpc _vendor;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param vendor Vendor to show.
   */
  public VendorDisplayWindowController(WindowController parent, VendorNpc vendor)
  {
    super(parent);
    _vendor=vendor;
    setVendor(vendor);
  }

  /**
   * Set vendor to display.
   * @param vendor Vendor to display.
   */
  private void setVendor(VendorNpc vendor)
  {
    _controller=new VendorDisplayPanelController(getParentController(),vendor);
    JDialog dialog=getDialog();
    Container container=dialog.getContentPane();
    container.removeAll();
    JPanel panel=_controller.getPanel();
    container.add(panel,BorderLayout.CENTER);
    dialog.setTitle("Vendor: "+vendor.getNpc().getName());
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    dialog.setResizable(true);
  }

  @Override
  public String getWindowIdentifier()
  {
    return getId(_vendor);
  }

  /**
   * Get the identifier for a vendor display window.
   * @param vendor Vendor to show.
   * @return A window identifier.
   */
  public static String getId(VendorNpc vendor)
  {
    return "VENDOR_DISPLAY#"+vendor.getIdentifier();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    disposePetPanel();
    super.dispose();
  }

  private void disposePetPanel()
  {
    if (_controller!=null)
    {
      _controller.dispose();
      _controller=null;
    }
  }
}
