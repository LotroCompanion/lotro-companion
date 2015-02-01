package delta.games.lotro.gui.character;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.LotroTestUtils;

/**
 * Test for character main window.
 * @author DAM
 */
public class MainTestCharacterMainWindow
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    //List<CharacterFile> toons=utils.getAllFiles();
    //List<CharacterFile> toons=CharactersManager.getInstance().getAllToons();
    CharacterFile toon=utils.getMainToon();
    //for(CharacterFile toon : toons)
    {
      CharacterMainWindowController controller=new CharacterMainWindowController(toon);
      controller.show();
    }
  }
}
