package delta.games.lotro.character.io.web;

import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import delta.common.utils.NumericTools;
import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.CharacterEquipment.SlotContents;
import delta.games.lotro.character.CharacterStat;
import delta.games.lotro.character.CharacterStat.STAT;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.utils.JerichoHtmlUtils;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for MyLotro.com character page.
 * @author DAM
 */
public class CharacterPageParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  private static final String SLOT_SEED="slot_";
  private static final String OF=" of ";
  private Character _character;

  private void parseCharacterDescription(Element charPanel)
  {
    // Character name
    // <div class="char_name header_color">Glumlug of Elendilmir</div>
    String charName=JerichoHtmlUtils.getTagContents(charPanel,HTMLElementName.DIV,"class","char_name header_color");
    if (charName!=null)
    {
      int indexOf=charName.indexOf(OF);
      if (indexOf!=-1)
      {
        String server=charName.substring(indexOf+OF.length());
        _character.setServer(server);
        charName=charName.substring(0,indexOf);
      }
      _character.setName(charName);
    }
    //System.out.println("Char name ["+charName+"]");
    // Character core:
    // <div class="char_core">
    Element charCore=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(charPanel,HTMLElementName.DIV,"class","char_core");
    if (charCore!=null)
    {
      //<div class="char_class header_color">Hunter</div>
      String charClassName=JerichoHtmlUtils.getTagContents(charPanel,HTMLElementName.DIV,"class","char_class header_color");
      CharacterClass cClass=CharacterClass.getByLabel(charClassName);
      _character.setCharacterClass(cClass);
      //<div class="char_race header_color">Dwarf</div>
      String charRace=JerichoHtmlUtils.getTagContents(charPanel,HTMLElementName.DIV,"class","char_race header_color");
      Race race=Race.getByLabel(charRace);
      _character.setRace(race);
      //<div class="char_nat header_color">Grey Mountains</div>
      String charNation=JerichoHtmlUtils.getTagContents(charPanel,HTMLElementName.DIV,"class","char_nat header_color");
      _character.setRegion(charNation);
      //<div class="char_level header_color">66</div> 
      String charLevelStr=JerichoHtmlUtils.getTagContents(charPanel,HTMLElementName.DIV,"class","char_level header_color");
      int charLevel=NumericTools.parseInt(charLevelStr,0);
      _character.setLevel(charLevel);
      //System.out.println("Class ["+charClassName+"], Race ["+charRace+"], Nation ["+charNation+"], Level="+charLevel);
    }
    // Character main stats:
    // <div class="core_stats header_color">
    Element charStats=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(charPanel,HTMLElementName.DIV,"class","core_stats header_color");
    if (charStats!=null)
    {
      //<div class="morale">4839</div>
      String moraleStr=JerichoHtmlUtils.getTagContents(charPanel,HTMLElementName.DIV,"class","morale");
      int morale=NumericTools.parseInt(moraleStr,0);
      _character.getStat(STAT.MORALE,true).setValue(Integer.valueOf(morale));
      //<div class="power">2243</div>
      String powerStr=JerichoHtmlUtils.getTagContents(charPanel,HTMLElementName.DIV,"class","power");
      int power=NumericTools.parseInt(powerStr,0);
      _character.getStat(STAT.POWER,true).setValue(Integer.valueOf(power));
      //<div class="armour">3213</div>
      String armourStr=JerichoHtmlUtils.getTagContents(charPanel,HTMLElementName.DIV,"class","armour");
      int armour=NumericTools.parseInt(armourStr,0);
      _character.getStat(STAT.ARMOUR,true).setValue(Integer.valueOf(armour));
      //System.out.println("Morale="+morale+", Power="+power+", Armour="+armour);
    }
    
    CharacterEquipment equipment=_character.getEquipment();
    // Object slots:
    //<a class="equipment_icon" href="http://lorebook.lotro.com/wiki/Special:LotroResource?id=1879205751">
    //<img class="slot_2" src="http://lorebook.lotro.com/icon.php?id=1879205751&type=item">
    //</a>
    List<Element> objectSlots=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(charPanel,HTMLElementName.A,"class","equipment_icon");
    for(Element objectSlot : objectSlots)
    {
      String objectPageURL=objectSlot.getAttributeValue("href");
      //System.out.println("Object ["+objectPageURL+"]");
      Element img=objectSlot.getFirstElement(HTMLElementName.IMG);
      if (img!=null)
      {
        String slotName=img.getAttributeValue("class");
        Integer slotNumber=null;
        if ((slotName!=null) && (slotName.startsWith(SLOT_SEED)))
        {
          slotName=slotName.substring(SLOT_SEED.length());
          slotNumber=NumericTools.parseInteger(slotName);
          if (slotNumber!=null)
          {
            String iconURL=img.getAttributeValue("src");
            EQUIMENT_SLOT slot=CharacterEquipment.getSlotByIndex(slotNumber.intValue());
            SlotContents contents=equipment.getSlotContents(slot,true);
            
            contents.setIconURL(iconURL);
            contents.setObjectURL(objectPageURL);
            //System.out.println("Slot ["+slotNumber+"], Icon URL ["+iconURL+"]");
          }
        }
      }
    }
  }
  
  private STAT getStatByName(String name)
  {
    if ("Might".equals(name)) return STAT.MIGHT;
    else if ("Agility".equals(name)) return STAT.AGILITY;
    else if ("Vitality".equals(name)) return STAT.VITALITY;
    else if ("Will".equals(name)) return STAT.WILL;
    else if ("Fate".equals(name)) return STAT.FATE;
    else if ("Critical Hit".equals(name)) return STAT.CRITICAL_HIT;
    else if ("Finesse".equals(name)) return STAT.FINESSE;
    else if ("Block".equals(name)) return STAT.BLOCK;
    else if ("Parry".equals(name)) return STAT.PARRY;
    else if ("Evade".equals(name)) return STAT.EVADE;
    else if ("Resistance".equals(name)) return STAT.RESISTANCE;
    else if ("Crit Avoid".equals(name)) return STAT.CRITICAL_AVOID;
    else if ("Physical Mit.".equals(name)) return STAT.PHYSICAL_MITIGATION;
    else if ("Tactical Mit.".equals(name)) return STAT.TACTICAL_MITIGATION;
    else return null;
  }

  private void parseStats(Element charPanel)
  {
    // <table class="stat_list">    
    List<Element> statsTables=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(charPanel,HTMLElementName.TABLE,"class","stat_list");
    for(Element statsTable : statsTables)
    {
      List<Element> rows=statsTable.getAllElements(HTMLElementName.TR);
      for(Element row : rows)
      {
        String statName=JerichoHtmlUtils.getTagContents(row,HTMLElementName.TH);
        if (statName!=null)
        {
          statName=statName.trim();
          if (statName.length()>0)
          {
            String statValue=JerichoHtmlUtils.getTagContents(row,HTMLElementName.TD);
            STAT stat=getStatByName(statName);
            if (stat!=null)
            {
              Integer value=null;
              if (!"N/A".equals(statValue))
              {
                value=NumericTools.parseInteger(statValue);
              }
              CharacterStat cstat=_character.getStat(stat,true);
              cstat.setValue(value);
              //System.out.println("Stat : name=["+statName+"], value=["+value+"]");
            }
          }
        }
      }
    }
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
  public Character parseMainPage(String name, String url, int retryNumber)
  {
    if (_logger.isInfoEnabled())
    {
      _logger.info("Character description page parsing for toon ["+name+"] "+((retryNumber>0)?" try #"+retryNumber:""));
    }
    Character ret=null;
    try
    {
      _character=new Character();
      Source source=new Source(new URL(url));
  
      //<table class="char_panel freep">
      Element charPanel=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.TABLE,"class","char_panel freep");
      if (charPanel!=null)
      {
        parseCharacterDescription(charPanel);
        parseStats(charPanel);
      }
      ret=_character;
      _character=null;
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
