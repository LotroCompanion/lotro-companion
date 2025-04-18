package delta.games.lotro.gui.lore.trade.barter.form;

import java.util.List;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.trade.barter.BarterEntry;
import delta.games.lotro.lore.trade.barter.BarterNpc;
import delta.games.lotro.lore.trade.barter.BarterProfile;
import delta.games.lotro.lore.trade.barter.BarterersManager;
import delta.games.lotro.lore.trade.barter.ItemBarterEntryElement;

/**
 * Simple test class to show some barterer windows.
 * @author DAM
 */
public class MainTestBartererFormWindow
{
  private void doIt()
  {
    BarterersManager barterersMgr=BarterersManager.getInstance();
    List<BarterNpc> barterers=barterersMgr.getAll();
    for(BarterNpc barterer : barterers)
    {
      if (useBarterer(barterer))
      {
        showBartererWindow(barterer);
      }
    }
  }

  private boolean useBarterer(BarterNpc barterer)
  {
    List<BarterProfile> profiles=barterer.getBarterProfiles();
    for(BarterProfile profile : profiles)
    {
      List<BarterEntry> entries=profile.getEntries();
      for(BarterEntry entry : entries)
      {
        List<ItemBarterEntryElement> toGive=entry.getElementsToGive();
        if (toGive.size()>2)
        {
          return true;
        }
      }
    }
    return false;
  }

  private void showBartererWindow(BarterNpc barterer)
  {
    NavigatorWindowController window=NavigatorFactory.buildNavigator(null,1);
    PageIdentifier ref=ReferenceConstants.getBartererReference(barterer.getIdentifier());
    window.navigateTo(ref);
    window.show(false);
  }

  /**
   * Main method for this test class.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestBartererFormWindow().doIt();
  }
}
