package delta.games.lotro.character;

import java.io.File;
import java.util.Date;

import delta.games.lotro.Config;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogsManager;

/**
 * Character file description.
 * @author DAM
 */
public class CharacterFile
{
  private String _name;
  private String _serverName;
  private String _baseMyLotroURL;
  private File _rootDir;
  private CharacterInfosManager _infosManager;
  private CharacterLogsManager _logsManager;

  /**
   * Constructor.
   */
  public CharacterFile()
  {
    _name="";
    _serverName="";
    _baseMyLotroURL="";
    _rootDir=null;
    _infosManager=new CharacterInfosManager(this);
    _logsManager=new CharacterLogsManager(this);
  }

  /**
   * Build a toon file using a toon name and server.
   * @param serverName Server name.
   * @param toonName Toon name.
   * @return A character file.
   */
  public static CharacterFile build(String serverName, String toonName)
  {
    CharacterFile toon=new CharacterFile();
    toon.setName(toonName);
    toon.setServerName(serverName);
    Config config=Config.getInstance();
    File toonDir=config.getToonDirectory(serverName,toonName);
    toon.setRootDir(toonDir);
    String url=config.getCharacterURL(serverName,toonName);
    toon.setBaseMyLotroURL(url);
    return toon;
  }

  /**
   * Get the name of the character file.
   * @return a character file name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Set the name of this file.
   * @param name the name to set.
   */
  public void setName(String name)
  {
    _name=name;
  }

  /**
   * Get the server name of the character file.
   * @return a character file name.
   */
  public String getServerName()
  {
    return _serverName;
  }

  /**
   * Set the server name of this file.
   * @param serverName the name to set.
   */
  public void setServerName(String serverName)
  {
    _serverName=serverName;
  }

  /**
   * Get a unique identifier for this toon file.
   * @return a string identifier.
   */
  public String getIdentifier()
  {
    return _serverName+"#"+_name;
  }

  /**
   * Get the base MyLOTRO URL for this character.
   * @return an URL.
   */
  public String getBaseMyLotroURL()
  {
    return _baseMyLotroURL;
  }

  /**
   * Set the base MyLOTRO URL for this character.
   * @param baseMyLotroURL the URL to set.
   */
  private void setBaseMyLotroURL(String baseMyLotroURL)
  {
    _baseMyLotroURL=baseMyLotroURL;
  }

  /**
   * Get the root directory of the character's file storage. 
   * @return a root directory.
   */
  public File getRootDir()
  {
    return _rootDir;
  }

  /**
   * Set the root directory for this character file.
   * @param rootDir the directory to set.
   */
  public void setRootDir(File rootDir)
  {
    _rootDir=rootDir;
  }

  /**
   * Get the date of the last character update.
   * @return A date or <code>null</code> if there's no log.
   */
  public Date getLastInfoUpdate()
  {
    Date ret=null;
    File lastFile=_infosManager.getLastInfoFile();
    if (lastFile!=null)
    {
      ret=CharacterInfosManager.getDateFromFilename(lastFile.getName());
    }
    return ret;
  }

  /**
   * Get latest character info.
   * @return A character description or <code>null</code> if an error occurs.
   */
  public Character getLastCharacterInfo()
  {
    File lastFile=_infosManager.getLastInfoFile();
    if (lastFile==null)
    {
      _infosManager.updateCharacterDescription();
    }
    Character c=_infosManager.getLastCharacterDescription();
    return c;
  }

  /**
   * Get the date of the last log update.
   * @return A date or <code>null</code> if there's no log.
   */
  public Date getLastLogUpdate()
  {
    Date ret=null;
    File lastFile=_logsManager.getLastLogFile();
    if (lastFile!=null)
    {
      ret=CharacterLogsManager.getDateFromFilename(lastFile.getName());
    }
    return ret;
  }

  /**
   * Get latest character log.
   * @return A character log or <code>null</code> if an error occurs.
   */
  public CharacterLog getLastCharacterLog()
  {
    File lastFile=_logsManager.getLastLogFile();
    if (lastFile==null)
    {
      _logsManager.updateLog();
    }
    CharacterLog log=_logsManager.getLastLog();
    return log;
  }

  /**
   * Get the logs manager.
   * @return the logs manager.
   */
  public CharacterLogsManager getLogsManager()
  {
    return _logsManager;
  }

  /**
   * Indicates if this character has a log file.
   * @return <code>true</code> if it does, <code>false</code> otherwise.
   */
  public boolean hasLog()
  {
    return _logsManager.hasLog();
  }

  @Override
  public String toString()
  {
    return getIdentifier();
  }
}
