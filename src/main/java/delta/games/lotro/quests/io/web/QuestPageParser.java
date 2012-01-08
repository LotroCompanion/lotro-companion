package delta.games.lotro.quests.io.web;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.TextExtractor;

import org.apache.log4j.Logger;

import delta.common.utils.NumericTools;
import delta.games.lotro.common.Faction;
import delta.games.lotro.common.Money;
import delta.games.lotro.common.Reputation;
import delta.games.lotro.common.ReputationItem;
import delta.games.lotro.common.Skill;
import delta.games.lotro.common.Title;
import delta.games.lotro.common.Trait;
import delta.games.lotro.common.Skill.SkillType;
import delta.games.lotro.common.objects.ObjectItem;
import delta.games.lotro.common.objects.ObjectsSet;
import delta.games.lotro.quests.QuestDescription;
import delta.games.lotro.quests.QuestDescription.SIZE;
import delta.games.lotro.quests.QuestDescription.TYPE;
import delta.games.lotro.quests.QuestRewards;
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
  private static final String RECEIVE_KEY="Receive";
  private static final String SELECT_ONE_OF_KEY="Select one of";
  private static final String TRAIT_URL_SEED="Trait:";
  private static final String PASSIVE_SKILL_URL_SEED="Passive_Skill:";
  private static final String URL_SEED="/wiki/";
  private static final String TITLE_URL_SEED="/wiki/Title:";
  private static final String QUEST_URL_SEED="/wiki/Quest:";

  private QuestDescription _quest;
  private String _identifier;

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
      // TODO: "Monster Play Quest"
      else
      {
        _logger.warn("Quest ["+_identifier+"]. Unmanaged quest field key ["+key+"]");
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
        else if ("Repeatable".equals(title))
        {
          _quest.setRepeatable(true);
        }
        else if ("Raid".equals(title))
        {
          _quest.setType(TYPE.RAID);
        }
        // TODO: "Instance"
        else
        {
          _logger.warn("Quest ["+_identifier+"]. Unmanaged quest icon title ["+title+"]");
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
      parseRewards(rewardsTable);
    }
  }

  private void parseRewards(Element rewardsTable)
  {
    List<Element> rewards=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(rewardsTable,HTMLElementName.DIV,"class","questReward");
    for(Element reward : rewards)
    {
      parseReward(reward);
    }
  }

  private Money parseMoneyReward(Element rewardDiv)
  {
    //System.out.println("Money reward!");
    Money m=new Money();
    List<Element> elements=rewardDiv.getChildElements();
    if ((elements!=null) && (elements.size()==2))
    {
      Element moneyNode=elements.get(1);
      List<Segment> nodes=JerichoHtmlUtils.getChildNodes(moneyNode);
      int nb=nodes.size();
      for(int i=0;i<nb;i++)
      {
        Segment s=nodes.get(i);
        if (s.getClass()==Segment.class)
        {
          int nbCoins=NumericTools.parseInt(s.toString(),0);
          if ((nbCoins>0) && (i<nb-1))
          {
            Segment tmp=nodes.get(i+1);
            if (tmp instanceof StartTag)
            {
              String className=((StartTag)tmp).getAttributeValue("class");
              if ("coin".equals(className))
              {
                String type=((StartTag)tmp).getAttributeValue("src");
                if (type!=null)
                {
                  if (type.contains("silver"))
                  {
                    m.setSilverCoins(nbCoins);
                  }
                  else if (type.contains("copper"))
                  {
                    m.setCopperCoins(nbCoins);
                  }
                  else if (type.contains("gold"))
                  {
                    m.setGoldCoins(nbCoins);
                  }
                  else
                  {
                    _logger.warn("Quest ["+_identifier+"]. Unknown coin type ["+type+"]");
                  }
                }
              }
            }
          }
        }
      }
    }
    return m;
    /*
    <div class="questReward">
    <div>
    <strong>Money:</strong>
    </div>
    <div>
    29
    <img class="coin" src="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/currency/silver.gif">
    &nbsp;5
    <img class="coin" src="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/currency/copper.gif">
    </div>
    </div>
    */    
  }

  private void parseTraitReward(Element rewardDiv)
  {
    List<Element> as=rewardDiv.getAllElements(HTMLElementName.A);
    int size=(as!=null)?as.size():0;
    if (size==2)
    {
      Element secondA=as.get(1);
      String name=CharacterReference.decodeCollapseWhiteSpace(secondA.getContent());
      //String iconURL=null;
      Element firstA=as.get(0);
      String url=firstA.getAttributeValue("href");
      if ((url!=null) && (url.startsWith(URL_SEED)))
      {
        String qualifiedIdentifier=url.substring(URL_SEED.length()).trim();
        if (qualifiedIdentifier.startsWith(TRAIT_URL_SEED))
        {
          String identifier=qualifiedIdentifier.substring(TRAIT_URL_SEED.length()).trim();
          Trait trait=new Trait(identifier,name);
          _quest.getQuestRewards().addTrait(trait);
          //System.out.println(trait.dump());
        }
        else if (qualifiedIdentifier.startsWith(PASSIVE_SKILL_URL_SEED))
        {
          String identifier=qualifiedIdentifier.substring(PASSIVE_SKILL_URL_SEED.length()).trim();
          Skill skill=new Skill(SkillType.PASSIVE,identifier,name);
          _quest.getQuestRewards().addSkill(skill);
          //System.out.println(skill.dump());
        }
        else
        {
          _logger.warn("Quest ["+_identifier+"]. Unmanaged trait/skill identifier ["+qualifiedIdentifier+"]");
        }
        /*
        List<Element> imgs=firstA.getAllElements(HTMLElementName.IMG);
        if ((imgs!=null) && (imgs.size()>=1))
        {
          Element img=imgs.get(0);
          iconURL=img.getAttributeValue("src");
        }
        */
      }
      else
      {
        _logger.warn("Quest ["+_identifier+"]. Malformed URL ["+url+"]");
      }
    }
    else
    {
      _logger.warn("Quest ["+_identifier+"]. Trait reward with "+size+" anchor tags!");
    }
    /*
    <div class="questReward">
    <div>
    <strong>Traits:</strong>
    </div>
    <div>
    <a href="/wiki/Trait:Expert_Woodworker_Proficiency">
    <img class="icon" src="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/trait/trait_c_craft_woodworker_complete_proficiency_3.png">
    </a>
    <a href="/wiki/Trait:Expert_Woodworker_Proficiency">Expert Woodworker Proficiency</a>
    </div>
    </div>
     */
  }

  private void parseTitleReward(Element rewardDiv)
  {
    List<Element> as=rewardDiv.getAllElements(HTMLElementName.A);
    int size=(as!=null)?as.size():0;
    if (size==1)
    {
      String name=null;
      String titleIdentifier=null;
      //String iconURL=null;
      Element firstA=as.get(0);
      String url=firstA.getAttributeValue("href");
      if ((url!=null) && (url.startsWith(TITLE_URL_SEED)))
      {
        titleIdentifier=url.substring(TITLE_URL_SEED.length()).trim();
      }
      name=CharacterReference.decodeCollapseWhiteSpace(firstA.getContent());
      if ((name!=null) && (titleIdentifier!=null))
      {
        Title title=new Title(titleIdentifier,name);
        _quest.getQuestRewards().addTitle(title);
      }
    }
    else
    {
      _logger.warn("Quest ["+_identifier+"]. Title with "+size+" anchor tags!");
    }
/*
<div class="questReward">
<div>
<strong>Titles:</strong>
</div>
<div>
<a href="/wiki/Title:_Honorary_Shirriff"> Honorary Shirriff</a>
</div>
</div>
 */
  }

  private Reputation parseReputationReward(Element rewardDiv)
  {
    //System.out.println("Reputation reward!");
    Reputation r=new Reputation();
    List<Element> elements=rewardDiv.getChildElements();
    if ((elements!=null) && (elements.size()==2))
    {
      Element reputationNode=elements.get(1);
      List<Segment> nodes=JerichoHtmlUtils.getChildNodes(reputationNode);
      int nbNodes=nodes.size();
      int nbItems=nbNodes/4;
      for(int i=0;i<nbItems;i++)
      {
        Segment valueNode=nodes.get(i*4+1);
        Segment factionNode=nodes.get(i*4+3);
        if ((valueNode.getClass()==Segment.class) && (factionNode.getClass()==Segment.class))
        {
          String valueStr=valueNode.toString();
          valueStr=valueStr.replace("with","").trim();
          valueStr=valueStr.replace("+","").trim();
          int reputation=NumericTools.parseInt(valueStr,0);
          String factionName=factionNode.toString().trim();
          Faction faction=Faction.getByName(factionName);
          ReputationItem item=new ReputationItem(faction);
          item.setAmount(reputation);
          r.add(item);
        }
      }
    }
    return r;
    /*
<div class="questReward">
<div>
<strong>Reputation:</strong>
</div>
<div>
<img class="icon" src="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/reputation_increase.gif">
+700 with
<a href="/wiki/Faction:Malledhrim">Malledhrim</a>
</div>
</div>
     */
  }

  private void parseItemReward(Element rewardDiv)
  {
    QuestRewards rewards=_quest.getQuestRewards();
    ObjectsSet objects=null;
    //System.out.println("Item reward!");
    String key="";
    List<Element> divs=rewardDiv.getAllElements(HTMLElementName.DIV);
    divs.remove(0); // remove reward div
    for(Element div : divs)
    {
      List<Element> strongs=div.getAllElements(HTMLElementName.STRONG);
      if (strongs.size()>0)
      {
        Element strong=strongs.get(0);
        key=CharacterReference.decodeCollapseWhiteSpace(strong.getContent());
        key=cleanupFieldName(key);
        if (RECEIVE_KEY.equals(key))
        {
          objects=rewards.getObjects();
        }
        else if (SELECT_ONE_OF_KEY.equals(key))
        {
          objects=rewards.getSelectObjects();
        }
        else
        {
          _logger.warn("Quest ["+_identifier+"]. Unmanaged object selection key ["+key+"]");
        }
      }
      else
      {
        List<Element> as=div.getAllElements(HTMLElementName.A);
        if ((as!=null) && (as.size()==2))
        {
          Element iconItem=as.get(0);
          String iconURL=null;
          List<Element> imgs=iconItem.getAllElements(HTMLElementName.IMG);
          if ((imgs!=null) && (imgs.size()==1))
          {
            Element img=imgs.get(0);
            iconURL=img.getAttributeValue("src");
            //System.out.println("Icon: "+iconURL);
          }
          Element textItem=as.get(1);
          String itemName=CharacterReference.decodeCollapseWhiteSpace(textItem.getContent());
          String url=textItem.getAttributeValue("href");
          ObjectItem item=new ObjectItem(itemName);
          item.setObjectURL(url);
          item.setIconURL(iconURL);
          
          int quantity=1;
          TextExtractor extractor=div.getTextExtractor();
          extractor.excludeElement(iconItem.getStartTag());
          extractor.excludeElement(textItem.getStartTag());
          String text=extractor.toString();
          int factorIndex=text.indexOf("(x");
          if (factorIndex!=-1)
          {
            int parenthesisIndex=text.indexOf(')',factorIndex+2);
            if (parenthesisIndex!=-1)
            {
              String factorStr=text.substring(factorIndex+2,parenthesisIndex);
              quantity=NumericTools.parseInt(factorStr,1);
            }
          }
          if (objects!=null)
          {
            objects.addObject(item,quantity);
          }
          else
          {
            _logger.warn("Quest ["+_identifier+"]. Ignored object ["+item+"], quantity="+quantity);
          }
          //System.out.println("Item: "+itemName+", URL: "+url);
        }
      }
    }
    /*

<div class="questReward">
<div>
<strong>Receive:</strong>
</div>
<div>
<a href="/wiki/Item:Drownholt_Compass">
<img class="icon" rel="" src="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/item/device/it_tracking_mom_book9_chapter5.png">
</a>
<a href="/wiki/Item:Drownholt_Compass">Drownholt Compass</a>
&nbsp;(x5)
</div>
</div>
     */
  }

  private boolean parseItemXPReward(Element rewardDiv)
  {
    //System.out.println("Item XP reward!");
    return true;
    /*
<div class="questReward">
<div>
<strong>IXP:</strong>
</div>
<div>
<img class="icon" src="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/questjournal_itemexp.png">
Item Advancement Experience
</div>
</div>
     */
  }

  private void parseDestinyPoints(Element rewardDiv)
  {
    // TODO: parse destiny points!
/*
<div class="questReward">
<div>
<strong>Destiny Points:</strong>
</div>
<div>
250
<img class="icon" src="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/icon_destiny_points_15.png">
</div>
</div>
 */
  }

  private void parseReward(Element rewardDiv)
  {
    List<Element> strongs=rewardDiv.getAllElements(HTMLElementName.STRONG);
    if ((strongs!=null) && (strongs.size()>=1))
    {
      QuestRewards rewards=_quest.getQuestRewards();
      Element strong=strongs.get(0);
      String key=CharacterReference.decodeCollapseWhiteSpace(strong.getContent());
      key=cleanupFieldName(key);
      if ("Money".equals(key))
      {
        Money m=parseMoneyReward(rewardDiv);
        rewards.getMoney().add(m);
      }
      else if ("Reputation".equals(key))
      {
        Reputation r=parseReputationReward(rewardDiv);
        rewards.getReputation().add(r);
      }
      else if ((RECEIVE_KEY.equals(key)) || (SELECT_ONE_OF_KEY.equals(key)))
      {
        parseItemReward(rewardDiv);
      }
      else if ("IXP".equals(key))
      {
        boolean itemXP=parseItemXPReward(rewardDiv);
        rewards.setHasItemXP(itemXP);
      }
      else if ("Traits".equals(key))
      {
        parseTraitReward(rewardDiv);
      }
      else if ("Titles".equals(key))
      {
        parseTitleReward(rewardDiv);
      }
      else if ("Destiny Points".equals(key))
      {
        parseDestinyPoints(rewardDiv);
      }
      else
      {
        _logger.error("Quest ["+_identifier+"]. Unknown reward type: "+key);
      }
    }
  }

  private String getTextFromTag(Element tag)
  {
    //TextExtractor extractor=tag.getTextExtractor();
    Renderer extractor=tag.getRenderer();
    extractor.setMaxLineLength(10000);
    extractor.setIncludeHyperlinkURLs(false);
    String text=extractor.toString();
    return text;
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
                String contents=getTextFromTag(bestowerTag);
                text.append(contents);
              }
            }
            if (link!=null)
            {
              Element bestowerLink=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(contentsSection,HTMLElementName.DIV,"class","bestowerlink");
              if (bestowerLink!=null)
              {
                String contents=getTextFromTag(bestowerLink);
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
      _logger.error("Quest ["+_identifier+"]. Cannot parse quest section!",e);
    }
    return ret;
  }

  /**
   * Parse the quest page at the given URL.
   * @param url URL of quest page.
   * @return A quest or <code>null</code> if an error occurred.
   */
  public QuestDescription parseQuestPage(String url)
  {
    List<QuestDescription> quests=null;
    try
    {
      // TODO: fetch page text first (centralized downloader to cope with proxy problems?)
      
      Source source=new Source(new URL(url));

      //<div id="lorebookNoedit">
      Element lorebook=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.DIV,"id","lorebookNoedit");
      if (lorebook!=null)
      {
        // identifier
        // <a id="ca-nstab-quest" class="lorebook_action_link" href="/wiki/Quest:A_Feminine_Curve_to_the_Steel">Article</a>
        _identifier=null;
        Element articleLink=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.A,"id","ca-nstab-quest");
        if (articleLink!=null)
        {
          String thisURL=articleLink.getAttributeValue("href");
          if ((thisURL!=null) && (thisURL.startsWith(QUEST_URL_SEED)))
          {
            _identifier=thisURL.substring(QUEST_URL_SEED.length()).trim();
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
              quest.setIdentifier(_identifier);
            }
          }
        }
      }
    }
    catch(Exception e)
    {
      quests=null;
      _logger.error("Cannot parse quest page ["+url+"]",e);
    }
    QuestDescription ret=null;
    if ((quests!=null) && (quests.size()>0))
    {
      ret=quests.get(0);
    }
    return ret;
  }
}
