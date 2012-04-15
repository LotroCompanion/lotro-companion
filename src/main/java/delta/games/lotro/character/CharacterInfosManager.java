package delta.games.lotro.character;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.character.io.web.CharacterPageParser;
import delta.games.lotro.character.io.xml.CharacterXMLParser;
import delta.games.lotro.character.io.xml.CharacterXMLWriter;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Manages info files for a single toon.
 * @author DAM
 */
public class CharacterInfosManager
{
  private static final Logger _logger=LotroLoggers.getCharacterLogger();

  private CharacterFile _toon;

  /**
   * Constructor.
   * @param toon Toon to manage.
   */
  public CharacterInfosManager(CharacterFile toon)
  {
    _toon=toon;
  }

  /**
   * Get the most recent character description.
   * @return A character description or <code>null</code> if not found or error.
   */
  public Character getLastCharacterDescription()
  {
    Character c=null;
    File lastInfo=getLastInfoFile();
    if (lastInfo!=null)
    {
      CharacterXMLParser xmlInfoParser=new CharacterXMLParser();
      c=xmlInfoParser.parseXML(lastInfo);
    }
    return c;
  }

  /**
   * Get the most recent info file.
   * @return a file or <code>null</code> if there is no info file for this toon.
   */
  public File getLastInfoFile()
  {
    File lastInfo=null;
    File characterDir=_toon.getRootDir();
    if (characterDir.exists())
    {
      FileFilter filter=new FileFilter()
      {
        public boolean accept(File pathname)
        {
          String name=pathname.getName();
          if ((name.startsWith("info ")) && (name.endsWith(".xml")))
          {
            return true;
          }
          return false;
        }
      };
      File[] files=characterDir.listFiles(filter);
      if ((files!=null) && (files.length>0))
      {
        Arrays.sort(files);
        lastInfo=files[files.length-1];
      }
    }
    return lastInfo;
  }

  /**
   * Update character log.
   * @return <code>true</code> if it was successfull, <code>false</code> otherwise.
   */
  public boolean updateCharacterDescription()
  {
    String url=_toon.getBaseMyLotroURL();
    CharacterPageParser parser=new CharacterPageParser();
    String name=_toon.getName();
    Character c=parser.parseMainPage(name,url);
    boolean ret=writeNewInfo(c);
    if (!ret)
    {
      _logger.error("Update failed for toon ["+name+"]!");
    }
    return ret;
  }

  /*
   * 
Updating toon [Tilmogrim]
# 2012-04-02 22:40:13,906 [main] [APPS.LOTRO.WEB_INPUT] ERROR delta.games.lotro.character.io.web.CharacterPageParser.parseMainPage(CharacterPageParser.java:203) - Cannot parse character page [http://my.lotro.com/home/character/elendilmir/tilmogrim/]
java.io.IOException: Server returned HTTP response code: 503 for URL: http://my.lotro.com/home/character/elendilmir/tilmogrim/
  at sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1225)
  at net.htmlparser.jericho.StreamEncodingDetector.<init>(StreamEncodingDetector.java:65)
  at net.htmlparser.jericho.EncodingDetector.<init>(EncodingDetector.java:43)
  at net.htmlparser.jericho.Source.<init>(Source.java:193)
  at delta.games.lotro.character.io.web.CharacterPageParser.parseMainPage(CharacterPageParser.java:189)
  at delta.games.lotro.character.CharacterInfosManager.updateCharacterDescription(CharacterInfosManager.java:92)
  at delta.games.lotro.character.MainTestCharacterParsing.main(MainTestCharacterParsing.java:26)
# 2012-04-02 22:40:13,921 [main] [APPS.LOTRO] ERROR delta.games.lotro.character.io.xml.CharacterXMLWriter.write(CharacterXMLWriter.java:64) - 
java.lang.NullPointerException
  at delta.games.lotro.character.io.xml.CharacterXMLWriter.write(CharacterXMLWriter.java:77)
  at delta.games.lotro.character.io.xml.CharacterXMLWriter.write(CharacterXMLWriter.java:58)
  at delta.games.lotro.character.CharacterInfosManager.writeNewInfo(CharacterInfosManager.java:123)
  at delta.games.lotro.character.CharacterInfosManager.updateCharacterDescription(CharacterInfosManager.java:93)
  at delta.games.lotro.character.MainTestCharacterParsing.main(MainTestCharacterParsing.java:26)
# 2012-04-02 22:40:13,921 [main] [APPS.LOTRO.CHARACTER] ERROR delta.games.lotro.character.CharacterInfosManager.updateCharacterDescription(CharacterInfosManager.java:97) - Update failed for toon [Tilmogrim]!
Updating toon [Warthil]
OK
   * 
   */
  
  
  /**
   * Write a new info file for this toon.
   * @param info Character info to write.
   * @return <code>true</code> it it succeeds, <code>false</code> otherwise.
   */
  public boolean writeNewInfo(Character info)
  {
    boolean ret=true;
    File logFile=getNewInfoFile();
    File parentFile=logFile.getParentFile();
    if (!parentFile.exists())
    {
      ret=parentFile.mkdirs();
      if (!ret)
      {
        _logger.error("Cannot create directory ["+parentFile+"]!");
      }
    }
    if (ret)
    {
      CharacterXMLWriter writer=new CharacterXMLWriter();
      ret=writer.write(logFile,info,EncodingNames.UTF_8);
    }
    return ret;
  }

  private File getNewInfoFile()
  {
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HHmm");
    String filename="info "+sdf.format(new Date())+".xml";
    File characterDir=_toon.getRootDir();
    File logFile=new File(characterDir,filename);
    return logFile;
  }
}
