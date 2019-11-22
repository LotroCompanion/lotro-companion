package delta.games.lotro.gui.log;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;

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
    CharacterFile toon=utils.getToonByName("Glumlug");
    CharacterLog log=toon.getLastCharacterLog();
    if (log!=null)
    {
      CharacterLogWindowController controller=new CharacterLogWindowController(toon);
      controller.show();
    }
  }
}
