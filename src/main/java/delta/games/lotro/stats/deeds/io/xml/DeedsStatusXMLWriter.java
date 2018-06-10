package delta.games.lotro.stats.deeds.io.xml;

import java.io.File;
import java.util.List;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.xml.XmlFileWriterHelper;
import delta.common.utils.io.xml.XmlWriter;
import delta.games.lotro.stats.deeds.DeedStatus;
import delta.games.lotro.stats.deeds.DeedsStatusManager;
import delta.games.lotro.stats.deeds.geo.DeedGeoPointStatus;
import delta.games.lotro.stats.deeds.geo.DeedGeoStatus;

/**
 * Writes a deeds status to an XML file.
 * @author DAM
 */
public class DeedsStatusXMLWriter
{
  private static final String CDATA="CDATA";

  /**
   * Write a deeds status to an XML file.
   * @param outFile Output file.
   * @param status Status to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, final DeedsStatusManager status, String encoding)
  {
    XmlFileWriterHelper helper=new XmlFileWriterHelper();
    XmlWriter writer=new XmlWriter()
    {
      @Override
      public void writeXml(TransformerHandler hd) throws Exception
      {
        writeDeedsStatus(hd,status);
      }
    };
    boolean ret=helper.write(outFile,encoding,writer);
    return ret;
  }

  /**
   * Write a deed status to the given XML stream.
   * @param hd XML output stream.
   * @param status Status to write.
   * @throws Exception If an error occurs.
   */
  private void writeDeedsStatus(TransformerHandler hd, DeedsStatusManager status) throws Exception
  {
    AttributesImpl attrs=new AttributesImpl();
    hd.startElement("","",DeedStatusXMLConstants.DEEDS_STATUS_TAG,attrs);

    List<DeedStatus> deedStatuses=status.getAll();

    for(DeedStatus deedStatus : deedStatuses)
    {
      AttributesImpl deedAttrs=new AttributesImpl();
      // Key
      String key=deedStatus.getDeedKey();
      deedAttrs.addAttribute("","",DeedStatusXMLConstants.DEED_STATUS_KEY_ATTR,CDATA,key);
      // Completed
      Boolean completed=deedStatus.isCompleted();
      if (completed!=null)
      {
        deedAttrs.addAttribute("","",DeedStatusXMLConstants.DEED_STATUS_COMPLETED_ATTR,CDATA,completed.toString());
      }
      // Completion date
      Long completionDate=deedStatus.getCompletionDate();
      if (completionDate!=null)
      {
        deedAttrs.addAttribute("","",DeedStatusXMLConstants.DEED_STATUS_COMPLETION_DATE_ATTR,CDATA,completionDate.toString());
      }
      hd.startElement("","",DeedStatusXMLConstants.DEED_STATUS_TAG,deedAttrs);
      writeGeoStatus(hd,deedStatus);
      hd.endElement("","",DeedStatusXMLConstants.DEED_STATUS_TAG);
    }
    hd.endElement("","",DeedStatusXMLConstants.DEEDS_STATUS_TAG);
  }

  /**
   * Write a greographic deed status to the given XML stream.
   * @param hd XML output stream.
   * @param status Status to write.
   * @throws Exception If an error occurs.
   */
  private void writeGeoStatus(TransformerHandler hd, DeedStatus status) throws Exception
  {
    DeedGeoStatus geoStatus=status.getGeoStatus();
    if (geoStatus!=null)
    {
      List<DeedGeoPointStatus> pointStatuses=geoStatus.getPointStatuses();
      for(DeedGeoPointStatus pointStatus : pointStatuses)
      {
        AttributesImpl deedAttrs=new AttributesImpl();
        // PointID
        int pointId=pointStatus.getPointId();
        deedAttrs.addAttribute("","",DeedStatusXMLConstants.GEO_POINT_STATUS_ID_ATTR,CDATA,String.valueOf(pointId));
        // Completed
        Boolean completed=pointStatus.isCompleted();
        if (completed!=null)
        {
          deedAttrs.addAttribute("","",DeedStatusXMLConstants.GEO_POINT_STATUS_COMPLETED_ATTR,CDATA,completed.toString());
        }
        // Completion date
        Long completionDate=pointStatus.getCompletionDate();
        if (completionDate!=null)
        {
          deedAttrs.addAttribute("","",DeedStatusXMLConstants.GEO_POINT_STATUS_COMPLETION_DATE_ATTR,CDATA,completionDate.toString());
        }
        hd.startElement("","",DeedStatusXMLConstants.GEO_POINT_STATUS_TAG,deedAttrs);
        hd.endElement("","",DeedStatusXMLConstants.GEO_POINT_STATUS_TAG);
      }
    }
  }
}
