package delta.games.lotro.gui.common.navigator;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;

/**
 * Controller for a "navigator" window.
 * @author DAM
 */
public class NavigatorWindowController extends DefaultDialogController
{
  // Controllers
  private NavigablePanelController _controller;
  private NavigatorContentsResolver _contentsResolver;
  private int _id;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param id Identifier.
   */
  public NavigatorWindowController(WindowController parent, int id)
  {
    super(parent);
    _id=id;
    _contentsResolver=new NavigatorContentsResolver(this);
  }

  /**
   * Navigate to show an object.
   * @param reference Object reference.
   */
  public void navigateTo(String reference)
  {
    NavigablePanelController panelController=_contentsResolver.resolveReference(reference);
    if (panelController!=null)
    {
      disposeCurrentPanel();
      _controller=panelController;
      JDialog dialog=getDialog();
      Container container=dialog.getContentPane();
      container.removeAll();
      JPanel panel=_controller.getPanel();
      container.add(panel,BorderLayout.CENTER);
      String title=_controller.getTitle();
      dialog.setTitle(title);
      dialog.pack();
      WindowController controller=getParentController();
      if (controller!=null)
      {
        Window parentWindow=controller.getWindow();
        dialog.setLocationRelativeTo(parentWindow);
      }
      dialog.setResizable(false);
    }
  }

  @Override
  public String getWindowIdentifier()
  {
    return "NAVIGATOR#"+_id;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    disposeCurrentPanel();
    super.dispose();
  }

  private void disposeCurrentPanel()
  {
    if (_controller!=null)
    {
      _controller.dispose();
      _controller=null;
    }
  }
}
