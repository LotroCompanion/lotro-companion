package delta.games.lotro.character.log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import delta.common.utils.text.EncodingNames;
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
    String glumlug="http://my.lotro.com/home/character/2427907/146366987891794854/";
    String alphael="http://my.lotro.com/home/character/2427907/146366987891895618/";
    String minirdil="http://my.lotro.com/home/character/2427907/146366987891842857/";
    String hirthrelthorn="http://my.lotro.com/home/character/2427907/149463212633928354/";
    String allurwyn="http://my.lotro.com/home/character/1069125/146366987890743296/";
    String beleganth="http://my.lotro.com/home/character/elendilmir/beleganth/";
    String allyriel="http://my.lotro.com/home/character/elendilmir/allyriel/";
    String feroce="http://my.lotro.com/home/character/elendilmir/feroce/";
    String serilis="http://my.lotro.com/home/character/elendilmir/serilis/";
    String noctivagant="http://my.lotro.com/home/character/elendilmir/noctivagant/";
    String[] urls={ glumlug,alphael,minirdil,hirthrelthorn,
        allurwyn,allyriel,noctivagant,
        beleganth,serilis,feroce
    };
    //String[] urls={ glumlug };
    CharacterLogPageParser parser=new CharacterLogPageParser();
    for(String url : urls)
    {
      CharacterLog log=parser.parseLogPages(url);
      if (log!=null)
      {
        System.out.println(log);
        File rootDir=new File("/home/dm/lotro");
        String name=log.getName();
        //log 2011-11-26 1056.xml
        File characterDir=new File(rootDir,name);
        characterDir.mkdirs();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HHmm");
        String filename="log "+sdf.format(new Date())+".xml";
        File logFile=new File(characterDir,filename);
        CharacterLogXMLWriter writer=new CharacterLogXMLWriter();
        writer.write(logFile,log,EncodingNames.UTF_8);
      }
    }
  }
}
