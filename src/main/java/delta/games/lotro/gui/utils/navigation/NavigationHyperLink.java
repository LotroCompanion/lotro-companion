package delta.games.lotro.gui.utils.navigation;

import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
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
  private WindowController _parent;
  private String _text;
  private PageIdentifier _pageId;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param text Link text.
   * @param pageId Page identifier.
   */
  public NavigationHyperLink(WindowController parent, String text, PageIdentifier pageId)
  {
    this(parent,text,pageId,GuiFactory.buildLabel(""));
  }

  /**
   * Constructor.
   * @param parent Parent window.
   * @param text Link text.
   * @param pageId Page identifier.
   * @param label Label to use.
   */
  public NavigationHyperLink(WindowController parent, String text, PageIdentifier pageId, JLabel label)
  {
    super(new LocalHyperlinkAction(text,new NavigationAction(parent,pageId)),label);
    _parent=parent;
    _text=text;
    _pageId=pageId;
  }

  /**
   * Set the page identifier.
   * @param pageId Page identifier to set.
   */
  public void setPageIdentifier(PageIdentifier pageId)
  {
    NavigationAction navigationAction=new NavigationAction(_parent,pageId);
    LocalHyperlinkAction hyperLinkAction=new LocalHyperlinkAction(_text,navigationAction);
    setAction(hyperLinkAction);
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
