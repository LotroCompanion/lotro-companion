package delta.games.lotro.character.log;

import java.io.File;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.io.xml.CharacterLogXMLParser;
import delta.games.lotro.character.log.io.xml.CharacterLogXMLWriter;

/**
 * Test for character log parsing.
 * @author DAM
 */
public class MainTestLoadCharacterLog
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    CharacterFile toon=utils.getMainToon();
    File toonDir=toon.getRootDir();
    File source=new File(toonDir,"log 2011-11-26 1159.xml");
    CharacterLogXMLParser parser=new CharacterLogXMLParser();
    CharacterLog log=parser.parseXML(source);
    File logFile=new File(source.getParentFile(),"new-"+source.getName());
    CharacterLogXMLWriter writer=new CharacterLogXMLWriter();
    writer.write(logFile,log,EncodingNames.UTF_8);
  }
}
