package delta.games.lotro.lore.items.io.tulkas;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import delta.common.utils.NumericTools;
import delta.common.utils.text.TextUtils;
import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.CharacterEquipment.SlotContents;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.Weapon;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for the items DB file or the Tulkas LOTRO plugin.
 * @author DAM
 */
public class TulkasItemsDBParser
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();
  
  // Sample item line:
  // "[1879049233] = {[\"Name\"]=\"Hengaim\";[\"Level\"]=0;[\"ItemLevel\"]=15;[\"Slot\"]=\"Hand\";[\"SubSlot\"]=\"\";[\"Type\"]=\"Light\";[\"Class\"]=\"\";[\"Color\"]=\"Yellow\";[\"Stats\"] = {[\"Armour\"]=55;[\"Will\"]=5;};};";

  private void parseItemsDef(Map<Object,Object> values, String strValue)
  {
    List<String> items=new ArrayList<String>();
    StringBuilder sb=new StringBuilder();
    int insertIndex=0;
    char[] context=new char[100];
    int length=strValue.length();
    for(int i=0;i<length;i++)
    {
      char c=strValue.charAt(i);
      if (c=='"')
      {
        if ((insertIndex>0)&&(context[insertIndex-1]=='"'))
        {
          insertIndex--;
        }
        else
        {
          context[insertIndex++]=c;
        }
        sb.append(c);
      }
      else if (c=='[')
      {
        context[insertIndex++]=c;
        sb.append(c);
      }
      else if (c==']')
      {
        if ((insertIndex>0)&&(context[insertIndex-1]=='['))
        {
          insertIndex--;
        }
        else
        {
          context[insertIndex++]=c;
        }
        sb.append(c);
      }
      else if (c=='{')
      {
        context[insertIndex++]=c;
        sb.append(c);
      }
      else if (c=='}')
      {
        if ((insertIndex>0)&&(context[insertIndex-1]=='{'))
        {
          insertIndex--;
        }
        else
        {
          context[insertIndex++]=c;
        }
        sb.append(c);
      }
      else if (c==';')
      {
        if (insertIndex==0)
        {
          items.add(sb.toString());
          sb.setLength(0);
        }
        else
        {
          sb.append(c);
        }
      }
      else
      {
        sb.append(c);
      }
    }
    if (sb.length()>0)
    {
      items.add(sb.toString());
    }
    for(String item:items)
    {
      parseItemDef(values,item);
    }
  }

  private void parseItemDef(Map<Object,Object> values, String strValue)
  {
    // ["toto"] = value;
    // [55] = value;
    // where value is "toto", NN, {values}
    int leftSquareBracketIndex=strValue.indexOf("[");
    if (leftSquareBracketIndex!=-1)
    {
      int rightSquareBracketIndex=strValue.indexOf("]",leftSquareBracketIndex+1);
      if (rightSquareBracketIndex!=-1)
      {
        int equalIndex=strValue.indexOf('=',rightSquareBracketIndex+1);
        if (equalIndex!=-1)
        {
          String keyStr=strValue.substring(leftSquareBracketIndex+1,rightSquareBracketIndex);
          Object key=parseKey(keyStr);
          String valueStr=strValue.substring(equalIndex+1);
          Object value=parseValue(valueStr);
          values.put(key,value);
        }
      }
    }
  }

  private Object parseKey(String keyStr)
  {
    Object ret=null;
    keyStr=keyStr.trim();
    int length=keyStr.length();
    if (length>0)
    {
      if ((length>=2)&&(keyStr.charAt(0)=='\"')&&(keyStr.charAt(length-1)=='\"'))
      {
        ret=keyStr.substring(1,length-1);
      }
      else
      {
        ret=NumericTools.parseInteger(keyStr);
      }
    }
    return ret;
  }

  private Object parseValue(String valueStr)
  {
    Object ret=null;
    valueStr=valueStr.trim();
    int length=valueStr.length();
    if (length>0)
    {
      if ((length>=2)&&(valueStr.charAt(0)=='\"')&&(valueStr.charAt(length-1)=='\"'))
      {
        ret=valueStr.substring(1,length-1);
      }
      else if ((length>=2)&&(valueStr.charAt(0)=='{')&&(valueStr.charAt(length-1)=='}'))
      {
        String itemValue=valueStr.substring(1,length-1);
        HashMap<Object,Object> map=new HashMap<Object,Object>();
        parseItemsDef(map,itemValue);
        ret=map;
      }
      else
      {
        int index=valueStr.indexOf('.');
        if (index>=0)
        {
          ret=NumericTools.parseFloat(valueStr);
        }
        else
        {
          ret=NumericTools.parseInteger(valueStr);
        }
      }
    }
    return ret;
  }

  private void inspectItems(HashMap<Integer,HashMap<Object,Object>> items)
  {
    Set<String> types=new HashSet<String>();
    Set<String> classes=new HashSet<String>();
    Set<String> slots=new HashSet<String>();
    Set<String> subSlots=new HashSet<String>();
    Set<String> colors=new HashSet<String>();
    for(HashMap<Object,Object> item:items.values())
    {
      String c=(String)item.get("Class");
      if ((c!=null)&&(c.length()>0)) classes.add(c);
      String type=(String)item.get("Type");
      if ((type!=null)&&(type.length()>0)) types.add(type);
      String slot=(String)item.get("Slot");
      if ((slot!=null)&&(slot.length()>0)) slots.add(slot);
      String subSlot=(String)item.get("SubSlot");
      if ((subSlot!=null)&&(subSlot.length()>0)) subSlots.add(subSlot);
      String color=(String)item.get("Color");
      if ((color!=null)&&(color.length()>0)) colors.add(color);
    }
    // Classes: [Champion, LoreMaster, Minstrel, Hunter, RuneKeeper, Burglar,
    // Captain, Guardian, Warden]
    System.out.println("Classes: "+classes);
    // Type: [Cloak, Medium, Heavy, Light]
    System.out.println("Type: "+types);
    // Slots: [Pocket, Head, Melee Weapon, Wrist, Hand, Ear, Chest, Neck, Back,
    // Finger, Leg, Shield, Feet, Ranged Weapon, Shoulder, Rune-stone]
    System.out.println("Slots: "+slots);
    // SubSlots: [Two-handed Sword, Staff, Halberd, Heavy, Two-handed Hammer, Bow, Javelin, Two-handed Club, One-handed Hammer, Spear, Warden, One-handed Club, One-handed Mace, Crossbow, Dagger, Light, One-handed Axe, One-handed Sword, Two-handed Axe]
    System.out.println("SubSlots: "+subSlots);
    // Color: [Yellow, Purple, Orange, Teal]
    System.out.println("Color: "+colors);
  }

  private Integer getIdFromURL(String url)
  {
    Integer ret=null;
    String separator="?id=";
    int index=url.indexOf(separator);
    if (index!=-1)
    {
      String indexStr=url.substring(index+separator.length());
      int id=NumericTools.parseInt(indexStr,-1);
      if (id!=-1)
      {
        ret=Integer.valueOf(id);
      }
    }
    return ret;
  }

  private void doIt()
  {
    HashMap<Integer,HashMap<Object,Object>> items=loadItems();
    inspectEquipment(items,"Elendilmir#Feroce");
    inspectEquipment(items,"Elendilmir#Glumlug");
    inspectEquipment(items,"Elendilmir#Beleganth");
    inspectEquipment(items,"Elendilmir#Utharr");
  }

  private void inspectEquipment(HashMap<Integer,HashMap<Object,Object>> items, String toonID)
  {
    System.out.println(toonID);
    HashMap<EQUIMENT_SLOT,HashMap<Object,Object>> equipment=loadEquipment(items,toonID);
    HashMap<String,Integer> bonuses=loadBonus(equipment);
    bonuses.remove("ItemLevel");
    bonuses.remove("Level");
    bonuses.remove("MinDMG");
    bonuses.remove("MaxDMG");
    System.out.println("Bonuses: "+bonuses);
  }

  private HashMap<String,Integer> loadBonus(HashMap<EQUIMENT_SLOT,HashMap<Object,Object>> equipment)
  {
    HashMap<String,Integer> bonuses=new HashMap<String,Integer>();
    for(Map.Entry<EQUIMENT_SLOT,HashMap<Object,Object>> entry:equipment.entrySet())
    {
      // System.out.println("Slot: "+entry.getKey());
      HashMap<Object,Object> map=entry.getValue();
      if (map!=null)
      {
        loadBonus(bonuses,map);
      }
    }
    return bonuses;
  }

  private HashMap<String,Integer> loadBonus(HashMap<String,Integer> bonuses, HashMap<Object,Object> map)
  {
    for(Object key:map.keySet())
    {
      if (key instanceof String)
      {
        String bonusName=(String)key;
        Object value=map.get(key);
        if (value instanceof Integer)
        {
          Integer newBonus=(Integer)value;
          Integer old=bonuses.get(bonusName);
          if (old==null)
          {
            bonuses.put(bonusName,newBonus);
          }
          else
          {
            bonuses.put(bonusName,Integer.valueOf(newBonus.intValue()+old.intValue()));
          }
          // System.out.println("Bonus: "+bonusName+", = "+newBonus);
        }
        else if (value instanceof HashMap)
        {
          @SuppressWarnings("unchecked")
          HashMap<Object,Object> subMap=(HashMap<Object,Object>)value;
          loadBonus(bonuses,subMap);
        }
      }
    }
    return bonuses;
  }

  private boolean isArmor(EquipmentLocation loc)
  {
    if (loc==EquipmentLocation.HEAD) return true;
    if (loc==EquipmentLocation.HAND) return true;
    if (loc==EquipmentLocation.CHEST) return true;
    if (loc==EquipmentLocation.BACK) return true;
    if (loc==EquipmentLocation.LEGS) return true;
    if (loc==EquipmentLocation.FEET) return true;
    if (loc==EquipmentLocation.SHIELD) return true;
    if (loc==EquipmentLocation.SHOULDER) return true;
    return false;
  }

  private Item buildItem(Integer id, HashMap<Object,Object> map)
  {
    String name=(String)map.get("Name");
    String slot=(String)map.get("Slot");
    EquipmentLocation loc=EquipmentLocation.getByName(slot);
    Item ret=null;
    @SuppressWarnings("unchecked")
    HashMap<Object,Object> statsMap=(HashMap<Object,Object>)map.get("Stats");
    if (isArmor(loc))
    {
      Armour a=new Armour();
      Integer armourValue=(Integer)statsMap.get("Armour");
      if (armourValue!=null)
      {
        a.setArmourValue(armourValue.intValue());
      }
      String armourTypeStr=(String)map.get("Type");
      ArmourType armourType=ArmourType.getArmourTypeByName(armourTypeStr);
      if (armourType==null)
      {
        armourType=ArmourType.LIGHT; // Assume light armour...
        _logger.warn("Unknown armour type: "+armourTypeStr+" (name="+name+")");
      }
      a.setArmourType(armourType);
      ret=a;
    }
    else
    {
      String subSlot=(String)map.get("SubSlot");
      WeaponType weaponType=null;
      if ((subSlot!=null) && (subSlot.length()>0))
      {
        weaponType=WeaponType.getWeaponTypeByName(subSlot);
      }
      if (weaponType!=null)
      {
        Weapon w=new Weapon();
        w.setWeaponType(weaponType);
        @SuppressWarnings("unchecked")
        HashMap<Object,Object> damageInfo=(HashMap<Object,Object>)statsMap.get("Damage");
        if (damageInfo!=null)
        {
          Integer minDMG=(Integer)damageInfo.get("MinDMG");
          if (minDMG!=null)
          {
            w.setMinDamage(minDMG.intValue());
          }
          Integer maxDMG=(Integer)damageInfo.get("MaxDMG");
          if (maxDMG!=null)
          {
            w.setMaxDamage(maxDMG.intValue());
          }
          Float dps=(Float)damageInfo.get("DPS");
          if (dps!=null)
          {
            w.setDPS(dps.intValue());
          }
          String typeStr=(String)damageInfo.get("TypeDMG");
          DamageType type=DamageType.getDamageTypeByName(typeStr);
          if (type==null)
          {
            type=DamageType.COMMON;
            _logger.warn("Unmanaged damage type ["+typeStr+"]");
          }
          w.setDamageType(type);
        }
        ret=w;
      }
      if (ret==null)
      {
        ret=new Item();
      }
    }
    // Name
    ret.setName(name);
    // Slot
    ret.setEquipmentLocation(loc);
    // Required level
    Integer requiredLevel=(Integer)map.get("Level");
    ret.setMinLevel(requiredLevel);
    // Item level
    Integer itemLevel=(Integer)map.get("ItemLevel");
    ret.setItemLevel(itemLevel);
    // Class
    String classStr=(String)map.get("Class");
    if ((classStr!=null) && (classStr.length()>0))
    {
      CharacterClass cClass=CharacterClass.getByLabel(classStr);
      if (cClass!=null)
      {
        ret.setRequiredClass(cClass);
      }
      else
      {
        System.err.println("Unknown class: "+classStr);
      }
    }
    return ret;
  }
  
  
  /*
OK: [1879247136] = {["Name"]="Helm of the Hytbold Shadow-fighter";
OK: ["Level"]=85;
OK: ["ItemLevel"]=85;
OK: ["Slot"]="Head";
["SubSlot"]="";
OK: ["Type"]="Heavy";
OK: ["Class"]="Guardian";
["Color"]="Teal";"Purple"
["Stats"] = {
  OK: ["Armour"]=520;
  ["Vitality"]=152;
  ["Might"]=54;
  ["Incoming Healing Rating"]=621;
  ["Parry Rating"]=310;
  ["Critical Rating"]=352;
  ["in-Combat Power Regen"]=113.4;
  ["Maximum Morale"]=183;
  ["Damage"]={
    OK:["MaxDMG"]=308;
    OK:["TypeDMG"]="Westernesse";
    OK:["MinDMG"]=185;
    OK:["DPS"]=129.6;
  };
};
["Set"] = {
  ["Name"]="Armour of the Hytbold Shadow-fighter";
  ["IDs"] = {1879247136,1879247141,1879247146,1879247148,1879247150,1879247158};
  ["Bonus"] = {
    [3] = {["Shield Damage"]="+5%";};
    [2] = {["Vitality"]="+38";["Maximum Morale"]="+214";};
    [4] = {["Incoming Healing Rating"]="+776";};
    [5] = {["Warrior's Fortitude Morale Heal"]="+200%";["Warrior's Fortitude Maximum Morale"]="+100%";
  };
};
*/

  private HashMap<EQUIMENT_SLOT,HashMap<Object,Object>> loadEquipment(HashMap<Integer,HashMap<Object,Object>> items, String toonID)
  {
    HashMap<EQUIMENT_SLOT,HashMap<Object,Object>> ret=new HashMap<EQUIMENT_SLOT,HashMap<Object,Object>>();
    CharactersManager cm=CharactersManager.getInstance();
    CharacterFile glumFile=cm.getToonById(toonID);
    Character glum=glumFile.getLastCharacterInfo();
    CharacterEquipment equipment=glum.getEquipment();
    for(CharacterEquipment.EQUIMENT_SLOT slot:EQUIMENT_SLOT.values())
    {
      SlotContents contents=equipment.getSlotContents(slot,false);
      if (contents!=null)
      {
        String url=contents.getObjectURL();
        Integer id=getIdFromURL(url);
        if (id!=null)
        {
          HashMap<Object,Object> item=items.get(id);
          if (item!=null)
          {
            Item i=buildItem(id,item);
            System.out.println("Item: "+i.dump());
            ret.put(slot,item);
          }
          else
          {
            System.out.println("Missing id: "+id+" (slot="+slot+")");
          }
          // System.out.println("Slot "+slot+": "+item);
        }
      }
      else
      {
        // System.out.println("Slot "+slot+": no item!");
      }
    }
    return ret;
  }

  private HashMap<Integer,HashMap<Object,Object>> loadItems()
  {
    Locale.setDefault(Locale.US);
    File dbFile=new File(new File("data"),"Items.lua");
    // String contents=TextUtils.loadTextFile(dbFile,
    // EncodingNames.DEFAULT_ENCODING);
    List<String> lines=TextUtils.readAsLines(dbFile);
    List<String> itemLines=new ArrayList<String>();
    for(String line:lines)
    {
      if ((line.length()>0)&&(line.charAt(0)=='['))
      {
        itemLines.add(line);
      }
    }
    lines=null;
    //int nb=itemLines.size();
    // System.out.println("Size: "+nb);
    HashMap<Integer,HashMap<Object,Object>> items=new HashMap<Integer,HashMap<Object,Object>>();
    for(String line:itemLines)
    {
      HashMap<Object,Object> map=new HashMap<Object,Object>();
      parseItemsDef(map,line);
      Map.Entry<Object,Object> entry=map.entrySet().iterator().next();
      Integer key=(Integer)entry.getKey();
      @SuppressWarnings("unchecked")
      HashMap<Object,Object> values=(HashMap<Object,Object>)entry.getValue();
      // System.out.println(map);
      items.put(key,values);
    }
    itemLines=null;
    // System.out.println("Done!");
    inspectItems(items);
    return items;
  }

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    new TulkasItemsDBParser().doIt();
  }
}
