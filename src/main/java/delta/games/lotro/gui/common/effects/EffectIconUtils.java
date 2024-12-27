package delta.games.lotro.gui.common.effects;

import javax.swing.Icon;

import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.gui.LotroIconsManager;

/**
 * Utility methods related to effect icons.
 * @author DAM
 */
public class EffectIconUtils
{
  /**
   * Build an effect icon.
   * @param effect Effect to use.
   * @return A new controller.
   */
  public static Icon buildEffectIcon(Effect effect)
  {
    Icon icon=null;
    if (effect!=null)
    {
      Integer iconID=effect.getIconId();
      if (iconID!=null)
      {
        icon=LotroIconsManager.getEffectIcon(iconID.intValue());
      }
    }
    if (icon==null)
    {
      icon=LotroIconsManager.getDefaultItemIcon();
    }
    return icon;
  }
}
