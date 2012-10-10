package delta.games.lotro.lore.quests.index.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.lore.quests.index.QuestsIndex;

/**
 * Parser for quest indexes stored in XML.
 * @author DAM
 */
public class QuestsIndexXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed quest or <code>null</code>.
   */
  public QuestsIndex parseXML(File source)
  {
    QuestsIndex index=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      index=parseQuest(root);
    }
    return index;
  }

  private QuestsIndex parseQuest(Element root)
  {
    QuestsIndex index=new QuestsIndex();

    // Categories
    List<Element> categoryTags=DOMParsingTools.getChildTagsByName(root,QuestsIndexXMLConstants.CATEGORY_TAG);
    if (categoryTags!=null)
    {
      for(Element categoryTag : categoryTags)
      {
        String name=DOMParsingTools.getStringAttribute(categoryTag.getAttributes(),QuestsIndexXMLConstants.CATEGORY_NAME_ATTR,null);
        if (name!=null)
        {
          // Quests
          List<Element> questTags=DOMParsingTools.getChildTagsByName(categoryTag,QuestsIndexXMLConstants.QUEST_TAG);
          if (questTags!=null)
          {
            for(Element questTag : questTags)
            {
              NamedNodeMap attrs=questTag.getAttributes();
              int questIdentifier=DOMParsingTools.getIntAttribute(attrs,QuestsIndexXMLConstants.QUEST_ID_ATTR,0);
              String questKey=DOMParsingTools.getStringAttribute(attrs,QuestsIndexXMLConstants.QUEST_KEY_ATTR,null);
              String questName=DOMParsingTools.getStringAttribute(attrs,QuestsIndexXMLConstants.QUEST_NAME_ATTR,null);
              index.addQuest(name,questIdentifier,questKey,questName);
            }
          }
        }
      }
    }
    return index;
  }
}
