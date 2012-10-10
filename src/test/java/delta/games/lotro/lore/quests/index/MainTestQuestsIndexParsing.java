package delta.games.lotro.lore.quests.index;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import delta.games.lotro.lore.quests.QuestsManager;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Quests index parsing from XML.
 * @author DAM
 */
public class MainTestQuestsIndexParsing
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    QuestsManager qm=QuestsManager.getInstance();
    QuestsIndex index=qm.getIndex();
    if (index!=null)
    {
      int nbItems=0;
      Set<String> keys=new HashSet<String>();
      String[] categories=index.getCategories();
      System.out.println(Arrays.deepToString(categories));
      for(String category : categories)
      {
        QuestCategory c=index.getCategory(category);
        QuestSummary[] quests=c.getQuests();
        nbItems+=quests.length;
        System.out.println(Arrays.deepToString(quests));
        for(QuestSummary summary : quests)
        {
          String id=summary.getKey();
          if (keys.contains(id))
          {
            System.out.println("Duplicate: "+id);
          }
          else
          {
            keys.add(id);
          }
        }
      }
      System.out.println("Categories: "+categories.length);
      System.out.println("Quests: "+nbItems);
    }
    else
    {
      _logger.error("Cannot gets quests index file!");
    }
  }
}
