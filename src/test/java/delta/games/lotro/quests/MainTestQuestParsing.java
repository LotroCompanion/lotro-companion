package delta.games.lotro.quests;

import java.io.File;

import delta.common.utils.environment.FileSystem;
import delta.common.utils.text.EncodingNames;
import delta.games.lotro.quests.io.web.QuestPageParser;
import delta.games.lotro.quests.io.xml.QuestXMLWriter;

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
    String url2="http://lorebook.lotro.com/wiki/Quest:Return_to_the_Elders";
    String url3="http://lorebook.lotro.com/wiki/Quest:A_Visit_to_the_Warren";
    String url4="http://lorebook.lotro.com/wiki/Quest:Ending_the_Nightmare";
    String url5="http://lorebook.lotro.com/wiki/Quest:Vol._II,_Book_8,_Chapter_4:_A_Relic_in_Nal%C3%A2-d%C3%BBm";
    String url6="http://lorebook.lotro.com/wiki/Quest:Quelling_the_Riot"; // repeatable
    String url7="http://lorebook.lotro.com/wiki/Quest:Set_the_Trap"; // Fellowship
    String url8="http://lorebook.lotro.com/wiki/Quest:Assault_on_the_Ringwraiths%27_Lair_--_Daily"; // Skirmish
    String url9="http://lorebook.lotro.com/wiki/Quest:Crafting%3A_Gems_for_Guleneth"; // crafting / receive&select objects
    String[] urls={ url0, url1, url2, url3, url4, url5, url6, url7, url8, url9 };
    //String[] urls={ url9 };
    QuestPageParser parser=new QuestPageParser();
    for(String url : urls)
    {
      QuestDescription q=parser.parseQuestPage(url);
      if (q!=null)
      {
        System.out.println(q.dump());
        QuestXMLWriter writer=new QuestXMLWriter();
        String name=q.getTitle();
        File tmpDir=FileSystem.getTmpDir();
        File out=new File(tmpDir,name+".xml");
        writer.write(out,q,EncodingNames.UTF_8);
      }
    }
  }
}
