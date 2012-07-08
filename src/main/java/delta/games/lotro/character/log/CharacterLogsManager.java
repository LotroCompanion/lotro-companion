package delta.games.lotro.character.log;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.character.log.io.web.CharacterLogPageParser;
import delta.games.lotro.character.log.io.xml.CharacterLogXMLParser;
import delta.games.lotro.character.log.io.xml.CharacterLogXMLWriter;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.quests.QuestsManager;
import delta.games.lotro.quests.io.web.MyLotroURL2Identifier;
import delta.games.lotro.utils.Escapes;
import delta.games.lotro.utils.LotroLoggers;
import delta.games.lotro.utils.resources.ResourcesMapping;

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
   * Get the most recent log file.
   * @return a file or <code>null</code> if there is no log file for this toon.
   */
  public File getLastLogFile()
  {
    File lastLog=null;
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
      File[] files=characterDir.listFiles(filter);
      if ((files!=null) && (files.length>0))
      {
        Arrays.sort(files);
        lastLog=files[files.length-1];
      }
    }
    return lastLog;
  }

  /**
   * Update character log.
   * @return <code>true</code> if it was successfull, <code>false</code> otherwise.
   */
  public boolean updateLog()
  {
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
      if (log!=null)
      {
        updateOK=true;
        int nb=log.getNbItems();
        for(int i=0;i<nb;i++)
        {
          CharacterLogItem item=log.getLogItem(i);
          updateIdentifier(item);
        }
      }
      else
      {
        updateOK=false;
      }
    }
    boolean ret=updateOK;
    if (updateOK)
    {
      int nbItems=log.getNbItems();
      if ((creation) || (nbItems!=nbItemsLastLog))
      {
        ret=writeNewLog(log);
        int nbNewItems=nbItems-nbItemsLastLog;
        System.out.println("Added "+nbNewItems+" log item(s)!");
      }
    }
    if (!ret)
    {
      String name=_toon.getName();
      _logger.error("Log update failed for toon ["+name+"]!");
    }
    return ret;
  }

  /**
   * Update quest identifiers.
   */
  public void updateIdentifiers()
  {
    File lastLog=getLastLogFile();
    if (lastLog!=null)
    {
      CharacterLogXMLParser xmlLogParser=new CharacterLogXMLParser();
      CharacterLog log=xmlLogParser.parseXML(lastLog);
      if (log!=null)
      {
        int nbItems=log.getNbItems();
        for(int i=0;i<nbItems;i++)
        {
          CharacterLogItem item=log.getLogItem(i);
          LogItemType type=item.getLogItemType();
          if ((type==LogItemType.DEED) || (type==LogItemType.QUEST))
          {
            String oldId=item.getIdentifier();
            if (oldId==null)
            {
              updateIdentifier(item);
            }
            else
            {
              oldId=Escapes.escapeIdentifier(oldId);
              item.setIdentifier(oldId);
            }
          }
        }
        writeNewLog(log);
      }
    }
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
          updateIdentifier(newItem);
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

  private void updateIdentifier(CharacterLogItem item)
  {
    if (item!=null)
    {
      LogItemType type=item.getLogItemType();
      if ((type==LogItemType.QUEST) || (type==LogItemType.DEED))
      {
        String itemURL=item.getAssociatedUrl();
        if ((itemURL!=null) && (itemURL.length()>0))
        {
          String id=null;
          if (type==LogItemType.QUEST)
          {
            QuestsManager questsManager=QuestsManager.getInstance();
            ResourcesMapping mapping=questsManager.getQuestResourcesMapping();
            int resource=mapping.getResourceIdentifierFromURL(itemURL);
            if (resource!=-1)
            {
              id=mapping.getIdentifier(resource);
            }
          }
          else if (type==LogItemType.DEED)
          {
            DeedsManager deedsManager=DeedsManager.getInstance();
            ResourcesMapping mapping=deedsManager.getDeedResourcesMapping();
            int resource=mapping.getResourceIdentifierFromURL(itemURL);
            if (resource!=-1)
            {
              id=mapping.getIdentifier(resource);
            }
          }
          if (id==null)
          {
            MyLotroURL2Identifier finder=new MyLotroURL2Identifier();
            id=finder.findIdentifier(itemURL);
            if (id!=null)
            {
              id=Escapes.escapeIdentifier(id);
            }
          }
          if (id!=null)
          {
            item.setIdentifier(id);
            _logger.info("Found id ["+id+"] for URL ["+itemURL+"]");
          }
          else
          {
            _logger.info("No match for ["+item+"]");
          }
        }
      }
    }
  }

  private File getNewLogFile()
  {
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HHmm");
    String filename="log "+sdf.format(new Date())+".xml";
    File characterDir=_toon.getRootDir();
    File logFile=new File(characterDir,filename);
    return logFile;
  }
}
