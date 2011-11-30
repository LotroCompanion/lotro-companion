package delta.games.lotro.character;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.character.io.web.CharacterPageParser;
import delta.games.lotro.character.io.xml.CharacterXMLWriter;
import delta.games.lotro.character.log.LotroTestUtils;

/**
 * Test for character description parsing.
 * @author DAM
 */
public class MainTestCharacterParsing
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    List<CharacterFile> toons=utils.getAllFiles();

    CharacterPageParser parser=new CharacterPageParser();
    for(CharacterFile toon : toons)
    {
      String name=toon.getName();
      System.out.println("Updating toon ["+name+"]");
      String url=toon.getBaseMyLotroURL();
      Character c=parser.parseMainPage(url);
      if (c!=null)
      {
        CharacterXMLWriter writer=new CharacterXMLWriter();
        File outDir=toon.getRootDir();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HHmm");
        String filename="info "+sdf.format(new Date())+".xml";
        File out=new File(outDir,filename);
        writer.write(out,c,EncodingNames.UTF_8);
      }
    }
  }
}
