package delta.games.lotro.character;

import java.util.List;

import delta.games.lotro.character.log.LotroTestUtils;

/**
 * Test for character description parsing.
 * @author DAM
 */
public class MainTestCharacterParsing
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    List<CharacterFile> toons=utils.getAllFiles();
    for(CharacterFile toon : toons)
    {
      String name=toon.getName();
      System.out.println("Updating toon ["+name+"]");
      CharacterInfosManager manager=new CharacterInfosManager(toon);
      boolean ok=manager.updateCharacterDescription();
      if (ok)
      {
        System.out.println("OK");
      }
    }
  }
}
