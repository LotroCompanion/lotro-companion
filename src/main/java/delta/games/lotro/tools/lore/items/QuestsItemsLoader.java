package delta.games.lotro.tools.lore.items;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import delta.common.utils.environment.FileSystem;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.objects.ObjectItem;
import delta.games.lotro.common.objects.ObjectsSet;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;
import delta.games.lotro.lore.quests.index.QuestCategory;
import delta.games.lotro.lore.quests.index.QuestSummary;
import delta.games.lotro.lore.quests.index.QuestsIndex;
import delta.games.lotro.utils.LotroLoggers;

/**
 * @author DAM
 */
public class QuestsItemsLoader
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  private File _workDir;
  private ItemsAndIconsManager _manager;
  private static final String WIKI_SEED="/wiki/";

  public QuestsItemsLoader()
  {
    _workDir=FileSystem.getTmpDir();
    _manager=new ItemsAndIconsManager(_workDir);
  }

  private void handleSet(ObjectsSet set)
  {
    if (set!=null)
    {
      int nb=set.getNbObjectItems();
      for(int i=0;i<nb;i++)
      {
        ObjectItem item=set.getItem(i);
        String url=item.getObjectURL();
        if ((url!=null) && (url.startsWith(WIKI_SEED)))
        {
          String itemId=url.substring(WIKI_SEED.length());
          _manager.handleItemId(itemId);
        }
        String icon=item.getIconURL();
        _manager.handleIcon(icon);
      }
    }
  }

  private void handleQuest(int index, QuestDescription q)
  {
    try
    {
      String key=q.getKey();
      System.out.println("#"+index+", quest: "+key);
      Rewards r=q.getQuestRewards();
      ObjectsSet set=r.getObjects();
      handleSet(set);
      ObjectsSet set2=r.getSelectObjects();
      handleSet(set2);
    }
    catch(Throwable t)
    {
      t.printStackTrace();
    }
  }
  
  private void doIt()
  {
    _manager.loadMaps();
    QuestsManager qm=QuestsManager.getInstance();
    QuestsIndex index=qm.getIndex();
    if (index!=null)
    {
      String[] categories=index.getCategories();
      System.out.println(Arrays.deepToString(categories));
      for(String category : categories)
      {
        QuestCategory c=index.getCategory(category);
        QuestSummary[] quests=c.getQuests();
        int indexQ=0;
        for(QuestSummary questSum : quests)
        {
          int id=questSum.getIdentifier();
          QuestDescription q=qm.getQuest(id);
          handleQuest(indexQ,q);
          indexQ++;
          if (indexQ%10==0) _manager.saveMaps();
        }
      }
    }
    else
    {
      _logger.error("Cannot gets quests index file!");
    }
  }

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new QuestsItemsLoader().doIt();
  }
}
