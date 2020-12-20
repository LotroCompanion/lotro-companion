package delta.games.lotro.utils.cfg;

import java.io.File;

import delta.common.utils.ListenersManager;
import delta.games.lotro.UserConfig;
import delta.games.lotro.dat.data.DatConfiguration;

/**
 * Configuration of the LotroCompanion application.
 * @author DAM
 */
public class ApplicationConfiguration
{
  private static final String DAT_CONFIGURATION="DatConfiguration";
  private static final String CLIENT_PATH="ClientPath";

  private static final ApplicationConfiguration _instance=new ApplicationConfiguration();
  private DatConfiguration _configuration;
  private ListenersManager<ConfigurationListener> _listeners;

  /**
   * Get the application configuration.
   * @return the application configuration.
   */
  public static final ApplicationConfiguration getInstance()
  {
    return _instance;
  }

  /**
   * Constructor.
   */
  private ApplicationConfiguration()
  {
    _configuration=new DatConfiguration();
    initConfiguration();
    _listeners=new ListenersManager<ConfigurationListener>();
  }

  /**
   * Get the DAT configuration. 
   * @return the DAT configuration.
   */
  public DatConfiguration getDatConfiguration()
  {
    return _configuration;
  }

  /**
   * Get the configuration listeners.
   * @return the configuration listeners.
   */
  public ListenersManager<ConfigurationListener> getListeners()
  {
    return _listeners;
  }

  private void initConfiguration()
  {
    _configuration=new DatConfiguration();
    String clientPath=UserConfig.getInstance().getStringValue(DAT_CONFIGURATION,CLIENT_PATH,null);
    if (clientPath!=null)
    {
      File rootPath=new File(clientPath);
      _configuration.setRootPath(rootPath);
    }
  }

  /**
   * Save configuration.
   */
  public void saveConfiguration()
  {
    String clientPath=_configuration.getRootPath().getAbsolutePath();
    UserConfig userCfg=UserConfig.getInstance();
    userCfg.setStringValue(DAT_CONFIGURATION,CLIENT_PATH,clientPath);
    UserConfig.getInstance().save();
  }

  /**
   * Called when the configuration has been updated.
   */
  public void configurationUpdated()
  {
    for(ConfigurationListener listener : _listeners)
    {
      listener.configurationUpdated(this);
    }
  }
}
