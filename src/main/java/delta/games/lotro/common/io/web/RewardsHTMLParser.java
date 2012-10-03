package delta.games.lotro.common.io.web;

import java.util.List;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.TextExtractor;

import org.apache.log4j.Logger;

import delta.common.utils.NumericTools;
import delta.games.lotro.common.Emote;
import delta.games.lotro.common.Faction;
import delta.games.lotro.common.Factions;
import delta.games.lotro.common.Money;
import delta.games.lotro.common.Reputation;
import delta.games.lotro.common.ReputationItem;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.Skill;
import delta.games.lotro.common.Skill.SkillType;
import delta.games.lotro.common.Title;
import delta.games.lotro.common.Trait;
import delta.games.lotro.common.Virtue;
import delta.games.lotro.common.objects.ObjectItem;
import delta.games.lotro.common.objects.ObjectsSet;
import delta.games.lotro.utils.JerichoHtmlUtils;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for rewards described in HTML.
 * @author DAM
 */
public class RewardsHTMLParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  private static final String RECEIVE_KEY="Receive";
  private static final String SELECT_ONE_OF_KEY="Select one of";
  private static final String TRAIT_URL_SEED="Trait:";
  private static final String PASSIVE_SKILL_URL_SEED="Passive_Skill:";
  private static final String URL_SEED="/wiki/";
  private static final String TITLE_URL_SEED="/wiki/Title:";
  private static final String VIRTUE_URL_SEED="/wiki/Trait:";
  //private static final String EMOTE_URL_SEED="/wiki/Emote:";
  private static final String EMOTE_SEED="Emote:";

  private String _objectId;

  /**
   * Constructor.
   * @param objectId
   */
  public RewardsHTMLParser(String objectId)
  {
    _objectId=objectId;
  }

  /**
   * Parse a rewards HTML element.
   * @param rewardsTable HTML element to parse.
   * @param rewards Storage object.
   */
  public void parseRewards(Element rewardsTable, Rewards rewards)
  {
    List<Element> rewardElements=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(rewardsTable,HTMLElementName.DIV,"class","questReward");
    for(Element rewardElement : rewardElements)
    {
      parseReward(rewardElement,rewards);
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
                    _logger.warn(_objectId+": unknown coin type ["+type+"]");
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

  private void parseTraitReward(Element rewardDiv, Rewards rewards)
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
          if ((identifier.startsWith(EMOTE_SEED)) && (name.startsWith(EMOTE_SEED)))
          {
            identifier=identifier.substring(EMOTE_SEED.length()).trim();
            name=name.substring(EMOTE_SEED.length()).trim();
            Emote emote=new Emote(identifier,name);
            rewards.addEmote(emote);
            //System.out.println(emote.dump());
          }
          else
          {
            Trait trait=new Trait(identifier,name);
            rewards.addTrait(trait);
            //System.out.println(trait.dump());
          }
        }
        else if (qualifiedIdentifier.startsWith(PASSIVE_SKILL_URL_SEED))
        {
          String identifier=qualifiedIdentifier.substring(PASSIVE_SKILL_URL_SEED.length()).trim();
          Skill skill=new Skill(SkillType.PASSIVE,identifier,name);
          rewards.addSkill(skill);
          //System.out.println(skill.dump());
        }
        else
        {
          _logger.warn(_objectId+": unmanaged trait/skill identifier ["+qualifiedIdentifier+"]");
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
        _logger.warn(_objectId+": malformed URL ["+url+"]");
      }
    }
    else
    {
      _logger.warn(_objectId+": trait reward with "+size+" anchor tags!");
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

  private void parseTitleReward(Element rewardDiv, Rewards rewards)
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
        rewards.addTitle(title);
      }
    }
    else
    {
      _logger.warn(_objectId+": title with "+size+" anchor tags!");
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

  private void parseEmoteReward(Element rewardDiv, Rewards rewards)
  {
    // Removed: parsed in the traits section
    /*
    List<Element> as=rewardDiv.getAllElements(HTMLElementName.A);
    int size=(as!=null)?as.size():0;
    if (size==1)
    {
      String name=null;
      String emoteIdentifier=null;
      //String iconURL=null;
      Element firstA=as.get(0);
      String url=firstA.getAttributeValue("href");
      if ((url!=null) && (url.startsWith(EMOTE_URL_SEED)))
      {
        emoteIdentifier=url.substring(EMOTE_URL_SEED.length()).trim();
      }
      name=CharacterReference.decodeCollapseWhiteSpace(firstA.getContent());
      if ((name!=null) && (emoteIdentifier!=null))
      {
        Emote emote=new Emote(emoteIdentifier,name);
        rewards.addEmote(emote);
      }
    }
    else
    {
      _logger.warn(_objectId+": title with "+size+" anchor tags!");
    }
    */
/*
<div class="questReward">
<div>
<strong>Emotes:</strong>
</div>
<div>
<a href="/wiki/Emote:swordsalute">/swordsalute</a>
</div>
</div>
 */
  }

  private void parseVirtuesReward(Element rewardDiv, Rewards rewards)
  {
    List<Element> as=rewardDiv.getAllElements(HTMLElementName.A);
    int size=(as!=null)?as.size():0;
    int nbItems=size/2;
    if (nbItems*2==size)
    {
      for(int i=0;i<nbItems;i++)
      {
        String name=null;
        String virtueIdentifier=null;
        //String iconURL=null;
        Element firstA=as.get(2*i+1);
        String url=firstA.getAttributeValue("href");
        if ((url!=null) && (url.startsWith(VIRTUE_URL_SEED)))
        {
          virtueIdentifier=url.substring(VIRTUE_URL_SEED.length()).trim();
        }
        name=CharacterReference.decodeCollapseWhiteSpace(firstA.getContent());
        if ((name!=null) && (virtueIdentifier!=null))
        {
          Virtue virtue=new Virtue(virtueIdentifier,name);
          rewards.addVirtue(virtue);
        }
      }
    }
    else
    {
      _logger.warn(_objectId+": virtue with "+size+" anchor tags!");
    }
/*
<div class="questReward">
<div>
<strong>Virtues:</strong>
</div>
<div>
<a href="/wiki/Trait:Patience">
<img class="icon" src="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/trait/trait_virtue_patience.png">
</a>
<a href="/wiki/Trait:Patience">Patience</a>
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
          Faction faction=Factions.getInstance().getByName(factionName);
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

  private void parseItemReward(Element rewardDiv, Rewards rewards)
  {
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
          _logger.warn(_objectId+": unmanaged object selection key ["+key+"]");
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
            _logger.warn(_objectId+": ignored object ["+item+"], quantity="+quantity);
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

  private int parseDestinyPoints(Element rewardDiv)
  {
    int nbDestinyPoints=0;
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
          nbDestinyPoints=NumericTools.parseInt(s.toString(),0);
        }
      }
    }
    return nbDestinyPoints;
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

  private void parseReward(Element rewardDiv, Rewards rewards)
  {
    List<Element> strongs=rewardDiv.getAllElements(HTMLElementName.STRONG);
    if ((strongs!=null) && (strongs.size()>=1))
    {
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
        parseItemReward(rewardDiv,rewards);
      }
      else if ("IXP".equals(key))
      {
        boolean itemXP=parseItemXPReward(rewardDiv);
        rewards.setHasItemXP(itemXP);
      }
      else if ("Traits".equals(key))
      {
        parseTraitReward(rewardDiv,rewards);
      }
      else if ("Titles".equals(key))
      {
        parseTitleReward(rewardDiv,rewards);
      }
      else if ("Virtues".equals(key))
      {
        parseVirtuesReward(rewardDiv,rewards);
      }
      else if ("Emotes".equals(key))
      {
        parseEmoteReward(rewardDiv,rewards);
      }
      else if ("Destiny Points".equals(key))
      {
        int nb=parseDestinyPoints(rewardDiv);
        rewards.setDestinyPoints(nb);
      }
      else
      {
        _logger.error(_objectId+": unknown reward type: "+key);
      }
    }
  }

  private String cleanupFieldName(String key)
  {
    if (key==null) key="";
    key=key.trim();
    if (key.endsWith(":")) key=key.substring(0,key.length()-1);
    return key;
  }
}
