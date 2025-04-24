package delta.games.lotro.gui.lore.birds.form;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.collections.birds.BirdDescription;
import delta.games.lotro.lore.collections.birds.BirdsManager;

/**
 * Test class for the bird display panel.
 * @author DAM
 */
public class MainTestBirdDisplayPanel
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    for(BirdDescription bird : BirdsManager.getInstance().getAll())
    {
      NavigatorWindowController window=NavigatorFactory.buildNavigator(null,1);
      PageIdentifier ref=ReferenceConstants.getBirdReference(bird.getIdentifier());
      window.navigateTo(ref);
      window.show(false);
    }
  }
}
