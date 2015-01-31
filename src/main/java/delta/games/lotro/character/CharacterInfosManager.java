package delta.games.lotro.character;

import java.io.File;
import java.io.FileFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.character.io.xml.CharacterXMLParser;
import delta.games.lotro.character.io.xml.CharacterXMLWriter;
import delta.games.lotro.utils.Formats;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Manages info files for a single toon.
 * @author DAM
 */
public class CharacterInfosManager
{
  private static final Logger _logger=LotroLoggers.getCharacterLogger();
  //private static final boolean USE_DATA_LOTRO=true;

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
      c=getCharacterDescription(lastInfo);
    }
    return c;
  }

  /**
   * Get the character data for a given file.
   * @param infoFile File to read.
   * @return A character data or <code>null</code> if a problem occurs.
   */
  public Character getCharacterDescription(File infoFile)
  {
    CharacterXMLParser xmlInfoParser=new CharacterXMLParser();
    Character c=xmlInfoParser.parseXML(infoFile);
    if (c!=null)
    {
      Date date=getDateFromFilename(infoFile.getName());
      c.setDate(Long.valueOf(date.getTime()));
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
    File[] infoFiles=getInfoFiles();
    if ((infoFiles!=null) && (infoFiles.length>0))
    {
      lastInfo=infoFiles[infoFiles.length-1];
    }
    return lastInfo;
  }

  /**
   * Get all the available character data files.
   * @return an array of files.
   */
  public File[] getInfoFiles()
  {
    File[] files=null;
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
      files=characterDir.listFiles(filter);
      if ((files!=null) && (files.length>0))
      {
        Arrays.sort(files);
      }
    }
    return files;
  }

  /**
   * Update character log.
   * @return <code>true</code> if it was successfull, <code>false</code> otherwise.
   */
  public boolean updateCharacterDescription()
  {
    /*
    String name=_toon.getName();
    Character c;
    if (USE_DATA_LOTRO)
    {
      String world=_toon.getServerName();
      String url="http://data.lotro.com/valamar/a8ca0c5de7c466ecdd8e7f2df1d610ea/charactersheet/w/"+world+"/c/"+name+"/";
      DataLotroCharacterPageParser parser=new DataLotroCharacterPageParser();
      c=parser.parseMainPage(name,url);
    }
    else
    {
      String url=_toon.getBaseMyLotroURL();
      CharacterPageParser parser=new CharacterPageParser();
      c=parser.parseMainPage(name,url);
    }
    boolean ret=false;
    if (c!=null)
    {
      ret=writeNewInfo(c);
    }
    if (!ret)
    {
      _logger.error("Update failed for toon ["+name+"]!");
    }
    return ret;
    */
    return false;
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
    SimpleDateFormat sdf=new SimpleDateFormat(Formats.FILE_DATE_PATTERN);
    String filename="info "+sdf.format(new Date())+".xml";
    File characterDir=_toon.getRootDir();
    File logFile=new File(characterDir,filename);
    return logFile;
  }

  /**
   * Get a date from a filename.
   * @param filename Filename to use.
   * @return A date or <code>null</code> if it cannot be parsed!
   */
  public static Date getDateFromFilename(String filename)
  {
    Date ret=null;
    if ((filename.startsWith("info ")) && (filename.endsWith(".xml")))
    {
      filename=filename.substring(5,filename.length()-4);
      SimpleDateFormat sdf=new SimpleDateFormat(Formats.FILE_DATE_PATTERN);
      try
      {
        ret=sdf.parse(filename);
      }
      catch(ParseException pe)
      {
        _logger.error("Cannot parse filename ["+filename+"]!",pe);
      }
    }
    return ret;
  }
}
