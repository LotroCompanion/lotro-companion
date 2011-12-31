package delta.games.lotro.quests.index;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;

import delta.common.utils.environment.FileSystem;
import delta.games.lotro.quests.index.io.xml.QuestsIndexXMLParser;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Quests index parsing from XML.
 * @author DAM
 */
public class MainTestQuestsIndexParsing
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
      File tmpDir=FileSystem.getTmpDir();
      File inFile=new File(tmpDir,"questsIndex.xml");
      QuestsIndexXMLParser xmlParser=new QuestsIndexXMLParser();
      QuestsIndex index=xmlParser.parseXML(inFile);
      if (index!=null)
      {
        String[] categories=index.getCategories();
        System.out.println(Arrays.deepToString(categories));
        for(String category : categories)
        {
          QuestCategory c=index.getCategory(category);
          QuestSummary[] quests=c.getQuests();
          System.out.println(Arrays.deepToString(quests));
        }
      }
      else
      {
        _logger.error("Cannot read quests index file ["+inFile+"]");
      }
  }
}
