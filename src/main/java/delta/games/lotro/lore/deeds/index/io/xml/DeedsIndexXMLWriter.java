package delta.games.lotro.lore.deeds.index.io.xml;

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
import delta.games.lotro.lore.deeds.index.DeedCategory;
import delta.games.lotro.lore.deeds.index.DeedSummary;
import delta.games.lotro.lore.deeds.index.DeedsIndex;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes LOTRO deeds indexes to XML files.
 * @author DAM
 */
public class DeedsIndexXMLWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";
  
  /**
   * Write a deed to a XML file.
   * @param outFile Output file.
   * @param index Index to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, DeedsIndex index, String encoding)
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
      write(hd,index);
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
  
  private void write(TransformerHandler hd, DeedsIndex index) throws Exception
  {
    AttributesImpl indexAttrs=new AttributesImpl();
    hd.startElement("","",DeedsIndexXMLConstants.INDEX_TAG,indexAttrs);

    String[] categories=index.getCategories();
    for(String category : categories)
    {
      AttributesImpl attrs=new AttributesImpl();
      attrs.addAttribute("","",DeedsIndexXMLConstants.CATEGORY_NAME_ATTR,CDATA,category);
      hd.startElement("","",DeedsIndexXMLConstants.CATEGORY_TAG,attrs);
      DeedCategory c=index.getCategory(category);
      DeedSummary[] deeds=c.getDeeds();
      for(DeedSummary deed : deeds)
      {
        AttributesImpl deedAttrs=new AttributesImpl();
        int identifier=deed.getIdentifier();
        if (identifier!=0)
        {
          deedAttrs.addAttribute("","",DeedsIndexXMLConstants.DEED_ID_ATTR,CDATA,String.valueOf(identifier));
        }
        deedAttrs.addAttribute("","",DeedsIndexXMLConstants.DEED_KEY_ATTR,CDATA,deed.getKey());
        deedAttrs.addAttribute("","",DeedsIndexXMLConstants.DEED_NAME_ATTR,CDATA,deed.getName());
        hd.startElement("","",DeedsIndexXMLConstants.DEED_TAG,deedAttrs);
        hd.endElement("","",DeedsIndexXMLConstants.DEED_TAG);
      }
      hd.endElement("","",DeedsIndexXMLConstants.CATEGORY_TAG);
    }
    hd.endElement("","",DeedsIndexXMLConstants.INDEX_TAG);
  }
}
