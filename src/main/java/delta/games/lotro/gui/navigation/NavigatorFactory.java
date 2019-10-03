package delta.games.lotro.gui.navigation;

import delta.common.ui.swing.navigator.NavigatorContentsResolver;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.common.navigator.AchievablePanelsFactory;

/**
 * @author DAM
 */
public class NavigatorFactory
{
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
