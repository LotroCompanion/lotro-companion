package delta.games.lotro.character.log;

import java.io.File;
import java.io.FileFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.Config;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.io.xml.CharacterLogXMLParser;
import delta.games.lotro.character.log.io.xml.CharacterLogXMLWriter;
import delta.games.lotro.utils.Formats;
import delta.games.lotro.utils.LotroLoggers;
import delta.games.lotro.utils.TypedProperties;

/**
 * Manages log files for a single toon.
 * @author DAM
 */
public class CharacterLogsManager
{
  private static final Logger _logger=LotroLoggers.getCharacterLogLogger();

  private CharacterFile _toon;

  /**
   * Constructor.
   * @param toon Toon to manage.
   */
  public CharacterLogsManager(CharacterFile toon)
  {
    _toon=toon;
  }

  /**
   * Get the most recent character log.
   * @return A character log or <code>null</code> if not found or error.
   */
  public CharacterLog getLastLog()
  {
    CharacterLog log=null;
    File lastLog=getLastLogFile();
    if (lastLog!=null)
    {
      CharacterLogXMLParser xmlLogParser=new CharacterLogXMLParser();
      log=xmlLogParser.parseXML(lastLog);
    }
    return log;
  }

  /**
   * Prune character log files.
   */
  public void pruneLogFiles()
  {
    TypedProperties props=Config.getInstance().getParameters();
    int nbMax=props.getIntProperty("character.log.max.files",10);
    File[] files=getLogFiles();
    if ((files!=null) && (files.length>nbMax))
    {
      int nbToRemove=files.length-nbMax;
      for(int i=0;i<nbToRemove;i++)
      {
        File toRemove=files[i];
        boolean ok=toRemove.delete();
        if (!ok)
        {
          _logger.warn("Cannot delete file ["+toRemove+"]!");
        }
      }
    }
  }

  /**
   * Get the most recent log file.
   * @return a file or <code>null</code> if there is no log file for this toon.
   */
  public File getLastLogFile()
  {
    File lastLog=null;
    File[] files=getLogFiles();
    if ((files!=null) && (files.length>0))
    {
      lastLog=files[files.length-1];
    }
    return lastLog;
  }

  private File[] getLogFiles()
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
          if ((name.startsWith("log ")) && (name.endsWith(".xml")))
          {
            return true;
          }
          return false;
        }
      };
      files=characterDir.listFiles(filter);
    }
    if ((files!=null) && (files.length>0))
    {
      Arrays.sort(files);
    }
    return files;
  }

  /**
   * Indicates if this character has a log file.
   * @return <code>true</code> if it does, <code>false</code> otherwise.
   */
  public boolean hasLog()
  {
    File lastLog=getLastLogFile();
    return (lastLog!=null);
  }

  /**
   * Update character log.
   * @return An integer value that indicates the number of new items, or <code>null</code> if it failed.
   */
  public Integer updateLog()
  {
    /*
    Integer ret=null;
    File lastLog=getLastLogFile();
    String url=_toon.getBaseMyLotroURL();
    CharacterLog log;
    int nbItemsLastLog=0;
    boolean updateOK;
    boolean creation=false;
    if (lastLog!=null)
    {
      CharacterLogXMLParser xmlLogParser=new CharacterLogXMLParser();
      log=xmlLogParser.parseXML(lastLog);
      nbItemsLastLog=log.getNbItems();
      updateOK=updateCharacterLog(log,url);
    }
    else
    {
      creation=true;
      CharacterLogPageParser parser=new CharacterLogPageParser();
      log=parser.parseLogPages(url,null);
      updateOK=(log!=null);
    }
    boolean result=updateOK;
    if (updateOK)
    {
      int nbItems=log.getNbItems();
      if ((creation) || (nbItems!=nbItemsLastLog))
      {
        result=writeNewLog(log);
        int nbNewItems=nbItems-nbItemsLastLog;
        ret=Integer.valueOf(nbNewItems);
        //System.out.println("Added "+nbNewItems+" log item(s)!");
      }
      else
      {
        ret=Integer.valueOf(0);
      }
    }
    if (result)
    {
      pruneLogFiles();
    }
    else
    {
      String name=_toon.getName();
      _logger.error("Log update failed for toon ["+name+"]!");
    }
    return ret;
    */
    return null;
  }

  /**
   * Write a new log file for this toon.
   * @param log Log to write.
   * @return <code>true</code> it it succeeds, <code>false</code> otherwise.
   */
  public boolean writeNewLog(CharacterLog log)
  {
    boolean ret=true;
    File logFile=getNewLogFile();
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
      CharacterLogXMLWriter writer=new CharacterLogXMLWriter();
      ret=writer.write(logFile,log,EncodingNames.UTF_8);
    }
    return ret;
  }

  /*
  private boolean updateCharacterLog(CharacterLog log, String url)
  {
    boolean ret=false;
    int nbItems=log.getNbItems();
    Long stopDate=null;
    if (nbItems>0)
    {
      CharacterLogItem item=log.getLogItem(0);
      long date=item.getDate();
      stopDate=Long.valueOf(date);
    }
    CharacterLogPageParser parser=new CharacterLogPageParser();
    CharacterLog newLog=parser.parseLogPages(url,stopDate);
    if (newLog!=null)
    {
      int nbToRemove=0;
      if (stopDate!=null)
      {
        List<CharacterLogItem> items=log.getItemsOfDay(stopDate.longValue());
        nbToRemove=items.size();
      }
      
      // Merge items
      int nbNewItems=newLog.getNbItems();
      if (nbNewItems>=nbToRemove)
      {
        int nbRemoved=log.removeItemsOfDay(stopDate.longValue());
        if (_logger.isInfoEnabled())
        {
          _logger.info("Removed "+nbRemoved+" item(s)!");
        }
        for(int i=nbNewItems-1;i>=0;i--)
        {
          CharacterLogItem newItem=newLog.getLogItem(i);
          log.addLogItem(newItem,0);
        }
        if (_logger.isInfoEnabled())
        {
          _logger.info("Added "+nbNewItems+" item(s)!");
        }
        ret=true;
      }
    }
    return ret;
  }
  */

  private File getNewLogFile()
  {
    SimpleDateFormat sdf=new SimpleDateFormat(Formats.FILE_DATE_PATTERN);
    String filename="log "+sdf.format(new Date())+".xml";
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
    if ((filename.startsWith("log ")) && (filename.endsWith(".xml")))
    {
      filename=filename.substring(4,filename.length()-4);
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
