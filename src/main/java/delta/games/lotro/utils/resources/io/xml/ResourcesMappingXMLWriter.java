package delta.games.lotro.utils.resources.io.xml;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.StreamTools;
import delta.games.lotro.utils.LotroLoggers;
import delta.games.lotro.utils.resources.ResourcesMapping;

/**
 * Writes resources mappings to XML files.
 * @author DAM
 */
public class ResourcesMappingXMLWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";
  
  /**
   * Write a mapping to a XML file.
   * @param outFile Output file.
   * @param mapping Mapping to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, ResourcesMapping mapping, String encoding)
  {
    boolean ret;
    FileOutputStream fos=null;
    try
    {
      fos=new FileOutputStream(outFile);
      SAXTransformerFactory tf=(SAXTransformerFactory)TransformerFactory.newInstance();
      TransformerHandler hd=tf.newTransformerHandler();
      Transformer serializer=hd.getTransformer();
      serializer.setOutputProperty(OutputKeys.ENCODING,encoding);
      serializer.setOutputProperty(OutputKeys.INDENT,"yes");

      StreamResult streamResult=new StreamResult(fos);
      hd.setResult(streamResult);
      hd.startDocument();
      write(hd,mapping);
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
  
  private void write(TransformerHandler hd, ResourcesMapping mapping) throws Exception
  {
    AttributesImpl mappingAttrs=new AttributesImpl();
    hd.startElement("","",ResourcesMappingXMLConstants.RESOURCES_TAG,mappingAttrs);
    Integer[] keys=mapping.getKeys();
    for(Integer key : keys)
    {
      AttributesImpl attrs=new AttributesImpl();
      attrs.addAttribute("","",ResourcesMappingXMLConstants.RESOURCE_VALUE_ATTR,CDATA,key.toString());
      String identifier=mapping.getIdentifier(key.intValue());
      attrs.addAttribute("","",ResourcesMappingXMLConstants.RESOURCE_IDENTIFIER_ATTR,CDATA,identifier);
      hd.startElement("","",ResourcesMappingXMLConstants.RESOURCE_TAG,attrs);
      hd.endElement("","",ResourcesMappingXMLConstants.RESOURCE_TAG);
    }
    hd.endElement("","",ResourcesMappingXMLConstants.RESOURCES_TAG);
  }
}
