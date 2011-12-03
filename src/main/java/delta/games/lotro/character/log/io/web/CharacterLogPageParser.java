package delta.games.lotro.character.log.io.web;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.TextExtractor;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import delta.common.utils.NumericTools;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.utils.JerichoHtmlUtils;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for MyLotro.com character log pages.
 * @author DAM
 */
public class CharacterLogPageParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  private static final String COMPLETED_SEED="Completed '";
  private static final String REACHED_LEVEL_SEED="Reached level ";

  private String _characterName;
  private Long _stopDate;
  private boolean _stop;

  /**
   * Parse the character page at the given URL.
   * @param url URL of character page.
   * @param stopDate Ignore items strictly before the given date.
   * <code>null</code> means no filtering.
   * @return A character or <code>null</code> if an error occurred..
   */
  public CharacterLog parseLogPages(String url, Long stopDate)
  {
    _characterName=null;
    _stopDate=stopDate;
    _stop=false;
    String rootURL=url+"/activitylog?"+URLEncoder.encode("cl[pp]");
    int nbPages=parseFirstPage(rootURL);
    CharacterLog log=null;
    if (nbPages>0)
    {
      log=new CharacterLog("");
      for(int i=1;i<=nbPages;i++)
      {
        parseLogPage(log, rootURL, i);
        if (_stop)
        {
          break;
        }
      }
      if (_characterName!=null)
      {
        log.setName(_characterName);
      }
    }
    return log;
  }
  
  private int parseFirstPage(String rootUrl)
  {
    int nbPages=0;
    String firstPageURL=rootUrl+"=1";
    try
    {
      Source source=new Source(new URL(firstPageURL));
  
      //<div id="widget_" class="widget ui-widget ui-corner-all" type="activitylog">
      Element activityLog=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.DIV,"type","activitylog");
      if (activityLog!=null)
      {
        // Fetch page numbers
        //<div class="pagination">
        Element pagination=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.DIV,"class","pagination");
        if (pagination!=null)
        {
          //<td class="current"> Page 1 of 121 </td>
          String pages=JerichoHtmlUtils.getTagContents(source,HTMLElementName.TD,"class","current");
          if (pages!=null)
          {
            String[] items=pages.split(" ");
            if ((items!=null) && (items.length==4))
            {
              nbPages=NumericTools.parseInt(items[3],0);
            }
          }
        }
      }
    }
    catch(Exception e)
    {
      _logger.error("Cannot parse character log page ["+firstPageURL+"]",e);
    }
    return nbPages;
  }

  private boolean parseLogPage(CharacterLog log, String rootURL, int pageNumber)
  {
    boolean ret=false;
    int maxTries=3;
    int retryNumber=0;
    while(retryNumber<maxTries)
    {
      boolean ok=parseLogPage(log,rootURL,pageNumber,retryNumber);
      if (ok)
      {
        ret=true;
        break;
      }
      retryNumber++;
    }
    if (!ret)
    {
      if (_logger.isEnabledFor(Level.ERROR))
      {
        String name=log.getName(); 
        _logger.error("Cannot parse character log page #"+pageNumber+" rootURL=["+rootURL+"] name=["+name+"]");
      }
    }
      
    return ret;
  }

  private boolean parseLogPage(CharacterLog log, String rootURL, int pageNumber, int retryNumber)
  {
    if (_logger.isInfoEnabled())
    {
      _logger.info("Page #"+pageNumber+((retryNumber>0)?" try #"+retryNumber:""));
    }
    boolean ret;
    String url=rootURL+"="+String.valueOf(pageNumber);
    try
    {
      Source source=new Source(new URL(url));
      //<table class="gradient_table activitylog">
      Element logTable=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.TABLE,"class","gradient_table activitylog");
      if (logTable!=null)
      {
        List<Element> trs=logTable.getAllElements(HTMLElementName.TR);
        if ((trs!=null) && (trs.size()>=1))
        {
          // ignore first (table headers)
          trs.remove(0);
          for(Element tr : trs)
          {
            CharacterLogItem item=parseLogItem(tr);
            if (item!=null)
            {
              boolean addIt=true;
              long date=item.getDate();
              if (_stopDate!=null)
              {
                if (date<_stopDate.longValue())
                {
                  addIt=false;
                  _stop=true;
                  break;
                }
              }
              if (addIt)
              {
                log.addLogItem(item);
              }
            }
          }
        }
      }
      ret=true;
    }
    catch(Exception e)
    {
      _logger.warn("Cannot parse character page ["+url+"]",e);
      ret=false;
    }
    return ret;
  }

  private CharacterLogItem parseLogItem(Element tr)
  {
/*
<td class="char">
<a href="/home/character/2427907/146366987891794854">Glumlug</a>
</td>
<td class="date">2011/11/24</td>
<td class="details">
<img src="http://content.turbine.com/sites/playerportal/modules/lotro-base/images/icons/log/icon_levelup.png">
Reached level 70
</td>
 */
/*
<td class="char">
<a href="/home/character/2427907/146366987891794854">Glumlug</a>
</td>
<td class="date">2011/11/24</td>
<td class="details">
<a href="http://lorebook.lotro.com/wiki/Special:LotroResource?id=1879208735">
<img src="http://content.turbine.com/sites/playerportal/modules/lotro-base/images/icons/log/icon_quest.png">
Completed 'The Practiced Arm'
</a>
</td>
 */
    
    CharacterLogItem ret=null;
    List<Element> tds=tr.getAllElements(HTMLElementName.TD);
    if ((tds!=null) && (tds.size()==3))
    {
      if (_characterName==null)
      {
        Element charName=tds.get(0);
        _characterName=JerichoHtmlUtils.getTagContents(charName,HTMLElementName.A);
      }
      Element tdDate=tds.get(1);
      String dateStr=CharacterReference.decodeCollapseWhiteSpace(tdDate.getContent());
      LogItemType type=null;
      Element tdDetails=tds.get(2);
      List<Element> imgs=tdDetails.getAllElements(HTMLElementName.IMG);
      Element img=null;
      if ((imgs!=null) && (imgs.size()==1))
      {
        img=imgs.get(0);
        String imgSrc=img.getAttributeValue("src");
        type=findType(imgSrc);
      }
      String url=null;
      Element a=tdDetails.getFirstElement(HTMLElementName.A);
      if (a!=null)
      {
        url=a.getAttributeValue("href");
      }
      TextExtractor extractor=tdDetails.getTextExtractor();
      if (img!=null) extractor.excludeElement(img.getStartTag());
      if (a!=null) extractor.excludeElement(a.getStartTag());
      String label=extractor.toString().trim();
      
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
      else if (imgSrc.endsWith("icon_pvmp.png"))
      {
        // Example label: Reached rank 'Footman'
        type=LogItemType.PVMP;
      }
      else
      {
        _logger.warn("Unmanaged icon type ["+imgSrc+"]");
      }
    }
    return type;
  }
}
