package delta.games.lotro.stats.completion;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.lore.quests.QuestsManager;
import delta.games.lotro.lore.quests.index.QuestsIndex;

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
    CharacterData c=mainToon.getLastCharacterInfo();
    CharacterLog log=mainToon.getLastCharacterLog();
    if (log!=null)
    {
      QuestsManager qm=QuestsManager.getInstance();
      QuestsIndex index=qm.getIndex();
      String[] categories=index.getCategories();
      for(String category : categories)
      {
        //String category="Epic - Vol. I, Book 1: Stirrings in the Darkness";
        QuestsCompletionStats stats=new QuestsCompletionStats(category,c,log);
        stats.dump(System.out,true);
      }
    }
  }
}
