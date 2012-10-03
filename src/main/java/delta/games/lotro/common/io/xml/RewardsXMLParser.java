package delta.games.lotro.common.io.xml;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
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
import delta.games.lotro.common.money.io.xml.MoneyXMLParser;
import delta.games.lotro.common.objects.ObjectItem;
import delta.games.lotro.common.objects.ObjectsSet;

/**
 * Parser for rewards stored in XML.
 * @author DAM
 */
public class RewardsXMLParser
{
  /**
   * Load rewards from XML.
   * @param root Reward description tag.
   * @param rewards Storage for loaded data. 
   */
  public static void loadRewards(Element root, Rewards rewards)
  {
    Element rewardsTag=DOMParsingTools.getChildTagByName(root,RewardsXMLConstants.REWARDS_TAG);
    if (rewardsTag!=null)
    {
      // Money
      Money money=rewards.getMoney();
      MoneyXMLParser.loadMoney(root,money);

      // Reputation
      Element reputationTag=DOMParsingTools.getChildTagByName(rewardsTag,RewardsXMLConstants.REPUTATION_TAG);
      if (reputationTag!=null)
      {
        Reputation reputation=rewards.getReputation();
        List<Element> reputationItemsTags=DOMParsingTools.getChildTagsByName(reputationTag,RewardsXMLConstants.REPUTATION_ITEM_TAG);
        for(Element reputationItemsTag : reputationItemsTags)
        {
          NamedNodeMap attrs=reputationItemsTag.getAttributes();
          String factionName=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.REPUTATION_ITEM_FACTION_ATTR,null);
          int amount=DOMParsingTools.getIntAttribute(attrs,RewardsXMLConstants.REPUTATION_ITEM_AMOUNT_ATTR,0);
          Faction faction=Factions.getInstance().getByName(factionName);
          if ((faction!=null) && (amount!=0))
          {
            ReputationItem item=new ReputationItem(faction);
            item.setAmount(amount);
            reputation.add(item);
          }
        }
      }
      // Destiny points
      Element destinyPointsTag=DOMParsingTools.getChildTagByName(rewardsTag,RewardsXMLConstants.DESTINY_POINTS_TAG);
      if (destinyPointsTag!=null)
      {
        NamedNodeMap attrs=destinyPointsTag.getAttributes();
        int destinyPoints=DOMParsingTools.getIntAttribute(attrs,RewardsXMLConstants.QUANTITY_DESTINY_POINTS_ATTR,0);
        rewards.setDestinyPoints(destinyPoints);
      }
      // Item XP
      Element itemXP=DOMParsingTools.getChildTagByName(rewardsTag,RewardsXMLConstants.ITEM_XP_TAG);
      rewards.setHasItemXP(itemXP!=null);
      
      // Traits
      List<Element> traitTags=DOMParsingTools.getChildTagsByName(rewardsTag,RewardsXMLConstants.TRAIT_TAG);
      for(Element traitTag : traitTags)
      {
        NamedNodeMap attrs=traitTag.getAttributes();
        String id=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.TRAIT_ID_ATTR,null);
        String name=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.TRAIT_NAME_ATTR,"");
        if (id!=null)
        {
          Trait trait=new Trait(id,name);
          rewards.addTrait(trait);
        }
      }
      // Skills
      List<Element> skillTags=DOMParsingTools.getChildTagsByName(rewardsTag,RewardsXMLConstants.SKILL_TAG);
      for(Element skillTag : skillTags)
      {
        NamedNodeMap attrs=skillTag.getAttributes();
        String id=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.SKILL_ID_ATTR,null);
        String typeStr=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.SKILL_TYPE_ATTR,"");
        SkillType type=SkillType.valueOf(typeStr);
        String name=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.SKILL_NAME_ATTR,"");
        if (id!=null)
        {
          Skill skill=new Skill(type,id,name);
          rewards.addSkill(skill);
        }
      }
      // Titles
      List<Element> titleTags=DOMParsingTools.getChildTagsByName(rewardsTag,RewardsXMLConstants.TITLE_TAG);
      for(Element titleTag : titleTags)
      {
        NamedNodeMap attrs=titleTag.getAttributes();
        String id=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.TITLE_ID_ATTR,null);
        String name=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.TITLE_NAME_ATTR,"");
        if (id!=null)
        {
          Title title=new Title(id,name);
          rewards.addTitle(title);
        }
      }
      // Virtues
      List<Element> virtueTags=DOMParsingTools.getChildTagsByName(rewardsTag,RewardsXMLConstants.VIRTUE_TAG);
      for(Element virtueTag : virtueTags)
      {
        NamedNodeMap attrs=virtueTag.getAttributes();
        String id=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.VIRTUE_ID_ATTR,null);
        String name=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.VIRTUE_NAME_ATTR,"");
        if (id!=null)
        {
          Virtue virtue=new Virtue(id,name);
          rewards.addVirtue(virtue);
        }
      }
      // Emotes
      List<Element> emoteTags=DOMParsingTools.getChildTagsByName(rewardsTag,RewardsXMLConstants.EMOTE_TAG);
      for(Element emoteTag : emoteTags)
      {
        NamedNodeMap attrs=emoteTag.getAttributes();
        String id=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.EMOTE_ID_ATTR,null);
        String name=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.EMOTE_NAME_ATTR,"");
        if (id!=null)
        {
          Emote emote=new Emote(id,name);
          rewards.addEmote(emote);
        }
      }

      // Objects
      List<Element> objectTags=DOMParsingTools.getChildTagsByName(rewardsTag,RewardsXMLConstants.OBJECT_TAG,false);
      parseObjectsList(objectTags,rewards.getObjects());
      // Select one of objects
      Element selectOneOf=DOMParsingTools.getChildTagByName(rewardsTag,RewardsXMLConstants.SELECT_ONE_OF_TAG);
      if (selectOneOf!=null)
      {
        List<Element> selectOneOfObjectTags=DOMParsingTools.getChildTagsByName(selectOneOf,RewardsXMLConstants.OBJECT_TAG,false);
        parseObjectsList(selectOneOfObjectTags,rewards.getSelectObjects());
      }
    }
  }

  private static void parseObjectsList(List<Element> objectTags, ObjectsSet set)
  {
    for(Element objectTag : objectTags)
    {
      parseObject(objectTag,set);
    }
  }
  
  private static void parseObject(Element objectTag, ObjectsSet set)
  {
    NamedNodeMap attrs=objectTag.getAttributes();
    String name=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.OBJECT_NAME_ATTR,null);
    String pageURL=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.OBJECT_PAGE_URL_ATTR,null);
    String iconURL=DOMParsingTools.getStringAttribute(attrs,RewardsXMLConstants.OBJECT_ICON_URL_ATTR,null);
    int quantity=DOMParsingTools.getIntAttribute(attrs,RewardsXMLConstants.OBJECT_QUANTITY_ATTR,0);
    if ((name!=null) && (quantity!=0))
    {
      ObjectItem item=new ObjectItem(name);
      item.setObjectURL(pageURL);
      item.setIconURL(iconURL);
      set.addObject(item,quantity);
    }
  }
}
