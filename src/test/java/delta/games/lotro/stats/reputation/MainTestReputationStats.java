package delta.games.lotro.stats.reputation;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.reputation.ReputationComputer;
import delta.games.lotro.character.reputation.ReputationStatus;

/**
 * Test for reputation stats.
 * @author DAM
 */
public class MainTestReputationStats
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    //List<CharacterFile> toons=utils.getAllFiles();
    //for(CharacterFile toon : toons)
    {
      CharacterFile toon=utils.getMainToon();
      //CharacterFile toon=utils.getToonByName("Glumlug");
      CharacterLog log=toon.getLastCharacterLog();
      if (log!=null)
      {
        ReputationComputer computer=new ReputationComputer();
        ReputationStatus stats=computer.buildReputationStatus(toon);
        stats.dump(System.out);
      }
    }
  }
}
