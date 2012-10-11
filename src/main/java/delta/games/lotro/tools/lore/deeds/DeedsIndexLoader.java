package delta.games.lotro.tools.lore.deeds;

import java.io.File;

import org.apache.log4j.Logger;

import delta.games.lotro.lore.deeds.index.DeedsIndex;
import delta.games.lotro.lore.deeds.index.io.web.DeedsIndexJSONParser;
import delta.games.lotro.lore.deeds.index.io.xml.DeedsIndexWriter;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Deeds index loader.
 * @author DAM
 */
public class DeedsIndexLoader
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  /**
   * Load deeds index for my.lotro.com and write it to a file.
   * @param indexFile Target file.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean doIt(File indexFile)
  {
    boolean ret=false;
    DeedsIndexJSONParser parser=new DeedsIndexJSONParser();
    DeedsIndex index=parser.parseDeedsIndex();
    if (index!=null)
    {
      DeedsIndexWriter writer=new DeedsIndexWriter();
      ret=writer.write(indexFile,index,"UTF-8");
      if (!ret)
      {
        _logger.error("Cannot write deeds index file ["+indexFile+"]");
      }
    }
    else
    {
      _logger.error("Deeds index is null");
    }
    return ret;
  }
}
