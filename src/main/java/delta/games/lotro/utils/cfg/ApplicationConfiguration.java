package delta.games.lotro.utils.cfg;

import java.io.File;

import delta.common.utils.ListenersManager;
import delta.common.utils.l10n.L10nConfiguration;
import delta.common.utils.l10n.dates.DateFormatID;
import delta.common.utils.l10n.numbers.NumberFormatID;
import delta.games.lotro.config.DataConfiguration;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.config.UserConfig;
import delta.games.lotro.config.labels.LabelsConfiguration;
import delta.games.lotro.dat.data.DatConfiguration;

/**
 * Configuration of the LotroCompanion application.
 * @author DAM
 */
public class ApplicationConfiguration
{
  // DAT
  private static final String DAT_CONFIGURATION="DatConfiguration";
  private static final String CLIENT_PATH="ClientPath";
  // L10n
  private static final String L10N_CONFIGURATION="Localization";
  private static final String DATE_FORMAT="DateFormat";
  private static final String DATETIME_FORMAT="DateTimeFormat";
  private static final String NUMBER_FORMAT="NumberFormat";

  private static final ApplicationConfiguration _instance=new ApplicationConfiguration();
  private DatConfiguration _datConfiguration;
  private L10nConfiguration _l10nConfiguration;
  private DataConfiguration _dataConfiguration;
  private LabelsConfiguration _labelsConfiguration;
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
    initConfiguration();
    _listeners=new ListenersManager<ConfigurationListener>();
  }

  /**
   * Get the DAT configuration. 
   * @return the DAT configuration.
   */
  public DatConfiguration getDatConfiguration()
  {
    return _datConfiguration;
  }

  /**
   * Get the localization configuration. 
   * @return the localization configuration.
   */
  public L10nConfiguration getL10nConfiguration()
  {
    return _l10nConfiguration;
  }

  /**
   * Get the data configuration. 
   * @return the data configuration.
   */
  public DataConfiguration getDataConfiguration()
  {
    return _dataConfiguration;
  }

  /**
   * Get the labels configuration.
   * @return the labels configuration.
   */
  public LabelsConfiguration getLabelsConfiguration()
  {
    return _labelsConfiguration;
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
    _datConfiguration=new DatConfiguration();
    UserConfig config=UserConfig.getInstance();
    // DAT
    String clientPath=config.getStringValue(DAT_CONFIGURATION,CLIENT_PATH,null);
    if (clientPath!=null)
    {
      File rootPath=new File(clientPath);
      _datConfiguration.setRootPath(rootPath);
    }
    // Localization
    _l10nConfiguration=new L10nConfiguration();
    String dateFormat=config.getStringValue(L10N_CONFIGURATION,DATE_FORMAT,DateFormatID.AUTO);
    _l10nConfiguration.setDateFormatID(dateFormat);
    String dateTimeFormat=config.getStringValue(L10N_CONFIGURATION,DATETIME_FORMAT,DateFormatID.AUTO);
    _l10nConfiguration.setDateTimeFormatID(dateTimeFormat);
    String integerFormat=config.getStringValue(L10N_CONFIGURATION,NUMBER_FORMAT,NumberFormatID.AUTO);
    _l10nConfiguration.setNumberFormatID(integerFormat);
    // Data
    _dataConfiguration=LotroCoreConfig.getInstance().getDataConfiguration();
    // Labels
    _labelsConfiguration=LotroCoreConfig.getInstance().getLabelsConfiguration();
    // Save...
    saveConfiguration();
  }

  /**
   * Save configuration.
   */
  public void saveConfiguration()
  {
    UserConfig userCfg=UserConfig.getInstance();
    // LOTRO client path
    String clientPath=_datConfiguration.getRootPath().getAbsolutePath();
    userCfg.setStringValue(DAT_CONFIGURATION,CLIENT_PATH,clientPath);
    // Default formats
    String dateFormat=_l10nConfiguration.getDateFormatID();
    userCfg.setStringValue(L10N_CONFIGURATION,DATE_FORMAT,dateFormat);
    String dateTimeFormat=_l10nConfiguration.getDateTimeFormatID();
    userCfg.setStringValue(L10N_CONFIGURATION,DATETIME_FORMAT,dateTimeFormat);
    String numberFormat=_l10nConfiguration.getNumberFormatID();
    userCfg.setStringValue(L10N_CONFIGURATION,NUMBER_FORMAT,numberFormat);
    // Data
    _dataConfiguration.save(userCfg);
    // Labels
    _labelsConfiguration.save(userCfg);
    // Save configuration
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
