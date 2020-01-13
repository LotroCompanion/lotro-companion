package delta.games.lotro.gui.navigation;

import delta.common.ui.swing.navigator.NavigatorContentsResolver;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.deed.DeedPanelsFactory;
import delta.games.lotro.gui.items.ItemPanelsFactory;
import delta.games.lotro.gui.lore.trade.barter.BartererPanelsFactory;
import delta.games.lotro.gui.quests.QuestPanelsFactory;
import delta.games.lotro.gui.recipes.RecipePanelsFactory;

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
    resolver.addFactory(new QuestPanelsFactory(window));
    resolver.addFactory(new DeedPanelsFactory(window));
    resolver.addFactory(new ItemPanelsFactory(window));
    resolver.addFactory(new RecipePanelsFactory(window));
    resolver.addFactory(new BartererPanelsFactory(window));
    window.setContentsResolver(resolver);
    return window;
  }
}
