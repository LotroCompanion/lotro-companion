package delta.games.lotro.lore.deeds.index;

import java.io.File;

import org.apache.log4j.Logger;

import delta.common.utils.environment.FileSystem;
import delta.games.lotro.lore.deeds.index.io.web.DeedsIndexJSONParser;
import delta.games.lotro.lore.deeds.index.io.xml.DeedsIndexWriter;
import delta.games.lotro.lore.deeds.index.io.xml.DeedsIndexXMLParser;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Deeds index parsing from MyLotro.
 * @author DAM
 */
public class MainTestLoadDeedsIndex
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    DeedsIndexJSONParser parser=new DeedsIndexJSONParser();
    DeedsIndex index=parser.parseDeedsIndex();
    if (index!=null)
    {
      DeedsIndexWriter writer=new DeedsIndexWriter();
      File tmpDir=FileSystem.getTmpDir();
      File outFile=new File(tmpDir,"deedsIndex.xml");
      boolean ok=writer.write(outFile,index,"UTF-8");
      if (!ok)
      {
        _logger.error("Cannot write deeds index file ["+outFile+"]");
        DeedsIndexXMLParser xmlParser=new DeedsIndexXMLParser();
        DeedsIndex index2=xmlParser.parseXML(outFile);
        String[] categories=index2.getCategories();
        System.out.println(categories);
        for(String category : categories)
        {
          DeedCategory c=index2.getCategory(category);
          DeedSummary[] deeds=c.getDeeds();
          System.out.println(deeds);
        }
      }
    }
    else
    {
      _logger.error("index is null");
    }
  }
}
