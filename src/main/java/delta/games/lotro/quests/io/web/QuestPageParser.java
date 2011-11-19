package delta.games.lotro.quests.io.web;

import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
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
import delta.games.lotro.common.objects.ObjectItem;
import delta.games.lotro.common.objects.ObjectsSet;
import delta.games.lotro.quests.QuestDescription;
import delta.games.lotro.quests.QuestRewards;
import delta.games.lotro.quests.QuestDescription.SIZE;
import delta.games.lotro.quests.QuestDescription.TYPE;
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

  private QuestDescription _quest;

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
      else
      {
        _logger.warn("Unmanaged quest field key ["+key+"]");
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
      String value=CharacterReference.decodeCollapseWhiteSpace(a.getContent());
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
        else
        {
          _logger.warn("Unmanaged quest icon title ["+title+"]");
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
                    _logger.warn("Unknown coin type ["+type+"]");
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
          _logger.warn("Unmanaged object selection key ["+key+"]");
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
            _logger.warn("Ignored object ["+item+"], quantity="+quantity);
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
      else
      {
        System.out.println("Unknown reward type: "+key);
      }
    }
  }

  /**
   * Parse the quest page at the given URL.
   * @param url URL of quest page.
   * @return A quest or <code>null</code> if an error occurred..
   */
  public QuestDescription parseQuestPage(String url)
  {
    QuestDescription ret=null;
    try
    {
      _quest=new QuestDescription();
      Source source=new Source(new URL(url));
  
      //<div id="lorebookNoedit">
      Element lorebook=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.DIV,"id","lorebookNoedit");
      if (lorebook!=null)
      {
        Element officialSection=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(lorebook,HTMLElementName.DIV,"class","officialsection");
        if (officialSection!=null)
        {
          parseQuestDescription(officialSection);
        }
      }
      ret=_quest;
    }
    catch(Exception e)
    {
      ret=null;
      _logger.error("Cannot parse quest page ["+url+"]",e);
    }
    return ret;
  }
}
