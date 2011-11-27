package delta.games.lotro.character.log;

import java.io.File;
import java.util.List;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.io.web.CharacterLogPageParser;
import delta.games.lotro.character.log.io.xml.CharacterLogXMLWriter;

/**
 * Test for character log parsing.
 * @author DAM
 */
public class MainTestCharacterActivityLogParsing
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    List<CharacterFile> toons=utils.getAllFiles();

    CharacterLogPageParser parser=new CharacterLogPageParser();
    for(CharacterFile toon : toons)
    {
      String url=toon.getBaseMyLotroURL();
      CharacterLog log=parser.parseLogPages(url,null);
      if (log!=null)
      {
        System.out.println(log);
        CharacterLogsManager manager=new CharacterLogsManager(toon);
        File logFile=manager.getNewLogFile();
        logFile.getParentFile().mkdirs();
        CharacterLogXMLWriter writer=new CharacterLogXMLWriter();
        writer.write(logFile,log,EncodingNames.UTF_8);
      }
    }
  }
}
