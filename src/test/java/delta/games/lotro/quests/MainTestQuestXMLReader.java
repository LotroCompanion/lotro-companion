package delta.games.lotro.quests;

import java.io.File;

import delta.common.utils.files.filter.ExtensionPredicate;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.quests.io.xml.QuestXMLParser;

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
    CharacterFile toon=new LotroTestUtils().getMainToon();
    File rootDir=toon.getRootDir();
    File questsDir=new File(rootDir,"quests");
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
