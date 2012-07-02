package delta.games.lotro.items.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.money.io.xml.MoneyXMLParser;
import delta.games.lotro.items.Item;
import delta.games.lotro.items.ItemBinding;
import delta.games.lotro.items.ItemCategory;
import delta.games.lotro.items.ItemFactory;
import delta.games.lotro.items.ItemSturdiness;
import delta.games.lotro.items.io.xml.ItemXMLConstants;

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
    String requiredClass=DOMParsingTools.getStringAttribute(attrs,ItemXMLConstants.ITEM_REQUIRED_CLASS_ATTR,null);
    ret.setRequiredClass(requiredClass);
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
    
    // TODO Handle subclasses: Armour, Weapon
    return ret;
  }
}
