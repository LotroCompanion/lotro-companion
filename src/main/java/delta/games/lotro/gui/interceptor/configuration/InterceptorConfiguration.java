package delta.games.lotro.gui.interceptor.configuration;

import java.io.File;

import delta.games.lotro.UserConfig;
import delta.games.lotro.dat.data.DatConfiguration;

/**
 * Interceptor configuration.
 * @author DAM
 */
public class InterceptorConfiguration
{
  private static final String DAT_CONFIGURATION="DatConfiguration";
  private static final String CLIENT_PATH="ClientPath";

  private DatConfiguration _configuration;

  /**
   * Constructor.
   */
  public InterceptorConfiguration()
  {
    _configuration=new DatConfiguration();
    initConfiguration();
  }

  /**
   * Get the DAT configuration. 
   * @return the DAT configuration.
   */
  public DatConfiguration getConfiguration()
  {
    return _configuration;
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
}
