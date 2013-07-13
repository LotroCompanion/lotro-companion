package delta.games.lotro.lore.recipes.index.io.web;

import java.io.File;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import delta.common.utils.NumericTools;
import delta.common.utils.environment.FileSystem;
import delta.common.utils.text.TextTools;
import delta.downloads.Downloader;
import delta.games.lotro.lore.recipes.index.RecipesIndex;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for MyLotro.com recipes index JSON source.
 * @author DAM
 */
public class RecipesIndexJSONParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  private static final String URL_TEMPLATE="http://lorebook.lotro.com/index.php?action=ajax&rs=efLotroSearchResultsAjaxWrapper&type=recipe&sEcho=1&iDisplayStart=";
  private static final String WIKI_RECIPE_SEED="/wiki/Recipe:";

  private RecipesIndex _index;
  private int _total;
  private int _currentItemIndex;
  
  /**
   * Constructor.
   */
  public RecipesIndexJSONParser()
  {
  }

  /**
   * Get recipes index from web.
   * @return A recipes index or <code>null</code> if a problem occurs.
   */
  public RecipesIndex parseRecipesIndex()
  {
    RecipesIndex ret;
    try
    {
      _index=new RecipesIndex();
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
      _logger.error("Cannot load recipes index JSON stream at index "+start);
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
      
      // Items:
      // - item:
      // <a href=\"\/wiki\/Item:Apple_and_Cheese_Pie\"><img src=\"http:\/\/content.turbine.com\/sites\/lorebook.lotro.com\/images\/icons\/item\/food\/it_food_apple_cheese_pie.png\" class=\"icon\" rel=\"\" \/><\/a>
      // - ""
      // - item:
      // <a href=\"\/wiki\/Item:Superior_Apple_and_Cheese_Pie\"><img src=\"http:\/\/content.turbine.com\/sites\/lorebook.lotro.com\/images\/icons\/item\/food\/it_c_food_apple_cheese_pie.png\" class=\"icon\" rel=\"\" \/><\/a>
      // - ""
      // - recipe: <a href=\"\/wiki\/Recipe:Apple_and_Cheese_Pie\">Apple and Cheese Pie Recipe<\/a>
      // - profession: <a href=\"\/wiki\/Profession:Cook\">Cook<\/a>
      // - tier (string): "7",
      // usage: "Unlimited"
      for(int i=0;i<nbItems;i++)
      {
        JSONArray item=data.getJSONArray(i);
        String recipeId=null;
        String link=item.getString(4);
        String url=TextTools.findBetween(link,"\"","\"");
        if (url!=null)
        {
          if (url.startsWith(WIKI_RECIPE_SEED))
          {
            recipeId=url.substring(WIKI_RECIPE_SEED.length());
          }
          else
          {
            _logger.error("Error with URL ["+url+"]");
          }
        }
        String recipeName=TextTools.findBetween(link,"\">","</a>");
        String professionLink=item.getString(5);
        String profession=TextTools.findBetween(professionLink,"\">","</a>");
        String tierStr=item.getString(6);
        int tier=NumericTools.parseInt(tierStr,-1);
        if ((recipeId!=null) && (recipeName!=null) && (profession!=null) && (tier!=-1))
        {
          _index.addRecipe(recipeId,recipeName,profession,tier);
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
