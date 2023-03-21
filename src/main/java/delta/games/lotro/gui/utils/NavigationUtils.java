package delta.games.lotro.gui.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;

import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.gui.navigation.NavigatorFactory;

/**
 * Utility methods related to navigation.
 * @author DAM
 */
public class NavigationUtils
{
  private static final Logger LOGGER=Logger.getLogger(NavigationUtils.class);

  /*
   * Policies:
   * - self if possible (flag): use parent if its a navigator
   * - single child
   * - multiple child
   */

  /**
   * Navigate to the given page using the given parent window.
   * @param pageId Page identifier.
   * @param parent Parent window.
   */
  public static void navigateTo(PageIdentifier pageId, WindowController parent)
  {
    if (pageId!=null)
    {
      LOGGER.info("Display page: "+pageId);
      NavigatorWindowController navigator=null;
      if (parent instanceof NavigatorWindowController)
      {
        navigator=(NavigatorWindowController)parent;
      }
      else
      {
        WindowsManager windows=parent.getWindowsManager();
        int id=windows.getFreeId();
        navigator=NavigatorFactory.buildNavigator(parent,id);
        windows.registerWindow(navigator);
      }
      navigator.navigateTo(pageId);
      navigator.bringToFront();
    }
  }

  /**
   * Navigate to the given page using the given parent window (use a single child).
   * @param pageId Page identifier.
   * @param parent Parent window.
   */
  public static void navigateToSingleChild(PageIdentifier pageId, WindowController parent)
  {
    WindowsManager windows=parent.getWindowsManager();
    int nbWindows=windows.getAll().size();
    if (nbWindows==0)
    {
      NavigatorWindowController window=NavigatorFactory.buildNavigator(parent,0);
      window.navigateTo(pageId);
      window.show(false);
      windows.registerWindow(window);
    }
    else
    {
      NavigatorWindowController window=(NavigatorWindowController)windows.getAll().get(0);
      window.navigateTo(pageId);
      window.bringToFront();
    }
  }

  /**
   * Build a navigation link controller.
   * @param parent Parent window.
   * @param text Link text.
   * @param pageId Page identifier.
   * @return a new controller.
   */
  public static HyperLinkController buildNavigationLink(final WindowController parent, String text, final PageIdentifier pageId)
  {
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        NavigationUtils.navigateTo(pageId,parent);
      }
    };
    LocalHyperlinkAction action=new LocalHyperlinkAction(text,al);
    HyperLinkController controller=new HyperLinkController(action);
    return controller;
  }
}
