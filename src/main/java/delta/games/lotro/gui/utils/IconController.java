package delta.games.lotro.gui.utils;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;

/**
 * Controller for an icon that brings a page.
 * @author DAM
 */
public class IconController extends AbstractIconController
{
  private PageIdentifier _pageId;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public IconController(WindowController parent)
  {
    this(parent,true);
  }

  /**
   * Constructor.
   * @param parent Parent window.
   * @param useNavigation Use navigation of not.
   */
  public IconController(WindowController parent, boolean useNavigation)
  {
    super(parent,useNavigation);
    if (useNavigation)
    {
      _listener=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          NavigationUtils.navigateTo(_pageId,_parent);
        }
      };
      _icon.addActionListener(_listener);
      _icon.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    else
    {
      _icon.setEnabled(false);
    }
  }

  /**
   * Clear the contents.
   * @param icon Default icon.
   */
  @Override
  public void clear(Icon icon)
  {
    super.clear(icon);
    _pageId=null;
  }

  /**
   * Set the page to use.
   * @param pageId Page identifier.
   */
  public void setPageId(PageIdentifier pageId)
  {
    _pageId=pageId;
  }

  /**
   * Get the page identifier.
   * @return the page identifier.
   */
  public PageIdentifier getPageIdentifier()
  {
    return _pageId;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    _pageId=null;
  }
}
