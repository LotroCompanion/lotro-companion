package delta.games.lotro.gui.lore.agents.mobs.form;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.agents.mobs.MobDescription;
import delta.games.lotro.lore.agents.mobs.MobsManager;

/**
 * Test class for the mob display panel.
 * @author DAM
 */
public class MainTestMobDisplayPanel
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    int index=1;
    for(MobDescription mob : MobsManager.getInstance().getMobs())
    {
      NavigatorWindowController window=NavigatorFactory.buildNavigator(null,index);
      PageIdentifier ref=ReferenceConstants.getMobReference(mob);
      window.navigateTo(ref);
      window.show(false);
      if (index==10)
      {
        break;
      }
      index++;
    }
  }
}
