package delta.games.lotro.gui.lore.hobbies.form;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.hobbies.HobbiesManager;
import delta.games.lotro.lore.hobbies.HobbyDescription;

/**
 * Test class for the hobby display panel.
 * @author DAM
 */
public class MainTestHobbyDisplayPanel
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    for(HobbyDescription hobby : HobbiesManager.getInstance().getAll())
    {
      NavigatorWindowController window=NavigatorFactory.buildNavigator(null,1);
      PageIdentifier ref=ReferenceConstants.getHobbyReference(hobby.getIdentifier());
      window.navigateTo(ref);
      window.show(false);
    }
  }
}
