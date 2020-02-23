package delta.games.lotro.gui.items.legendary.relics;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.items.legendary.relics.Relic;

/**
 * Tools related to relics UI.
 * @author DAM
 */
public class RelicUiTools
{
  /**
   * Show the form window for a relic.
   * @param parent Parent window.
   * @param relic Relic to show.
   */
  public static void showRelicForm(WindowController parent, Relic relic)
  {
    NavigatorWindowController window=null;
    if (parent instanceof NavigatorWindowController)
    {
      window=(NavigatorWindowController)parent;
    }
    else
    {
      WindowsManager windows=parent.getWindowsManager();
      int id=windows.getAll().size();
      window=NavigatorFactory.buildNavigator(parent,id);
    }
    PageIdentifier ref=ReferenceConstants.getRelicReference(relic.getIdentifier());
    window.navigateTo(ref);
    window.show(false);
  }
}
