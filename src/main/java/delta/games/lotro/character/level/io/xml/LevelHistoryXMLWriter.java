package delta.games.lotro.character.level.io.xml;

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
import delta.games.lotro.character.level.LevelHistory;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes level history of LOTRO characters to XML files.
 * @author DAM
 */
public class LevelHistoryXMLWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";
  
  /**
   * Write the level history of a character to a XML file.
   * @param outFile Output file.
   * @param history Data to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, LevelHistory history, String encoding)
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
      write(hd,history);
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
  
  private void write(TransformerHandler hd, LevelHistory history) throws Exception
  {
    AttributesImpl characterAttrs=new AttributesImpl();
    String name=history.getName();
    if (name!=null)
    {
      characterAttrs.addAttribute("","",LevelHistoryXMLConstants.LEVEL_HISTORY_NAME_ATTR,CDATA,name);
    }
    hd.startElement("","",LevelHistoryXMLConstants.LEVEL_HISTORY_TAG,characterAttrs);
    int[] levels=history.getLevels();
    for(int i=0;i<levels.length;i++)
    {
      int level=levels[i];
      Long date=history.getDate(level);
      AttributesImpl levelAttrs=new AttributesImpl();
      levelAttrs.addAttribute("","",LevelHistoryXMLConstants.LEVEL_VALUE_ATTR,CDATA,String.valueOf(level));
      levelAttrs.addAttribute("","",LevelHistoryXMLConstants.LEVEL_DATE_ATTR,CDATA,String.valueOf(date.longValue()));
      hd.startElement("","",LevelHistoryXMLConstants.LEVEL_TAG,levelAttrs);
      hd.endElement("","",LevelHistoryXMLConstants.LEVEL_TAG);
    }
    hd.endElement("","",LevelHistoryXMLConstants.LEVEL_HISTORY_TAG);
  }
}
