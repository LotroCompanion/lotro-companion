package delta.games.lotro.character;

import java.io.File;

/**
 * Character file description.
 * @author DAM
 */
public class CharacterFile
{
  private String _name;
  private String _baseMyLotroURL;
  private File _rootDir;

  /**
   * Constructor.
   */
  public CharacterFile()
  {
    _name="";
    _baseMyLotroURL="";
    _rootDir=null;
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
  public void setBaseMyLotroURL(String baseMyLotroURL)
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
}
