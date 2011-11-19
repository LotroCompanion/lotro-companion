package delta.games.lotro.character;

import java.io.File;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.character.Character;
import delta.games.lotro.character.io.web.CharacterPageParser;
import delta.games.lotro.character.io.xml.CharacterXMLWriter;

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
    CharacterPageParser parser=new CharacterPageParser();
    for(String url : urls)
    {
      Character c=parser.parseMainPage(url);
      if (c!=null)
      {
        System.out.println(c);
        CharacterXMLWriter writer=new CharacterXMLWriter();
        String name=c.getName();
        File out=new File("/tmp/"+name+".xml");
        writer.write(out,c,EncodingNames.UTF_8);
      }
    }
  }
}
