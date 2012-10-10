package delta.games.lotro.tools.lore.quests;

import java.io.File;

import org.apache.log4j.Logger;

import delta.games.lotro.lore.quests.index.QuestsIndex;
import delta.games.lotro.lore.quests.index.io.web.QuestsIndexJSONParser;
import delta.games.lotro.lore.quests.index.io.xml.QuestsIndexWriter;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Quests index loader.
 * @author DAM
 */
public class QuestsIndexLoader
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  /**
   * Load quests index for my.lotro.com and write it to a file.
   * @param indexFile Target file.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean doIt(File indexFile)
  {
    boolean ret=false;
    QuestsIndexJSONParser parser=new QuestsIndexJSONParser();
    QuestsIndex index=parser.parseQuestsIndex();
    if (index!=null)
    {
      QuestsIndexWriter writer=new QuestsIndexWriter();
      ret=writer.write(indexFile,index,"UTF-8");
      if (!ret)
      {
        _logger.error("Cannot write quests index file ["+indexFile+"]");
      }
    }
    else
    {
      _logger.error("Quests index is null");
    }
    return ret;
  }
}
