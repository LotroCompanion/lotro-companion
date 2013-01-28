package delta.games.lotro.character;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import delta.common.utils.files.FilesDeleter;
import delta.common.utils.files.filter.FileTypePredicate;
import delta.games.lotro.Config;

/**
 * Manages the characters of a single server.
 * @author DAM
 */
public class ServerCharactersManager
{
  private HashMap<String,CharacterFile> _toons;

  private File _serverDir;
  private String _serverName;

  /**
   * Constructor.
   * @param serverName Name of managed server.
   */
  public ServerCharactersManager(String serverName)
  {
    _serverName=serverName;
    _toons=new HashMap<String,CharacterFile>();
    init();
  }

  private void init()
  {
    Config cfg=Config.getInstance();
    File toonsDir=cfg.getToonsDir();
    _serverDir=new File(toonsDir,_serverName);
    FileFilter fileFilter=new FileTypePredicate(FileTypePredicate.DIRECTORY);
    File[] toonDirs=_serverDir.listFiles(fileFilter);
    if (toonDirs!=null)
    {
      for(File toonDir : toonDirs)
      {
        String toonName=toonDir.getName();
        File[] toonFiles=toonDir.listFiles();
        if ((toonFiles!=null) && (toonFiles.length>0))
        {
          initToon(toonName);
        }
        else
        {
          removeToon(toonName);
        }
      }
    }
  }
  
  private CharacterFile initToon(String toonName)
  {
    CharacterFile toon=CharacterFile.build(_serverName,toonName);
    _toons.put(toonName,toon);
    return toon;
  }

  /**
   * Get a list of all managed toons, sorted by name.
   * @return a list of toons.
   */
  public List<CharacterFile> getAllToons()
  {
    List<CharacterFile> toons=new ArrayList<CharacterFile>();
    List<String> toonNames=new ArrayList<String>(_toons.keySet());
    Collections.sort(toonNames);
    for(String toonName : toonNames)
    {
      CharacterFile toon=_toons.get(toonName);
      toons.add(toon);
    }
    return toons;
  }

  /**
   * Get a toon using its name.
   * @param toonName Name of the toon to get.
   * @return A toon or <code>null</code> if not found.
   */
  public CharacterFile getToonByName(String toonName)
  {
    CharacterFile ret=_toons.get(toonName);
    return ret;
  }

  /**
   * Add a new toon in this server.
   * @param toonName Toon to add.
   * @return The character file for the newly created toon, or <code>null</code>
   * if a problem occurs or if the toon already exists.
   */
  public CharacterFile addToon(String toonName)
  {
    CharacterFile ret=null;
    CharacterFile old=_toons.get(toonName);
    if (old==null)
    {
      ret=initToon(toonName);
      File dir=ret.getRootDir();
      boolean ok=(dir.exists()?true:dir.mkdirs());
      if (ok)
      {
        _toons.put(toonName,ret);
      }
      else
      {
        ret=null;
      }
    }
    return ret;
  }

  /**
   * Remove a toon from this server.
   * It removes all data for this toon.
   * @param toonName Name of toon to renames.
   */
  public void removeToon(String toonName)
  {
    _toons.remove(toonName);
    Config config=Config.getInstance();
    File toonDir=config.getToonDirectory(_serverName,toonName);
    if (toonDir.exists())
    {
      FilesDeleter deleter=new FilesDeleter(toonDir,null,true);
      deleter.doIt();
    }
  }
}
