package delta.games.lotro.lore.deeds.index.io.web;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import delta.common.utils.text.TextTools;
import delta.downloads.Downloader;
import delta.games.lotro.lore.deeds.index.DeedsIndex;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for MyLotro.com deeds index JSON source.
 * @author DAM
 */
public class DeedsIndexJSONParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  private static final String URL_TEMPLATE="http://lorebook.lotro.com/index.php?action=ajax&rs=efLotroSearchResultsAjaxWrapper&type=deed&sEcho=1&iDisplayStart=";
  private static final String WIKI_DEED_SEED="/wiki/Deed:";

  private DeedsIndex _index;
  private int _total;
  private int _currentItemIndex;
  
  /**
   * Constructor.
   */
  public DeedsIndexJSONParser()
  {
  }

  /**
   * Get deeds index from web.
   * @return A deeds index or <code>null</code> if a problem occurs.
   */
  public DeedsIndex parseDeedsIndex()
  {
    DeedsIndex ret;
    try
    {
      _index=new DeedsIndex();
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
        _logger.warn("Number of duplicated deeds:"+totalNbDuplicates);
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
    try
    {
      String url=URL_TEMPLATE+start;
      String page=d.downloadString(url);
      nbDuplicates=parsePage(page);
    }
    catch(Exception e)
    {
      _logger.error("Cannot load deeds index JSON stream at index "+start);
    }
    return nbDuplicates;
  }

  private int parsePage(String page) throws Exception
  {
    int nbDuplicates=0;
    try
    {
      JSONObject o=new JSONObject(page);
      _total=o.getInt("iTotalDisplayRecords");
      //System.out.println("Total: "+_total);
      JSONArray data=o.getJSONArray("aaData");
      int nbItems=data.length();
      for(int i=0;i<nbItems;i++)
      {
        JSONArray item=data.getJSONArray(i);
        String deedId=null;
        String link=item.getString(0);
        String url=TextTools.findBetween(link,"\"","\"");
        if (url!=null)
        {
          if (url.startsWith(WIKI_DEED_SEED))
          {
            deedId=url.substring(WIKI_DEED_SEED.length());
          }
          else
          {
            _logger.error("Error with URL ["+url+"]");
          }
        }
        String deedName=TextTools.findBetween(link,"\">","</a>");
        //System.out.println(deedId);
        //System.out.println(deedName);
        //String text=item.getString(1);
        //String level=item.getString(2);
        //System.out.println(text);
        //System.out.println(level);
        String category="???";
        if ((deedId!=null) && (deedName!=null) && (category!=null))
        {
          boolean ok=_index.addDeed(category,deedId,deedName);
          if (!ok) nbDuplicates++;
        }
      }
      _currentItemIndex+=nbItems;
    }
    catch(Exception e)
    {
      _logger.error("Error when parsing JSON file!",e);
    }
    return nbDuplicates;
  }
}
