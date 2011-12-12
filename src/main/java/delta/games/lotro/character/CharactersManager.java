package delta.games.lotro.character;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import delta.common.utils.files.filter.FileTypePredicate;
import delta.games.lotro.Config;

/**
 * @author DAM
 */
public class CharactersManager
{
  private static CharactersManager _instance=new CharactersManager();
  
  private HashMap<String,ServerCharactersManager> _servers;

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static CharactersManager getInstance()
  {
    return _instance;
  }

  /**
   * Private constructor.
   */
  private CharactersManager()
  {
    _servers=new HashMap<String,ServerCharactersManager>();
    init();
  }

  private void init()
  {
    Config cfg=Config.getInstance();
    File toonsDir=cfg.getToonsDir();
    FileFilter fileFilter=new FileTypePredicate(FileTypePredicate.DIRECTORY);
    File[] serverDirs=toonsDir.listFiles(fileFilter);
    if (serverDirs!=null)
    {
      for(File serverDir : serverDirs)
      {
        String serverName=serverDir.getName();
        ServerCharactersManager manager=new ServerCharactersManager(serverName);
        _servers.put(serverName,manager);
      }
    }
  }

  /**
   * Get a list of all managed toons, sorted by name.
   * @return a list of toons.
   */
  public List<CharacterFile> getAllToons()
  {
    List<CharacterFile> toons=new ArrayList<CharacterFile>();
    List<String> serverNames=new ArrayList<String>(_servers.keySet());
    Collections.sort(serverNames);
    for(String serverName : serverNames)
    {
      ServerCharactersManager manager=_servers.get(serverName);
      List<CharacterFile> toonsForServer=manager.getAllToons();
      toons.addAll(toonsForServer);
    }
    return toons;
  }

  /**
   * Add a new toon.
   * @param serverName Server name.
   * @param toonName Toon name.
   * @return A character file or <code>null</code> if an errors occurs.
   */
  public CharacterFile addToon(String serverName, String toonName)
  {
    ServerCharactersManager server=_servers.get(serverName);
    if (server==null)
    {
      server=new ServerCharactersManager(serverName);
    }
    CharacterFile toon=server.addToon(toonName);
    if (toon!=null)
    {
      ServerCharactersManager tmp=_servers.get(serverName);
      if (tmp==null)
      {
        _servers.put(serverName,server);
      }
    }
    return toon;
  }
}
