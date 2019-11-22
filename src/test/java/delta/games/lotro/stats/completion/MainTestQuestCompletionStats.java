package delta.games.lotro.stats.completion;

import java.util.List;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.lore.quests.QuestsManager;

/**
 * Test for quest completion stats.
 * @author DAM
 */
public class MainTestQuestCompletionStats
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    //CharacterFile mainToon=utils.getMainToon();
    CharacterFile mainToon=utils.getToonByName("Feroce");
    CharacterSummary summary=mainToon.getSummary();
    CharacterLog log=mainToon.getLastCharacterLog();
    if (log!=null)
    {
      QuestsManager qm=QuestsManager.getInstance();
      List<String> categories=qm.getCategories();
      for(String category : categories)
      {
        //String category="Epic - Vol. I, Book 1: Stirrings in the Darkness";
        QuestsCompletionStats stats=new QuestsCompletionStats(category,summary,log);
        stats.dump(System.out,true);
      }
    }
  }
}
