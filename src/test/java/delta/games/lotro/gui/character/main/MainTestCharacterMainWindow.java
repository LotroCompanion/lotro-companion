package delta.games.lotro.gui.character.main;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;

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
    CharacterFile toon=utils.getMainToon();
    {
      CharacterFileWindowController controller=new CharacterFileWindowController(toon);
      controller.show();
    }
  }
}
