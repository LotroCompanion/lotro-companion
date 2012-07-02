package delta.games.lotro.items;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.items.io.web.ItemPageParser;

/**
 * Test for item description parsing.
 * @author DAM
 */
public class MainTestItemParsing
{
  private String[] getTestURLs()
  {
    String url0="http://lorebook.lotro.com/wiki/Tool:Superior_Tools_of_the_Explorer";
    String url1="http://lorebook.lotro.com/wiki/Item:Glossy_Eye";
    String url2="http://lorebook.lotro.com/wiki/Armour:Jacket_of_the_Impossible_Shot";
    String url3="http://lorebook.lotro.com/wiki/Item:Bright_Lamp";
    String url4="http://lorebook.lotro.com/wiki/Item:Westfold_Tome_of_the_Wind-rider";
    String url5="http://lorebook.lotro.com/wiki/Special:LotroResource?id=1879219223";
    String[] urls={ url0, url1, url2, url3, url4, url5 };
    //String[] urls={ url2 };
    return urls;
  }

  /**
   * Parse some items.
   * @return A list of items.
   */
  public List<Item> parseItems()
  {
    List<Item> ret=new ArrayList<Item>();
    ItemPageParser parser=new ItemPageParser();
    String[] urls=getTestURLs();
    for(String url : urls)
    {
      Item item=parser.parseItemPage(url);
      ret.add(item);
    }
    return ret;
  }

  private void doIt()
  {
    String[] urls=getTestURLs();
    List<Item> items=parseItems();
    int index=0;
    for(Item item : items)
    {
      if (item!=null)
      {
        System.out.println(item.dump());
      }
      else
      {
        System.out.println("Item ["+urls[index]+"] is null!");
      }
      index++;
    }
  }

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestItemParsing().doIt();
  }
}
