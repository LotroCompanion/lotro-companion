package delta.games.lotro.gui.character.status.achievables;

import java.util.List;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.io.DeedsStatusIo;
import delta.games.lotro.character.status.achievables.statistics.GlobalAchievablesStatistics;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;

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
        GlobalAchievablesStatistics stats=new GlobalAchievablesStatistics();
        stats.useAchievables(deedsStatus,deeds);
        System.out.println(stats.dumpResults());
      }
    }
  }
}
