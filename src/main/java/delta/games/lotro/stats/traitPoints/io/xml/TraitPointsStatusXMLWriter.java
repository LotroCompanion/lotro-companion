package delta.games.lotro.stats.traitPoints.io.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.StreamTools;
import delta.games.lotro.stats.traitPoints.TraitPointsStatus;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes a trait points status to an XML file.
 * @author DAM
 */
public class TraitPointsStatusXMLWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";

  /**
   * Write a registry to an XML file.
   * @param outFile Output file.
   * @param status Registry to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, TraitPointsStatus status, String encoding)
  {
    boolean ret;
    FileOutputStream fos=null;
    try
    {
      File parentFile=outFile.getParentFile();
      if (!parentFile.exists())
      {
        parentFile.mkdirs();
      }
      fos=new FileOutputStream(outFile);
      SAXTransformerFactory tf=(SAXTransformerFactory)TransformerFactory.newInstance();
      TransformerHandler hd=tf.newTransformerHandler();
      Transformer serializer=hd.getTransformer();
      serializer.setOutputProperty(OutputKeys.ENCODING,encoding);
      serializer.setOutputProperty(OutputKeys.INDENT,"yes");

      StreamResult streamResult=new StreamResult(fos);
      hd.setResult(streamResult);
      hd.startDocument();
      write(hd,status);
      hd.endDocument();
      ret=true;
    }
    catch (Exception exception)
    {
      _logger.error("",exception);
      ret=false;
    }
    finally
    {
      StreamTools.close(fos);
    }
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
      pointAttrs.addAttribute("","",TraitPointsStatusXMLConstants.TRAIT_POINT_ID_ATTR,CDATA,id);
      hd.startElement("","",TraitPointsStatusXMLConstants.TRAIT_POINT_TAG,pointAttrs);
      hd.endElement("","",TraitPointsStatusXMLConstants.TRAIT_POINT_TAG);
    }
    hd.endElement("","",TraitPointsStatusXMLConstants.TRAIT_POINTS_TAG);
  }
}
