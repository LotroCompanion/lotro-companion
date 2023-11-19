package delta.games.lotro.gui.utils.navigation;

import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;

/**
 * Navigation hyperlink.
 * @author DAM
 */
public class NavigationHyperLink extends HyperLinkController
{
  private PageIdentifier _pageId;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param text Link text.
   * @param pageId Page identifier.
   */
  public NavigationHyperLink(WindowController parent, String text, PageIdentifier pageId)
  {
    super(new LocalHyperlinkAction(text,new NavigationAction(parent,pageId)));
    _pageId=pageId;
  }

  /**
   * Get the managed page identifier.
   * @return the managed page identifier.
   */
  public PageIdentifier getPageIdentifier()
  {
    return _pageId;
  }
}
