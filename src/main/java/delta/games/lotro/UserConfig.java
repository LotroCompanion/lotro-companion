package delta.games.lotro;

import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;

/**
 * User-level configuration.
 * @author DAM
 */
public class UserConfig
{
  private static UserConfig _instance=new UserConfig();

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static UserConfig getInstance()
  {
    return _instance;
  }

  /**
   * Private constructor.
   */
  private UserConfig()
  {
    // Nothing!
  }

  /**
   * Get a string value.
   * @param category Category.
   * @param key Key.
   * @param defaultValue Default value.
   * @return A string value.
   */
  public String getStringValue(String category, String key, String defaultValue)
  {
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties props=preferences.getPreferences(category);
    String value=props.getStringProperty(key,defaultValue);
    return value;
  }

  /**
   * Set a string value.
   * @param category Category.
   * @param key Key.
   * @param value Value to set.
   */
  public void setStringValue(String category, String key, String value)
  {
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties props=preferences.getPreferences(category);
    props.setStringProperty(key,value);
  }

  /**
   * Save user configuration.
   */
  public void save()
  {
    Preferences preferences=Config.getInstance().getPreferences();
    preferences.saveAllPreferences();
  }
}
