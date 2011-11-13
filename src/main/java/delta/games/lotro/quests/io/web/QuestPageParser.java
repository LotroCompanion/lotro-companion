package delta.games.lotro.quests.io.web;

import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import delta.common.utils.NumericTools;
import delta.games.lotro.utils.JerichoHtmlUtils;

/**
 * @author DAM
 */
public class QuestPageParser
{
  private void handleQuestAttribute(String key, String value)
  {
    System.out.println(key+"="+value);
  }

  private String cleanupFieldName(String key)
  {
    if (key==null) key="";
    key=key.trim();
    if (key.endsWith(":")) key=key.substring(0,key.length()-1);
    return key;
  }

  private void parseQuestField(Element questField)
  {
    List<Element> strongs=questField.getAllElements("strong");
    if ((strongs!=null) && (strongs.size()==1))
    {
      List<Segment> nodes=JerichoHtmlUtils.getChildNodes(questField);
      Element strong=strongs.get(0);
      String key=CharacterReference.decodeCollapseWhiteSpace(strong.getContent());
      key=cleanupFieldName(key);
      if ("Category".equals(key))
      {
        String value=nodes.get(3).toString().trim();
        handleQuestAttribute(key,value);
      }
      else if ("Scope".equals(key))
      {
        String value=nodes.get(3).toString().trim();
        handleQuestAttribute(key,value);
      }
      else if ("Minimum Level".equals(key))
      {
        String value=nodes.get(3).toString().trim();
        handleQuestAttribute(key,value);
      }
      else if (("Prerequisite Quests".equals(key)) || ("Next Quests".equals(key)))
      {
        List<Element> dds=questField.getAllElements("dd");
        if ((dds!=null) && (dds.size()==1))
        {
          parseQuestsList(dds.get(0),key);
        }
      }
    }
  }

  private void parseQuestsList(Element dd,String category)
  {
    List<Element> as=dd.getAllElements(HTMLElementName.A);
    for(Element a : as)
    {
      String value=CharacterReference.decodeCollapseWhiteSpace(a.getContent());
      System.out.println(category+" -> "+value);
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
    }
    System.out.println("Title: "+title);
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

  private void parseMoneyReward(Element rewardDiv)
  {
    //System.out.println("Money reward!");
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
                  if (type.contains("silver")) System.out.println("Silver: "+nbCoins);
                  else if (type.contains("copper")) System.out.println("Copper: "+nbCoins);
                  else if (type.contains("gold")) System.out.println("Gold: "+nbCoins);
                  else
                  {
                    System.out.println("Unknown coin type!");
                  }
                }
              }
            }
          }
        }
      }
    }
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

  private void parseReputationReward(Element rewardDiv)
  {
    //System.out.println("Reputation reward!");
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
          String faction=factionNode.toString().trim();
          System.out.println("Faction: '"+faction+"': "+reputation);
        }
      }
    }
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
    System.out.println("Item reward!");
    List<Element> divs=rewardDiv.getAllElements(HTMLElementName.DIV);
    for(Element div : divs)
    {
      List<Element> as=div.getAllElements(HTMLElementName.A);
      if ((as!=null) && (as.size()==2))
      {
        Element iconItem=as.get(0);
        List<Element> imgs=iconItem.getAllElements(HTMLElementName.IMG);
        if ((imgs!=null) && (imgs.size()==1))
        {
          Element img=imgs.get(0);
          String iconURL=img.getAttributeValue("src");
          System.out.println("Icon: "+iconURL);
        }
        Element textItem=as.get(1);
        String itemName=CharacterReference.decodeCollapseWhiteSpace(textItem.getContent());
        String url=textItem.getAttributeValue("href");
        System.out.println("Item: "+itemName+", URL: "+url);
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

  private void parseItemXPReward(Element rewardDiv)
  {
    System.out.println("Item XP reward!");
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
    List<Element> strongs=rewardDiv.getAllElements("strong");
    if ((strongs!=null) && (strongs.size()==1))
    {
      Element strong=strongs.get(0);
      String key=CharacterReference.decodeCollapseWhiteSpace(strong.getContent());
      key=cleanupFieldName(key);
      if ("Money".equals(key))
      {
        parseMoneyReward(rewardDiv);
      }
      else if ("Reputation".equals(key))
      {
        parseReputationReward(rewardDiv);
      }
      else if (("Receive".equals(key)) || ("Select one of".equals(key)))
      {
        parseItemReward(rewardDiv);
      }
      else if ("IXP".equals(key))
      {
        parseItemXPReward(rewardDiv);
      }
      else
      {
        System.out.println("Unknown reward type: "+key);
      }
    }
  }

  public void parseQuestPage(String url)
  {
    try
    {
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
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public static void main(String[] args)
  {
    //String url="http://lorebook.lotro.com/wiki/Special:LotroResource?id=1879157116";
    //String url="http://lorebook.lotro.com/wiki/Quest:Return_to_the_Elders";
    String url="http://lorebook.lotro.com/wiki/Quest:A_Visit_to_the_Warren";
    //String url="http://lorebook.lotro.com/wiki/Quest:Ending_the_Nightmare";
    QuestPageParser parser=new QuestPageParser();
    parser.parseQuestPage(url);
  }
}
