package delta.games.lotro.lore.quests;

import java.util.List;

import delta.games.lotro.lore.quests.io.web.QuestPageParser;

/**
 * Test for quest description parsing.
 * @author DAM
 */
public class MainTestQuestParsing
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    String url0="http://lorebook.lotro.com/wiki/Quest:Disrupting_the_Ritual";
    String url1="http://lorebook.lotro.com/wiki/Special:LotroResource?id=1879157116";
    //String url2="http://lorebook.lotro.com/wiki/Quest:Return_to_the_Elders";
    //String url3="http://lorebook.lotro.com/wiki/Quest:A_Visit_to_the_Warren";
    //String url4="http://lorebook.lotro.com/wiki/Quest:Ending_the_Nightmare";
    String url5="http://lorebook.lotro.com/wiki/Quest:Vol._II,_Book_8,_Chapter_4:_A_Relic_in_Nal%C3%A2-d%C3%BBm";
    String url6="http://lorebook.lotro.com/wiki/Quest:Quelling_the_Riot"; // repeatable
    //String url7="http://lorebook.lotro.com/wiki/Quest:Set_the_Trap"; // Fellowship
    String url8="http://lorebook.lotro.com/wiki/Quest:Assault_on_the_Ringwraiths%27_Lair_--_Daily"; // Skirmish
    String url9="http://lorebook.lotro.com/wiki/Quest:Crafting%3A_Gems_for_Guleneth"; // crafting / receive&select objects
    String url10="http://lorebook.lotro.com/wiki/Quest:A_Song_for_the_Company"; // required classes
    String url11="http://lorebook.lotro.com/wiki/Quest:Task:_Mossy_Carapaces"; // maximum level
    //String url12="http://lorebook.lotro.com/wiki/Quest:The_Heart_of_the_Wood%2C_Part_III"; // traits
    String url13="http://lorebook.lotro.com/wiki/Quest:A_Bounder_of_Great_Merit"; // titles
    String url14="http://lorebook.lotro.com/wiki/Quest:A_Secret_Club"; // passive skill
    String url15="http://lorebook.lotro.com/wiki/Quest:A_Feminine_Curve_to_the_Steel"; // required races / multiple definition
    String url16="http://lorebook.lotro.com/wiki/Quest:Task%3A_Coarse_Fur_%28Repeatable%29"; // multiple definition
    String url17="http://lorebook.lotro.com/wiki/Quest:A_Cauldron_of_Iron"; // destiny points
    String url18="http://lorebook.lotro.com/wiki/Quest:An_Iron_Belly"; // monster play
    String url19="http://lorebook.lotro.com/wiki/Quest:Behind_Bars_--_Instance"; // instanced
    String url20="http://lorebook.lotro.com/wiki/Quest:Agarochir%2C_Keeper_of_Tirband"; // null faction
    //String singleUrl="http://lorebook.lotro.com/wiki/Quest:Vol._I,_Book_11,_Chapter_10:_A_Pouch_of_Gems_for_a_Box_of_Keys";
    String[] urls={ url0, url1, /*url2, url3, url4,*/ url5, url6, /*url7,*/ url8, url9, url10,
        url11, /*url12,*/ url13, url14, url15, url16, url17, url18, url19, url20 };
    //String[] urls={ url16 };
    QuestPageParser parser=new QuestPageParser();
    for(String url : urls)
    {
      List<QuestDescription> quests=parser.parseQuestPage(url);
      if (quests!=null)
      {
        for(QuestDescription quest : quests)
        {
          System.out.println(quest.dump());
        }
      }
      else
      {
        System.out.println("Quest list for URL ["+url+"] is null!");
      }
    }
  }
}
