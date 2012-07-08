package delta.games.lotro.lore.deeds;

import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.io.web.DeedPageParser;

/**
 * Test for quest description parsing.
 * @author DAM
 */
public class MainTestDeedParsing
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    String url0="http://lorebook.lotro.com/wiki/Deed:Allies_of_the_King";
    String url1="http://lorebook.lotro.com/wiki/Deed:Ale_Association_Friend";
    String url2="http://lorebook.lotro.com/wiki/Deed:A_Shot_in_the_Dark"; // Trait
    String url3="http://lorebook.lotro.com/wiki/Deed:Ancient_Stones_of_Forochel"; // Virtue
    String url4="http://lorebook.lotro.com/wiki/Deed:Ale_Association_Initiation"; // Title+Reputation
    String url5="http://lorebook.lotro.com/wiki/Deed:Hero"; // Emote
    String[] urls={ url0, url1, url2, url3, url4, url5 };
    //String[] urls={ url5 };
    DeedPageParser parser=new DeedPageParser();
    for(String url : urls)
    {
      DeedDescription deed=parser.parseDeedPage(url);
      if (deed!=null)
      {
        System.out.println(deed.dump());
      }
      else
      {
        System.out.println("Deed ["+url+"] is null!");
      }
    }
  }
}
