package delta.games.lotro.lore.deeds.index.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.lore.deeds.index.DeedsIndex;

/**
 * Parser for deed indexes stored in XML.
 * @author DAM
 */
public class DeedsIndexXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed deeds index or <code>null</code>.
   */
  public DeedsIndex parseXML(File source)
  {
    DeedsIndex index=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      index=parseDeedsIndex(root);
    }
    return index;
  }

  private DeedsIndex parseDeedsIndex(Element root)
  {
    DeedsIndex index=new DeedsIndex();

    // Categories
    List<Element> categoryTags=DOMParsingTools.getChildTagsByName(root,DeedsIndexXMLConstants.CATEGORY_TAG);
    if (categoryTags!=null)
    {
      for(Element categoryTag : categoryTags)
      {
        String name=DOMParsingTools.getStringAttribute(categoryTag.getAttributes(),DeedsIndexXMLConstants.CATEGORY_NAME_ATTR,null);
        if (name!=null)
        {
          // Deeds
          List<Element> deedTags=DOMParsingTools.getChildTagsByName(categoryTag,DeedsIndexXMLConstants.DEED_TAG);
          if (deedTags!=null)
          {
            for(Element deedTag : deedTags)
            {
              NamedNodeMap attrs=deedTag.getAttributes();
              int deedIdentifier=DOMParsingTools.getIntAttribute(attrs,DeedsIndexXMLConstants.DEED_ID_ATTR,0);
              String deedKey=DOMParsingTools.getStringAttribute(attrs,DeedsIndexXMLConstants.DEED_KEY_ATTR,null);
              String deedName=DOMParsingTools.getStringAttribute(attrs,DeedsIndexXMLConstants.DEED_NAME_ATTR,null);
              index.addDeed(name,deedIdentifier,deedKey,deedName);
            }
          }
        }
      }
    }
    return index;
  }
}
