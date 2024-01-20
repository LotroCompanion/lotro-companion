package delta.games.lotro.gui.lore.traits.form;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;

/**
 * Test class for the trait display panel.
 * @author DAM
 */
public class MainTestTraitDisplayPanel
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    int[] traitIDs=new int[]
    {
        1879269704, // Physical Mastery
        1879269705, // Careful Shield-work
        1879215547, // Lightly Armoured Fire Orc Appearance"
        1879272589, // Hands of Healing (several stats with same name)
        1879062819, // Track Mines// (with skills, no stats)
        1879154339  // Light Armour Training
    };
    for(int traitID : traitIDs)
    {
      NavigatorWindowController window=NavigatorFactory.buildNavigator(null,1);
      PageIdentifier ref=ReferenceConstants.getTraitReference(traitID);
      window.navigateTo(ref);
      window.show(false);
    }
  }
}
