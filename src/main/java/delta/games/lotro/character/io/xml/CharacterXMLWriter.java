package delta.games.lotro.character.io.xml;

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
import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.CharacterEquipment.SlotContents;
import delta.games.lotro.character.CharacterStat;
import delta.games.lotro.character.CharacterStat.STAT;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes LOTRO characters to XML files.
 * @author DAM
 */
public class CharacterXMLWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";
  
  /**
   * Write a character to a XML file.
   * @param outFile Output file.
   * @param character Character to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, Character character, String encoding)
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
      write(hd,character);
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
  
  private void write(TransformerHandler hd, Character character) throws Exception
  {
    AttributesImpl characterAttrs=new AttributesImpl();
    String name=character.getName();
    if (name!=null)
    {
      characterAttrs.addAttribute("","",CharacterXMLConstants.CHARACTER_NAME_ATTR,CDATA,name);
    }
    String server=character.getServer();
    if (server!=null)
    {
      characterAttrs.addAttribute("","",CharacterXMLConstants.CHARACTER_SERVER_ATTR,CDATA,server);
    }
    CharacterClass characterClass=character.getCharacterClass();
    if (characterClass!=null)
    {
      String cClass=characterClass.getKey();
      characterAttrs.addAttribute("","",CharacterXMLConstants.CHARACTER_CLASS_ATTR,CDATA,cClass);
    }
    Race race=character.getRace();
    if (race!=null)
    {
      String cRace=race.getLabel();
      characterAttrs.addAttribute("","",CharacterXMLConstants.CHARACTER_RACE_ATTR,CDATA,cRace);
    }
    String region=character.getRegion();
    if (region!=null)
    {
      characterAttrs.addAttribute("","",CharacterXMLConstants.CHARACTER_REGION_ATTR,CDATA,region);
    }
    int level=character.getLevel();
    characterAttrs.addAttribute("","",CharacterXMLConstants.CHARACTER_LEVEL_ATTR,CDATA,String.valueOf(level));
    
    hd.startElement("","",CharacterXMLConstants.CHARACTER_TAG,characterAttrs);
    writeStats(hd,character);
    CharacterEquipment equipment=character.getEquipment();
    if (equipment!=null)
    {
      writeEquipment(hd,equipment);
    }
    hd.endElement("","",CharacterXMLConstants.CHARACTER_TAG);
  }

  private void writeStats(TransformerHandler hd, Character character) throws Exception
  {
    AttributesImpl statsAttrs=new AttributesImpl();
    hd.startElement("","",CharacterXMLConstants.STATS_TAG,statsAttrs);
    STAT[] stats=STAT.values();
    for(STAT stat : stats)
    {
      CharacterStat characterStat=character.getStat(stat,false);
      if (characterStat!=null)
      {
        writeStat(hd,characterStat);
      }
    }
    hd.endElement("","",CharacterXMLConstants.STATS_TAG);
  }

  private void writeStat(TransformerHandler hd, CharacterStat characterStat) throws Exception
  {
    STAT stat=characterStat.getStat();
    if (stat!=null)
    {
      AttributesImpl statAtts=new AttributesImpl();
      statAtts.addAttribute("","",CharacterXMLConstants.STAT_NAME_ATTR,CDATA,stat.name());
      Integer value=characterStat.getValue();
      if (value!=null)
      {
        statAtts.addAttribute("","",CharacterXMLConstants.STAT_VALUE_ATTR,CDATA,String.valueOf(value.intValue()));
      }
      hd.startElement("","",CharacterXMLConstants.STAT_TAG,statAtts);
      hd.endElement("","",CharacterXMLConstants.STAT_TAG);
    }
  }

  private void writeEquipment(TransformerHandler hd, CharacterEquipment equipment) throws Exception
  {
    AttributesImpl fieldAtts=new AttributesImpl();
    hd.startElement("","",CharacterXMLConstants.EQUIPMENT_TAG,fieldAtts);
    EQUIMENT_SLOT[] slots=EQUIMENT_SLOT.values();
    for(EQUIMENT_SLOT slot : slots)
    {
      SlotContents slotContents=equipment.getSlotContents(slot,false);
      if (slotContents!=null)
      {
        writeEquipmentSlot(hd,slotContents);
      }
    }
    hd.endElement("","",CharacterXMLConstants.EQUIPMENT_TAG);
  }

  private void writeEquipmentSlot(TransformerHandler hd, SlotContents slotContents) throws Exception
  {
    EQUIMENT_SLOT slot=slotContents.getSlot();
    if (slot!=null)
    {
      AttributesImpl slotAtts=new AttributesImpl();
      slotAtts.addAttribute("","",CharacterXMLConstants.SLOT_NAME_ATTR,CDATA,slot.name());
      String objectURL=slotContents.getObjectURL();
      if (objectURL!=null)
      {
        slotAtts.addAttribute("","",CharacterXMLConstants.SLOT_OBJECT_URL_ATTR,CDATA,objectURL);
      }
      String iconURL=slotContents.getIconURL();
      if (iconURL!=null)
      {
        slotAtts.addAttribute("","",CharacterXMLConstants.SLOT_ICON_URL_ATTR,CDATA,iconURL);
      }
      hd.startElement("","",CharacterXMLConstants.SLOT_TAG,slotAtts);
      hd.endElement("","",CharacterXMLConstants.SLOT_TAG);
    }
  }
}
