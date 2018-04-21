package delta.games.lotro.stats.deeds;

import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.stats.deeds.io.DeedsStatusIo;

/**
 * Simple test class to show deed statistics.
 * @author DAM
 */
public class MainTestDeedStatistics
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    List<DeedDescription> deeds=DeedsManager.getInstance().getAll();
    LotroTestUtils utils=new LotroTestUtils();
    List<CharacterFile> toons=utils.getAllFiles();
    for(CharacterFile toon : toons)
    {
      DeedsStatusManager deedsStatus=DeedsStatusIo.load(toon);
      int nbDeeds=deedsStatus.getAll().size();
      if (nbDeeds>0)
      {
        System.out.println(toon.getName());
        DeedsStatistics stats=new DeedsStatistics();
        stats.useDeeds(deedsStatus,deeds);
        stats.showResults();
      }
    }
  }
}
