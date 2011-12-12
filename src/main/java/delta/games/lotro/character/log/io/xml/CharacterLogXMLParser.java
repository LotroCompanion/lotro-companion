package delta.games.lotro.character.log.io.xml;

import java.io.File;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;

/**
 * Parser for character logs stored in XML.
 * @author DAM
 */
public class CharacterLogXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed log or <code>null</code>.
   */
  public CharacterLog parseXML(File source)
  {
    CharacterLog log=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      log=parseLog(root);
    }
    return log;
  }

  private CharacterLog parseLog(Element root)
  {
    String name=DOMParsingTools.getStringAttribute(root.getAttributes(),CharacterLogXMLConstants.CHARACTER_LOG_NAME_ATTR,"");
    CharacterLog log=new CharacterLog(name);
    parseLogItems(log,root);
    return log;
  }

  private void parseLogItems(CharacterLog log, Element rootNode)
  {
    NodeList nl=rootNode.getElementsByTagName(CharacterLogXMLConstants.LOG_ITEM_TAG);
    int nbNodes=nl.getLength();
    for(int i=0;i<nbNodes;i++)
    {
      Node n=nl.item(i);
      CharacterLogItem item=parseLogItem((Element)n);
      if (item!=null)
      {
        log.addLogItem(item);
      }
    }
  }

  private CharacterLogItem parseLogItem(Element element)
  {
    NamedNodeMap attrs=element.getAttributes();
    long date=DOMParsingTools.getLongAttribute(attrs,CharacterLogXMLConstants.LOG_ITEM_DATE_ATTR,0);
    String typeStr=DOMParsingTools.getStringAttribute(attrs,CharacterLogXMLConstants.LOG_ITEM_TYPE_ATTR,null);
    LogItemType type=LogItemType.valueOf(typeStr);
    String label=DOMParsingTools.getStringAttribute(attrs,CharacterLogXMLConstants.LOG_ITEM_LABEL_ATTR,null);
    String url=DOMParsingTools.getStringAttribute(attrs,CharacterLogXMLConstants.LOG_ITEM_URL_ATTR,null);
    CharacterLogItem item=new CharacterLogItem(date,type,label,url);
    return item;
  }
}
