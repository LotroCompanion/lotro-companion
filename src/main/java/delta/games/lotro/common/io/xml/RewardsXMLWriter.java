package delta.games.lotro.common.io.xml;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.helpers.AttributesImpl;

import delta.games.lotro.common.Emote;
import delta.games.lotro.common.Money;
import delta.games.lotro.common.Reputation;
import delta.games.lotro.common.ReputationItem;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.Skill;
import delta.games.lotro.common.Title;
import delta.games.lotro.common.Trait;
import delta.games.lotro.common.Virtue;
import delta.games.lotro.common.money.io.xml.MoneyXMLWriter;
import delta.games.lotro.common.objects.ObjectItem;
import delta.games.lotro.common.objects.ObjectsSet;

/**
 * Writes LOTRO rewards to XML documents.
 * @author DAM
 */
public class RewardsXMLWriter
{
  private static final String CDATA="CDATA";
  
  /**
   * Write a rewards object to an XML document.
   * @param hd Output transformer.
   * @param rewards Rewards to write.
   * @throws Exception If an error occurs.
   */
  public static void write(TransformerHandler hd, Rewards rewards) throws Exception
  {
    hd.startElement("","",RewardsXMLConstants.REWARDS_TAG,new AttributesImpl());
    Money money=rewards.getMoney();
    MoneyXMLWriter.writeMoney(hd,money);
    Reputation reputation=rewards.getReputation();
    if (!reputation.isEmpty())
    {
      hd.startElement("","",RewardsXMLConstants.REPUTATION_TAG,new AttributesImpl());
      ReputationItem[] items=reputation.getItems();
      for(ReputationItem item : items)
      {
        AttributesImpl reputationAttrs=new AttributesImpl();
        String factionName=item.getFaction().getName();
        reputationAttrs.addAttribute("","",RewardsXMLConstants.REPUTATION_ITEM_FACTION_ATTR,CDATA,factionName);
        reputationAttrs.addAttribute("","",RewardsXMLConstants.REPUTATION_ITEM_AMOUNT_ATTR,CDATA,String.valueOf(item.getAmount()));
        hd.startElement("","",RewardsXMLConstants.REPUTATION_ITEM_TAG,reputationAttrs);
        hd.endElement("","",RewardsXMLConstants.REPUTATION_ITEM_TAG);
      }
      hd.endElement("","",RewardsXMLConstants.REPUTATION_TAG);
    }
    // Destiny points XP
    int destinyPoints=rewards.getDestinyPoints();
    if (destinyPoints>0)
    {
      AttributesImpl attrs=new AttributesImpl();
      attrs.addAttribute("","",RewardsXMLConstants.QUANTITY_DESTINY_POINTS_ATTR,CDATA,String.valueOf(destinyPoints));
      hd.startElement("","",RewardsXMLConstants.DESTINY_POINTS_TAG,attrs);
      hd.endElement("","",RewardsXMLConstants.DESTINY_POINTS_TAG);
    }
    // Item XP
    boolean hasItemXP=rewards.hasItemXP();
    if (hasItemXP)
    {
      hd.startElement("","",RewardsXMLConstants.ITEM_XP_TAG,new AttributesImpl());
      hd.endElement("","",RewardsXMLConstants.ITEM_XP_TAG);
    }
    // Traits
    Trait[] traits=rewards.getTraits();
    if (traits!=null)
    {
      for(Trait trait : traits)
      {
        AttributesImpl attrs=new AttributesImpl();
        String id=trait.getIdentifier();
        attrs.addAttribute("","",RewardsXMLConstants.TRAIT_ID_ATTR,CDATA,id);
        String name=trait.getName();
        attrs.addAttribute("","",RewardsXMLConstants.TRAIT_NAME_ATTR,CDATA,name);
        hd.startElement("","",RewardsXMLConstants.TRAIT_TAG,attrs);
        hd.endElement("","",RewardsXMLConstants.TRAIT_TAG);
      }
    }
    // Skills
    Skill[] skills=rewards.getSkills();
    if (skills!=null)
    {
      for(Skill skill : skills)
      {
        AttributesImpl attrs=new AttributesImpl();
        String id=skill.getIdentifier();
        attrs.addAttribute("","",RewardsXMLConstants.SKILL_ID_ATTR,CDATA,id);
        String type=skill.getType().toString();
        attrs.addAttribute("","",RewardsXMLConstants.SKILL_TYPE_ATTR,CDATA,type);
        String name=skill.getName();
        attrs.addAttribute("","",RewardsXMLConstants.SKILL_NAME_ATTR,CDATA,name);
        hd.startElement("","",RewardsXMLConstants.SKILL_TAG,attrs);
        hd.endElement("","",RewardsXMLConstants.SKILL_TAG);
      }
    }
    // Titles
    Title[] titles=rewards.getTitles();
    if (titles!=null)
    {
      for(Title title : titles)
      {
        AttributesImpl attrs=new AttributesImpl();
        String id=title.getIdentifier();
        attrs.addAttribute("","",RewardsXMLConstants.TITLE_ID_ATTR,CDATA,id);
        String name=title.getName();
        attrs.addAttribute("","",RewardsXMLConstants.TITLE_NAME_ATTR,CDATA,name);
        hd.startElement("","",RewardsXMLConstants.TITLE_TAG,attrs);
        hd.endElement("","",RewardsXMLConstants.TITLE_TAG);
      }
    }
    // Virtues
    Virtue[] virtues=rewards.getVirtues();
    if (virtues!=null)
    {
      for(Virtue virtue : virtues)
      {
        AttributesImpl attrs=new AttributesImpl();
        String id=virtue.getIdentifier();
        attrs.addAttribute("","",RewardsXMLConstants.VIRTUE_ID_ATTR,CDATA,id);
        String name=virtue.getName();
        attrs.addAttribute("","",RewardsXMLConstants.VIRTUE_NAME_ATTR,CDATA,name);
        hd.startElement("","",RewardsXMLConstants.VIRTUE_TAG,attrs);
        hd.endElement("","",RewardsXMLConstants.VIRTUE_TAG);
      }
    }
    // Emotes
    Emote[] emotes=rewards.getEmotes();
    if (emotes!=null)
    {
      for(Emote emote : emotes)
      {
        AttributesImpl attrs=new AttributesImpl();
        String id=emote.getIdentifier();
        attrs.addAttribute("","",RewardsXMLConstants.EMOTE_ID_ATTR,CDATA,id);
        String name=emote.getName();
        attrs.addAttribute("","",RewardsXMLConstants.EMOTE_NAME_ATTR,CDATA,name);
        hd.startElement("","",RewardsXMLConstants.EMOTE_TAG,attrs);
        hd.endElement("","",RewardsXMLConstants.EMOTE_TAG);
      }
    }
    
    writeObjectsList(hd,rewards.getObjects(),null);
    writeObjectsList(hd,rewards.getSelectObjects(),RewardsXMLConstants.SELECT_ONE_OF_TAG);
    hd.endElement("","",RewardsXMLConstants.REWARDS_TAG);
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
      attrs.addAttribute("","",RewardsXMLConstants.OBJECT_NAME_ATTR,CDATA,name);
    }
    String pageURL=object.getObjectURL();
    if (pageURL!=null)
    {
      attrs.addAttribute("","",RewardsXMLConstants.OBJECT_PAGE_URL_ATTR,CDATA,pageURL);
    }
    String iconURL=object.getIconURL();
    if (iconURL!=null)
    {
      attrs.addAttribute("","",RewardsXMLConstants.OBJECT_ICON_URL_ATTR,CDATA,iconURL);
    }
    attrs.addAttribute("","",RewardsXMLConstants.OBJECT_QUANTITY_ATTR,CDATA,String.valueOf(quantity));
    hd.startElement("","",RewardsXMLConstants.OBJECT_TAG,attrs);
    hd.endElement("","",RewardsXMLConstants.OBJECT_TAG);
  }
}
