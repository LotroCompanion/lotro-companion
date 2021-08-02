package delta.games.lotro.gui.lore.titles.form;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.titles.TitleDescription;

/**
 * Controller for a "title display" window.
 * @author DAM
 */
public class TitleDisplayWindowController extends DefaultDialogController
{
  // Controllers
  private TitleDisplayPanelController _controller;
  // Data
  private TitleDescription _title;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param title Title to show.
   */
  public TitleDisplayWindowController(WindowController parent, TitleDescription title)
  {
    super(parent);
    _title=title;
    setTitle(title);
  }

  /**
   * Set title to display.
   * @param title Title to display.
   */
  private void setTitle(TitleDescription title)
  {
    _controller=new TitleDisplayPanelController(title);
    JDialog dialog=getDialog();
    Container container=dialog.getContentPane();
    container.removeAll();
    JPanel panel=_controller.getPanel();
    container.add(panel,BorderLayout.CENTER);
    dialog.setTitle("Title: "+title.getName());
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
    return getId(_title);
  }

  /**
   * Get the identifier for a title display window.
   * @param title Title to show.
   * @return A window identifier.
   */
  public static String getId(TitleDescription title)
  {
    return "TITLE_DISPLAY#"+title.getIdentifier();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    disposeTitlePanel();
    super.dispose();
  }

  private void disposeTitlePanel()
  {
    if (_controller!=null)
    {
      _controller.dispose();
      _controller=null;
    }
  }
}
