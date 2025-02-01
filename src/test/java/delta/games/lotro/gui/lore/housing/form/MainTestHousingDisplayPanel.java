package delta.games.lotro.gui.lore.housing.form;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.character.status.housing.HouseAddress;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;

/**
 * Test class for the house display panel.
 * @author DAM
 */
public class MainTestHousingDisplayPanel
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    HouseAddress address=new HouseAddress(1879094517,1879094585);
    NavigatorWindowController window=NavigatorFactory.buildNavigator(null,1);
    PageIdentifier ref=ReferenceConstants.getHouseReference(address);
    window.navigateTo(ref);
    window.show(false);
  }
}
