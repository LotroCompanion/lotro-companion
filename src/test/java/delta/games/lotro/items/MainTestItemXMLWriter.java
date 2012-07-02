package delta.games.lotro.items;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.utils.environment.FileSystem;
import delta.common.utils.text.EncodingNames;
import delta.games.lotro.items.io.xml.ItemXMLWriter;
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
    List<Item> items=new MainTestItemParsing().parseItems();
    File tmpDir=FileSystem.getTmpDir();
    File toDir=new File(tmpDir,"items");
    toDir.mkdirs();
    ItemXMLWriter writer=new ItemXMLWriter();
    for(Item item : items)
    {
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
