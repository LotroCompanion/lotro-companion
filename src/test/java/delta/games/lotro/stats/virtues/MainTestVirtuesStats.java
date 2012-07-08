package delta.games.lotro.stats.virtues;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.LotroTestUtils;

/**
 * Test for virtues stats.
 * @author DAM
 */
public class MainTestVirtuesStats
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    CharacterFile mainToon=utils.getMainToon();
    //CharacterFile mainToon=utils.getToonByName("Feroce");
    CharacterLog log=mainToon.getLastCharacterLog();
    if (log!=null)
    {
      VirtuesStats stats=new VirtuesStats(log);
      stats.dump(System.out,true);
    }
  }
}
