package delta.games.lotro.gui.lore.trade.barter.form;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.trade.barter.BarterNpc;

/**
 * Controller for a "barterer display" window.
 * @author DAM
 */
public class BarterDisplayWindowController extends DefaultDialogController
{
  @Override
  public void configureWindow()
  {
    super.configureWindow();
    getWindow().setPreferredSize(new Dimension(500,675));
  }

  // Controllers
  private BarterDisplayPanelController _controller;
  // Data
  private BarterNpc _barterer;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param barterer Barterer to show.
   */
  public BarterDisplayWindowController(WindowController parent, BarterNpc barterer)
  {
    super(parent);
    _barterer=barterer;
    setBarterer(barterer);
  }

  /**
   * Set barterer to display.
   * @param barterer Barterer to display.
   */
  private void setBarterer(BarterNpc barterer)
  {
    _controller=new BarterDisplayPanelController(getParentController(),barterer);
    JDialog dialog=getDialog();
    Container container=dialog.getContentPane();
    container.removeAll();
    JPanel panel=_controller.getPanel();
    container.add(panel,BorderLayout.CENTER);
    dialog.setTitle("Barterer: "+barterer.getNpc().getName());
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
    return getId(_barterer);
  }

  /**
   * Get the identifier for a barterer display window.
   * @param barterer Barterer to show.
   * @return A window identifier.
   */
  public static String getId(BarterNpc barterer)
  {
    return "BARTER_DISPLAY#"+barterer.getIdentifier();
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
