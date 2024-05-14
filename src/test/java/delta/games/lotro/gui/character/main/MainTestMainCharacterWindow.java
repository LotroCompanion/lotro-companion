package delta.games.lotro.gui.character.main;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;

/**
 * Test for character main window.
 * @author DAM
 */
public class MainTestMainCharacterWindow
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    CharacterFile toon=utils.getMainToon();
    {
      MainCharacterWindowController controller=new MainCharacterWindowController(toon);
      controller.show();
    }
  }
}
