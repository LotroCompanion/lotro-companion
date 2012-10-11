package delta.games.lotro.lore.deeds.io.web;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import delta.common.utils.NumericTools;
import delta.common.utils.text.TextTools;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.io.web.RewardsHTMLParser;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedDescription.TYPE;
import delta.games.lotro.utils.DownloadService;
import delta.games.lotro.utils.JerichoHtmlUtils;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for MyLotro.com deed page.
 * @author DAM
 */
public class DeedPageParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();
  private static final String DEED_URL_SEED="/wiki/Deed:";
  private static final String CLASS_URL_SEED="/wiki/Class:";
  private static final String LEVEL_FIELD_SEED="Level:";

  private DeedDescription _deed;
  private String _key;

  private String getTagContent(Element deedTooltip, String tagIdentifier)
  {
    String contents=null;
    Element element=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(deedTooltip,HTMLElementName.DIV,"class",tagIdentifier);
    if (element!=null)
    {
      contents=CharacterReference.decodeCollapseWhiteSpace(element.getContent());
    }
    return contents;
  }

  private TYPE getType(String typeStr)
  {
    TYPE ret = null;
    if ((typeStr!=null) && (typeStr.length()>0))
    {
      if ("Class".equalsIgnoreCase(typeStr))
      {
        ret=TYPE.CLASS;
      }
      else if ("Explorer".equalsIgnoreCase(typeStr))
      {
        ret=TYPE.EXPLORER;
      }
      else if ("Lore".equalsIgnoreCase(typeStr))
      {
        ret=TYPE.LORE;
      }
      else if ("Race".equalsIgnoreCase(typeStr))
      {
        ret=TYPE.RACE;
      }
      else if ("Reputation".equalsIgnoreCase(typeStr))
      {
        ret=TYPE.REPUTATION;
      }
      else if ("Slayer".equalsIgnoreCase(typeStr))
      {
        ret=TYPE.SLAYER;
      }
      else if ("Event".equalsIgnoreCase(typeStr))
      {
        ret=TYPE.EVENT;
      }
      else
      {
        _logger.warn("Deed ["+_key+"]. Unmanaged deed type information ["+typeStr+"]!");
      }
    }
    return ret;
  }

  private void parseDeedTooltip(Element deedTooltip)
  {
    // <td class="tooltipbody">
    Element body=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(deedTooltip,HTMLElementName.TD,"class","tooltipbody");
    if (body!=null)
    {
      // Deed name
      // <div class="deedname">Allies of the King</div>
      String name=getTagContent(body,"deedname");
      _deed.setName(name);
      // Deed description
      // <div class="itemsd">Unravel the epic story for Allies of the King.</div>
      String description=getTagContent(body,"itemsd");
      _deed.setDescription(description);
      // Type
      TYPE type=null;
      // <div class="green">Lore</div>
      String typeStr=getTagContent(body,"green");
      if (typeStr!=null)
      {
        // typeStr="<a href="/wiki/Class:Hunter">Hunter</a>"
        if (typeStr.contains(CLASS_URL_SEED))
        {
          String className=TextTools.findBetween(typeStr,CLASS_URL_SEED,"\">");
          _deed.setClassName(className);
          type=TYPE.CLASS;
        }
        else if ("Rune-keeper".equals(typeStr))
        {
          _deed.setClassName("Rune-keeper");
          type=TYPE.CLASS;
        }
        else
        {
          type=getType(typeStr);
        }
      }
      _deed.setType(type);
      // Level
      Integer minLevel=null;
      List<Element> tables=body.getAllElements(HTMLElementName.TABLE);
      for(Element table : tables)
      {
        List<Element> divs=table.getAllElements(HTMLElementName.DIV);
        for(Element div : divs)
        {
          String contents=CharacterReference.decodeCollapseWhiteSpace(div.getContent()).trim();
          if (contents.startsWith(LEVEL_FIELD_SEED))
          {
            String level=contents.substring(LEVEL_FIELD_SEED.length()).trim();
            minLevel=NumericTools.parseInteger(level,true);
          }
        }
      }
      _deed.setMinLevel(minLevel);
    }
  }

  private void parseDeedObjectives(Element deedSection)
  {
    // Texts
    List<Element> textSections=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(deedSection,HTMLElementName.DIV,"class","iteminfosection widget ui-corner-all");
    for(Element textSection : textSections)
    {
      Element titleSection=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(textSection,HTMLElementName.DIV,"class","widget-head ui-widget-header ui-corner-top");
      if (titleSection!=null)
      {
        String textSectionTitle=CharacterReference.decodeCollapseWhiteSpace(titleSection.getContent());
        if ("Objectives".equals(textSectionTitle))
        {
          Element contentsSection=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(textSection,HTMLElementName.DIV,"class","widget-body ui-widget-content ui-corner-bottom");
          if (contentsSection!=null)
          {
            String contents=JerichoHtmlUtils.getTextFromTag(contentsSection);
            _deed.setObjectives(contents);
          }
        }
        else if ("Rewards".equals(textSectionTitle))
        {
          Element rewardsTable=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(textSection,HTMLElementName.TABLE,"class","questrewards");
          if (rewardsTable!=null)
          {
            Rewards rewards=_deed.getRewards();
            RewardsHTMLParser parser=new RewardsHTMLParser("Deed ["+_key+"]");
            parser.parseRewards(rewardsTable,rewards);
          }
        }
      }
    }
  }

  private DeedDescription parseDeedSection(Element deedSection)
  {
    DeedDescription ret=new DeedDescription();
    try
    {
      _deed=new DeedDescription();
      Element deedTooltip=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(deedSection,HTMLElementName.TABLE,"class","tooltip");
      if (deedTooltip!=null)
      {
        parseDeedTooltip(deedTooltip);
      }
      parseDeedObjectives(deedSection);
      ret=_deed;
      _deed=null;
    }
    catch(Exception e)
    {
      ret=null;
      _logger.error("Deed ["+_key+"]. Cannot parse deed section!",e);
    }
    return ret;
  }

  private String fetchIdentifier(Segment root)
  {
    String id=null;
    Element articleTop=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(root,HTMLElementName.DIV,"id","article_top");
    if (articleTop!=null)
    {
      Element article=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(articleTop,HTMLElementName.A,"class","lorebook_action_link");
      if (article!=null)
      {
        String href=article.getAttributeValue("href");
        if (href!=null)
        {
          if (href.startsWith(DEED_URL_SEED))
          {
            id=href.substring(DEED_URL_SEED.length());
          }
        }
      }
    }
    return id;
  }

  private void findIdentifiers(List<DeedDescription> deeds)
  {
    String url="http://lorebook.lotro.com/index.php?title=Deed:"+_key+"&action=edit";
    DownloadService downloader=DownloadService.getInstance();
    try
    {
      String page=downloader.getPage(url);
      Source s=new Source(page);
      //<textarea id="wpTextbox1"
      Element pageSource=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(s,HTMLElementName.TEXTAREA,"id","wpTextbox1");
      String text=JerichoHtmlUtils.getTextFromTag(pageSource);
      parsePageSource(text,deeds);
    }
    catch(Exception e)
    {
      _logger.error("Parsing error",e);
    }
  }

  private void parsePageSource(String text, List<DeedDescription> quests)
  {
    try
    {
      String seed="</noedit>";
      int index=text.indexOf(seed);
      if (index!=-1)
      {
        text=text.substring(0,index+seed.length());
      }
      text=text.replace("&"," ");
      DocumentBuilder builder=DocumentBuilderFactory.newInstance().newDocumentBuilder();
      InputSource is=new InputSource(new StringReader(text));
      Document doc=builder.parse(is);
      org.w3c.dom.Element root=doc.getDocumentElement();
      List<org.w3c.dom.Element> questTags=DOMParsingTools.getChildTagsByName(root,"accomplishment",true);
      int nbQuestTags=0;
      if (questTags!=null)
      {
        nbQuestTags=questTags.size();
      }
      int nbQuests=quests.size();
      if (nbQuestTags==nbQuests)
      {
        int questIndex=0;
        for(org.w3c.dom.Element questTag : questTags)
        {
          String idStr=DOMParsingTools.getStringAttribute(questTag.getAttributes(),"id",null);
          int id=NumericTools.parseInt(idStr,-1);
          if (id!=-1)
          {
            quests.get(questIndex).setIdentifier(id);
          }
          else
          {
            _logger.error("Bad identifier ["+idStr+"] for quest key ["+_key+"] index "+questIndex);
          }
          questIndex++;
        }
      }
      else
      {
        _logger.error("Bad number of quest identifiers for quest key ["+_key+"]. Expected "+nbQuests+", got "+nbQuestTags);
      }
    }
    catch (Exception e)
    {
      _logger.error("Parsing error",e);
    }
  }

  /**
   * Parse the deed page at the given URL.
   * @param url URL of deed page.
   * @return A list of deeds or <code>null</code> if an error occurred.
   */
  public List<DeedDescription> parseDeedPage(String url)
  {
    List<DeedDescription> deeds=null;
    try
    {
      DownloadService downloader=DownloadService.getInstance();
      String page=downloader.getPage(url);
      Source source=new Source(page);

      //<div id="lorebookNoedit">
      Element lorebook=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.DIV,"id","lorebookNoedit");
      if (lorebook!=null)
      {
        _key=fetchIdentifier(source);
        deeds=new ArrayList<DeedDescription>();
        List<Element> deedSections=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(lorebook,HTMLElementName.DIV,"class","lorebookquest");
        if ((deedSections!=null) && (deedSections.size()>0))
        {
          for(Element deedSection : deedSections)
          {
            DeedDescription deed=parseDeedSection(deedSection);
            if (deed!=null)
            {
              deeds.add(deed);
              deed.setKey(_key);
            }
          }
        }
        findIdentifiers(deeds);
      }
    }
    catch(Exception e)
    {
      _logger.error("Cannot parse deed page ["+url+"]",e);
    }
    return deeds;
  }
}
