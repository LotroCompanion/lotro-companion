package delta.games.lotro.quests.index.io.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import delta.common.utils.environment.FileSystem;
import delta.common.utils.io.StreamTools;
import delta.common.utils.text.TextTools;
import delta.downloads.Downloader;
import delta.games.lotro.quests.index.QuestsIndex;
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
      int totalNbDuplicates=0;
      while ((_total==0) || (_currentItemIndex<_total))
      {
        int nbDuplicates=load(d,_currentItemIndex);
        totalNbDuplicates+=nbDuplicates;
      }
      ret=_index;
      _index=null;
      if (totalNbDuplicates>0)
      {
        _logger.warn("Number of duplicated quests:"+totalNbDuplicates);
      }
    }
    catch(Exception e)
    {
      ret=null;
    }
    return ret;
  }

  private int load(Downloader d, int start) throws Exception
  {
    int nbDuplicates=0;
    File tmpDir=FileSystem.getTmpDir();
    File tmpFile=new File(tmpDir,"json.txt");
    try
    {
      String url=URL_TEMPLATE+start;
      d.downloadPage(url,tmpFile);
      nbDuplicates=parseFile(tmpFile);
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
    return nbDuplicates;
  }

  private int parseFile(File file) throws Exception
  {
    int nbDuplicates=0;
    String all=null;
    InputStream is=null;
    InputStreamReader r=null;
    BufferedReader br=null;
    try
    {
      is=new FileInputStream(file);
      r=new InputStreamReader(is,"ASCII");
      br=new BufferedReader(r);
      StringBuilder sb=new StringBuilder();
      while (true)
      {
        String line=br.readLine();
        if (line==null) break;
        sb.append(line);
        sb.append("\n");
      }
      all=sb.toString();
      JSONObject o=new JSONObject(all);
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
          boolean ok=_index.addQuest(category,questId,questName);
          if (!ok) nbDuplicates++;
        }
      }
      _currentItemIndex+=nbItems;
    }
    catch(Exception e)
    {
      _logger.error("Error when parsing JSON file!",e);
    }
    finally
    {
      StreamTools.close(br);
      StreamTools.close(r);
      StreamTools.close(is);
    }
    return nbDuplicates;
  }
}
