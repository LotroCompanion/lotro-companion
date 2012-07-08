package delta.games.lotro.lore.deeds;

import java.io.File;

import delta.common.utils.files.filter.ExtensionPredicate;
import delta.games.lotro.Config;
import delta.games.lotro.lore.deeds.io.xml.DeedXMLParser;

/**
 * Test for deed XML files reading. 
 * @author DAM
 */
public class MainTestDeedXMLReader
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    File deedsDir=Config.getInstance().getDeedsDir();
    if (deedsDir.exists())
    {
      ExtensionPredicate extFilter=new ExtensionPredicate(".xml");
      File[] deedFiles=deedsDir.listFiles(extFilter);
      if (deedFiles!=null)
      {
        DeedXMLParser parser=new DeedXMLParser();
        for(File deedFile : deedFiles)
        {
          DeedDescription deed=parser.parseXML(deedFile);
          if (deed.getType()==null)
          {
            String id=deed.getIdentifier();
            if (id!=null)
            {
              deedFile.delete();
            }
          }
        }
      }
    }
  }
}
