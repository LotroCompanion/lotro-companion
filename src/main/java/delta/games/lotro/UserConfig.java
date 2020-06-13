package delta.games.lotro;

import java.io.File;

import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.dat.data.DatConfiguration;

/**
 * User-level configuration.
 * @author DAM
 */
public class UserConfig
{
  private static final String DAT_CONFIGURATION="DatConfiguration";
  private static final String CLIENT_PATH="ClientPath";

  private DatConfiguration _datConfiguration;

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
    _datConfiguration=new DatConfiguration();
    load();
  }

  /**
   * Get the DAT configuration.
   * @return the DAT configuration.
   */
  public DatConfiguration getDatConfiguration()
  {
    return _datConfiguration;
  }

  private void load()
  {
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties datProps=preferences.getPreferences(DAT_CONFIGURATION);
    String clientPath=datProps.getStringProperty(CLIENT_PATH,null);
    if (clientPath!=null)
    {
      _datConfiguration.setRootPath(new File(clientPath));
    }
  }

  /**
   * Save user configuration.
   */
  public void save()
  {
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties datProps=preferences.getPreferences(DAT_CONFIGURATION);
    String clientPath=_datConfiguration.getRootPath().getAbsolutePath();
    datProps.setStringProperty(CLIENT_PATH,clientPath);
    preferences.saveAllPreferences();
  }
}
