package delta.games.lotro.quests.index;

import java.io.File;

import org.apache.log4j.Logger;

import delta.common.utils.environment.FileSystem;
import delta.games.lotro.quests.index.io.web.QuestsIndexJSONParser;
import delta.games.lotro.quests.index.io.xml.QuestsIndexWriter;
import delta.games.lotro.quests.index.io.xml.QuestsIndexXMLParser;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Quests index parsing from MyLotro.
 * @author DAM
 */
public class MainTestLoadQuestsIndex
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    QuestsIndexJSONParser parser=new QuestsIndexJSONParser();
    QuestsIndex index=parser.parseQuestsIndex();
    if (index!=null)
    {
      QuestsIndexWriter writer=new QuestsIndexWriter();
      File tmpDir=FileSystem.getTmpDir();
      File outFile=new File(tmpDir,"questsIndex.xml");
      boolean ok=writer.write(outFile,index,"UTF-8");
      if (!ok)
      {
        _logger.error("Cannot write quests index file ["+outFile+"]");
        QuestsIndexXMLParser xmlParser=new QuestsIndexXMLParser();
        QuestsIndex index2=xmlParser.parseXML(outFile);
        String[] categories=index2.getCategories();
        System.out.println(categories);
        for(String category : categories)
        {
          QuestCategory c=index2.getCategory(category);
          QuestSummary[] quests=c.getQuests();
          System.out.println(quests);
        }
      }
    }
    else
    {
      _logger.error("index is null");
    }
  }
}
