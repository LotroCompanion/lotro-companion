package delta.games.lotro.lore.quests.io.xml;

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
import delta.games.lotro.common.SIZE;
import delta.games.lotro.common.io.xml.RewardsXMLWriter;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestDescription.FACTION;
import delta.games.lotro.lore.quests.QuestDescription.TYPE;
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

    int id=quest.getIdentifier();
    if (id!=0)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_ID_ATTR,CDATA,String.valueOf(id));
    }
    String key=quest.getKey();
    if (key!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_KEY_ATTR,CDATA,key);
    }
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
    Integer minLevel=quest.getMinimumLevel();
    if (minLevel!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_MIN_LEVEL_ATTR,CDATA,String.valueOf(minLevel));
    }
    Integer maxLevel=quest.getMaximumLevel();
    if (maxLevel!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_MAX_LEVEL_ATTR,CDATA,String.valueOf(maxLevel));
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
    FACTION faction=quest.getFaction();
    if (faction!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_FACTION_ATTR,CDATA,faction.name());
    }
    boolean repeatable=quest.isRepeatable();
    questAttrs.addAttribute("","",QuestXMLConstants.QUEST_REPEATABLE_ATTR,CDATA,String.valueOf(repeatable));
    boolean instanced=quest.isInstanced();
    questAttrs.addAttribute("","",QuestXMLConstants.QUEST_INSTANCED_ATTR,CDATA,String.valueOf(instanced));
    String description=quest.getDescription();
    if (description!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_DESCRIPTION_ATTR,CDATA,description);
    }
    String bestower=quest.getBestower();
    if (bestower!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_BESTOWER_ATTR,CDATA,bestower);
    }
    String bestowerText=quest.getBestowerText();
    if (bestowerText!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_BESTOWER_TEXT_ATTR,CDATA,bestowerText);
    }
    String objectives=quest.getObjectives();
    if (objectives!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_OBJECTIVES_ATTR,CDATA,objectives);
    }

    hd.startElement("","",QuestXMLConstants.QUEST_TAG,questAttrs);

    List<String> requiredClasses=quest.getRequiredClasses();
    if (requiredClasses!=null)
    {
      for(String requiredClass : requiredClasses)
      {
        AttributesImpl attrs=new AttributesImpl();
        attrs.addAttribute("","",QuestXMLConstants.REQUIRED_CLASS_NAME_ATTR,CDATA,requiredClass);
        hd.startElement("","",QuestXMLConstants.REQUIRED_CLASS_TAG,attrs);
        hd.endElement("","",QuestXMLConstants.REQUIRED_CLASS_TAG);
      }
    }
    
    List<String> requiredRaces=quest.getRequiredRaces();
    if (requiredRaces!=null)
    {
      for(String requiredRace : requiredRaces)
      {
        AttributesImpl attrs=new AttributesImpl();
        attrs.addAttribute("","",QuestXMLConstants.REQUIRED_RACE_NAME_ATTR,CDATA,requiredRace);
        hd.startElement("","",QuestXMLConstants.REQUIRED_RACE_TAG,attrs);
        hd.endElement("","",QuestXMLConstants.REQUIRED_RACE_TAG);
      }
    }
    
    writeQuestsList(hd,quest.getPrerequisiteQuests(),QuestXMLConstants.PREREQUISITES_TAG,QuestXMLConstants.PREREQUISITE_TAG,QuestXMLConstants.PREREQUISITE_NAME_ATTR);
    writeQuestsList(hd,quest.getNextQuests(),QuestXMLConstants.NEXT_QUESTS_TAG,QuestXMLConstants.NEXT_QUEST_TAG,QuestXMLConstants.NEXT_QUEST_NAME_ATTR);
    RewardsXMLWriter.write(hd,quest.getQuestRewards());
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
