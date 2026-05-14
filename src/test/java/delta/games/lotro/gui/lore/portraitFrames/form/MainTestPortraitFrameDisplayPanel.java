package delta.games.lotro.gui.lore.portraitFrames.form;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.portraitFrames.PortraitFrameDescription;
import delta.games.lotro.lore.portraitFrames.PortraitFramesManager;

/**
 * Test class for the portrait frame display panel.
 * @author DAM
 */
public class MainTestPortraitFrameDisplayPanel
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    PortraitFramesManager mgr=PortraitFramesManager.getInstance();
    for(PortraitFrameDescription portraitFrame : mgr.getAll())
    {
      NavigatorWindowController window=NavigatorFactory.buildNavigator(null,1);
      PageIdentifier ref=ReferenceConstants.getPortraitFrameReference(portraitFrame.getCode());
      window.navigateTo(ref);
      window.show(false);
    }
  }
}
