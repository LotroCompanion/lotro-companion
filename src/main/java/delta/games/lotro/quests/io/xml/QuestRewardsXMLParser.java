package delta.games.lotro.quests.io.xml;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.Faction;
import delta.games.lotro.common.Money;
import delta.games.lotro.common.Reputation;
import delta.games.lotro.common.ReputationItem;
import delta.games.lotro.common.objects.ObjectItem;
import delta.games.lotro.common.objects.ObjectsSet;
import delta.games.lotro.quests.QuestRewards;

/**
 * Parser for quest rewards stored in XML.
 * @author DAM
 */
public class QuestRewardsXMLParser
{
  /**
   * Load quest rewards from XML.
   * @param root Quest description tag.
   * @param rewards Storage for loaded data. 
   */
  public static void loadQuestRewards(Element root, QuestRewards rewards)
  {
    Element rewardsTag=DOMParsingTools.getChildTagByName(root,QuestRewardsXMLConstants.REWARDS_TAG);
    if (rewardsTag!=null)
    {
      // Money
      Element moneyTag=DOMParsingTools.getChildTagByName(rewardsTag,QuestRewardsXMLConstants.MONEY_TAG);
      if (moneyTag!=null)
      {
        Money money=rewards.getMoney();
        NamedNodeMap attrs=moneyTag.getAttributes();
        int gold=DOMParsingTools.getIntAttribute(attrs,QuestRewardsXMLConstants.MONEY_GOLD_ATTR,0);
        money.setGoldCoins(gold);
        int silver=DOMParsingTools.getIntAttribute(attrs,QuestRewardsXMLConstants.MONEY_SILVER_ATTR,0);
        money.setSilverCoins(silver);
        int copper=DOMParsingTools.getIntAttribute(attrs,QuestRewardsXMLConstants.MONEY_COPPER_ATTR,0);
        money.setCopperCoins(copper);
      }

      // Reputation
      Element reputationTag=DOMParsingTools.getChildTagByName(rewardsTag,QuestRewardsXMLConstants.REPUTATION_TAG);
      if (reputationTag!=null)
      {
        Reputation reputation=rewards.getReputation();
        List<Element> reputationItemsTags=DOMParsingTools.getChildTagsByName(reputationTag,QuestRewardsXMLConstants.REPUTATION_ITEM_TAG);
        for(Element reputationItemsTag : reputationItemsTags)
        {
          NamedNodeMap attrs=reputationItemsTag.getAttributes();
          String factionName=DOMParsingTools.getStringAttribute(attrs,QuestRewardsXMLConstants.REPUTATION_ITEM_FACTION_ATTR,null);
          int amount=DOMParsingTools.getIntAttribute(attrs,QuestRewardsXMLConstants.REPUTATION_ITEM_AMOUNT_ATTR,0);
          Faction faction=Faction.getByName(factionName);
          if ((faction!=null) && (amount!=0))
          {
            ReputationItem item=new ReputationItem(faction);
            item.setAmount(amount);
            reputation.add(item);
          }
        }
      }
      // Item XP
      Element itemXP=DOMParsingTools.getChildTagByName(rewardsTag,QuestRewardsXMLConstants.ITEM_XP_TAG);
      rewards.setHasItemXP(itemXP!=null);

      // Objects
      List<Element> objectTags=DOMParsingTools.getChildTagsByName(rewardsTag,QuestRewardsXMLConstants.OBJECT_TAG,false);
      parseObjectsList(objectTags,rewards.getObjects());
      // Select one of objects
      Element selectOneOf=DOMParsingTools.getChildTagByName(rewardsTag,QuestRewardsXMLConstants.SELECT_ONE_OF_TAG);
      if (selectOneOf!=null)
      {
        List<Element> selectOneOfObjectTags=DOMParsingTools.getChildTagsByName(selectOneOf,QuestRewardsXMLConstants.OBJECT_TAG,false);
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
    String name=DOMParsingTools.getStringAttribute(attrs,QuestRewardsXMLConstants.OBJECT_NAME_ATTR,null);
    String pageURL=DOMParsingTools.getStringAttribute(attrs,QuestRewardsXMLConstants.OBJECT_PAGE_URL_ATTR,null);
    String iconURL=DOMParsingTools.getStringAttribute(attrs,QuestRewardsXMLConstants.OBJECT_ICON_URL_ATTR,null);
    int quantity=DOMParsingTools.getIntAttribute(attrs,QuestRewardsXMLConstants.OBJECT_QUANTITY_ATTR,0);
    if ((name!=null) && (quantity!=0))
    {
      ObjectItem item=new ObjectItem(name);
      item.setObjectURL(pageURL);
      item.setIconURL(iconURL);
      set.addObject(item,quantity);
    }
  }
}
