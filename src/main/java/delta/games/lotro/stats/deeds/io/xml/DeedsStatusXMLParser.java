package delta.games.lotro.stats.deeds.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.NumericTools;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.stats.deeds.DeedStatus;
import delta.games.lotro.stats.deeds.DeedsStatusManager;
import delta.games.lotro.stats.deeds.geo.DeedGeoPointStatus;
import delta.games.lotro.stats.deeds.geo.DeedGeoStatus;

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
        DeedGeoStatus geoStatus=parseGeoStatus(deedStatusTag);
        deedStatus.setGeoStatus(geoStatus);
      }
    }
    return status;
  }

  private DeedGeoStatus parseGeoStatus(Element root)
  {
    DeedGeoStatus status=null;
    List<Element> geoPointStatusTags=DOMParsingTools.getChildTagsByName(root,DeedStatusXMLConstants.GEO_POINT_STATUS_TAG);
    int nbPoints=geoPointStatusTags.size();
    if (nbPoints>0)
    {
      status=new DeedGeoStatus();
      for(Element geoPointStatusTag : geoPointStatusTags)
      {
        NamedNodeMap attrs=geoPointStatusTag.getAttributes();
        int pointId=DOMParsingTools.getIntAttribute(attrs,DeedStatusXMLConstants.GEO_POINT_STATUS_ID_ATTR,0);
        DeedGeoPointStatus pointStatus=status.getStatus(pointId,true);
        // Completed
        String completedStr=DOMParsingTools.getStringAttribute(attrs,DeedStatusXMLConstants.GEO_POINT_STATUS_COMPLETED_ATTR,null);
        if (completedStr!=null)
        {
          Boolean completed=Boolean.valueOf(completedStr);
          pointStatus.setCompleted(completed);
        }
        // Completion date
        String completionDateStr=DOMParsingTools.getStringAttribute(attrs,DeedStatusXMLConstants.GEO_POINT_STATUS_COMPLETION_DATE_ATTR,null);
        if (completionDateStr!=null)
        {
          Long completionDate=NumericTools.parseLong(completionDateStr);
          pointStatus.setCompletionDate(completionDate);
        }
      }
    }
    return status;
  }
}
