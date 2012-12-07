package delta.games.lotro.lore.quests.io.xml;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.SIZE;
import delta.games.lotro.common.io.xml.RewardsXMLParser;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestDescription.FACTION;
import delta.games.lotro.lore.quests.QuestDescription.TYPE;

/**
 * Parser for quest descriptions stored in XML.
 * @author DAM
 */
public class QuestXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed quest or <code>null</code>.
   */
  public QuestDescription parseXML(File source)
  {
    QuestDescription c=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      c=parseQuest(root);
    }
    return c;
  }

  /**
   * Parse the XML stream.
   * @param source Source stream.
   * @return Parsed quest or <code>null</code>.
   */
  public QuestDescription parseXML(InputStream source)
  {
    QuestDescription quest=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      quest=parseQuest(root);
    }
    return quest;
  }

  private QuestDescription parseQuest(Element root)
  {
    QuestDescription q=new QuestDescription();

    NamedNodeMap attrs=root.getAttributes();
    // Identifier
    int id=DOMParsingTools.getIntAttribute(attrs,QuestXMLConstants.QUEST_ID_ATTR,0);
    q.setIdentifier(id);
    // Key
    String key=DOMParsingTools.getStringAttribute(attrs,QuestXMLConstants.QUEST_KEY_ATTR,null);
    q.setKey(key);
    // Title
    String title=DOMParsingTools.getStringAttribute(attrs,QuestXMLConstants.QUEST_TITLE_ATTR,null);
    q.setTitle(title);
    // Category
    String category=DOMParsingTools.getStringAttribute(attrs,QuestXMLConstants.QUEST_CATEGORY_ATTR,null);
    q.setCategory(category);
    // Score
    String scope=DOMParsingTools.getStringAttribute(attrs,QuestXMLConstants.QUEST_SCOPE_ATTR,null);
    q.setQuestScope(scope);
    // Quest arc
    String arc=DOMParsingTools.getStringAttribute(attrs,QuestXMLConstants.QUEST_ARC_ATTR,null);
    q.setQuestArc(arc);
    // Minimum level
    int minimumLevel=DOMParsingTools.getIntAttribute(attrs,QuestXMLConstants.QUEST_MIN_LEVEL_ATTR,-1);
    if (minimumLevel!=-1)
    {
      q.setMinimumLevel(Integer.valueOf(minimumLevel));
    }
    // Maximum level
    int maximumLevel=DOMParsingTools.getIntAttribute(attrs,QuestXMLConstants.QUEST_MAX_LEVEL_ATTR,-1);
    if (maximumLevel!=-1)
    {
      q.setMaximumLevel(Integer.valueOf(maximumLevel));
    }
    // Type
    String typeStr=DOMParsingTools.getStringAttribute(attrs,QuestXMLConstants.QUEST_TYPE_ATTR,null);
    TYPE type=TYPE.valueOf(typeStr);
    q.setType(type);
    // Size
    String sizeStr=DOMParsingTools.getStringAttribute(attrs,QuestXMLConstants.QUEST_SIZE_ATTR,null);
    SIZE size=SIZE.valueOf(sizeStr);
    q.setSize(size);
    // Faction
    String factionStr=DOMParsingTools.getStringAttribute(attrs,QuestXMLConstants.QUEST_FACTION_ATTR,null);
    FACTION faction=FACTION.valueOf(factionStr);
    q.setFaction(faction);
    // Repeatable
    boolean repeatable=DOMParsingTools.getBooleanAttribute(attrs,QuestXMLConstants.QUEST_REPEATABLE_ATTR,false);
    q.setRepeatable(repeatable);
    // Instanced
    boolean instanced=DOMParsingTools.getBooleanAttribute(attrs,QuestXMLConstants.QUEST_INSTANCED_ATTR,false);
    q.setInstanced(instanced);
    // Description
    String description=DOMParsingTools.getStringAttribute(attrs,QuestXMLConstants.QUEST_DESCRIPTION_ATTR,null);
    q.setDescription(description);
    // Bestower
    String bestower=DOMParsingTools.getStringAttribute(attrs,QuestXMLConstants.QUEST_BESTOWER_ATTR,null);
    q.setBestower(bestower);
    // Bestower text
    String bestowerText=DOMParsingTools.getStringAttribute(attrs,QuestXMLConstants.QUEST_BESTOWER_TEXT_ATTR,null);
    q.setBestowerText(bestowerText);
    // Objectives
    String objectives=DOMParsingTools.getStringAttribute(attrs,QuestXMLConstants.QUEST_OBJECTIVES_ATTR,null);
    q.setObjectives(objectives);

    // Required classes
    List<Element> requiredClassTags=DOMParsingTools.getChildTagsByName(root,QuestXMLConstants.REQUIRED_CLASS_TAG);
    if (requiredClassTags!=null)
    {
      for(Element requiredClassTag : requiredClassTags)
      {
        String className=DOMParsingTools.getStringAttribute(requiredClassTag.getAttributes(),QuestXMLConstants.REQUIRED_CLASS_NAME_ATTR,null);
        q.addRequiredClass(className);
      }
    }
    
    // Required races
    List<Element> requiredRaceTags=DOMParsingTools.getChildTagsByName(root,QuestXMLConstants.REQUIRED_RACE_TAG);
    if (requiredRaceTags!=null)
    {
      for(Element requiredRaceTag : requiredRaceTags)
      {
        String raceName=DOMParsingTools.getStringAttribute(requiredRaceTag.getAttributes(),QuestXMLConstants.REQUIRED_RACE_NAME_ATTR,null);
        q.addRequiredRace(raceName);
      }
    }
    
    // Prerequisite quests
    List<Element> prerequisiteTags=DOMParsingTools.getChildTagsByName(root,QuestXMLConstants.PREREQUISITE_TAG);
    if (prerequisiteTags!=null)
    {
      for(Element prerequisiteTag : prerequisiteTags)
      {
        String name=DOMParsingTools.getStringAttribute(prerequisiteTag.getAttributes(),QuestXMLConstants.PREREQUISITE_NAME_ATTR,null);
        q.addPrerequisiteQuest(name);
      }
    }
    
    // Next quests
    List<Element> nextQuestTags=DOMParsingTools.getChildTagsByName(root,QuestXMLConstants.NEXT_QUEST_TAG);
    if (nextQuestTags!=null)
    {
      for(Element nextQuestTag : nextQuestTags)
      {
        String name=DOMParsingTools.getStringAttribute(nextQuestTag.getAttributes(),QuestXMLConstants.NEXT_QUEST_NAME_ATTR,null);
        q.addNextQuest(name);
      }
    }

    RewardsXMLParser.loadRewards(root,q.getQuestRewards());
    return q;
  }
}
