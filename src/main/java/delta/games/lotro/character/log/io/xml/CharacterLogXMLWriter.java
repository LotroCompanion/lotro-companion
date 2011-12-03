package delta.games.lotro.character.log.io.xml;

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
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes LOTRO character log to XML documents.
 * @author DAM
 */
public class CharacterLogXMLWriter
{
  private static final Logger _logger=LotroLoggers.getCharacterLogLogger();

  private static final String CDATA="CDATA";
  
  /**
   * Write a character log to a XML file.
   * @param outFile Output file.
   * @param log Character log to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, CharacterLog log, String encoding)
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
      write(hd,log);
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
  
  private void write(TransformerHandler hd, CharacterLog log) throws Exception
  {
    AttributesImpl questAttrs=new AttributesImpl();

    String name=log.getName();
    if (name!=null)
    {
      questAttrs.addAttribute("","",CharacterLogXMLConstants.CHARACTER_LOG_NAME_ATTR,CDATA,name);
    }
    hd.startElement("","",CharacterLogXMLConstants.CHARACTER_LOG_TAG,questAttrs);
    int nb=log.getNbItems();
    for(int i=0;i<nb;i++)
    {
      CharacterLogItem item=log.getLogItem(i);
      writeLogItem(hd,item);
    }
    hd.endElement("","",CharacterLogXMLConstants.CHARACTER_LOG_TAG);
  }

  private void writeLogItem(TransformerHandler hd, CharacterLogItem item) throws Exception
  {
    AttributesImpl attrs=new AttributesImpl();
    long date=item.getDate();
    attrs.addAttribute("","",CharacterLogXMLConstants.LOG_ITEM_DATE_ATTR,CDATA,String.valueOf(date));
    LogItemType type=item.getLogItemType();
    if (type!=null)
    {
      attrs.addAttribute("","",CharacterLogXMLConstants.LOG_ITEM_TYPE_ATTR,CDATA,type.name());
    }
    String label=item.getLabel();
    if (label!=null)
    {
      attrs.addAttribute("","",CharacterLogXMLConstants.LOG_ITEM_LABEL_ATTR,CDATA,label);
    }
    String url=item.getAssociatedUrl();
    if (url!=null)
    {
      attrs.addAttribute("","",CharacterLogXMLConstants.LOG_ITEM_URL_ATTR,CDATA,url);
    }
    hd.startElement("","",CharacterLogXMLConstants.LOG_ITEM_TAG,attrs);
    hd.endElement("","",CharacterLogXMLConstants.LOG_ITEM_TAG);
  }
}
