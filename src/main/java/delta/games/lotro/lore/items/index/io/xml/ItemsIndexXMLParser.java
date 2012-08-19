package delta.games.lotro.lore.items.index.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.lore.items.index.ItemsIndex;

/**
 * Parser for item indexes stored in XML.
 * @author DAM
 */
public class ItemsIndexXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed item or <code>null</code>.
   */
  public ItemsIndex parseXML(File source)
  {
    ItemsIndex index=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      index=parseItem(root);
    }
    return index;
  }

  private ItemsIndex parseItem(Element root)
  {
    ItemsIndex index=new ItemsIndex();

    // Categories
    List<Element> categoryTags=DOMParsingTools.getChildTagsByName(root,ItemsIndexXMLConstants.CATEGORY_TAG);
    if (categoryTags!=null)
    {
      for(Element categoryTag : categoryTags)
      {
        String name=DOMParsingTools.getStringAttribute(categoryTag.getAttributes(),ItemsIndexXMLConstants.CATEGORY_NAME_ATTR,null);
        if (name!=null)
        {
          // Items
          List<Element> itemTags=DOMParsingTools.getChildTagsByName(categoryTag,ItemsIndexXMLConstants.ITEM_TAG);
          if (itemTags!=null)
          {
            for(Element itemTag : itemTags)
            {
              NamedNodeMap attrs=itemTag.getAttributes();
              String itemId=DOMParsingTools.getStringAttribute(attrs,ItemsIndexXMLConstants.ITEM_ID_ATTR,null);
              String itemName=DOMParsingTools.getStringAttribute(attrs,ItemsIndexXMLConstants.ITEM_NAME_ATTR,null);
              index.addItem(name,itemId,itemName);
            }
          }
        }
      }
    }
    return index;
  }
}
