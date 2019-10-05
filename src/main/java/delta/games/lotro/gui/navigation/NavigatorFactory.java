package delta.games.lotro.gui.navigation;

import delta.common.ui.swing.navigator.NavigatorContentsResolver;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.common.navigator.AchievablePanelsFactory;

/**
 * Factory for LOTRO navigator window controllers.
 * @author DAM
 */
public class NavigatorFactory
{
  /**
   * Build a new navigator window controller.
   * @param parent Parent window, if any.
   * @param index Index of the navigator window.
   * @return a new, customized navigator window.
   */
  public static NavigatorWindowController buildNavigator(WindowController parent, int index)
  {
    NavigatorWindowController window=new NavigatorWindowController(parent,index);
    NavigatorContentsResolver resolver=new NavigatorContentsResolver();
    AchievablePanelsFactory factory=new AchievablePanelsFactory(window);
    resolver.addFactory(factory);
    window.setContentsResolver(resolver);
    return window;
  }
}
