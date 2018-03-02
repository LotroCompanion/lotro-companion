package delta.games.lotro.gui.deed.form;

import java.awt.Window;

import javax.swing.JComponent;
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

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param deed Deed to display.
   */
  public DeedDisplayWindowController(WindowController parent, DeedDescription deed)
  {
    super(parent);
    _controller=new DeedDisplayPanelController(parent,deed);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Deed");
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    dialog.setResizable(false);
    return dialog;
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
