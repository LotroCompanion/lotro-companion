package delta.games.lotro.stats.traitPoints.io.xml;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.xml.XmlFileWriterHelper;
import delta.common.utils.io.xml.XmlWriter;
import delta.games.lotro.stats.traitPoints.TraitPointsStatus;

/**
 * Writes a trait points status to an XML file.
 * @author DAM
 */
public class TraitPointsStatusXMLWriter
{
  /**
   * Write a trait points status to an XML file.
   * @param outFile Output file.
   * @param status Data to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, final TraitPointsStatus status, String encoding)
  {
    XmlWriter writer=new XmlWriter()
    {
      @Override
      public void writeXml(TransformerHandler hd) throws Exception
      {
        write(hd,status);
      }
    };
    XmlFileWriterHelper helper=new XmlFileWriterHelper();
    boolean ret=helper.write(outFile,encoding,writer);
    return ret;
  }

  /**
   * Write a trait points status to the given XML stream.
   * @param hd XML output stream.
   * @param status Status to write.
   * @throws Exception If an error occurs.
   */
  public void write(TransformerHandler hd, TraitPointsStatus status) throws Exception
  {
    AttributesImpl attrs=new AttributesImpl();
    hd.startElement("","",TraitPointsStatusXMLConstants.TRAIT_POINTS_TAG,attrs);

    List<String> ids=status.getAcquiredTraitPointsIds();
    Collections.sort(ids);

    for(String id : ids)
    {
      AttributesImpl pointAttrs=new AttributesImpl();
      pointAttrs.addAttribute("","",TraitPointsStatusXMLConstants.TRAIT_POINT_ID_ATTR,XmlWriter.CDATA,id);
      hd.startElement("","",TraitPointsStatusXMLConstants.TRAIT_POINT_TAG,pointAttrs);
      hd.endElement("","",TraitPointsStatusXMLConstants.TRAIT_POINT_TAG);
    }
    hd.endElement("","",TraitPointsStatusXMLConstants.TRAIT_POINTS_TAG);
  }
}
