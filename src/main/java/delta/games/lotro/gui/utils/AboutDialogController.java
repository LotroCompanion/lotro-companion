package delta.games.lotro.gui.utils;

import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.games.lotro.utils.gui.DefaultDialogController;
import delta.games.lotro.utils.gui.WindowController;

/**
 * Controller for a "about" window.
 * @author DAM
 */
public class AboutDialogController extends DefaultDialogController
{
  // Controllers
  private AboutPanelController _controller;

  /**
   * Constructor.
   * @param parent Parent controller.
   */
  public AboutDialogController(WindowController parent)
  {
    super(parent);
    _controller=new AboutPanelController();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("About");
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    dialog.setResizable(false);
    //dialog.setMinimumSize(new Dimension(400,300));
    return dialog;
  }

  /**
   * Get the window identifier for a given toon.
   * @return A window identifier.
   */
  public static String getIdentifier()
  {
    return "ABOUT";
  }

  @Override
  public String getWindowIdentifier()
  {
    return getIdentifier();
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=_controller.getPanel();
    return panel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_controller!=null)
    {
      _controller.dispose();
      _controller=null;
    }
    super.dispose();
  }
}
