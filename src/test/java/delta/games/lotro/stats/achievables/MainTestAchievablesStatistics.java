package delta.games.lotro.stats.achievables;

import java.util.List;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.achievables.AchievablesStatusManager;
import delta.games.lotro.character.achievables.io.DeedsStatusIo;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.stats.achievables.AchievablesStatistics;

/**
 * Simple test class to show achievable statistics.
 * @author DAM
 */
public class MainTestAchievablesStatistics
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
      AchievablesStatusManager deedsStatus=DeedsStatusIo.load(toon);
      int nbDeeds=deedsStatus.getAll().size();
      if (nbDeeds>0)
      {
        System.out.println(toon.getName());
        AchievablesStatistics stats=new AchievablesStatistics();
        stats.useAchievables(deedsStatus,deeds);
        stats.showResults();
      }
    }
  }
}
