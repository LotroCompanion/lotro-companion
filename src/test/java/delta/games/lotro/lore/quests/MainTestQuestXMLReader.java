package delta.games.lotro.lore.quests;

import java.io.File;

import delta.common.utils.files.filter.ExtensionPredicate;
import delta.games.lotro.Config;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.io.xml.QuestXMLParser;

/**
 * Test for quest XML files reading. 
 * @author DAM
 */
public class MainTestQuestXMLReader
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    File questsDir=Config.getInstance().getQuestsDir();
    if (questsDir.exists())
    {
      ExtensionPredicate extFilter=new ExtensionPredicate(".xml");
      File[] questFiles=questsDir.listFiles(extFilter);
      if (questFiles!=null)
      {
        QuestXMLParser parser=new QuestXMLParser();
        for(File questFile : questFiles)
        {
          QuestDescription q=parser.parseXML(questFile);
          System.out.println(q.dump());
        }
      }
    }
  }
}
