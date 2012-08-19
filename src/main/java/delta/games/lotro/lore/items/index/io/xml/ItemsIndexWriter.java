package delta.games.lotro.lore.items.index.io.xml;

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
import delta.games.lotro.lore.items.index.ItemCategory;
import delta.games.lotro.lore.items.index.ItemSummary;
import delta.games.lotro.lore.items.index.ItemsIndex;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes LOTRO items indexes to XML files.
 * @author DAM
 */
public class ItemsIndexWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";
  
  /**
   * Write a item to a XML file.
   * @param outFile Output file.
   * @param index Index to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, ItemsIndex index, String encoding)
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
  
  private void write(TransformerHandler hd, ItemsIndex index) throws Exception
  {
    AttributesImpl indexAttrs=new AttributesImpl();
    hd.startElement("","",ItemsIndexXMLConstants.INDEX_TAG,indexAttrs);

    String[] categories=index.getCategories();
    for(String category : categories)
    {
      AttributesImpl attrs=new AttributesImpl();
      attrs.addAttribute("","",ItemsIndexXMLConstants.CATEGORY_NAME_ATTR,CDATA,category);
      hd.startElement("","",ItemsIndexXMLConstants.CATEGORY_TAG,attrs);
      ItemCategory c=index.getCategory(category);
      ItemSummary[] items=c.getItems();
      for(ItemSummary item : items)
      {
        AttributesImpl itemAttrs=new AttributesImpl();
        itemAttrs.addAttribute("","",ItemsIndexXMLConstants.ITEM_ID_ATTR,CDATA,item.getId());
        itemAttrs.addAttribute("","",ItemsIndexXMLConstants.ITEM_NAME_ATTR,CDATA,item.getName());
        hd.startElement("","",ItemsIndexXMLConstants.ITEM_TAG,itemAttrs);
        hd.endElement("","",ItemsIndexXMLConstants.ITEM_TAG);
      }
      hd.endElement("","",ItemsIndexXMLConstants.CATEGORY_TAG);
    }
    hd.endElement("","",ItemsIndexXMLConstants.INDEX_TAG);
  }
}
