package delta.games.lotro.gui.about;

import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for a "credits" window.
 * @author DAM
 */
public class CreditsDialogController extends DefaultDialogController
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="CREDITS";

  // Controllers
  private CreditsPanelController _controller;

  /**
   * Constructor.
   * @param parent Parent controller.
   */
  public CreditsDialogController(WindowController parent)
  {
    super(parent);
    _controller=new CreditsPanelController();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle(Labels.getLabel("credits.title"));
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
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
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
