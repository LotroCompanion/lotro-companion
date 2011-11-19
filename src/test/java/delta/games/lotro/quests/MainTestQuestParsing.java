package delta.games.lotro.quests;

import delta.games.lotro.quests.io.web.QuestPageParser;

/**
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
    String url1="http://lorebook.lotro.com/wiki/Special:LotroResource?id=1879157116";
    String url2="http://lorebook.lotro.com/wiki/Quest:Return_to_the_Elders";
    String url3="http://lorebook.lotro.com/wiki/Quest:A_Visit_to_the_Warren";
    String url4="http://lorebook.lotro.com/wiki/Quest:Ending_the_Nightmare";
    String[] urls={ url1, url2, url3, url4 };
    QuestPageParser parser=new QuestPageParser();
    for(String url : urls)
    {
      QuestDescription q=parser.parseQuestPage(url);
      if (q!=null)
      {
        System.out.println(q);
        /*
        CharacterXMLWriter writer=new CharacterXMLWriter();
        String name=c.getName();
        File out=new File("/tmp/"+name+".xml");
        writer.write(out,c,EncodingNames.UTF_8);
        */
      }
    }
  }
}
