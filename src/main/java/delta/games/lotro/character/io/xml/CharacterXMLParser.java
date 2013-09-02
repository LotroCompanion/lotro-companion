package delta.games.lotro.character.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.CharacterEquipment.SlotContents;
import delta.games.lotro.character.CharacterStat;
import delta.games.lotro.character.CharacterStat.STAT;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;

/**
 * Parser for character infos stored in XML.
 * @author DAM
 */
public class CharacterXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed character or <code>null</code>.
   */
  public Character parseXML(File source)
  {
    Character c=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      c=parseCharacter(root);
    }
    return c;
  }

  private Character parseCharacter(Element root)
  {
    Character c=new Character();
    // Name
    String name=DOMParsingTools.getStringAttribute(root.getAttributes(),CharacterXMLConstants.CHARACTER_NAME_ATTR,"");
    c.setName(name);
    // Server
    String server=DOMParsingTools.getStringAttribute(root.getAttributes(),CharacterXMLConstants.CHARACTER_SERVER_ATTR,"");
    c.setServer(server);
    // Class
    String characterClass=DOMParsingTools.getStringAttribute(root.getAttributes(),CharacterXMLConstants.CHARACTER_CLASS_ATTR,"");
    CharacterClass cClass=CharacterClass.getByKey(characterClass);
    c.setCharacterClass(cClass);
    // Race
    String race=DOMParsingTools.getStringAttribute(root.getAttributes(),CharacterXMLConstants.CHARACTER_RACE_ATTR,"");
    Race cRace=Race.getByLabel(race); 
    c.setRace(cRace);
    // Region
    String region=DOMParsingTools.getStringAttribute(root.getAttributes(),CharacterXMLConstants.CHARACTER_REGION_ATTR,"");
    c.setRegion(region);
    // Level
    int level=DOMParsingTools.getIntAttribute(root.getAttributes(),CharacterXMLConstants.CHARACTER_LEVEL_ATTR,0);
    c.setLevel(level);

    Element statsTag=DOMParsingTools.getChildTagByName(root,CharacterXMLConstants.STATS_TAG);
    parseStats(c,statsTag);
    Element equipmentTag=DOMParsingTools.getChildTagByName(root,CharacterXMLConstants.EQUIPMENT_TAG);
    parseEquipment(c,equipmentTag);
    
    return c;
  }

  private void parseStats(Character c, Element statsTag)
  {
    if (statsTag!=null)
    {
      List<Element> statTags=DOMParsingTools.getChildTagsByName(statsTag,CharacterXMLConstants.STAT_TAG);
      for(Element statTag : statTags)
      {
        NamedNodeMap attrs=statTag.getAttributes();
        // Stat name
        String name=DOMParsingTools.getStringAttribute(attrs,CharacterXMLConstants.STAT_NAME_ATTR,"");
        STAT stat=STAT.valueOf(name);
        if (stat!=null)
        {
          Node n=attrs.getNamedItem(CharacterXMLConstants.STAT_VALUE_ATTR);
          Integer value=null;
          if (n!=null)
          {
            int v=DOMParsingTools.getIntAttribute(attrs,CharacterXMLConstants.STAT_VALUE_ATTR,0);
            value=Integer.valueOf(v);
          }
          CharacterStat cStat=c.getStat(stat,true);
          cStat.setValue(value);
        }
      }
    }
  }

  private void parseEquipment(Character c, Element equipmentTag)
  {
    if (equipmentTag!=null)
    {
      CharacterEquipment equipment=c.getEquipment();
      List<Element> slotTags=DOMParsingTools.getChildTagsByName(equipmentTag,CharacterXMLConstants.SLOT_TAG);
      for(Element slotTag : slotTags)
      {
        NamedNodeMap attrs=slotTag.getAttributes();
        // Slot name
        String name=DOMParsingTools.getStringAttribute(attrs,CharacterXMLConstants.SLOT_NAME_ATTR,"");
        EQUIMENT_SLOT slot=EQUIMENT_SLOT.valueOf(name);
        if (slot!=null)
        {
          String objectURL=DOMParsingTools.getStringAttribute(attrs,CharacterXMLConstants.SLOT_OBJECT_URL_ATTR,"");
          String iconURL=DOMParsingTools.getStringAttribute(attrs,CharacterXMLConstants.SLOT_ICON_URL_ATTR,"");
          SlotContents slotContents=equipment.getSlotContents(slot,true);
          slotContents.setObjectURL(objectURL);
          slotContents.setIconURL(iconURL);
        }
      }
    }
  }
}
