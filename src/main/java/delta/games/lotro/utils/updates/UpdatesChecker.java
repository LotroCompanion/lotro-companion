package delta.games.lotro.utils.updates;

import java.awt.Window;
import java.io.InputStream;
import java.net.URL;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import delta.games.lotro.Config;
import delta.games.lotro.Preferences;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.utils.LotroLoggers;
import delta.games.lotro.utils.TypedProperties;

/**
 * Checks for software updates.
 * @author DAM
 */
public class UpdatesChecker
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String UPDATES_PREFERENCES_NAME="updates";
  private static final String LAST_UPDATE_CHECK_PROPERTY_NAME="lastUpdateCheck.timestamp";

  private Window _window;

  /**
   * Constructor.
   * @param window Parent window.
   */
  public UpdatesChecker(Window window)
  {
    _window=window;
  }

  private TypedProperties loadRemoteProperties()
  {
    TypedProperties ret=null;
    TypedProperties conf=Config.getInstance().getParameters();
    String urlStr=conf.getStringProperty("update.check.url",null);
    if (urlStr!=null)
    {
      try
      {
        URL url=new URL(urlStr);
        InputStream is=url.openStream();
        TypedProperties props=new TypedProperties();
        boolean ok=props.loadFromInputStream(is);
        if (ok)
        {
          ret=props;
        }
      }
      catch(Exception e)
      {
        _logger.error("Updates checker error", e);
      }
    }
    return ret;
  }

  private Version loadCurrentVersion()
  {
    return loadVersionFromProps(Config.getInstance().getParameters());
  }

  private Version loadRemoteVersion()
  {
    Version ret=null;
    TypedProperties props=loadRemoteProperties();
    if (props!=null)
    {
      ret=loadVersionFromProps(props);
    }
    return ret;
  }

  private Version loadVersionFromProps(TypedProperties props)
  {
    int id=props.getIntProperty("current.version.id",0);
    String name=props.getStringProperty("current.version.name",null);
    String url=props.getStringProperty("current.version.url",null);
    if (name!=null)
    {
      return new Version(id,name,url);
    }
    return null;
  }

  private void showInfoWindow(final Version current, final Version remote)
  {
    Runnable r=new Runnable()
    {
      public void run()
      {
        StringBuilder sb=new StringBuilder();
        sb.append("A new version is available (").append(remote.getName()).append(").\n");
        sb.append("Installed version is ").append(current.getName()).append(".\n");
        sb.append("See ").append(remote.getUrl());
        GuiFactory.showInformationDialog(_window,sb.toString(),"Update available");
      }
    };
    SwingUtilities.invokeLater(r);
  }

  private void doCheck()
  {
    Version current=loadCurrentVersion();
    Version remote=loadRemoteVersion();
    if ((current!=null) && (remote!=null))
    {
      int remoteId=remote.getId();
      int currentId=current.getId();
      if (remoteId>currentId)
      {
        _logger.info("New version available: " + remote);
        _logger.info("Old version: " + current);
        showInfoWindow(current,remote);
      }
    }
    // Update the timestamp of the check for updates
    long now=System.currentTimeMillis();
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties props=preferences.getPreferences(UPDATES_PREFERENCES_NAME);
    props.setLongProperty(LAST_UPDATE_CHECK_PROPERTY_NAME,now);
    preferences.savePreferences(props);
  }

  /**
   * Check for updates process.
   */
  public void check()
  {
    // Load the timestamp of the last update
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties props=preferences.getPreferences(UPDATES_PREFERENCES_NAME);
    long last=props.getLongProperty(LAST_UPDATE_CHECK_PROPERTY_NAME,0);
    // Load the check period
    TypedProperties conf=Config.getInstance().getParameters();
    int period=conf.getIntProperty("update.check.period",24);
    // Compute if a new check is necessary
    long nextCheckTimeStamp=last+period*3600*1000;
    long now=System.currentTimeMillis();
    if (now>nextCheckTimeStamp)
    {
      Runnable r=new Runnable()
      {
        public void run()
        {
          doCheck();
        }
      };
      Thread t=new Thread(r,"Update check");
      t.setDaemon(true);
      t.start();
    }
  }

  /**
   * Perform check activites.
   * @param parent Parent window.
   */
  public static void doIt(Window parent)
  {
    new UpdatesChecker(parent).check();
  }
}
