package delta.games.lotro.character.log.io.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import delta.common.utils.text.TextTools;
import delta.common.utils.text.TextUtils;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for LOTRO character log HTML pages. 
 * @author DAM
 */
public class CharacterLogPageParser
{
  private static final Logger _logger=LotroLoggers.getCharacterLogLogger();

  private static final String TABLE_START="<table class=\"gradient_table activitylog\">";
  private static final String DATE_ROW_START="<td class=\"date\">";
  private static final String DETAILS_ROW_START="<td class=\"details\">";
  private static final String COMPLETED_SEED="Completed '";
  private static final String REACHED_LEVEL_SEED="Reached level ";

  /**
   * Parse an HTML page.
   * @param page Page to parse.
   * @return A list of character log items, or <code>null</code> if a problem occured.
   */
  public List<CharacterLogItem> parseLogPage(File page)
  {
    List<CharacterLogItem> ret=null;
    List<String> lines=TextUtils.readAsLines(page);
    int tableStartIndex=-1;
    int index=0;
    for(String line : lines)
    {
      if (line.trim().startsWith(TABLE_START))
      {
        tableStartIndex=index;
        break;
      }
      index++;
    }
    if (tableStartIndex!=-1)
    {
      ret=parseLogItems(lines, tableStartIndex);
    }
    return ret;
  }

  private List<CharacterLogItem> parseLogItems(List<String> lines, int startIndex)
  {
    List<CharacterLogItem> ret=new ArrayList<CharacterLogItem>();
    int nbLines=lines.size();
    List<Integer> startRowIndexes=new ArrayList<Integer>();
    for(int index=startIndex;index<nbLines;index++)
    {
      String line=lines.get(index);
      if (line.trim().startsWith("<tr>"))
      {
        startRowIndexes.add(Integer.valueOf(index));
      }
    }
    if (startRowIndexes.size()>0)
    {
      startRowIndexes.remove(0);
    }
    //System.out.println(startRowIndexes);
    for(Integer startRowIndex : startRowIndexes)
    {
      CharacterLogItem logItem=parseLogItem(lines,startRowIndex.intValue());
      if (logItem!=null)
      {
        ret.add(logItem);
      }
    }
    return ret;
  }
  
  private CharacterLogItem parseLogItem(List<String> lines, int startRowIndex) {
    String dateStr=null,label=null,url=null;
    LogItemType type=null;
    int nbLines=lines.size();
    for(int index=startRowIndex;index<nbLines;index++)
    {
      String line=lines.get(index).trim();
      if (line.startsWith(DATE_ROW_START))
      {
        dateStr=TextTools.findBetween(line,DATE_ROW_START,"</td>");
      }
      else if (line.startsWith(DETAILS_ROW_START))
      {
        for(int index2=index;index2<nbLines;index2++,index++)
        {
          line=lines.get(index2).trim();
          if (line.contains("images/icons/log/icon_"))
          {
            url=TextTools.findBetween(line,"<a href=\"","\">");
            String imgSrc=TextTools.findBetween(line,"<img src=\"","\" />");
            type=findType(imgSrc);
            label=TextTools.findAfter(line,".png\" />");
            if ((label!=null) && (label.endsWith("</a>")))
            {
              label=label.substring(0,label.length()-4);
            }
            break;
          }
        }
        break;
      }
    }
    CharacterLogItem ret=null;
    if ((dateStr!=null) && (type!=null) && (label!=null))
    {
      try
      {
        Calendar c=GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
        String[] items=dateStr.split("/");
        int year=Integer.parseInt(items[0]);
        int month=Integer.parseInt(items[1]);
        int day=Integer.parseInt(items[2]);
        label=tuneLabel(label);
        if (url!=null)
        {
          url=url.trim();
        }
        c.setTimeInMillis(0);
        c.set(year,month-1,day);
        long date=c.getTimeInMillis();
        ret=new CharacterLogItem(date,type,label,url);
      }
      catch(Exception e)
      {
        _logger.error("Cannot parse LOTRO character log item!",e);
      }
    }
    return ret;
  }

  private String tuneLabel(String label)
  {
    label=label.trim();
    if (label.startsWith(COMPLETED_SEED))
    {
      label=label.substring(COMPLETED_SEED.length());
      if (label.endsWith("'"))
      {
        label=label.substring(0,label.length()-1);
      }
    }
    else if (label.startsWith(REACHED_LEVEL_SEED))
    {
      label=label.substring(REACHED_LEVEL_SEED.length());
    }
    label=label.trim();
    return label;
  }
  private LogItemType findType(String imgSrc)
  {
    LogItemType type=LogItemType.UNKNOWN;
    if (imgSrc!=null)
    {
      if (imgSrc.endsWith("icon_quest.png"))
      {
        type=LogItemType.QUEST;
      }
      else if (imgSrc.endsWith("icon_profession.png"))
      {
        type=LogItemType.PROFESSION;
      }
      else if (imgSrc.endsWith("icon_levelup.png"))
      {
        type=LogItemType.LEVELUP;
      }
      else if (imgSrc.endsWith("icon_deed.png"))
      {
        type=LogItemType.DEED;
      }
      else if (imgSrc.endsWith("icon_vocation.png"))
      {
        type=LogItemType.VOCATION;
      }
      else
      {
        _logger.warn("Unmanaged icon type ["+imgSrc+"]");
      }
    }
    return type;
  }
}
