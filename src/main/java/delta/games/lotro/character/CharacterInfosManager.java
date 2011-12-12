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
    Character c=parser.parseMainPage(url);
    boolean ret=writeNewInfo(c);
    if (!ret)
    {
      String name=_toon.getName();
      _logger.error("Update failed for toon ["+name+"]!");
    }
    return ret;
  }

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
