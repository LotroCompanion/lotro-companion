package delta.games.lotro.lore.deeds;

import org.apache.log4j.Logger;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.character.log.CharacterLogsManager;
import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.lore.deeds.index.DeedCategory;
import delta.games.lotro.lore.deeds.index.DeedSummary;
import delta.games.lotro.lore.deeds.index.DeedsIndex;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Batch deed parsing from MyLotro.
 * @author DAM
 */
public class MainTestBatchDeedsParsing
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();
  private static final boolean DO_TOON_DEEDS=false;
  private static final boolean DO_INDEX_DEEDS=true;
  private int _totalNumberOfDeeds;
  private int _totalParsedDeeds;

  private void doIt()
  {
    if (DO_TOON_DEEDS)
    {
      CharacterFile toon=new LotroTestUtils().getMainToon();
      loadToonDeeds(toon);
    }
    if (DO_INDEX_DEEDS)
    {
      loadDeeds();
      System.out.println("Total number of deeds: "+_totalNumberOfDeeds);
      System.out.println("Total parsed deeds: "+_totalParsedDeeds);
    }
  }

  private void loadDeeds()
  {
    DeedsManager deedsManager=DeedsManager.getInstance();
    DeedsIndex index=deedsManager.getIndex();
    String[] categories=index.getCategories();
    for(String category : categories)
    {
      loadDeedsForCategory(category);
    }
  }

  private void loadDeedsForCategory(String categoryName)
  {
    System.out.println("Category ["+categoryName+"]");
    DeedsManager deedsManager=DeedsManager.getInstance();
    DeedsIndex index=deedsManager.getIndex();
    DeedCategory category=index.getCategory(categoryName);
    DeedSummary[] summaries=category.getDeeds();
    int nbItems=summaries.length;
    int nbOK=0;
    for(DeedSummary summary : summaries)
    {
      String id=summary.getId();
      _totalNumberOfDeeds++;
      DeedDescription deed=deedsManager.getDeed(id);
      if (deed!=null)
      {
        /*
        String name=summary.getName();
        String title=q.getTitle();
        if (!name.equals(title))
        {
          System.out.println("Warn name=["+name+"], title=["+title+"]!");
        }
        */
        _totalParsedDeeds++;
        nbOK++;
      }
    }
    System.out.println("Category ["+categoryName+"]: got "+nbOK+"/"+nbItems);
  }

  private void loadToonDeeds(CharacterFile toon)
  {
    CharacterLogsManager logManager=new CharacterLogsManager(toon);
    CharacterLog log=logManager.getLastLog();
    if (log!=null)
    {
      DeedsManager qm=DeedsManager.getInstance();
      int nb=log.getNbItems();
      for(int i=0;i<nb;i++)
      {
        CharacterLogItem item=log.getLogItem(i);
        if (item.getLogItemType()==LogItemType.DEED)
        {
          String id=item.getIdentifier();
          _logger.info("Item "+(i+1)+"/"+nb+": "+item.getLabel());
          if ((id!=null) && (id.length()>0))
          {
            DeedDescription q=qm.getDeed(id);
            if (q!=null)
            {
              //System.out.println(q.dump());
            }
            else
            {
              _logger.error("Could not get deed ["+id+"]!");
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
    new MainTestBatchDeedsParsing().doIt();
  }
}
