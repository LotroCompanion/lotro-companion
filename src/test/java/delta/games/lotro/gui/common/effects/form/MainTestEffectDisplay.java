package delta.games.lotro.gui.common.effects.form;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.common.effects.EffectsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;

/**
 * Test class for the effect display panel.
 * @author DAM
 */
public class MainTestEffectDisplay
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    Effect effect=EffectsManager.getInstance().getEffectById(1879330910);
    NavigatorWindowController window=NavigatorFactory.buildNavigator(null,1);
    PageIdentifier ref=ReferenceConstants.getEffectReference(effect);
    window.navigateTo(ref);
    window.show(false);
  }
}
