package delta.games.lotro.quests;

import org.apache.log4j.Logger;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.character.log.CharacterLogsManager;
import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.quests.index.QuestCategory;
import delta.games.lotro.quests.index.QuestSummary;
import delta.games.lotro.quests.index.QuestsIndex;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Batch quest parsing from MyLotro.
 * @author DAM
 */
public class MainTestBatchQuestsParsing
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();
  private static final boolean DO_TOON_QUESTS=false;
  private static final boolean DO_INDEX_QUESTS=true;
  private int _totalNumberOfQuests;
  private int _totalParsedQuests;

  private void doIt()
  {
    if (DO_TOON_QUESTS)
    {
      CharacterFile toon=new LotroTestUtils().getMainToon();
      loadToonQuests(toon);
    }
    if (DO_INDEX_QUESTS)
    {
      loadQuests();
      System.out.println("Total number of quests: "+_totalNumberOfQuests);
      System.out.println("Total parsed quests: "+_totalParsedQuests);
    }
  }

  private void loadQuests()
  {
    QuestsManager qm=QuestsManager.getInstance();
    QuestsIndex index=qm.getIndex();
    String[] categories=index.getCategories();
    for(String category : categories)
    {
      loadQuestsForCategory(category);
    }
  }

  private void loadQuestsForCategory(String categoryName)
  {
    System.out.println("Category ["+categoryName+"]");
    QuestsManager qm=QuestsManager.getInstance();
    QuestsIndex index=qm.getIndex();
    QuestCategory category=index.getCategory(categoryName);
    QuestSummary[] summaries=category.getQuests();
    int nbItems=summaries.length;
    int nbOK=0;
    for(QuestSummary summary : summaries)
    {
      String id=summary.getId();
      _totalNumberOfQuests++;
      QuestDescription q=qm.getQuest(id);
      if (q!=null)
      {
        /*
        String name=summary.getName();
        String title=q.getTitle();
        if (!name.equals(title))
        {
          System.out.println("Warn name=["+name+"], title=["+title+"]!");
        }
        */
        _totalParsedQuests++;
        nbOK++;
      }
    }
    System.out.println("Category ["+categoryName+"]: got "+nbOK+"/"+nbItems);
  }

  private void loadToonQuests(CharacterFile toon)
  {
    CharacterLogsManager logManager=new CharacterLogsManager(toon);
    CharacterLog log=logManager.getLastLog();
    if (log!=null)
    {
      QuestsManager qm=QuestsManager.getInstance();
      int nb=log.getNbItems();
      for(int i=0;i<nb;i++)
      {
        CharacterLogItem item=log.getLogItem(i);
        if (item.getLogItemType()==LogItemType.QUEST)
        {
          String id=item.getIdentifier();
          _logger.info("Item "+(i+1)+"/"+nb+": "+item.getLabel());
          if ((id!=null) && (id.length()>0))
          {
            QuestDescription q=qm.getQuest(id);
            if (q!=null)
            {
              //System.out.println(q.dump());
            }
            else
            {
              _logger.error("Could not get quest ["+id+"]!");
            }
          }
          else
          {
            _logger.info("Identifier is not set for this item!");
          }
        }
      }
    }
  }

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    //_logger.setLevel(Level.INFO);
    new MainTestBatchQuestsParsing().doIt();
  }
}
