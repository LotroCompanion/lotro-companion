package delta.games.lotro.lore.deeds.io.xml;

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
import delta.games.lotro.common.io.xml.RewardsXMLWriter;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedDescription.TYPE;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes LOTRO deeds to XML files.
 * @author DAM
 */
public class DeedXMLWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";
  
  /**
   * Write a deed to a XML file.
   * @param outFile Output file.
   * @param deed Deed to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, DeedDescription deed, String encoding)
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
      write(hd,deed);
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
  
  private void write(TransformerHandler hd, DeedDescription deed) throws Exception
  {
    AttributesImpl deedAttrs=new AttributesImpl();

    int id=deed.getIdentifier();
    if (id!=0)
    {
      deedAttrs.addAttribute("","",DeedXMLConstants.DEED_ID_ATTR,CDATA,String.valueOf(id));
    }
    String key=deed.getKey();
    if (key!=null)
    {
      deedAttrs.addAttribute("","",DeedXMLConstants.DEED_KEY_ATTR,CDATA,key);
    }
    String name=deed.getName();
    if (name!=null)
    {
      deedAttrs.addAttribute("","",DeedXMLConstants.DEED_NAME_ATTR,CDATA,name);
    }
    TYPE type=deed.getType();
    if (type!=null)
    {
      deedAttrs.addAttribute("","",DeedXMLConstants.DEED_TYPE_ATTR,CDATA,type.name());
    }
    String className=deed.getClassName();
    if (className!=null)
    {
      deedAttrs.addAttribute("","",DeedXMLConstants.DEED_CLASS_ATTR,CDATA,className);
    }
    Integer minLevel=deed.getMinLevel();
    if (minLevel!=null)
    {
      deedAttrs.addAttribute("","",DeedXMLConstants.DEED_MIN_LEVEL_ATTR,CDATA,String.valueOf(minLevel));
    }
    String description=deed.getDescription();
    if (description!=null)
    {
      deedAttrs.addAttribute("","",DeedXMLConstants.DEED_DESCRIPTION_ATTR,CDATA,description);
    }
    String objectives=deed.getObjectives();
    if (objectives!=null)
    {
      deedAttrs.addAttribute("","",DeedXMLConstants.DEED_OBJECTIVES_ATTR,CDATA,objectives);
    }
    hd.startElement("","",DeedXMLConstants.DEED_TAG,deedAttrs);
    RewardsXMLWriter.write(hd,deed.getRewards());
    hd.endElement("","",DeedXMLConstants.DEED_TAG);
  }
}
