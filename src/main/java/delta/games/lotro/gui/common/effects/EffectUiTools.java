package delta.games.lotro.gui.common.effects;

import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.NavigationUtils;

/**
 * Tools related to effects UI.
 * @author DAM
 */
public class EffectUiTools
{
  /**
   * Show the form window for an effect.
   * @param parent Parent window.
   * @param effect Effect to show.
   */
  public static void showEffectForm(WindowController parent, Effect effect)
  {
    PageIdentifier ref=ReferenceConstants.getEffectReference(effect);
    NavigationUtils.navigateTo(ref,parent);
  }
}
