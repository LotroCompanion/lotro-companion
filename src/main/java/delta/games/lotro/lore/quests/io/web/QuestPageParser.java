package delta.games.lotro.lore.quests.io.web;

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
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.io.web.RewardsHTMLParser;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestDescription.FACTION;
import delta.games.lotro.lore.quests.QuestDescription.SIZE;
import delta.games.lotro.lore.quests.QuestDescription.TYPE;
import delta.games.lotro.utils.DownloadService;
import delta.games.lotro.utils.JerichoHtmlUtils;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for MyLotro.com quest page.
 * @author DAM
 */
public class QuestPageParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();
  private static final String PREREQUISITE_QUESTS="Prerequisite Quests";
  private static final String NEXT_QUESTS="Next Quests";
  private static final String QUEST_SEED="Quest:";
  private static final String QUEST_URL_SEED="/wiki/Quest:";

  private QuestDescription _quest;
  private String _key;

  private String cleanupFieldName(String key)
  {
    if (key==null) key="";
    key=key.trim();
    if (key.endsWith(":")) key=key.substring(0,key.length()-1);
    return key;
  }

  private void parseQuestField(Element questField)
  {
    List<Element> strongs=questField.getAllElements(HTMLElementName.STRONG);
    if ((strongs!=null) && (strongs.size()==1))
    {
      List<Segment> nodes=JerichoHtmlUtils.getChildNodes(questField);
      Element strong=strongs.get(0);
      String key=CharacterReference.decodeCollapseWhiteSpace(strong.getContent());
      key=cleanupFieldName(key);
      if ("Category".equals(key))
      {
        String value=nodes.get(3).toString().trim();
        _quest.setCategory(value);
      }
      else if ("Scope".equals(key))
      {
        String value=nodes.get(3).toString().trim();
        if ("n/a".equals(value)) value="";
        _quest.setQuestScope(value);
      }
      else if ("Minimum Level".equals(key))
      {
        String value=nodes.get(3).toString().trim();
        Integer minLevel=NumericTools.parseInteger(value);
        _quest.setMinimumLevel(minLevel);
      }
      else if ("Maximum Level".equals(key))
      {
        String value=nodes.get(3).toString().trim();
        Integer maxLevel=NumericTools.parseInteger(value);
        _quest.setMaximumLevel(maxLevel);
      }
      else if ("Required Classes".equals(key))
      {
        List<Element> as=questField.getAllElements(HTMLElementName.A);
        for(Element a : as)
        {
          String className=CharacterReference.decodeCollapseWhiteSpace(a.getContent());
          _quest.addRequiredClass(className);
        }
      }
      else if ((PREREQUISITE_QUESTS.equals(key)) || (NEXT_QUESTS.equals(key)))
      {
        List<Element> dds=questField.getAllElements("dd");
        if ((dds!=null) && (dds.size()==1))
        {
          parseQuestsList(dds.get(0),key);
        }
      }
      else if ("Arc(s)".equals(key))
      {
        String value=nodes.get(5).toString().trim();
        _quest.setQuestArc(value);
      }
      else if ("Required Races".equals(key))
      {
        List<Element> as=questField.getAllElements(HTMLElementName.A);
        for(Element a : as)
        {
          String race=CharacterReference.decodeCollapseWhiteSpace(a.getContent());
          _quest.addRequiredRace(race);
        }
      }
      else if ("Monster Play Quest".equals(key))
      {
        _quest.setFaction(FACTION.MONSTER_PLAY);
      }
      else
      {
        _logger.warn("Quest ["+_key+"]. Unmanaged quest field key ["+key+"]");
      }
    }
    else
    {
      parseQuestIcons(questField);
    }
  }

  private void parseQuestsList(Element dd,String category)
  {
    List<Element> as=dd.getAllElements(HTMLElementName.A);
    for(Element a : as)
    {
      String url=a.getAttributeValue("href");
      if ((url!=null) && (url.startsWith(QUEST_URL_SEED)))
      {
        String value=url.substring(QUEST_URL_SEED.length());
        if (PREREQUISITE_QUESTS.equals(category))
        {
          _quest.addPrerequisiteQuest(value);
        }
        else if (NEXT_QUESTS.equals(category))
        {
          _quest.addNextQuest(value);
        }
      }
    }
  }

  private void parseQuestIcons(Element questField)
  {
    List<Element> imgs=questField.getAllElements(HTMLElementName.IMG);
    for(Element img : imgs)
    {
      String title=img.getAttributeValue("title");
      if (title!=null)
      {
        if ("Epic".equals(title))
        {
          _quest.setType(TYPE.EPIC);
        }
        else if ("Small Fellowship".equals(title))
        {
          _quest.setSize(SIZE.SMALL_FELLOWSHIP);
        }
        else if ("Fellowship".equals(title))
        {
          _quest.setSize(SIZE.FELLOWSHIP);
        }
        else if ("Raid".equals(title))
        {
          _quest.setSize(SIZE.RAID);
        }
        else if ("Repeatable".equals(title))
        {
          _quest.setRepeatable(true);
        }
        else if ("Instance".equals(title))
        {
          _quest.setInstanced(true);
        }
        else
        {
          _logger.warn("Quest ["+_key+"]. Unmanaged quest icon title ["+title+"]");
        }
      }
    }
  }

  private void parseQuestDescription(Element officialSection)
  {
    // Title
    String title=null;
    Element titleElement=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(officialSection,HTMLElementName.DIV,"class","lorebooktitle");
    if (titleElement!=null)
    {
      title=CharacterReference.decodeCollapseWhiteSpace(titleElement.getContent());
      if (title!=null)
      {
        if (title.startsWith(QUEST_SEED))
        {
          title=title.substring(QUEST_SEED.length()).trim();
        }
      }
    }
    _quest.setTitle(title);
    // Quest attributes
    //System.out.println("Title: "+title);
    List<Element> questFields=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(officialSection,HTMLElementName.DIV,"class","questfield");
    for(Element questField : questFields)
    {
      parseQuestField(questField);
    }
    // Rewards
    //<table class="questrewards">
    Element rewardsTable=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(officialSection,HTMLElementName.TABLE,"class","questrewards");
    if (rewardsTable!=null)
    {
      Rewards rewards=_quest.getQuestRewards();
      RewardsHTMLParser parser=new RewardsHTMLParser("Quest ["+_key+"]");
      parser.parseRewards(rewardsTable,rewards);
    }
  }

  private QuestDescription parseQuestSection(Element questSection)
  {
    QuestDescription ret=null;
    try
    {
      _quest=new QuestDescription();
      Element officialSection=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(questSection,HTMLElementName.DIV,"class","officialsection");
      if (officialSection!=null)
      {
        parseQuestDescription(officialSection);
      }
      // Texts
      StringBuilder description=new StringBuilder();
      StringBuilder bestower=new StringBuilder();
      StringBuilder bestowerText=new StringBuilder();
      StringBuilder objectives=new StringBuilder();
      List<Element> textSections=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(questSection,HTMLElementName.DIV,"class","iteminfosection widget ui-corner-all");
      for(Element textSection : textSections)
      {
        Element titleSection=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(textSection,HTMLElementName.DIV,"class","widget-head ui-widget-header ui-corner-top");
        if (titleSection!=null)
        {
          String textSectionTitle=CharacterReference.decodeCollapseWhiteSpace(titleSection.getContent());
          Element contentsSection=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(textSection,HTMLElementName.DIV,"class","widget-body ui-widget-content ui-corner-bottom");
          if (contentsSection!=null)
          {
            StringBuilder link=null;
            StringBuilder text=null;
            if ("Description".equals(textSectionTitle))
            {
              text=description;
            }
            else if ("Bestower".equals(textSectionTitle))
            {
              link=bestower;
              text=bestowerText;
            }
            else if ("Objectives".equals(textSectionTitle))
            {
              text=objectives;
            }
            if (text!=null)
            {
              Element bestowerTag=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(contentsSection,HTMLElementName.DIV,"class","bestowertext");
              if (bestowerTag!=null)
              {
                String contents=JerichoHtmlUtils.getTextFromTag(bestowerTag);
                text.append(contents);
              }
            }
            if (link!=null)
            {
              Element bestowerLink=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(contentsSection,HTMLElementName.DIV,"class","bestowerlink");
              if (bestowerLink!=null)
              {
                String contents=JerichoHtmlUtils.getTextFromTag(bestowerLink);
                link.append(contents);
              }
            }
          }
        }
      }
      _quest.setDescription(description.toString().trim());
      _quest.setBestower(bestower.toString().trim());
      _quest.setBestowerText(bestowerText.toString().trim());
      _quest.setObjectives(objectives.toString().trim());
      ret=_quest;
      _quest=null;
    }
    catch(Exception e)
    {
      ret=null;
      _logger.error("Quest ["+_key+"]. Cannot parse quest section!",e);
    }
    return ret;
  }

  private void findIdentifiers(List<QuestDescription> quests)
  {
    String url="http://lorebook.lotro.com/index.php?title=Quest:"+_key+"&action=edit";
    DownloadService downloader=DownloadService.getInstance();
    try
    {
      String page=downloader.getPage(url);
      Source s=new Source(page);
      //<textarea id="wpTextbox1"
      Element pageSource=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(s,HTMLElementName.TEXTAREA,"id","wpTextbox1");
      String text=JerichoHtmlUtils.getTextFromTag(pageSource);
      parsePageSource(text,quests);
    }
    catch(Exception e)
    {
      _logger.error("Parsing error",e);
    }
  }

  private void parsePageSource(String text, List<QuestDescription> quests)
  {
    try
    {
      String seed="</noedit>";
      int index=text.indexOf(seed);
      if (index!=-1)
      {
        text=text.substring(0,index+seed.length());
      }
      DocumentBuilder builder=DocumentBuilderFactory.newInstance().newDocumentBuilder();
      InputSource is=new InputSource(new StringReader(text));
      Document doc=builder.parse(is);
      org.w3c.dom.Element root=doc.getDocumentElement();
      List<org.w3c.dom.Element> questTags=DOMParsingTools.getChildTagsByName(root,"quest",true);
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
   * Parse the quest page at the given URL.
   * @param url URL of quest page.
   * @return A list of quests or <code>null</code> if an error occurred.
   */
  public List<QuestDescription> parseQuestPage(String url)
  {
    List<QuestDescription> quests=null;
    try
    {
      DownloadService downloader=DownloadService.getInstance();
      String page=downloader.getPage(url);
      Source source=new Source(page);

      //<div id="lorebookNoedit">
      Element lorebook=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.DIV,"id","lorebookNoedit");
      if (lorebook!=null)
      {
        // identifier
        // <a id="ca-nstab-quest" class="lorebook_action_link" href="/wiki/Quest:A_Feminine_Curve_to_the_Steel">Article</a>
        _key=null;
        Element articleLink=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.A,"id","ca-nstab-quest");
        if (articleLink!=null)
        {
          String thisURL=articleLink.getAttributeValue("href");
          if ((thisURL!=null) && (thisURL.startsWith(QUEST_URL_SEED)))
          {
            _key=thisURL.substring(QUEST_URL_SEED.length()).trim();
          }
        }

        quests=new ArrayList<QuestDescription>();
        List<Element> questSections=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(lorebook,HTMLElementName.DIV,"class","lorebookquest");
        if ((questSections!=null) && (questSections.size()>0))
        {
          for(Element questSection : questSections)
          {
            QuestDescription quest=parseQuestSection(questSection);
            if (quest!=null)
            {
              //System.out.println(quest.dump());
              quests.add(quest);
              quest.setKey(_key);
            }
          }
        }
        findIdentifiers(quests);
      }
    }
    catch(Exception e)
    {
      quests=null;
      _logger.error("Cannot parse quest page ["+url+"]",e);
    }
    return quests;
  }
}
