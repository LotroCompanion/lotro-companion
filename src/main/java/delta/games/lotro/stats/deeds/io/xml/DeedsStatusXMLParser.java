package delta.games.lotro.stats.deeds.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.NumericTools;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.stats.deeds.DeedStatus;
import delta.games.lotro.stats.deeds.DeedsStatusManager;

/**
 * Parser for the deeds status stored in XML.
 * @author DAM
 */
public class DeedsStatusXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed status or <code>null</code>.
   */
  public DeedsStatusManager parseXML(File source)
  {
    DeedsStatusManager status=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      status=parseStatus(root);
    }
    return status;
  }

  private DeedsStatusManager parseStatus(Element root)
  {
    DeedsStatusManager status=new DeedsStatusManager();
    List<Element> deedStatusTags=DOMParsingTools.getChildTagsByName(root,DeedStatusXMLConstants.DEED_STATUS_TAG,false);
    for(Element deedStatusTag : deedStatusTags)
    {
      NamedNodeMap attrs=deedStatusTag.getAttributes();
      String key=DOMParsingTools.getStringAttribute(attrs,DeedStatusXMLConstants.DEED_STATUS_KEY_ATTR,null);
      if (key!=null)
      {
        // Create deed status
        DeedStatus deedStatus=status.get(key,true);
        // Completed
        String completedStr=DOMParsingTools.getStringAttribute(attrs,DeedStatusXMLConstants.DEED_STATUS_COMPLETED_ATTR,null);
        if (completedStr!=null)
        {
          Boolean completed=Boolean.valueOf(completedStr);
          deedStatus.setCompleted(completed);
        }
        // Completion date
        String completionDateStr=DOMParsingTools.getStringAttribute(attrs,DeedStatusXMLConstants.DEED_STATUS_COMPLETION_DATE_ATTR,null);
        if (completionDateStr!=null)
        {
          Long completionDate=NumericTools.parseLong(completionDateStr);
          deedStatus.setCompletionDate(completionDate);
        }
      }
    }
    return status;
  }
}
