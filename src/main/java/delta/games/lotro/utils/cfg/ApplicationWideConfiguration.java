package delta.games.lotro.utils.cfg;

import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.config.UserConfig;

/**
 * Application-wide configuration.
 * @author DAM
 */
public class ApplicationWideConfiguration
{
  // Labels
  private static final String CONFIGURATION_ID="ApplicationWideConfiguration";
  private static final String QUIT_CONFIRMATION_KEY="AskForQuit";

  private boolean _askForQuit;

  /**
   * Constructor.
   */
  public ApplicationWideConfiguration()
  {
    _askForQuit=false;
  }

  /**
   * Ask confirmation before quit.
   * @return <code>true</code> to ask, <code>false</code> to do it directly.
   */
  public boolean askForQuit()
  {
    return _askForQuit;
  }

  /**
   * Set the value of the 'ask for quit' flag.
   * @param askForQuit Value to set.
   */
  public void setAskForQuit(boolean askForQuit)
  {
    _askForQuit=askForQuit;
  }

  /**
   * Save configuration.
   * @param userCfg User configuration.
   */
  public void save(UserConfig userCfg)
  {
    userCfg.setStringValue(CONFIGURATION_ID,QUIT_CONFIRMATION_KEY,String.valueOf(_askForQuit));
  }

  /**
   * Initialize from preferences.
   * @param preferences Preferences to use.
   */
  public void fromPreferences(Preferences preferences)
  {
    TypedProperties props=preferences.getPreferences(CONFIGURATION_ID);
    boolean askForQuit=props.getBooleanProperty(QUIT_CONFIRMATION_KEY,false);
    setAskForQuit(askForQuit);
  }
}
