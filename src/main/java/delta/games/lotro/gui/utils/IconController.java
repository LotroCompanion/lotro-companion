package delta.games.lotro.gui.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

import org.apache.log4j.Logger;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.navigation.NavigatorFactory;

/**
 * Controller for an icon that brings a page.
 * @author DAM
 */
public class IconController extends AbstractIconController
{
  private static final Logger LOGGER=Logger.getLogger(IconController.class);

  protected PageIdentifier _pageId;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public IconController(WindowController parent)
  {
    super(parent);
    _listener=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showForm();
      }
    };
    _icon.addActionListener(_listener);
  }

  private void showForm()
  {
    if (_pageId!=null)
    {
      LOGGER.info("Display page: "+_pageId);
      NavigatorWindowController navigator=null;
      if (_parent instanceof NavigatorWindowController)
      {
        navigator=(NavigatorWindowController)_parent;
      }
      else
      {
        int id=_parent.getWindowsManager().getAll().size();
        navigator=NavigatorFactory.buildNavigator(_parent,id);
      }
      navigator.navigateTo(_pageId);
      navigator.bringToFront();
    }
  }

  /**
   * Clear the contents.
   * @param icon Default icon.
   */
  public void clear(Icon icon)
  {
    super.clear(icon);
    _pageId=null;
  }

  /**
   * Set the page to use.
   * @param pageId Page identifier.
   */
  protected void setPageId(PageIdentifier pageId)
  {
    _pageId=pageId;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    _pageId=null;
  }
}
