package delta.games.lotro.gui.lore.virtues.form;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.character.virtues.VirtuesManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;

/**
 * Test class for the virtue display panel.
 * @author DAM
 */
public class MainTestVirtueDisplayPanel
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    VirtuesManager virtuesMgr=VirtuesManager.getInstance();
    for(VirtueDescription virtue : virtuesMgr.getAll())
    {
      int virtueID=virtue.getIdentifier();
      NavigatorWindowController window=NavigatorFactory.buildNavigator(null,1);
      PageIdentifier ref=ReferenceConstants.getVirtueReference(virtueID);
      window.navigateTo(ref);
      window.show(false);
    }
  }
}
