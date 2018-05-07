package delta.games.lotro.gui.deed.form;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.deeds.DeedDescription;

/**
 * Controller for a "deed display" window.
 * @author DAM
 */
public class DeedDisplayWindowController extends DefaultDialogController
{
  // Controllers
  private DeedDisplayPanelController _controller;
  private int _id;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param id Identifier.
   */
  public DeedDisplayWindowController(WindowController parent, int id)
  {
    super(parent);
    _id=id;
  }

  /**
   * Set deed to display.
   * @param deed Deed to display.
   */
  public void setDeed(DeedDescription deed)
  {
    disposeDeedPanel();
    _controller=new DeedDisplayPanelController(this,deed);
    JDialog dialog=getDialog();
    Container container=dialog.getContentPane();
    container.removeAll();
    JPanel panel=_controller.getPanel();
    container.add(panel,BorderLayout.CENTER);
    dialog.setTitle("Deed: "+deed.getName());
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    dialog.setResizable(false);
  }

  @Override
  public String getWindowIdentifier()
  {
    return "DEED_DISPLAY#"+_id;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    disposeDeedPanel();
    super.dispose();
  }

  private void disposeDeedPanel()
  {
    if (_controller!=null)
    {
      _controller.dispose();
      _controller=null;
    }
  }
}
