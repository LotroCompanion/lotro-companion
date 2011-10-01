package delta.games.lotro.characterLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import delta.common.utils.files.TextFileReader;
import delta.common.utils.text.TextTools;
import delta.games.lotro.characterLog.LotroLogItem.LogItemType;

/**
 * @author DAM
 */
public class LotroActivityLogPageParser
{
  private static final String TABLE_START="<table class=\"gradient_table activitylog\">";
  private static final String DATE_ROW_START="<td class=\"date\">";
  private static final String DETAILS_ROW_START="<td class=\"details\">";

  public List<LotroLogItem> parseLogPage(File page)
  {
    List<LotroLogItem> ret=null;
    List<String> lines=TextFileReader.readAsLines(page);
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

  private List<LotroLogItem> parseLogItems(List<String> lines, int startIndex)
  {
    List<LotroLogItem> ret=new ArrayList<LotroLogItem>();
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
      LotroLogItem logItem=parseLogItem(lines,startRowIndex.intValue());
      if (logItem!=null)
      {
        ret.add(logItem);
      }
    }
    return ret;
  }
  
  private LotroLogItem parseLogItem(List<String> lines, int startRowIndex) {
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
    LotroLogItem ret=null;
    if ((dateStr!=null) && (type!=null) && (label!=null))
    {
      try
      {
        Calendar c=GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
        String[] items=dateStr.split("/");
        int year=Integer.parseInt(items[0]);
        int month=Integer.parseInt(items[1]);
        int day=Integer.parseInt(items[2]);
        label=label.trim();
        if (url!=null)
        {
          url=url.trim();
        }
        c.setTimeInMillis(0);
        c.set(year,month-1,day);
        long date=c.getTimeInMillis();
        ret=new LotroLogItem(date,type,label,url);
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
    return ret;
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
    }
    return type;
  }

  public static void main(String[] args)
  {
    File rootDir=new File("/home/dm/lotroPages/glumlug");
    File[] files=rootDir.listFiles();
    if (files!=null)
    {
      List<LotroLogItem> completeLog=new ArrayList<LotroLogItem>();
      LotroActivityLogPageParser parser=new LotroActivityLogPageParser();
      for(File file : files)
      {
        if (file.getName().endsWith(".html"))
        {
          List<LotroLogItem> items=parser.parseLogPage(file);
          if ((items!=null) && (items.size()>0))
          {
            completeLog.addAll(items);
            /*
            if (items.size()!=20)
            {
              System.out.println(items.size()+" "+file);
            }
            */
          }
        }
      }
      for(LotroLogItem logItem : completeLog)
      {
        System.out.println(logItem);
      }
      System.out.println(completeLog.size());
    }
  }
}
