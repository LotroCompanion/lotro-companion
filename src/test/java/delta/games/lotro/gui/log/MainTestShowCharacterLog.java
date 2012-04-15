package delta.games.lotro.gui.log;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.LotroTestUtils;

/**
 * Test for character log window.
 * @author DAM
 */
public class MainTestShowCharacterLog
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    CharacterFile toon=utils.getToonByName("Kargarth");
    CharacterLog log=toon.getLastCharacterLog();
    if (log!=null)
    {
      CharacterLogWindowController controller=new CharacterLogWindowController(toon);
      controller.show();
    }
  }
}
