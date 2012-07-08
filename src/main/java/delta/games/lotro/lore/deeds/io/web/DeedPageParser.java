package delta.games.lotro.lore.deeds.io.web;

import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

import org.apache.log4j.Logger;

import delta.common.utils.NumericTools;
import delta.common.utils.text.TextTools;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.io.web.RewardsHTMLParser;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedDescription.TYPE;
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
  private String _identifier;

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
        _logger.warn("Deed ["+_identifier+"]. Unmanaged deed type information ["+typeStr+"]!");
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
            RewardsHTMLParser parser=new RewardsHTMLParser("Deed ["+_identifier+"]");
            parser.parseRewards(rewardsTable,rewards);
          }
        }
      }
    }
  }

  private DeedDescription parseDeedSection(String identifier, Element deedSection)
  {
    DeedDescription ret=new DeedDescription();
    try
    {
      _identifier=identifier;
      _deed=new DeedDescription();
      _deed.setIdentifier(identifier);
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
      _logger.error("Deed ["+_identifier+"]. Cannot parse deed section!",e);
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

  /**
   * Parse the deed page at the given URL.
   * @param url URL of deed page.
   * @return A deed or <code>null</code> if an error occurred.
   */
  public DeedDescription parseDeedPage(String url)
  {
    DeedDescription deed=null;
    try
    {
      // TODO: fetch page text first (centralized downloader to cope with proxy problems?)
      Source source=new Source(new URL(url));

      //<div id="lorebookNoedit">
      Element lorebook=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.DIV,"id","lorebookNoedit");
      if (lorebook!=null)
      {
        String identifier=fetchIdentifier(source);
        deed=parseDeedSection(identifier,lorebook);
      }
    }
    catch(Exception e)
    {
      _logger.error("Cannot parse deed page ["+url+"]",e);
    }
    return deed;
  }
}
