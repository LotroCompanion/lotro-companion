package delta.games.lotro.character.log;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Test for character log update.
 * @author DAM
 */
public class MainTestCharacterLogUpdate
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    //CharacterFile toon=utils.getMainToon();
    List<CharacterFile> toons=utils.getAllFiles();
    for(CharacterFile toon : toons)
    {
      String name=toon.getName();
      System.out.println("Updating toon ["+name+"]");
      CharacterLogsManager manager=new CharacterLogsManager(toon);
      boolean ok=manager.updateLog();
      if (ok)
      {
        System.out.println("OK");
        System.out.println("Updating identifiers...");
        Logger logger=LotroLoggers.getCharacterLogLogger();
        logger.setLevel(Level.INFO);
        manager.updateIdentifiers();
        System.out.println("Done!");
      }
    }
  }
}
