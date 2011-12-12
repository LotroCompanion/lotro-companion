package delta.games.lotro.character.log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import delta.games.lotro.character.CharacterFile;

/**
 * Test utilities.
 * @author DAM
 */
public class LotroTestUtils
{
  private static File ROOT_DIR=new File("/home/dm/lotro/characters/elendilmir");

  private HashMap<String,CharacterFile> _files;

  /**
   * Constructor.
   */
  public LotroTestUtils()
  {
    _files=new HashMap<String,CharacterFile>();
    initTestToons();
  }

  private void newCharacterFile(String name, String url)
  {
    CharacterFile file=new CharacterFile();
    file.setName(name);
    file.setBaseMyLotroURL(url);
    File charDir=new File(ROOT_DIR,name);
    file.setRootDir(charDir);
    _files.put(name,file);
  }

  private void initTestToons()
  {
    newCharacterFile("Glumlug","http://my.lotro.com/home/character/2427907/146366987891794854/");
    newCharacterFile("Alphael","http://my.lotro.com/home/character/2427907/146366987891895618/");
    newCharacterFile("Minirdil","http://my.lotro.com/home/character/2427907/146366987891842857/");
    newCharacterFile("Hirthrelthorn","http://my.lotro.com/home/character/2427907/149463212633928354/");
    newCharacterFile("Allurwyn","http://my.lotro.com/home/character/1069125/146366987890743296/");
    newCharacterFile("Beleganth","http://my.lotro.com/home/character/elendilmir/beleganth/");
    newCharacterFile("Allyriel","http://my.lotro.com/home/character/elendilmir/allyriel/");
    newCharacterFile("Feroce","http://my.lotro.com/home/character/elendilmir/feroce/");
    newCharacterFile("Serilis","http://my.lotro.com/home/character/elendilmir/serilis/");
    newCharacterFile("Noctivagant","http://my.lotro.com/home/character/elendilmir/noctivagant/");
    newCharacterFile("Tilmogrim","http://my.lotro.com/home/character/elendilmir/tilmogrim/");
  }

  /**
   * Get all registered files.
   * @return A list of character files, sorted by character name.
   */
  public List<CharacterFile> getAllFiles()
  {
    List<String> names=new ArrayList<String>(_files.keySet());
    Collections.sort(names);
    List<CharacterFile> files=new ArrayList<CharacterFile>();
    for(String name : names)
    {
      files.add(_files.get(name));
    }
    return files;
  }

  /**
   * Get test toon by its name.
   * @param name Name of the toon to get.
   * @return A character file or <code>null</code> if not found.
   */
  public CharacterFile getToonByName(String name)
  {
    return _files.get(name);
  }

  /**
   * Get main test toon. 
   * @return A character file.
   */
  public CharacterFile getMainToon()
  {
    return _files.get("Glumlug");
  }
}
