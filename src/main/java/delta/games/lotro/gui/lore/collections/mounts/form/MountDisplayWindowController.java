package delta.games.lotro.gui.lore.collections.mounts.form;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.collections.mounts.MountDescription;

/**
 * Controller for a "mount display" window.
 * @author DAM
 */
public class MountDisplayWindowController extends DefaultDialogController
{
  // Controllers
  private MountDisplayPanelController _controller;
  // Data
  private MountDescription _mount;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param mount Mount to show.
   */
  public MountDisplayWindowController(WindowController parent, MountDescription mount)
  {
    super(parent);
    _mount=mount;
    setMount(mount);
  }

  /**
   * Set mount to display.
   * @param mount Mount to display.
   */
  private void setMount(MountDescription mount)
  {
    _controller=new MountDisplayPanelController(mount);
    JDialog dialog=getDialog();
    Container container=dialog.getContentPane();
    container.removeAll();
    JPanel panel=_controller.getPanel();
    container.add(panel,BorderLayout.CENTER);
    dialog.setTitle("Mount: "+mount.getName()); // 18n
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
    return getId(_mount);
  }

  /**
   * Get the identifier for a mount display window.
   * @param mount Mount to show.
   * @return A window identifier.
   */
  public static String getId(MountDescription mount)
  {
    return "MOUNT_DISPLAY#"+mount.getIdentifier();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    disposeMountPanel();
    super.dispose();
  }

  private void disposeMountPanel()
  {
    if (_controller!=null)
    {
      _controller.dispose();
      _controller=null;
    }
  }
}
