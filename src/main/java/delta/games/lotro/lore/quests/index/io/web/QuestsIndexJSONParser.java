package delta.games.lotro.lore.quests.index.io.web;

import java.io.File;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import delta.common.utils.environment.FileSystem;
import delta.common.utils.text.TextTools;
import delta.downloads.Downloader;
import delta.games.lotro.lore.quests.index.QuestsIndex;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for MyLotro.com quests index JSON source.
 * @author DAM
 */
public class QuestsIndexJSONParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  private static final String URL_TEMPLATE="http://lorebook.lotro.com/index.php?action=ajax&rs=efLotroSearchResultsAjaxWrapper&type=quest&sEcho=1&iDisplayStart=";
  private static final String WIKI_QUEST_SEED="/wiki/Quest:";

  private QuestsIndex _index;
  private int _total;
  private int _currentItemIndex;
  
  /**
   * Constructor.
   */
  public QuestsIndexJSONParser()
  {
  }

  /**
   * Get quests index from web.
   * @return A quests index or <code>null</code> if a problem occurs.
   */
  public QuestsIndex parseQuestsIndex()
  {
    QuestsIndex ret;
    try
    {
      _index=new QuestsIndex();
      Downloader d=new Downloader();
      while ((_total==0) || (_currentItemIndex<_total))
      {
        load(d,_currentItemIndex);
      }
      ret=_index;
      _index=null;
    }
    catch(Exception e)
    {
      ret=null;
    }
    return ret;
  }

  private void load(Downloader d, int start) throws Exception
  {
    File tmpDir=FileSystem.getTmpDir();
    File tmpFile=new File(tmpDir,"json.txt");
    try
    {
      String url=URL_TEMPLATE+start;
      String page=d.downloadString(url);
      parsePage(page);
    }
    catch(Exception e)
    {
      _logger.error("Cannot load quests index JSON stream at index "+start);
    }
    finally
    {
      if (tmpFile.exists())
      {
        tmpFile.delete();
      }
    }
  }

  private void parsePage(String page) throws Exception
  {
    try
    {
      JSONObject o=new JSONObject(page);
      _total=o.getInt("iTotalDisplayRecords");
      JSONArray data=o.getJSONArray("aaData");
      int nbItems=data.length();
      for(int i=0;i<nbItems;i++)
      {
        JSONArray item=data.getJSONArray(i);
        String questId=null;
        String link=item.getString(1);
        String url=TextTools.findBetween(link,"\"","\"");
        if (url!=null)
        {
          if (url.startsWith(WIKI_QUEST_SEED))
          {
            questId=url.substring(WIKI_QUEST_SEED.length());
          }
          else
          {
            _logger.error("Error with URL ["+url+"]");
          }
        }
        String questName=TextTools.findBetween(link,"\">","</a>");
        String category=item.getString(2);
        if ((questId!=null) && (questName!=null) && (category!=null))
        {
          _index.addQuest(category,0,questId,questName);
        }
      }
      _currentItemIndex+=nbItems;
    }
    catch(Exception e)
    {
      _logger.error("Error when parsing JSON file!",e);
    }
  }
}
