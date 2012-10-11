package delta.games.lotro.tools.lore.deeds;

import java.io.File;

import delta.common.utils.files.filter.ExtensionPredicate;
import delta.common.utils.text.EncodingNames;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.index.DeedsIndex;
import delta.games.lotro.lore.deeds.index.io.xml.DeedsIndexWriter;
import delta.games.lotro.lore.deeds.io.xml.DeedXMLParser;

/**
 * Builds a deeds index for a series of deed definition files. 
 * @author DAM
 */
public class DeedsIndexBuilder
{
  private File _deedsDir;
  private File _indexFile;

  /**
   * Constructor.
   * @param deedsDir Deeds directory. 
   * @param indexFile Index file.
   */
  public DeedsIndexBuilder(File deedsDir, File indexFile)
  {
    _deedsDir=deedsDir;
    _indexFile=indexFile;
  }

  /**
   * Do build deeds index.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean doIt()
  {
    boolean ret=false;
    if (_deedsDir.exists())
    {
      DeedsIndex index=new DeedsIndex();
      ExtensionPredicate extFilter=new ExtensionPredicate(".xml");
      File[] deedFiles=_deedsDir.listFiles(extFilter);
      if (deedFiles!=null)
      {
        DeedXMLParser parser=new DeedXMLParser();
        for(File deedFile : deedFiles)
        {
          DeedDescription deed=parser.parseXML(deedFile);
          String category=deed.getType().toString();
          String key=deed.getKey();
          String name=deed.getName();
          int id=deed.getIdentifier();
          index.addDeed(category,id,key,name);
        }
        DeedsIndexWriter writer=new DeedsIndexWriter();
        ret=writer.write(_indexFile,index,EncodingNames.UTF_8);
      }
    }
    return ret;
  }
}
