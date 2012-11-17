package delta.games.lotro;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import delta.common.utils.text.EncodingNames;
import delta.common.utils.text.TextUtils;
import delta.games.lotro.utils.TypedProperties;

/**
 * Configuration.
 * @author DAM
 */
public class Config
{
  private static Config _instance=new Config();
  
  private String _myLotroRootURL;
  private File _rootDataDir;
  private File _configDir;
  private File _toonsDir;
  private File _loreDir;
  private File _indexesDir;
  private File _questsDir;
  private File _deedsDir;
  private File _iconsDir;
  private File _itemsDir;
  private TypedProperties _parameters;
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
    _configDir=new File(_rootDataDir,"config");
    _toonsDir=new File(_rootDataDir,"characters");
    _loreDir=new File(_rootDataDir,"lore");
    _indexesDir=new File(_loreDir,"indexes");
    _servers=new ArrayList<String>();
    File parametersFiles=new File(_configDir,"params.txt");
    _parameters=new TypedProperties();
    _parameters.loadFromFile(parametersFiles);
    loadServers();
    _questsDir=new File(_rootDataDir,"quests");
    _deedsDir=new File(_rootDataDir,"deeds");
    _iconsDir=new File(_rootDataDir,"icons");
    _itemsDir=new File(_rootDataDir,"items");
  }

  /**
   * Get the root directory for indexes storage.
   * @return a directory.
   */
  public File getIndexesDir()
  {
    return _indexesDir;
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
   * Get the root directory for lore data storage.
   * @return a directory.
   */
  public File getLoreDir()
  {
    return _loreDir;
  }

  /**
   * Get the root directory for deed data storage.
   * @return a directory.
   */
  public File getDeedsDir()
  {
    return _deedsDir;
  }

  /**
   * Get the root directory for icons data storage.
   * @return a directory.
   */
  public File getIconsDir()
  {
    return _iconsDir;
  }

  /**
   * Get the root directory for items data storage.
   * @return a directory.
   */
  public File getItemsDir()
  {
    return _itemsDir;
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
   * Get the root storage directory for configuration files.
   * @return a directory.
   */
  public File getConfigDir()
  {
    return _configDir;
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
   * Get the configuration parameters.
   * @return the configuration parameters.
   */
  public TypedProperties getParameters()
  {
    return _parameters;
  }

  /**
   * Get a list of known server names.
   * @return a list of known server names.
   */
  public List<String> getServerNames()
  {
    return _servers;
  }

  private void loadServers()
  {
    File serversFiles=new File(_configDir,"servers.txt"); 
    List<String> servers=TextUtils.readAsLines(serversFiles,EncodingNames.UTF_8);
    if (servers!=null)
    {
      Collections.sort(servers);
      _servers.addAll(servers);
    }
  }
}
