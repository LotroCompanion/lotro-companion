package delta.games.lotro.character.log;

import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.io.web.CharacterLogPageParser;

/**
 * Test for character log parsing.
 * @author DAM
 */
public class MainTestCharacterActivityLogIO
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    List<CharacterFile> toons=utils.getAllFiles();

    CharacterLogPageParser parser=new CharacterLogPageParser();
    for(CharacterFile toon : toons)
    {
      String url=toon.getBaseMyLotroURL();
      CharacterLog log=parser.parseLogPages(url,null);
      if (log!=null)
      {
        System.out.println(log);
        CharacterLogsManager manager=new CharacterLogsManager(toon);
        boolean ok=manager.writeNewLog(log);
        if (ok)
        {
          System.out.println("OK");
        }
      }
    }
  }
}
