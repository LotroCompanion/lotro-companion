package delta.games.lotro.character.log;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.io.web.CharacterLogPageParser;
import delta.games.lotro.character.log.io.web.CharacterLogUpdater;
import delta.games.lotro.character.log.io.xml.CharacterLogXMLParser;
import delta.games.lotro.character.log.io.xml.CharacterLogXMLWriter;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Test for character log update.
 * @author DAM
 */
public class MainTestCharacterLogUpdate
{
  private static final Logger _logger=LotroLoggers.getCharacterLogLogger();

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    List<CharacterFile> toons=utils.getAllFiles();

    for(CharacterFile toon : toons)
    {
      String name=toon.getName();
      System.out.println("Updating toon ["+name+"]");
      CharacterLogsManager manager=new CharacterLogsManager(toon);
      File lastLog=manager.getLastLogFile();
      String url=toon.getBaseMyLotroURL();
      CharacterLog log;
      int nbItemsLastLog=0;
      if (lastLog!=null)
      {
        CharacterLogXMLParser xmlLogParser=new CharacterLogXMLParser();
        log=xmlLogParser.parseXML(lastLog);
        nbItemsLastLog=log.getNbItems();
        CharacterLogUpdater updater=new CharacterLogUpdater();
        updater.updateCharacterLog(log,url);
      }
      else
      {
        CharacterLogPageParser parser=new CharacterLogPageParser();
        log=parser.parseLogPages(url,null);
      }
      if (log!=null)
      {
        int nbItems=log.getNbItems();
        if (nbItems!=nbItemsLastLog)
        {
          File logFile=manager.getNewLogFile();
          CharacterLogXMLWriter writer=new CharacterLogXMLWriter();
          logFile.getParentFile().mkdirs();
          writer.write(logFile,log,EncodingNames.UTF_8);
        }
      }
      else
      {
        _logger.error("Log is null for toon ["+name+"]!");
      }
    }
  }
}
