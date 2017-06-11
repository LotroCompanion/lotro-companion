package delta.games.lotro.stats.traitPoints.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.stats.traitPoints.TraitPointsStatus;

/**
 * Parser for the trait point status stored in XML.
 * @author DAM
 */
public class TraitPointsStatusXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed status or <code>null</code>.
   */
  public TraitPointsStatus parseXML(File source)
  {
    TraitPointsStatus status=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      status=parseStatus(root);
    }
    return status;
  }

  private TraitPointsStatus parseStatus(Element root)
  {
    TraitPointsStatus status=new TraitPointsStatus();
    List<Element> pointTags=DOMParsingTools.getChildTagsByName(root,TraitPointsRegistryXMLConstants.TRAIT_POINT_TAG,false);
    for(Element pointTag : pointTags)
    {
      NamedNodeMap attrs=pointTag.getAttributes();
      String id=DOMParsingTools.getStringAttribute(attrs,TraitPointsRegistryXMLConstants.TRAIT_POINT_ID_ATTR,null);
      status.setStatus(id,true);
    }
    return status;
  }
}
