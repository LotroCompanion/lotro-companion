package delta.games.lotro.lore.items.index;

import java.io.File;

import org.apache.log4j.Logger;

import delta.common.utils.environment.FileSystem;
import delta.games.lotro.lore.items.index.ItemCategory;
import delta.games.lotro.lore.items.index.ItemSummary;
import delta.games.lotro.lore.items.index.ItemsIndex;
import delta.games.lotro.lore.items.index.io.web.ItemsIndexJSONParser;
import delta.games.lotro.lore.items.index.io.xml.ItemsIndexWriter;
import delta.games.lotro.lore.items.index.io.xml.ItemsIndexXMLParser;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Items index parsing from MyLotro.
 * @author DAM
 */
public class MainTestLoadItemsIndex
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    ItemsIndexJSONParser parser=new ItemsIndexJSONParser();
    ItemsIndex index=parser.parseItemsIndex();
    if (index!=null)
    {
      ItemsIndexWriter writer=new ItemsIndexWriter();
      File tmpDir=FileSystem.getTmpDir();
      File outFile=new File(tmpDir,"itemsIndex.xml");
      boolean ok=writer.write(outFile,index,"UTF-8");
      if (!ok)
      {
        _logger.error("Cannot write items index file ["+outFile+"]");
        ItemsIndexXMLParser xmlParser=new ItemsIndexXMLParser();
        ItemsIndex index2=xmlParser.parseXML(outFile);
        String[] categories=index2.getCategories();
        System.out.println(categories);
        for(String category : categories)
        {
          ItemCategory c=index2.getCategory(category);
          ItemSummary[] items=c.getItems();
          System.out.println(items);
        }
      }
    }
    else
    {
      _logger.error("index is null");
    }
  }
}
