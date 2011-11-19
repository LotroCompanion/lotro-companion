package delta.games.lotro.quests.io.xml;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.helpers.AttributesImpl;

import delta.games.lotro.common.Money;
import delta.games.lotro.common.Reputation;
import delta.games.lotro.common.ReputationItem;
import delta.games.lotro.common.objects.ObjectItem;
import delta.games.lotro.common.objects.ObjectsSet;
import delta.games.lotro.quests.QuestRewards;

/**
 * Writes LOTRO quest rewards to XML documents.
 * @author DAM
 */
public class QuestRewardsXMLWriter
{
  private static final String CDATA="CDATA";
  
  /**
   * Write a quest rewards to an XML document.
   * @param hd Output transformer.
   * @param rewards Rewards to write.
   * @throws Exception If an error occurs.
   */
  public static void write(TransformerHandler hd, QuestRewards rewards) throws Exception
  {
    hd.startElement("","",QuestRewardsXMLConstants.REWARDS_TAG,new AttributesImpl());
    Money money=rewards.getMoney();
    if (!money.isEmpty())
    {
      AttributesImpl moneyAttrs=new AttributesImpl();
      moneyAttrs.addAttribute("","",QuestRewardsXMLConstants.MONEY_GOLD_ATTR,CDATA,String.valueOf(money.getGoldCoins()));
      moneyAttrs.addAttribute("","",QuestRewardsXMLConstants.MONEY_SILVER_ATTR,CDATA,String.valueOf(money.getSilverCoins()));
      moneyAttrs.addAttribute("","",QuestRewardsXMLConstants.MONEY_COPPER_ATTR,CDATA,String.valueOf(money.getCopperCoins()));
      hd.startElement("","",QuestRewardsXMLConstants.MONEY_TAG,moneyAttrs);
      hd.endElement("","",QuestRewardsXMLConstants.MONEY_TAG);
    }
    Reputation reputation=rewards.getReputation();
    if (!reputation.isEmpty())
    {
      hd.startElement("","",QuestRewardsXMLConstants.REPUTATION_TAG,new AttributesImpl());
      ReputationItem[] items=reputation.getItems();
      for(ReputationItem item : items)
      {
        AttributesImpl reputationAttrs=new AttributesImpl();
        String factionName=item.getFaction().getName();
        reputationAttrs.addAttribute("","",QuestRewardsXMLConstants.REPUTATION_ITEM_FACTION_ATTR,CDATA,factionName);
        reputationAttrs.addAttribute("","",QuestRewardsXMLConstants.REPUTATION_ITEM_AMOUNT_ATTR,CDATA,String.valueOf(item.getAmount()));
        hd.startElement("","",QuestRewardsXMLConstants.REPUTATION_ITEM_TAG,reputationAttrs);
        hd.endElement("","",QuestRewardsXMLConstants.REPUTATION_ITEM_TAG);
      }
      hd.endElement("","",QuestRewardsXMLConstants.REPUTATION_TAG);
    }
    boolean hasItemXP=rewards.hasItemXP();
    if (hasItemXP)
    {
      hd.startElement("","",QuestRewardsXMLConstants.ITEM_XP_TAG,new AttributesImpl());
      hd.endElement("","",QuestRewardsXMLConstants.ITEM_XP_TAG);
    }
    writeObjectsList(hd,rewards.getObjects(),null);
    writeObjectsList(hd,rewards.getSelectObjects(),QuestRewardsXMLConstants.SELECT_ONE_OF_TAG);
    hd.endElement("","",QuestRewardsXMLConstants.REWARDS_TAG);
  }

  private static void writeObjectsList(TransformerHandler hd, ObjectsSet objects, String mainTag) throws Exception
  {
    int nb=objects.getNbObjectItems();
    if (nb>0)
    {
      if (mainTag!=null)
      {
        hd.startElement("","",mainTag,new AttributesImpl());
      }
      for(int i=0;i<nb;i++)
      {
        ObjectItem item=objects.getItem(i);
        int quantity=objects.getQuantity(i);
        writeObject(hd,item,quantity);
      }
      if (mainTag!=null)
      {
        hd.endElement("","",mainTag);
      }
    }
  }

  private static void writeObject(TransformerHandler hd, ObjectItem object, int quantity) throws Exception
  {
    AttributesImpl attrs=new AttributesImpl();
    String name=object.getName();
    if (name!=null)
    {
      attrs.addAttribute("","",QuestRewardsXMLConstants.OBJECT_NAME_ATTR,CDATA,name);
    }
    String pageURL=object.getObjectURL();
    if (pageURL!=null)
    {
      attrs.addAttribute("","",QuestRewardsXMLConstants.OBJECT_PAGE_URL_ATTR,CDATA,pageURL);
    }
    String iconURL=object.getIconURL();
    if (iconURL!=null)
    {
      attrs.addAttribute("","",QuestRewardsXMLConstants.OBJECT_ICON_URL_ATTR,CDATA,iconURL);
    }
    attrs.addAttribute("","",QuestRewardsXMLConstants.OBJECT_QUANTITY_ATTR,CDATA,String.valueOf(quantity));
    hd.startElement("","",QuestRewardsXMLConstants.OBJECT_TAG,attrs);
    hd.endElement("","",QuestRewardsXMLConstants.OBJECT_TAG);
  }
}
