package delta.games.lotro.lore.items;

import java.io.File;
import java.io.FileFilter;

import org.apache.log4j.Logger;

import delta.common.utils.environment.FileSystem;
import delta.common.utils.files.filter.ExtensionPredicate;
import delta.common.utils.text.EncodingNames;
import delta.games.lotro.Config;
import delta.games.lotro.lore.items.io.xml.ItemXMLParser;
import delta.games.lotro.lore.items.io.xml.ItemXMLWriter;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Test for item XML parsing.
 * @author DAM
 */
public class MainTestItemXMLWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    File itemsDir=Config.getInstance().getItemsDir();
    FileFilter fileFilter=new ExtensionPredicate("xml");
    File[] itemFiles=itemsDir.listFiles(fileFilter);
    if (itemFiles!=null)
    {
      File tmpDir=FileSystem.getTmpDir();
      File toDir=new File(tmpDir,"items");
      toDir.mkdirs();
      ItemXMLWriter writer=new ItemXMLWriter();
      ItemXMLParser reader=new ItemXMLParser();
      for(File itemFile : itemFiles)
      {
        Item item=reader.parseXML(itemFile);
        String name=item.getName();
        File to=new File(toDir,name+".xml");
        //if (!to.exists())
        {
          boolean ok=writer.write(to,item,EncodingNames.UTF_8);
          if (!ok)
          {
            _logger.error("Write failed for ["+item.getName()+"]");
          }
        }
      }
    }
  }
}
