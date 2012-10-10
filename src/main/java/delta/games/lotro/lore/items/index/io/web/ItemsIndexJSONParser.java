package delta.games.lotro.lore.items.index.io.web;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import delta.common.utils.text.TextTools;
import delta.downloads.Downloader;
import delta.games.lotro.lore.items.index.ItemsIndex;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for MyLotro.com items index JSON source.
 * @author DAM
 */
public class ItemsIndexJSONParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  private static final String URL_TEMPLATE="http://lorebook.lotro.com/index.php?action=ajax&rs=efLotroSearchResultsAjaxWrapper&type=item&sEcho=1&iDisplayStart=";
  // item_type: w(5911), a(20609), j(4250), r (3182), c(1262), d(489), i(248),
  // w: icon, rarity, Name   Type  Slot  Item Lvl  Req Lvl   Armour
  // &item_type%5B%5D=w
  
  private static final String WIKI_SEED="/wiki/";

  private ItemsIndex _index;
  private int _total;
  private int _currentItemIndex;
  
  /**
   * Constructor.
   */
  public ItemsIndexJSONParser()
  {
  }

  /**
   * Get items index from web.
   * @return An items index or <code>null</code> if a problem occurs.
   */
  public ItemsIndex parseItemsIndex()
  {
    ItemsIndex ret;
    try
    {
      _index=new ItemsIndex();
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
      _logger.error("Cannot load items index JSON stream at index "+start);
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
        String itemId=null;
        String link=item.getString(0);
        String url=TextTools.findBetween(link,"\"","\"");
        if (url!=null)
        {
          if (url.startsWith(WIKI_SEED))
          {
            itemId=url.substring(WIKI_SEED.length());
          }
          else
          {
            _logger.error("Error with URL ["+url+"]");
          }
        }
        String link2=item.getString(2);
        String itemName=TextTools.findBetween(link2,"\">","</a>");
        String category=item.getString(3);
        if ((itemId!=null) && (itemName!=null) && (category!=null))
        {
          boolean ok=_index.addItem(category,itemId,itemName);
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
