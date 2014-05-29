package delta.games.lotro.lore.items.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.NumericTools;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.lore.items.ItemsSet;

/**
 * Parser for items sets descriptions stored in XML.
 * @author DAM
 */
public class ItemsSetXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed items set or <code>null</code>.
   */
  public ItemsSet parseXML(File source)
  {
    ItemsSet set=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      set=parseSet(root);
    }
    return set;
  }

  private ItemsSet parseSet(Element root)
  {
    ItemsSet ret=new ItemsSet();

    NamedNodeMap attrs=root.getAttributes();
    // Identifier
    String id=DOMParsingTools.getStringAttribute(attrs,ItemsSetXMLConstants.ITEMS_SET_KEY_ATTR,null);
    ret.setKey(id);
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,ItemsSetXMLConstants.ITEMS_SET_NAME_ATTR,null);
    ret.setName(name);
    // Items
    List<Element> itemTags=DOMParsingTools.getChildTagsByName(root,ItemsSetXMLConstants.ITEM_TAG);
    if (itemTags!=null)
    {
      for(Element itemTag : itemTags)
      {
        String setItemKey=DOMParsingTools.getStringAttribute(itemTag.getAttributes(),ItemsSetXMLConstants.ITEM_KEY_ATTR,null);
        int setItemId=DOMParsingTools.getIntAttribute(itemTag.getAttributes(),ItemsSetXMLConstants.ITEM_ID_ATTR,0);
        ret.addItem(setItemId,setItemKey);
      }
    }
    // Bonuses
    List<Element> bonusTags=DOMParsingTools.getChildTagsByName(root,ItemsSetXMLConstants.BONUS_TAG);
    if (bonusTags!=null)
    {
      for(Element bonusTag : bonusTags)
      {
        NamedNodeMap bonusAttrs=bonusTag.getAttributes();
        String nbStr=DOMParsingTools.getStringAttribute(bonusAttrs,ItemsSetXMLConstants.BONUS_NB_ITEMS_ATTR,null);
        int nb=NumericTools.parseInt(nbStr,0);
        String value=DOMParsingTools.getStringAttribute(bonusAttrs,ItemsSetXMLConstants.BONUS_VALUE_ATTR,null);
        if ((nb!=0) && (value!=null))
        {
          ret.addBonus(nb,value);
        }
      }
    }
    return ret;
  }
}
