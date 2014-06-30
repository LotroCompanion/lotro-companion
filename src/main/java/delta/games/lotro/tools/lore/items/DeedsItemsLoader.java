package delta.games.lotro.tools.lore.items;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;

import delta.common.utils.environment.FileSystem;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.objects.ObjectItem;
import delta.games.lotro.common.objects.ObjectsSet;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.deeds.index.DeedCategory;
import delta.games.lotro.lore.deeds.index.DeedSummary;
import delta.games.lotro.lore.deeds.index.DeedsIndex;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Loader for the reward items of deeds.
 * @author DAM
 */
public class DeedsItemsLoader
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  private File _workDir;
  private ItemsAndIconsManager _manager;
  private static final String WIKI_SEED="/wiki/";

  /**
   * Constructor.
   */
  public DeedsItemsLoader()
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
          _manager.handleItemKey(itemId);
        }
        String icon=item.getIconURL();
        _manager.handleIcon(icon);
      }
    }
  }

  private void handleDeed(int index, DeedDescription q)
  {
    try
    {
      String key=q.getKey();
      System.out.println("#"+index+", quest: "+key);
      Rewards r=q.getRewards();
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
    DeedsManager qm=DeedsManager.getInstance();
    DeedsIndex index=qm.getIndex();
    if (index!=null)
    {
      String[] categories=index.getCategories();
      System.out.println(Arrays.deepToString(categories));
      for(String category : categories)
      {
        DeedCategory c=index.getCategory(category);
        DeedSummary[] quests=c.getDeeds();
        int indexQ=0;
        for(DeedSummary questSum : quests)
        {
          int id=questSum.getIdentifier();
          DeedDescription q=qm.getDeed(id);
          handleDeed(indexQ,q);
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
    new DeedsItemsLoader().doIt();
  }
}
