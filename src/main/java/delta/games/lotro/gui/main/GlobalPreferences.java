package delta.games.lotro.gui.main;

import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.Config;

/**
 * Access to global preferences.
 * @author DAM
 */
public class GlobalPreferences
{
  /**
   * Get properties from the global preferences.
   * @param id Identifier of the preferences set.
   * @return Some properties or <code>null</code> if not managed.
   */
  public static TypedProperties getGlobalProperties(String id)
  {
    Preferences prefs=Config.getInstance().getPreferences();
    TypedProperties props=prefs.getPreferences(id);
    return props;
  }
}
