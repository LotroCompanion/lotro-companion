package delta.games.lotro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration.
 * @author DAM
 */
public class Config
{
  private static Config _instance=new Config();
  
  private String _myLotroRootURL;
  private File _rootDataDir;
  private File _toonsDir;
  private File _questsDir;
  private List<String> _servers;

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static Config getInstance()
  {
    return _instance;
  }

  /**
   * Private constructor.
   */
  private Config()
  {
    _myLotroRootURL="http://my.lotro.com/";
    _rootDataDir=new File("data");
    _toonsDir=new File(_rootDataDir,"characters");
    _servers=new ArrayList<String>();
    _servers.add("Elendilmir");
    _servers.add("Riddermark");
    _questsDir=new File(_rootDataDir,"quests");
  }

  /**
   * Get the root directory for quest data storage.
   * @return a directory.
   */
  public File getQuestsDir()
  {
    return _questsDir;
  }

  /**
   * Get the root storage directory for toons.
   * @return a directory.
   */
  public File getToonsDir()
  {
    return _toonsDir;
  }

  /**
   * Get the root storage directory for a toon.
   * @param serverName Server of toon.
   * @param toonName Name of toon.
   * @return a directory.
   */
  public File getToonDirectory(String serverName, String toonName)
  {
    File serverDir=new File(_toonsDir,serverName);
    File toonDir=new File(serverDir,toonName);
    return toonDir;
  }

  /**
   * Get the URL for a toon.
   * @param serverName Server of toon.
   * @param toonName Name of toon.
   * @return An URL.
   */
  public String getCharacterURL(String serverName, String toonName)
  {
    String ret=_myLotroRootURL+"home/character/"+serverName.toLowerCase()+"/"+toonName.toLowerCase();
    return ret;
  }

  /**
   * Get a list of known server names.
   * @return a list of known server names.
   */
  public List<String> getServerNames()
  {
    return _servers;
  }
}
