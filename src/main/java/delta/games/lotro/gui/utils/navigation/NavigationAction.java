package delta.games.lotro.gui.utils.navigation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.utils.NavigationUtils;

/**
 * Navigation action.
 * @author DAM
 */
public class NavigationAction implements ActionListener
{
  private WindowController _parent;
  private PageIdentifier _pageId;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param pageId Page identifier.
   */
  public NavigationAction(WindowController parent, PageIdentifier pageId)
  {
    _parent=parent;
    _pageId=pageId;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    NavigationUtils.navigateTo(_pageId,_parent);
  }
}
