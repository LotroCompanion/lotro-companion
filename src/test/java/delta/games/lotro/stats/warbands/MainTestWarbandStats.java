package delta.games.lotro.stats.warbands;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;

/**
 * Test for warband stats.
 * @author DAM
 */
public class MainTestWarbandStats
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    CharacterFile toon=utils.getToonByName("Allyriel");
    CharacterLog log=toon.getLastCharacterLog();
    if (log!=null)
    {
      WarbandsStats stats=new WarbandsStats(log);
      stats.dump(System.out);
    }
  }
}
