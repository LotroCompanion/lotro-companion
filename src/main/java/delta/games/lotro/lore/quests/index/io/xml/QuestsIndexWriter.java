package delta.games.lotro.lore.quests.index.io.xml;

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
import delta.games.lotro.lore.quests.index.QuestCategory;
import delta.games.lotro.lore.quests.index.QuestSummary;
import delta.games.lotro.lore.quests.index.QuestsIndex;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes LOTRO quests indexes to XML files.
 * @author DAM
 */
public class QuestsIndexWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";
  
  /**
   * Write a quest to a XML file.
   * @param outFile Output file.
   * @param index Index to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, QuestsIndex index, String encoding)
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
  
  private void write(TransformerHandler hd, QuestsIndex index) throws Exception
  {
    AttributesImpl indexAttrs=new AttributesImpl();
    hd.startElement("","",QuestsIndexXMLConstants.INDEX_TAG,indexAttrs);

    String[] categories=index.getCategories();
    for(String category : categories)
    {
      AttributesImpl attrs=new AttributesImpl();
      attrs.addAttribute("","",QuestsIndexXMLConstants.CATEGORY_NAME_ATTR,CDATA,category);
      hd.startElement("","",QuestsIndexXMLConstants.CATEGORY_TAG,attrs);
      QuestCategory c=index.getCategory(category);
      QuestSummary[] quests=c.getQuests();
      for(QuestSummary quest : quests)
      {
        AttributesImpl questAttrs=new AttributesImpl();
        int identifier=quest.getIdentifier();
        if (identifier!=0)
        {
          questAttrs.addAttribute("","",QuestsIndexXMLConstants.QUEST_ID_ATTR,CDATA,String.valueOf(identifier));
        }
        questAttrs.addAttribute("","",QuestsIndexXMLConstants.QUEST_KEY_ATTR,CDATA,quest.getKey());
        questAttrs.addAttribute("","",QuestsIndexXMLConstants.QUEST_NAME_ATTR,CDATA,quest.getName());
        hd.startElement("","",QuestsIndexXMLConstants.QUEST_TAG,questAttrs);
        hd.endElement("","",QuestsIndexXMLConstants.QUEST_TAG);
      }
      hd.endElement("","",QuestsIndexXMLConstants.CATEGORY_TAG);
    }
    hd.endElement("","",QuestsIndexXMLConstants.INDEX_TAG);
  }
}
