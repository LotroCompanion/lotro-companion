package delta.games.lotro.character.io.web;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.NumericTools;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.CharacterEquipment.SlotContents;
import delta.games.lotro.character.CharacterStat.STAT;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.utils.DownloadService;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for data.lotro character page.
 * @author DAM
 */
public class DataLotroCharacterPageParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  private Character parseCharacter(byte[] xmlBuffer)
  {
    Character ret=null;
    if (xmlBuffer!=null)
    {
      ByteArrayInputStream bis=new ByteArrayInputStream(xmlBuffer);
      Element root=DOMParsingTools.parse(bis);
      if (root!=null)
      {
        Element characterTag=DOMParsingTools.getChildTagByName(root,"character");
        if (characterTag!=null)
        {
          ret=new Character();
          // Character name
          NamedNodeMap attrs=characterTag.getAttributes();
          String charName=DOMParsingTools.getStringAttribute(attrs,"name",null);
          ret.setName(charName);
          // World/server
          String server=DOMParsingTools.getStringAttribute(attrs,"world",null);
          ret.setServer(server);

          // Class
          String charClassName=DOMParsingTools.getStringAttribute(attrs,"class",null);
          CharacterClass cClass=CharacterClass.getByName(charClassName);
          ret.setCharacterClass(cClass);
          // Race
          String charRace=DOMParsingTools.getStringAttribute(attrs,"race",null);
          Race race=Race.getByLabel(charRace);
          ret.setRace(race);
          // Nation/origin
          String charNation=DOMParsingTools.getStringAttribute(attrs,"origin",null);
          if (charNation!=null)
          {
            charNation=charNation.replace('_',' ');
          }
          ret.setRegion(charNation);
          // Level
          String charLevelStr=DOMParsingTools.getStringAttribute(attrs,"level",null);
          int charLevel=NumericTools.parseInt(charLevelStr,0);
          ret.setLevel(charLevel);
          //System.out.println("Class ["+charClassName+"], Race ["+charRace+"], Nation ["+charNation+"], Level="+charLevel);

          Element statsTag=DOMParsingTools.getChildTagByName(characterTag,"stats");
          if (statsTag!=null)
          {
            List<Element> statTags=DOMParsingTools.getChildTagsByName(statsTag,"stat");
            for(Element statTag : statTags)
            {
              NamedNodeMap statAttrs=statTag.getAttributes();
              String statName=DOMParsingTools.getStringAttribute(statAttrs,"name",null);
              String statValue=DOMParsingTools.getStringAttribute(statAttrs,"value",null);
              Integer value=null;
              if ((!"N/A".equals(statValue)) && (!("??".equals(statValue))))
              {
                value=NumericTools.parseInteger(statValue);
              }
              STAT stat=getStatByName(statName);
              if (stat!=null)
              {
                ret.getStat(stat,true).setValue(value);
              }
            }
          }
          
          // Equipment
          Element equipmentTag=DOMParsingTools.getChildTagByName(characterTag,"equipment");
          if (equipmentTag!=null)
          {
            CharacterEquipment equipment=ret.getEquipment();
            List<Element> itemTags=DOMParsingTools.getChildTagsByName(equipmentTag,"item");
            for(Element itemTag : itemTags)
            {
              NamedNodeMap itemAttrs=itemTag.getAttributes();
              // Identifier
              int id=DOMParsingTools.getIntAttribute(itemAttrs,"item_id",0);
              String objectPageURL=DOMParsingTools.getStringAttribute(itemAttrs,"lorebookEntry",null);
              String slotName=DOMParsingTools.getStringAttribute(itemAttrs,"slot",null);
              EQUIMENT_SLOT slot=getSlotByName(slotName);
              if (slot!=null)
              {
                SlotContents contents=equipment.getSlotContents(slot,true);
                String iconURL="http://lorebook.lotro.com/icon.php?id="+id+"&type=item";
                contents.setIconURL(iconURL);
                contents.setObjectURL(objectPageURL);
              }
            }
          }
        }
      }
    }
    return ret;
  }
  
  private STAT getStatByName(String name)
  {
    if ("morale".equals(name)) return STAT.MORALE;
    else if ("power".equals(name)) return STAT.POWER;
    else if ("armour".equals(name)) return STAT.ARMOUR;
    else if ("might".equals(name)) return STAT.MIGHT;
    else if ("agility".equals(name)) return STAT.AGILITY;
    else if ("vitality".equals(name)) return STAT.VITALITY;
    else if ("will".equals(name)) return STAT.WILL;
    else if ("fate".equals(name)) return STAT.FATE;
    else if ("criticalPoints".equals(name)) return STAT.CRITICAL_HIT;
    else if ("finessePoints".equals(name)) return STAT.FINESSE;
    else if ("block".equals(name)) return STAT.BLOCK;
    else if ("parry".equals(name)) return STAT.PARRY;
    else if ("evade".equals(name)) return STAT.EVADE;
    else if ("theOneResistance".equals(name)) return STAT.RESISTANCE;
    else if ("criticalDefense".equals(name)) return STAT.CRITICAL_AVOID;
    else if ("physicalMitigation".equals(name)) return STAT.PHYSICAL_MITIGATION;
    else if ("tacticalMitigation".equals(name)) return STAT.TACTICAL_MITIGATION;
    else return null;
  }

  private EQUIMENT_SLOT getSlotByName(String slot)
  {
    if ("Head".equals(slot)) return EQUIMENT_SLOT.HEAD;
    else if ("Chest".equals(slot)) return EQUIMENT_SLOT.BREAST;
    else if ("Legs".equals(slot)) return EQUIMENT_SLOT.LEGS;
    else if ("Gloves".equals(slot)) return EQUIMENT_SLOT.HANDS;
    else if ("Boots".equals(slot)) return EQUIMENT_SLOT.FEET;
    else if ("Shoulder".equals(slot)) return EQUIMENT_SLOT.SHOULDER;
    else if ("Back".equals(slot)) return EQUIMENT_SLOT.BACK;
    else if ("Bracelet1".equals(slot)) return EQUIMENT_SLOT.LEFT_WRIST;
    else if ("Bracelet2".equals(slot)) return EQUIMENT_SLOT.RIGHT_WRIST;
    else if ("Necklace".equals(slot)) return EQUIMENT_SLOT.NECK;
    else if ("Ring1".equals(slot)) return EQUIMENT_SLOT.LEFT_FINGER;
    else if ("Ring2".equals(slot)) return EQUIMENT_SLOT.RIGHT_FINGER;
    else if ("Earring1".equals(slot)) return EQUIMENT_SLOT.LEFT_EAR;
    else if ("Earring2".equals(slot)) return EQUIMENT_SLOT.RIGHT_EAR;
    else if ("Weapon_Primary".equals(slot)) return EQUIMENT_SLOT.MAIN_MELEE;
    else if ("Weapon_Secondary".equals(slot)) return EQUIMENT_SLOT.OTHER_MELEE;
    else if ("Pocket1".equals(slot)) return EQUIMENT_SLOT.POCKET;
    else if ("Weapon_Ranged".equals(slot)) return EQUIMENT_SLOT.RANGED;
    else if ("Last".equals(slot)) return EQUIMENT_SLOT.CLASS_ITEM;
    else if ("CraftTool".equals(slot)) return EQUIMENT_SLOT.TOOL;
    else return null;
  }
        
  /**
   * Parse the character page at the given URL.
   * @param name Toon name.
   * @param url URL of character page.
   * @return A character or <code>null</code> if an error occurred..
   */
  public Character parseMainPage(String name, String url)
  {
    Character ret=null;
    int maxTries=3;
    int retryNumber=0;
    while(retryNumber<maxTries)
    {
      Character subRet=parseMainPage(name,url,retryNumber);
      if (subRet!=null)
      {
        ret=subRet;
        break;
      }
      retryNumber++;
    }
    if (ret==null)
    {
      if (_logger.isEnabledFor(Level.ERROR))
      {
        _logger.error("Cannot parse character description page url=["+url+"] name=["+name+"]");
      }
    }
    return ret;
  }

  /**
   * Parse the character page at the given URL.
   * @param name Toon name.
   * @param url URL of character page.
   * @param retryNumber Number of previous tries.
   * @return A character or <code>null</code> if an error occurred..
   */
  private Character parseMainPage(String name, String url, int retryNumber)
  {
    if (_logger.isInfoEnabled())
    {
      _logger.info("Character description page parsing for toon ["+name+"] "+((retryNumber>0)?" try #"+retryNumber:""));
    }
    Character ret=null;
    try
    {
      DownloadService downloader=DownloadService.getInstance();
      byte[] page=downloader.getBuffer(url);
      ret = parseCharacter(page);
    }
    catch(Exception e)
    {
      if (_logger.isInfoEnabled())
      {
        _logger.info("Cannot parse character page ["+url+"]",e);
      }
    }
    return ret;
  }
}
