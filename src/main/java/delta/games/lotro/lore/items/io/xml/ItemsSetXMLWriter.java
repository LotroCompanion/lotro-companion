package delta.games.lotro.lore.items.io.xml;

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
import delta.games.lotro.lore.items.ItemsSet;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes LOTRO items sets to XML files.
 * @author DAM
 */
public class ItemsSetXMLWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";
  
  /**
   * Write an items set to a XML file.
   * @param outFile Output file.
   * @param set Items set to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, ItemsSet set, String encoding)
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
      write(hd,set);
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
  
  private void write(TransformerHandler hd, ItemsSet set) throws Exception
  {
    AttributesImpl itemAttrs=new AttributesImpl();

    // Key
    String key=set.getKey();
    if (key!=null)
    {
      itemAttrs.addAttribute("","",ItemsSetXMLConstants.ITEMS_SET_KEY_ATTR,CDATA,key);
    }
    // Name
    String name=set.getName();
    if (name!=null)
    {
      itemAttrs.addAttribute("","",ItemsSetXMLConstants.ITEMS_SET_NAME_ATTR,CDATA,name);
    }
    hd.startElement("","",ItemsSetXMLConstants.ITEMS_SET_TAG,itemAttrs);

    // Items
    int[] ids=set.getItemIds();
    String[] keys=set.getItemKeys();
    if ((ids!=null) && (keys!=null))
    {
      int nb=Math.min(ids.length,keys.length);
      for(int i=0;i<nb;i++)
      {
        AttributesImpl attrs=new AttributesImpl();
        attrs.addAttribute("","",ItemsSetXMLConstants.ITEM_ID_ATTR,CDATA,String.valueOf(ids[i]));
        attrs.addAttribute("","",ItemsSetXMLConstants.ITEM_KEY_ATTR,CDATA,keys[i]);
        hd.startElement("","",ItemsSetXMLConstants.ITEM_TAG,attrs);
        hd.endElement("","",ItemsSetXMLConstants.ITEM_TAG);
      }
    }
    // Bonuses
    int[] nbs=set.getNumberOfItemsForBonuses();
    if (nbs!=null)
    {
      for(int nb : nbs)
      {
        String[] bonuses=set.getBonus(nb);
        for(String bonus : bonuses)
        {
          AttributesImpl attrs=new AttributesImpl();
          attrs.addAttribute("","",ItemsSetXMLConstants.BONUS_NB_ITEMS_ATTR,CDATA,String.valueOf(nb));
          attrs.addAttribute("","",ItemsSetXMLConstants.BONUS_VALUE_ATTR,CDATA,bonus);
          hd.startElement("","",ItemsSetXMLConstants.BONUS_TAG,attrs);
          hd.endElement("","",ItemsSetXMLConstants.BONUS_TAG);
        }
      }
    }
    hd.endElement("","",ItemsSetXMLConstants.ITEMS_SET_TAG);
  }
}
