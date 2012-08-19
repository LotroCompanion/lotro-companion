package delta.games.lotro.items.io.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.money.io.xml.MoneyXMLParser;
import delta.games.lotro.items.Armour;
import delta.games.lotro.items.DamageType;
import delta.games.lotro.items.Item;
import delta.games.lotro.items.ItemBinding;
import delta.games.lotro.items.ItemCategory;
import delta.games.lotro.items.ItemFactory;
import delta.games.lotro.items.ItemSturdiness;
import delta.games.lotro.items.Weapon;

/**
 * Parser for item descriptions stored in XML.
 * @author DAM
 */
public class ItemXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed item or <code>null</code>.
   */
  public Item parseXML(File source)
  {
    Item item=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      item=parseItem(root);
    }
    return item;
  }

  private Item parseItem(Element root)
  {
    Item ret=null;
    NamedNodeMap attrs=root.getAttributes();

    // Category
    ItemCategory category=null;
    String categoryStr=DOMParsingTools.getStringAttribute(attrs,ItemXMLConstants.ITEM_CATEGORY_ATTR,null);
    if (categoryStr!=null)
    {
      category=ItemCategory.valueOf(categoryStr);
    }
    ret=ItemFactory.buildItem(category);
    ret.setCategory(category);
    // Identifier
    String id=DOMParsingTools.getStringAttribute(attrs,ItemXMLConstants.ITEM_ID_ATTR,null);
    ret.setIdentifier(id);
    // Set identifier
    String setId=DOMParsingTools.getStringAttribute(attrs,ItemXMLConstants.ITEM_SET_ID_ATTR,null);
    ret.setSetIdentifier(setId);
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,ItemXMLConstants.ITEM_NAME_ATTR,null);
    ret.setName(name);
    // Icon URL
    String iconURL=DOMParsingTools.getStringAttribute(attrs,ItemXMLConstants.ITEM_ICON_URL_ATTR,null);
    ret.setIconURL(iconURL);
    // Sub-category
    String subCategory=DOMParsingTools.getStringAttribute(attrs,ItemXMLConstants.ITEM_SUBCATEGORY_ATTR,null);
    ret.setSubCategory(subCategory);
    // Item binding
    ItemBinding binding=null;
    String bindingStr=DOMParsingTools.getStringAttribute(attrs,ItemXMLConstants.ITEM_BINDING_ATTR,null);
    if (bindingStr!=null)
    {
      binding=ItemBinding.valueOf(bindingStr);
    }
    ret.setBinding(binding);
    // Uniqueness
    boolean unique=DOMParsingTools.getBooleanAttribute(attrs,ItemXMLConstants.ITEM_UNIQUE_ATTR,false);
    ret.setUnique(unique);
    // Bonuses
    List<Element> bonusTags=DOMParsingTools.getChildTagsByName(root,ItemXMLConstants.BONUS_TAG);
    List<String> bonuses=new ArrayList<String>();
    if (bonusTags!=null)
    {
      for(Element bonusTag : bonusTags)
      {
        String value=DOMParsingTools.getStringAttribute(bonusTag.getAttributes(),ItemXMLConstants.BONUS_VALUE_ATTR,null);
        if (value!=null)
        {
          bonuses.add(value);
        }
      }
    }
    ret.setBonus(bonuses);
    // Durability
    int durability=DOMParsingTools.getIntAttribute(attrs,ItemXMLConstants.ITEM_DURABILITY_ATTR,-1);
    if (durability!=-1)
    {
      ret.setDurability(Integer.valueOf(durability));
    }
    // Sturdiness
    ItemSturdiness sturdiness=null;
    String sturdinessStr=DOMParsingTools.getStringAttribute(attrs,ItemXMLConstants.ITEM_STURDINESS_ATTR,null);
    if (sturdinessStr!=null)
    {
      sturdiness=ItemSturdiness.valueOf(sturdinessStr);
    }
    ret.setSturdiness(sturdiness);
    // Minimum level
    int minimumLevel=DOMParsingTools.getIntAttribute(attrs,ItemXMLConstants.ITEM_MINLEVEL_ATTR,-1);
    if (minimumLevel!=-1)
    {
      ret.setMinLevel(Integer.valueOf(minimumLevel));
    }
    // Required class
    CharacterClass cClass=null;
    String requiredClass=DOMParsingTools.getStringAttribute(attrs,ItemXMLConstants.ITEM_REQUIRED_CLASS_ATTR,null);
    if (requiredClass!=null)
    {
      cClass=CharacterClass.getByLabel(requiredClass);
    }
    ret.setRequiredClass(cClass);
    // Full description
    String description=DOMParsingTools.getStringAttribute(attrs,ItemXMLConstants.ITEM_DESCRIPTION_ATTR,null);
    ret.setDescription(description);

    MoneyXMLParser.loadMoney(root,ret.getValue());
    // Stack max
    int stackMax=DOMParsingTools.getIntAttribute(attrs,ItemXMLConstants.ITEM_STACK_MAX_ATTR,-1);
    if (stackMax!=-1)
    {
      ret.setStackMax(Integer.valueOf(stackMax));
    }
    // Armor specific:
    if (category==ItemCategory.ARMOUR)
    {
      Armour armour=(Armour)ret;
      int armourValue=DOMParsingTools.getIntAttribute(attrs,ItemXMLConstants.ARMOUR_ATTR,0);
      armour.setArmourValue(armourValue);
    }
    // Weapon specific:
    if (category==ItemCategory.WEAPON)
    {
      Weapon weapon=(Weapon)ret;
      float dps=DOMParsingTools.getFloatAttribute(attrs,ItemXMLConstants.DPS_ATTR,0.0f);
      weapon.setDPS(dps);
      int minDamage=DOMParsingTools.getIntAttribute(attrs,ItemXMLConstants.MIN_DAMAGE_ATTR,0);
      weapon.setMinDamage(minDamage);
      int maxDamage=DOMParsingTools.getIntAttribute(attrs,ItemXMLConstants.MAX_DAMAGE_ATTR,0);
      weapon.setMaxDamage(maxDamage);
      String damageTypeStr=DOMParsingTools.getStringAttribute(attrs,ItemXMLConstants.DAMAGE_TYPE_ATTR,null);
      if (damageTypeStr!=null)
      {
        DamageType type=DamageType.valueOf(damageTypeStr);
        weapon.setDamageType(type);
      }
    }
    return ret;
  }
}
