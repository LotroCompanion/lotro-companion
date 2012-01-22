package delta.games.lotro.utils.resources.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.utils.resources.ResourcesMapping;

/**
 * Parser for resources mappings stored in XML.
 * @author DAM
 */
public class ResourcesMappingXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed mapping or <code>null</code>.
   */
  public ResourcesMapping parseXML(File source)
  {
    ResourcesMapping mapping=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      mapping=parseMapping(root);
    }
    return mapping;
  }

  private ResourcesMapping parseMapping(Element root)
  {
    ResourcesMapping mapping=new ResourcesMapping();

    // Required classes
    List<Element> resourceTags=DOMParsingTools.getChildTagsByName(root,ResourcesMappingXMLConstants.RESOURCE_TAG);
    if (resourceTags!=null)
    {
      for(Element resourceTag : resourceTags)
      {
        NamedNodeMap attrs=resourceTag.getAttributes();
        int resource=DOMParsingTools.getIntAttribute(attrs,ResourcesMappingXMLConstants.RESOURCE_VALUE_ATTR,-1);
        String identifier=DOMParsingTools.getStringAttribute(attrs,ResourcesMappingXMLConstants.RESOURCE_IDENTIFIER_ATTR,null);
        mapping.registerMapping(resource,identifier);
      }
    }
    return mapping;
  }
}
