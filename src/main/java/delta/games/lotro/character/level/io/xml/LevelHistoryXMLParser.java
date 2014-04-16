package delta.games.lotro.character.level.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.character.level.LevelHistory;

/**
 * Parser for character level history stored in XML.
 * @author DAM
 */
public class LevelHistoryXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed data or <code>null</code>.
   */
  public LevelHistory parseXML(File source)
  {
    LevelHistory h=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      h=parseHistory(root);
    }
    return h;
  }

  private LevelHistory parseHistory(Element root)
  {
    // Name
    String name=DOMParsingTools.getStringAttribute(root.getAttributes(),LevelHistoryXMLConstants.LEVEL_HISTORY_NAME_ATTR,"");
    LevelHistory h=new LevelHistory(name);

    List<Element> levelTags=DOMParsingTools.getChildTagsByName(root,LevelHistoryXMLConstants.LEVEL_TAG);
    for(Element levelTag : levelTags)
    {
      NamedNodeMap attrs=levelTag.getAttributes();
      // Level
      int level=DOMParsingTools.getIntAttribute(attrs,LevelHistoryXMLConstants.LEVEL_VALUE_ATTR,0);
      long date=DOMParsingTools.getLongAttribute(attrs,LevelHistoryXMLConstants.LEVEL_DATE_ATTR,0);
      h.setLevel(level,date);
    }
    return h;
  }
}
