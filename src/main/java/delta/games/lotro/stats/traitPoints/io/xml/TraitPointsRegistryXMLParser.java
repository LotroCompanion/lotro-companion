package delta.games.lotro.stats.traitPoints.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.lore.items.io.xml.ItemXMLConstants;
import delta.games.lotro.stats.traitPoints.TraitPoint;
import delta.games.lotro.stats.traitPoints.TraitPointsRegistry;

/**
 * Parser for the trait point registry stored in XML.
 * @author DAM
 */
public class TraitPointsRegistryXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed registry or <code>null</code>.
   */
  public TraitPointsRegistry parseXML(File source)
  {
    TraitPointsRegistry registry=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      registry=parseRegistry(root);
    }
    return registry;
  }

  private TraitPointsRegistry parseRegistry(Element root)
  {
    TraitPointsRegistry registry=new TraitPointsRegistry();
    List<Element> pointTags=DOMParsingTools.getChildTagsByName(root,TraitPointsRegistryXMLConstants.TRAIT_POINT_TAG,false);
    for(Element itemTag : pointTags)
    {
      TraitPoint item=parsePoint(itemTag);
      registry.registerTraitPoint(item);
    }
    return registry;
  }

  /**
   * Build a trait point from an XML tag.
   * @param root Root XML tag.
   * @return A trait point.
   */
  public TraitPoint parsePoint(Element root)
  {
    TraitPoint ret=null;
    NamedNodeMap attrs=root.getAttributes();

    // Identifier
    String id=DOMParsingTools.getStringAttribute(attrs,TraitPointsRegistryXMLConstants.TRAIT_POINT_ID_ATTR,null);
    // Label
    String label=DOMParsingTools.getStringAttribute(attrs,TraitPointsRegistryXMLConstants.TRAIT_POINT_LABEL_ATTR,null);
    // Required class
    CharacterClass requiredCharacterClass=null;
    String requiredClass=DOMParsingTools.getStringAttribute(attrs,ItemXMLConstants.ITEM_REQUIRED_CLASS_ATTR,null);
    if (requiredClass!=null)
    {
      requiredCharacterClass=CharacterClass.getByKey(requiredClass);
    }
    if (id!=null)
    {
      ret=new TraitPoint(id,requiredCharacterClass);
      if (label!=null)
      {
        ret.setLabel(label);
      }
    }
    return ret;
  }
}
