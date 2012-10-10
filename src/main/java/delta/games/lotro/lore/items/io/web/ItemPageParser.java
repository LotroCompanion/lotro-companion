package delta.games.lotro.lore.items.io.web;

import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import org.apache.log4j.Logger;

import delta.common.utils.NumericTools;
import delta.common.utils.text.TextTools;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Money;
import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemBinding;
import delta.games.lotro.lore.items.ItemSturdiness;
import delta.games.lotro.lore.items.ItemsSet;
import delta.games.lotro.lore.items.Weapon;
import delta.games.lotro.utils.DownloadService;
import delta.games.lotro.utils.JerichoHtmlUtils;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for MyLotro.com item page.
 * @author DAM
 */
public class ItemPageParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();
  
  private static final String WIKI_SEED="/wiki/";
  private static final String SET_NUMBER_SEED="Set (";
  private static final String SET_NUMBER_END="):";

  private Item _item;
  private ItemsSet _set;
  private String _identifier;

  private String getTagContent(Element itemTooltip, String tagIdentifier)
  {
    String contents=null;
    Element element=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(itemTooltip,HTMLElementName.DIV,"class",tagIdentifier);
    if (element!=null)
    {
      contents=CharacterReference.decodeCollapseWhiteSpace(element.getContent());
    }
    return contents;
  }

  private ItemBinding getBinding(String bindingStr)
  {
    ItemBinding ret = null;
    if ((bindingStr!=null) && (bindingStr.length()>0))
    {
      if ("Bind on Equip".equalsIgnoreCase(bindingStr))
      {
        ret=ItemBinding.BIND_ON_EQUIP;
      }
      else if ("Bind on Acquire".equalsIgnoreCase(bindingStr))
      {
        ret=ItemBinding.BIND_ON_ACQUIRE;
      }
      else
      {
        _logger.warn("Unmanaged bind information ["+bindingStr+"]!");
      }
    }
    return ret;
  }

  private ItemSturdiness getSturdiness(String sturdinessStr)
  {
    ItemSturdiness ret = null;
    if ((sturdinessStr!=null) && (sturdinessStr.length()>0))
    {
      if ("Tough".equalsIgnoreCase(sturdinessStr))
      {
        ret=ItemSturdiness.TOUGH;
      }
      else if ("Normal".equalsIgnoreCase(sturdinessStr))
      {
        ret=ItemSturdiness.NORMAL;
      }
      else if ("Brittle".equalsIgnoreCase(sturdinessStr))
      {
        ret=ItemSturdiness.BRITTLE;
      }
      else
      {
        _logger.warn("Unmanaged sturdiness information ["+sturdinessStr+"]!");
      }
    }
    return ret;
  }

  private Integer getDurability(String durabilityStr)
  {
    // Expected input string format: "Durability 60 / 60".
    Integer ret = null;
    if ((durabilityStr!=null) && (durabilityStr.startsWith("Durability")))
    {
      try
      {
        int slashIndex = durabilityStr.indexOf('/');
        if (slashIndex!=-1)
        {
          String valueStr = durabilityStr.substring(slashIndex+1).trim();
          ret = Integer.valueOf(valueStr);
        }
      }
      catch(Exception e)
      {
        _logger.warn("Unexpected durability value ["+durabilityStr+"]!");
      }
    }
    return ret;
  }

  private Integer getArmour(String armourValueStr)
  {
    // Expected input string format: "1130 Armour Value" or "Max: 1130 Armour Value"
    Integer ret = null;
    if ((armourValueStr!=null) && (armourValueStr.endsWith("Armour Value")))
    {
      try
      {
        if (armourValueStr.startsWith("Max:"))
        {
          armourValueStr=armourValueStr.substring(4).trim();
        }
        int separatorIndex = armourValueStr.indexOf(' ');
        if (separatorIndex!=-1)
        {
          String valueStr = armourValueStr.substring(0,separatorIndex).trim();
          ret = Integer.valueOf(valueStr);
        }
      }
      catch(Exception e)
      {
        _logger.warn("Unexpected armour value ["+armourValueStr+"]!");
      }
    }
    return ret;
  }

  private Float getDPS(String dpsStr)
  {
    // Expected input string format: "126.5 DPS"
    Float ret = null;
    if ((dpsStr!=null) && (dpsStr.endsWith("DPS")))
    {
      try
      {
        int separatorIndex = dpsStr.indexOf(' ');
        if (separatorIndex!=-1)
        {
          String valueStr = dpsStr.substring(0,separatorIndex).trim();
          ret = Float.valueOf(valueStr);
        }
      }
      catch(Exception e)
      {
        _logger.warn("Unexpected DPS value ["+dpsStr+"]!");
      }
    }
    return ret;
  }

  private Integer getMinLevel(String minLevelStr)
  {
    // Expected input string format: "Minimum Level: 55".
    Integer ret = null;
    if ((minLevelStr!=null) && (minLevelStr.startsWith("Minimum Level")))
    {
      try
      {
        int separatorIndex = minLevelStr.indexOf(':');
        if (separatorIndex!=-1)
        {
          String valueStr = minLevelStr.substring(separatorIndex+1).trim();
          ret = Integer.valueOf(valueStr);
        }
      }
      catch(Exception e)
      {
        _logger.warn("Unexpected minimum level value ["+minLevelStr+"]!");
      }
    }
    return ret;
  }

  private Integer getStackSize(String stackSizeStr)
  {
    // Expected input string format: "Stacks to 100".
    Integer ret = null;
    if ((stackSizeStr!=null) && (stackSizeStr.startsWith("Stacks")))
    {
      try
      {
        int separatorIndex = stackSizeStr.indexOf("to ");
        if (separatorIndex!=-1)
        {
          String valueStr = stackSizeStr.substring(separatorIndex+3).trim();
          ret = Integer.valueOf(valueStr);
        }
      }
      catch(Exception e)
      {
        _logger.warn("Unexpected stackability value ["+stackSizeStr+"]!");
      }
    }
    return ret;
  }

  private String parseClassRequirement(Element requirementTag)
  {
    // <div class="itemrequirement">
    //    Class:
    //    <a href="/wiki/Class:Hunter">Hunter</a>
    // </div>
    String className=JerichoHtmlUtils.getTagContents(requirementTag,HTMLElementName.A);
    return className;
  }
  
  private static String[] ITEM_NAME_TAGS={
    "itemname common",
    "itemname uncommon",
    "itemname incomparable",
    "itemname rare",
    "itemname epic"
  };

  private String findName(Element itemTooltip)
  {
    String name=null;
    for(String tagName : ITEM_NAME_TAGS)
    {
      name=getTagContent(itemTooltip,tagName);
      if (name!=null) break;
    }
    if (name==null)
    {
      _logger.warn("No name found in tooltip ["+itemTooltip+"]");
    }
    return name;
  }

  private String getIconURL(Element itemTooltip)
  {
    String url=null;
    Element iconElement=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(itemTooltip,HTMLElementName.DIV,"class","itemicon");
    if (iconElement!=null)
    {
      Element imgElement=iconElement.getFirstElement(HTMLElementName.IMG);
      url=imgElement.getAttributeValue("src");
    }
    return url;
  }

  private ItemsSet parseItemsSet(Element itemTooltip)
  {
    ItemsSet ret=null;
    Element itemsSet=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(itemTooltip,HTMLElementName.DIV,"class","itemset");
    if (itemsSet!=null)
    {
      ret=new ItemsSet();
      String setIdentifier=null;
      String setName="";
      String setURL=null;
      Element itemsSetName=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(itemsSet,HTMLElementName.DIV,"class","itemsn");
      if (itemsSetName!=null)
      {
        Element aElement=itemsSetName.getFirstElement(HTMLElementName.A);
        if (aElement!=null)
        {
          setURL=aElement.getAttributeValue("href");
          if ((setURL!=null) && (setURL.startsWith(WIKI_SEED)))
          {
            setIdentifier=setURL.substring(WIKI_SEED.length());
            int index=setIdentifier.indexOf(":");
            if (index!=-1)
            {
              setIdentifier=setIdentifier.substring(index+1);
            }
          }
          setName=JerichoHtmlUtils.getTagContents(itemsSetName,HTMLElementName.A);
        }
      }
      //System.out.println("Set name: "+setName);
      //System.out.println("Set URL: "+setURL);
      ret.setName(setName);
      ret.setId(setIdentifier);
      // Set items
      Element setItemsElement=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(itemsSet,HTMLElementName.DIV,"class","itemsps");
      if (setItemsElement!=null)
      {
        List<Element> setItemElements=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(itemsSet,HTMLElementName.DIV,"class","itemsp");
        for(Element setItemElement : setItemElements)
        {
          String itemId=null;
          String itemURL=null;
          //String itemName=null;
          Element aElement=setItemElement.getFirstElement(HTMLElementName.A);
          if (aElement!=null)
          {
            itemURL=aElement.getAttributeValue("href");
            if ((itemURL!=null) && (itemURL.startsWith(WIKI_SEED)))
            {
              itemId=itemURL.substring(WIKI_SEED.length());
            }
            //itemName=JerichoHtmlUtils.getTagContents(setItemElement,HTMLElementName.A);
          }
          if (itemId!=null)
          {
            ret.addItemId(itemId);
          }
          //System.out.println("Item ["+itemName+"]. URL=["+itemURL+"]");
        }
      }
      // Set effects
      Element setEffectsElement=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(itemsSet,HTMLElementName.DIV,"class","itemseteff");
      if (setEffectsElement!=null)
      {
        List<Element> divs=setEffectsElement.getAllElements(HTMLElementName.DIV);
        divs.remove(0); // Remove self
        int nbOfItems=0;
        for(Element div : divs)
        {
          String effectLine=JerichoHtmlUtils.getTagContents(div,HTMLElementName.DIV);
          //System.out.println("Effect line ["+effectLine+"]");
          effectLine=effectLine.trim();
          if (effectLine.length()>0)
          {
            if ((effectLine.startsWith(SET_NUMBER_SEED)) && (effectLine.endsWith(SET_NUMBER_END)))
            {
              String nbStr=TextTools.findBetween(effectLine,SET_NUMBER_SEED,SET_NUMBER_END);
              nbOfItems=NumericTools.parseInt(nbStr,0);
            }
            else
            {
              if (nbOfItems!=0)
              {
                ret.addBonus(nbOfItems,effectLine);
              }
            }
          }
        }
      }
    }
    /*
<div class="itemset">
  <div class="itemsn"><a href="/wiki/Item:Armaments_of_the_Impossible_Shot">Armaments of the Impossible Shot</a></div>
  <div class="itemsps">
    <div class="itemsp"><a href="/wiki/Armour:Helm_of_the_Impossible_Shot">Helm of the Impossible Shot</a></div>
    <div class="itemsp"><a href="/wiki/Armour:Shoulder_guards_of_Impossible_Shot">Shoulder guards of Impossible Shot</a></div>
    <div class="itemsp"><a href="/wiki/Armour:Jacket_of_the_Impossible_Shot">Jacket of the Impossible Shot</a></div>
    <div class="itemsp"><a href="/wiki/Armour:Leggings_of_the_Impossible_Shot">Leggings of the Impossible Shot</a></div>
    <div class="itemsp"><a href="/wiki/Armour:Gloves_of_the_Impossible_Shot">Gloves of the Impossible Shot</a></div>
    <div class="itemsp"><a href="/wiki/Armour:Boots_of_the_Impossible_Shot">Boots of the Impossible Shot</a></div>
  </div>
  <div class="itemseteff">
    <div>Set (2):</div>
    <div>+52.8 in-Combat Power Regen</div>
    <div>Set (3):</div>
    <div>+10% Heart Seeker Damage</div>
    <div>Set (4):</div>
    <div>+600 Physical Mastery Rating</div>
    <div> </div>
    <div>Set (5):</div>
    <div>Intent Concentration recovers all Archer's Art skills</div>
  </div>
  <div class="itemsd"></div>
</div>
     */
    return ret;
  }

  private void parseItemDescription(Element itemTooltip)
  {
    // Find out type of item
    Armour armour=null;
    // Armor?
    // <div class="itemarmor">1130 Armour Value</div>
    String armorStr=getTagContent(itemTooltip,"itemarmor");
    Weapon weapon=null;
    // Weapon?
    // <div class="itemdps">126.5 DPS</div>
    String dpsStr=getTagContent(itemTooltip,"itemdps");
    if (armorStr!=null)
    {
      armour=new Armour();
      _item=armour;
      Integer armourValue=getArmour(armorStr);
      if (armourValue!=null)
      {
        armour.setArmourValue(armourValue.intValue());
      }
    }
    else if (dpsStr!=null)
    {
      weapon=new Weapon();
      _item=weapon;
      Float dpsValue=getDPS(dpsStr);
      if (dpsValue!=null)
      {
        weapon.setDPS(dpsValue.floatValue());
      }
    }
    else
    {
      _item=new Item();
    }
    
    // Icon
    // <div class="itemicon"><img src="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/item/tool/eq_c_craft_tool_voc_explorer_tier6.png"></div>
    String url=getIconURL(itemTooltip);
    
    // Name
    String name=findName(itemTooltip);
    _item.setName(name);
    if (url!=null) {
      _item.setIconURL(url);
    }
    // Item sub-category
    // <div class="itemtype">Craft Tool</div>
    String subCategory=getTagContent(itemTooltip,"itemtype");
    _item.setSubCategory(subCategory);
    // Uniqueness
    // <div class="itemunique"></div>
    String uniqueStr=getTagContent(itemTooltip,"itemunique");
    if ("Unique".equalsIgnoreCase(uniqueStr))
    {
      _item.setUnique(true);
    }
    // Item bind
    // <div class="itembind">Bind on Equip</div>
    String itemBindStr=getTagContent(itemTooltip,"itembind");
    ItemBinding binding=getBinding(itemBindStr);
    _item.setBinding(binding);

    // Damage:
    // <div class="itemdamage">197 - 359 Common Damage</div>
    String damage=getTagContent(itemTooltip,"itemdamage");
    if (damage!=null)
    {
      try
      {
        if (damage.endsWith("Damage"))
        {
          String tmp=damage.substring(0,damage.length()-6).trim();
          String[] split=tmp.split(" ",4);
          int minDamage=Integer.parseInt(split[0]);
          int maxDamage=Integer.parseInt(split[2]);
          String typeStr=split[3];
          DamageType type=DamageType.COMMON;
          if ("Common".equals(typeStr)) type=DamageType.COMMON;
          else if ("Ancient Dwarf-make".equals(typeStr)) type=DamageType.ANCIENT_DWARF;
          else if ("Beleriand".equals(typeStr)) type=DamageType.BELERIAND;
          else if ("Westernesse".equals(typeStr)) type=DamageType.WESTERNESSE;
          else if ("Fire".equals(typeStr)) type=DamageType.FIRE;
          else if ("Shadow".equals(typeStr)) type=DamageType.SHADOW;
          else if ("Light".equals(typeStr)) type=DamageType.LIGHT;
          else
          {
            _logger.warn("Unmanaged damage type ["+typeStr+"]");
          }
          weapon.setMinDamage(minDamage);
          weapon.setMaxDamage(maxDamage);
          weapon.setDamageType(type);
        }
      }
      catch(Exception e)
      {
        _logger.error("Damage parsing exception on ["+damage+"]",e);
      }
    }
    
    // Bonuses
    // <div class="itemes">
    //   <div class="iteme">
    //     <div>-3s Forester Chopping Duration</div>
    //   </div>
    //   ...
    // </div>
    List<String> bonuses=new ArrayList<String>();
    Element itemsContainer=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(itemTooltip,HTMLElementName.DIV,"class","itemes");
    if (itemsContainer!=null)
    {
      List<Element> itemsList=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(itemsContainer,HTMLElementName.DIV,"class","iteme");
      for(Element item : itemsList)
      {
        List<Element> children = item.getChildElements();
        if (children != null)
        {
          for(Element child : children)
          {
            String line = JerichoHtmlUtils.getTagContents(child,HTMLElementName.DIV);
            bonuses.add(line);
          }
        }
      }
    }
    _item.setBonus(bonuses);
    
    
    // TODO <div class="itemmsi">+5 Damage to The Dead</div>
    String msi=getTagContent(itemTooltip,"itemmsi");
    if ((msi!=null) && (msi.length()>0))
    {
      _logger.warn("Unmanaged itemmsi ["+msi+"] for "+_item.getName());
    }

    // Item set
    ItemsSet set=parseItemsSet(itemTooltip);
    if (set!=null)
    {
      _item.setSetIdentifier(set.getId());
      _set=set;
    }

    // Possible legacies TODO
    /*
<div class="itemes">Possible Initial Legacies:</div>
  <div>Focus Bow Critical Multiplier (Tier(s):
    <span class="legacytier rare">4</span>
    ,
    <span class="legacytier incomparable">5</span>
    ,
    <span class="legacytier incomparable">6</span>
    )
  </div>
  <div>Focus Bow Power Cost (Tier(s):
    <span class="legacytier rare">4</span>
    ,
    <span class="legacytier incomparable">5</span>
    ,
    <span class="legacytier incomparable">6</span>
  )
  </div>
<div>
Induction Bow Critical Multiplier (Tier(s):
<span class="legacytier rare">4</span>
,
<span class="legacytier incomparable">5</span>
,
<span class="legacytier incomparable">6</span>
)
</div>
<div>
<div>
Merciful Shot Cooldown (Tier(s):
<span class="legacytier rare">4</span>
,
<span class="legacytier incomparable">5</span>
,
<span class="legacytier incomparable">6</span>
)
</div>
<div>
Quick Shot Critical Chance (Tier(s):
<span class="legacytier rare">4</span>
,
<span class="legacytier incomparable">5</span>
,
<span class="legacytier incomparable">6</span>
)
</div>
<div>
Ranged Skill Block Chance Modifier (Tier(s):
<span class="legacytier rare">4</span>
,
<span class="legacytier incomparable">5</span>
,
<span class="legacytier incomparable">6</span>
)
</div>
<div>
Ranged Skill Evade Chance Modifier (Tier(s):
<span class="legacytier rare">4</span>
,
<span class="legacytier incomparable">5</span>
,
<span class="legacytier incomparable">6</span>
)
</div>
<div>
Strength Quick Shot Slow (Tier(s):
<span class="legacytier rare">4</span>
,
<span class="legacytier incomparable">5</span>
,
<span class="legacytier incomparable">6</span>
)
</div>
     * 
     */
    
    // Item durability:
    // - durability
    // <div class="itemdurability">Durability 60 / 60</div>
    String durabilityStr=getTagContent(itemTooltip,"itemdurability");
    Integer durability=getDurability(durabilityStr);
    _item.setDurability(durability);
    // - sturdiness
    // <div class="itemsturdiness">Tough</div>
    String sturdinessStr=getTagContent(itemTooltip,"itemsturdiness");
    ItemSturdiness sturdiness=getSturdiness(sturdinessStr);
    _item.setSturdiness(sturdiness);
    
    // Item requirements
    List<Element> requirements=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(itemTooltip,HTMLElementName.DIV,"class","itemrequirement");
    for(Element requirement : requirements)
    {
      //String contents=getTagContent(requirement,"itemrequirement");
      String contents=CharacterReference.decodeCollapseWhiteSpace(requirement.getContent());
      if (contents.contains("Minimum Level"))
      {
        // - minimum level
        // <div class="itemrequirement">Minimum Level: 55</div>
        String minLevelStr=getTagContent(itemTooltip,"itemrequirement");
        Integer minLevel=getMinLevel(minLevelStr);
        _item.setMinLevel(minLevel);
      }
      else if (contents.contains("Class"))
      {
        // - class
        String className=parseClassRequirement(requirement);
        if (className!=null)
        {
          CharacterClass cClass=CharacterClass.getByLabel(className);
          _item.setRequiredClass(cClass);
        }
      }
    }
    
    // Description
    // <div class="itemdescription">A collection of indispensable tools for tailors, foresters, and prospectors.</div>
    //String description=getTagContent(itemTooltip,"");
    Element element=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(itemTooltip,HTMLElementName.DIV,"class","itemdescription");
    if (element!=null)
    {
      String description=JerichoHtmlUtils.getTextFromTag(element);
      _item.setDescription(description);
    }

    // Money
    // <div class="itemworth">
    Element worth=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(itemTooltip,HTMLElementName.DIV,"class","itemworth");
    if (worth!=null)
    {
      Money m=parseMoneyReward(worth);
      _item.setValue(m);
    }
    // Stackability
    // <div class="itemstacksize">Stacks to 100</div>
    String stackabilityStr=getTagContent(itemTooltip,"itemstacksize");
    Integer stackSize=getStackSize(stackabilityStr);
    _item.setStackMax(stackSize);
    
    /*
    // Item category: Armour, Tool, ...
    private ItemCategory _category;
    // Item identifier: "Jacket_of_the_Impossible_Shot", ...
    private String _id;
    */
  }

  private Money parseMoneyReward(Element rewardDiv)
  {
    Money m=new Money();
    List<Segment> nodes=JerichoHtmlUtils.getChildNodes(rewardDiv);
    int nb=nodes.size();
    for(int i=0;i<nb;i++)
    {
      Segment s=nodes.get(i);
      if (s.getClass()==Segment.class)
      {
        String text = s.toString();
        if (text.equalsIgnoreCase("Worth")) continue;
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
    return m;
    /*
     <div class="itemworth">
        Worth&nbsp;&nbsp;&nbsp;3
        <img class="coin" src="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/currency/silver.gif">
        &nbsp;30
        <img class="coin" src="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/currency/copper.gif">
     </div>
    */    
  }

  private Item parseItemSection(Element itemSection)
  {
    Item ret=null;
    try
    {
      _item=null;
      _set=null;
      Element itemTooltip=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(itemSection,HTMLElementName.TABLE,"class","tooltip");
      if (itemTooltip!=null)
      {
        parseItemDescription(itemTooltip);
      }
      ret=_item;
    }
    catch(Exception e)
    {
      ret=null;
      _logger.error("Item ["+_identifier+"]. Cannot parse item section!",e);
    }
    return ret;
  }

  /**
   * Parse the item page at the given URL.
   * @param url URL of quest page.
   * @return A list of items or <code>null</code> if an error occurred.
   */
  public List<Item> parseItemPage(String url)
  {
    List<Item> ret=internalParseItemPage(url);
    /*
    Item ret=null;
    if (url.contains("?id="))
    {
      MyLotroURL2Identifier id=new MyLotroURL2Identifier();
      String itemId=id.findIdentifier(url,true);
      if (itemId!=null)
      {
        String explicitURL=ROOT_URL+itemId;
        ret=internalParseItemPage(explicitURL);
      }
    }
    else
    {
      ret=internalParseItemPage(url);
    }
    */
    return ret;
  }

  /**
   * Parse the item page at the given URL.
   * @param url URL of quest page.
   * @return An item or <code>null</code> if an error occurred.
   */
  public Item parseOneItemPage(String url)
  {
    Item ret=null;
    List<Item> items=internalParseItemPage(url);
    if ((items!=null) && (items.size()>0))
    {
      ret=items.get(0);
    }
    return ret;
  }

  /**
   * Get the extracted items set (if any).
   * @return An items set or <code>null</code>.
   */
  public ItemsSet getItemsSet()
  {
    return _set;
  }

  private static final String HREF_IDENTIFIER_SEED="/wiki/";

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
          if (href.startsWith(HREF_IDENTIFIER_SEED))
          {
            id=href.substring(HREF_IDENTIFIER_SEED.length());
          }
        }
      }
    }
    return id;
  }

  /**
   * Parse the item page at the given URL.
   * @param url URL of quest page.
   * @return A quest or <code>null</code> if an error occurred.
   */
  private List<Item> internalParseItemPage(String url)
  {
    List<Item> items=null;
    try
    {
      DownloadService downloader=DownloadService.getInstance();
      String page=downloader.getPage(url);
      Source source=new Source(page);

      //<div id="lorebookNoedit">
      //Element lorebook=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.DIV,"id","lorebookNoedit");
      //if (lorebook!=null)
      {
        items=new ArrayList<Item>();
        // Fetch identifier
        List<Element> itemSections=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(source,HTMLElementName.TABLE,"class","tooltip");
        //List<Element> itemSections=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(source,HTMLElementName.DIV,"class","lorebookclass");
        if ((itemSections!=null) && (itemSections.size()>0))
        {
          String identifier=fetchIdentifier(source);
          for(Element itemSection : itemSections)
          {
            Item item=parseItemSection(itemSection);
            if (item!=null)
            {
              items.add(item);
              item.setIdentifier(identifier);
            }
          }
        }
      }
    }
    catch(Exception e)
    {
      items=null;
      _logger.error("Cannot parse quest page ["+url+"]",e);
    }
    /*
    Item ret=null;
    if ((items!=null) && (items.size()>0))
    {
      ret=items.get(0);
    }
    */
    return items;
  }
}
