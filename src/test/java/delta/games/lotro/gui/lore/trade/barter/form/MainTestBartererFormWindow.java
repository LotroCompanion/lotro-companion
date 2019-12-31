package delta.games.lotro.gui.lore.trade.barter.form;

import java.util.List;

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
    int index=0;
    for(BarterNpc barterer : barterers)
    {
      if (useBarterer(barterer,index))
      {
        showBartererWindow(barterer);
      }
      index++;
    }
  }

  private boolean useBarterer(BarterNpc barterer, int index)
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
        /*
        BarterEntryElement toReceive=entry.getElementToReceive();
        if (toReceive instanceof ReputationBarterEntryElement)
        {
          return true;
        }
        */
        /*
        BarterEntryElement toReceive=entry.getElementToReceive();
        if (toReceive instanceof ItemBarterEntryElement)
        {
          ItemBarterEntryElement itemToReceive=(ItemBarterEntryElement)toReceive;
          if (itemToReceive.getQuantity()>1)
          {
            return true;
          }
        }
        */
      }
    }
    return false;
  }

  private void showBartererWindow(BarterNpc barterer)
  {
    BarterDisplayWindowController window=new BarterDisplayWindowController(null,barterer);
    window.show();
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
