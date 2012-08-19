package delta.games.lotro.character;

import java.util.List;

import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.gui.character.CharacterMainWindowController;

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
    List<CharacterFile> toons=utils.getAllFiles();
    //CharacterFile toon=utils.getMainToon();
    for(CharacterFile toon : toons)
    {
      CharacterMainWindowController controller=new CharacterMainWindowController(toon);
      controller.show();
    }
  }
}
