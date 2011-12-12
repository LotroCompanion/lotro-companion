package delta.games.lotro.quests.io.xml;

import java.io.File;
import java.io.FileOutputStream;
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
import delta.games.lotro.quests.QuestDescription;
import delta.games.lotro.quests.QuestDescription.SIZE;
import delta.games.lotro.quests.QuestDescription.TYPE;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes LOTRO quests to XML files.
 * @author DAM
 */
public class QuestXMLWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";
  
  /**
   * Write a quest to a XML file.
   * @param outFile Output file.
   * @param quest Quest to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, QuestDescription quest, String encoding)
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
      write(hd,quest);
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
  
  private void write(TransformerHandler hd, QuestDescription quest) throws Exception
  {
    AttributesImpl questAttrs=new AttributesImpl();

    String title=quest.getTitle();
    if (title!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_TITLE_ATTR,CDATA,title);
    }
    String category=quest.getCategory();
    if (category!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_CATEGORY_ATTR,CDATA,category);
    }
    String scope=quest.getQuestScope();
    if (scope!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_SCOPE_ATTR,CDATA,scope);
    }
    String arc=quest.getQuestArc();
    if (arc!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_ARC_ATTR,CDATA,arc);
    }
    Integer level=quest.getMinimumLevel();
    if (level!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_LEVEL_ATTR,CDATA,String.valueOf(level));
    }
    TYPE type=quest.getType();
    if (type!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_TYPE_ATTR,CDATA,type.name());
    }
    SIZE size=quest.getSize();
    if (size!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_SIZE_ATTR,CDATA,size.name());
    }
    boolean repeatable=quest.isRepeatable();
    questAttrs.addAttribute("","",QuestXMLConstants.QUEST_REPEATABLE_ATTR,CDATA,String.valueOf(repeatable));
    
    String description=quest.getDescription();
    if (description!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_DESCRIPTION_ATTR,CDATA,description);
    }

    hd.startElement("","",QuestXMLConstants.QUEST_TAG,questAttrs);
    writeQuestsList(hd,quest.getPrerequisiteQuests(),QuestXMLConstants.PREREQUISITES_TAG,QuestXMLConstants.PREREQUISITE_TAG,QuestXMLConstants.PREREQUISITE_NAME_ATTR);
    writeQuestsList(hd,quest.getNextQuests(),QuestXMLConstants.NEXT_QUESTS_TAG,QuestXMLConstants.NEXT_QUEST_TAG,QuestXMLConstants.NEXT_QUEST_NAME_ATTR);
    QuestRewardsXMLWriter.write(hd,quest.getQuestRewards());
    hd.endElement("","",QuestXMLConstants.QUEST_TAG);
  }

  private void writeQuestsList(TransformerHandler hd, List<String> questNames, String mainTag, String tag, String attrName) throws Exception
  {
    if ((questNames!=null) && (questNames.size()>0))
    {
      AttributesImpl attrs=new AttributesImpl();
      hd.startElement("","",mainTag,attrs);
      for(String questName : questNames)
      {
        AttributesImpl questAttrs=new AttributesImpl();
        questAttrs.addAttribute("","",attrName,CDATA,questName);
        hd.startElement("","",tag,questAttrs);
        hd.endElement("","",tag);
      }
      hd.endElement("","",mainTag);
    }
  }
}
