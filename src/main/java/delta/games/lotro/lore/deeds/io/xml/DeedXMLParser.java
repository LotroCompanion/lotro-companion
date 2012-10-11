package delta.games.lotro.lore.deeds.io.xml;

import java.io.File;
import java.io.InputStream;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.io.xml.RewardsXMLParser;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedDescription.TYPE;

/**
 * Parser for deed descriptions stored in XML.
 * @author DAM
 */
public class DeedXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed deed or <code>null</code>.
   */
  public DeedDescription parseXML(File source)
  {
    DeedDescription c=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      c=parseDeed(root);
    }
    return c;
  }

  /**
   * Parse the XML stream.
   * @param source Source stream.
   * @return Parsed deed or <code>null</code>.
   */
  public DeedDescription parseXML(InputStream source)
  {
    DeedDescription deed=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      deed=parseDeed(root);
    }
    return deed;
  }

  private DeedDescription parseDeed(Element root)
  {
    DeedDescription deed=new DeedDescription();

    NamedNodeMap attrs=root.getAttributes();

    // Identifier
    int id=DOMParsingTools.getIntAttribute(attrs,DeedXMLConstants.DEED_ID_ATTR,0);
    deed.setIdentifier(id);
    // Key
    String key=DOMParsingTools.getStringAttribute(attrs,DeedXMLConstants.DEED_KEY_ATTR,null);
    deed.setKey(key);
    // Name
    String title=DOMParsingTools.getStringAttribute(attrs,DeedXMLConstants.DEED_NAME_ATTR,null);
    deed.setName(title);
    // Type
    TYPE type=null;
    String typeStr=DOMParsingTools.getStringAttribute(attrs,DeedXMLConstants.DEED_TYPE_ATTR,null);
    if (typeStr!=null)
    {
      type=TYPE.valueOf(typeStr);
    }
    deed.setType(type);
    // Class
    String className=DOMParsingTools.getStringAttribute(attrs,DeedXMLConstants.DEED_CLASS_ATTR,null);
    deed.setClassName(className);
    // Minimum level
    int minimumLevel=DOMParsingTools.getIntAttribute(attrs,DeedXMLConstants.DEED_MIN_LEVEL_ATTR,-1);
    if (minimumLevel!=-1)
    {
      deed.setMinLevel(Integer.valueOf(minimumLevel));
    }
    // Description
    String description=DOMParsingTools.getStringAttribute(attrs,DeedXMLConstants.DEED_DESCRIPTION_ATTR,null);
    deed.setDescription(description);
    // Objectives
    String objectives=DOMParsingTools.getStringAttribute(attrs,DeedXMLConstants.DEED_OBJECTIVES_ATTR,null);
    deed.setObjectives(objectives);

    RewardsXMLParser.loadRewards(root,deed.getRewards());
    return deed;
  }
}
