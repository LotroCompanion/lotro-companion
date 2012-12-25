package delta.games.lotro.lore.warbands.io.xml;

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
import delta.games.lotro.common.SIZE;
import delta.games.lotro.lore.warbands.WarbandDefinition;
import delta.games.lotro.lore.warbands.WarbandsRegistry;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes a warbands registry to an XML file.
 * @author DAM
 */
public class WarbandsRegistryXMLWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";
  
  /**
   * Write a warbands registry to an XML file.
   * @param outFile Output file.
   * @param registry Registry to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, WarbandsRegistry registry, String encoding)
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
      write(hd,registry);
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
  
  private void write(TransformerHandler hd, WarbandsRegistry registry) throws Exception
  {
    hd.startElement("","",WarbandsRegistryXMLConstants.WARBANDS_TAG,new AttributesImpl());

    WarbandDefinition[] warbands=registry.getAllWarbands();
    for(WarbandDefinition warband : warbands)
    {
      AttributesImpl attrs=new AttributesImpl();
      String name=warband.getName();
      attrs.addAttribute("","",WarbandsRegistryXMLConstants.WARBAND_NAME_ATTR,CDATA,name);
      String shortName=warband.getShortName();
      if (shortName!=null)
      {
        attrs.addAttribute("","",WarbandsRegistryXMLConstants.WARBAND_SHORTNAME_ATTR,CDATA,shortName);
      }
      String iconName=warband.getIconName();
      attrs.addAttribute("","",WarbandsRegistryXMLConstants.WARBAND_ICON_NAME_ATTR,CDATA,iconName);
      Integer level=warband.getLevel();
      if (level!=null) attrs.addAttribute("","",WarbandsRegistryXMLConstants.WARBAND_LEVEL_ATTR,CDATA,String.valueOf(level));
      Integer morale=warband.getMorale();
      if (morale!=null) attrs.addAttribute("","",WarbandsRegistryXMLConstants.WARBAND_MORALE_ATTR,CDATA,String.valueOf(morale));
      String region=warband.getRegion();
      if (region!=null) attrs.addAttribute("","",WarbandsRegistryXMLConstants.WARBAND_REGION_ATTR,CDATA,region);
      String description=warband.getDescription();
      if (description!=null) attrs.addAttribute("","",WarbandsRegistryXMLConstants.WARBAND_DESCRIPTION_ATTR,CDATA,description);
      SIZE size=warband.getSize();
      if (size!=null) attrs.addAttribute("","",WarbandsRegistryXMLConstants.WARBAND_SIZE_ATTR,CDATA,size.name());
      hd.startElement("","",WarbandsRegistryXMLConstants.WARBAND_TAG,attrs);
      hd.endElement("","",WarbandsRegistryXMLConstants.WARBAND_TAG);
    }
    hd.endElement("","",WarbandsRegistryXMLConstants.WARBANDS_TAG);
  }
}
