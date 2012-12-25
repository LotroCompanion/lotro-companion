package delta.games.lotro.lore.warbands.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.SIZE;
import delta.games.lotro.lore.warbands.WarbandDefinition;
import delta.games.lotro.lore.warbands.WarbandsRegistry;

/**
 * Parser for the warbands registry stored in XML.
 * @author DAM
 */
public class WarbandsRegistryXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed warbands or <code>null</code>.
   */
  public WarbandsRegistry parseXML(File source)
  {
    WarbandsRegistry registry=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      registry=parseWarbandsRegistry(root);
    }
    return registry;
  }

  private WarbandsRegistry parseWarbandsRegistry(Element root)
  {
    WarbandsRegistry registry=new WarbandsRegistry();

    // Warbands
    List<Element> warbandTags=DOMParsingTools.getChildTagsByName(root,WarbandsRegistryXMLConstants.WARBAND_TAG);
    if (warbandTags!=null)
    {
      for(Element warbandTag : warbandTags)
      {
        NamedNodeMap attrs=warbandTag.getAttributes();
        String name=DOMParsingTools.getStringAttribute(attrs,WarbandsRegistryXMLConstants.WARBAND_NAME_ATTR,"???");
        String shortName=DOMParsingTools.getStringAttribute(attrs,WarbandsRegistryXMLConstants.WARBAND_SHORTNAME_ATTR,null);
        String iconName=DOMParsingTools.getStringAttribute(attrs,WarbandsRegistryXMLConstants.WARBAND_ICON_NAME_ATTR,name);
        int level=DOMParsingTools.getIntAttribute(attrs,WarbandsRegistryXMLConstants.WARBAND_LEVEL_ATTR,0);
        int morale=DOMParsingTools.getIntAttribute(attrs,WarbandsRegistryXMLConstants.WARBAND_MORALE_ATTR,0);
        String region=DOMParsingTools.getStringAttribute(attrs,WarbandsRegistryXMLConstants.WARBAND_REGION_ATTR,null);
        String description=DOMParsingTools.getStringAttribute(attrs,WarbandsRegistryXMLConstants.WARBAND_DESCRIPTION_ATTR,null);
        SIZE size=null;
        String sizeStr=DOMParsingTools.getStringAttribute(attrs,WarbandsRegistryXMLConstants.WARBAND_SIZE_ATTR,null);
        if (sizeStr!=null)
        {
          size=SIZE.valueOf(sizeStr);
        }
        WarbandDefinition warband=new WarbandDefinition();
        warband.setName(name);
        warband.setShortName(shortName);
        warband.setIconName(iconName);
        if (level!=0) warband.setLevel(Integer.valueOf(level));
        if (morale!=0) warband.setMorale(Integer.valueOf(morale));
        warband.setRegion(region);
        warband.setDescription(description);
        warband.setSize(size);
        registry.addWarband(warband);
      }
    }
    return registry;
  }
}
