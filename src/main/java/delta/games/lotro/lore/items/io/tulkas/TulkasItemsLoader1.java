package delta.games.lotro.lore.items.io.tulkas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.ItemsSet;
import delta.games.lotro.lore.items.Weapon;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.bonus.Bonus;
import delta.games.lotro.lore.items.bonus.BonusManager;
import delta.games.lotro.lore.items.bonus.BonusType;
import delta.games.lotro.lore.items.bonus.Bonus.BONUS_OCCURRENCE;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Items/sets loader for Tulkas DB (version 1).
 * @author DAM
 */
public class TulkasItemsLoader1 extends TulkasItemsLoader
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  /**
   * Inspect loaded data items to fetch possible values in fields.
   * @param items Loaded data items.
   */
  /*
  public void inspectItems(HashMap<Integer,HashMap<Object,Object>> items)
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
  */

  /**
   * Build items from raw data items.
   * @param items Loaded data items.
   */
  public void buildItems(HashMap<Integer,HashMap<Object,Object>> items)
  {
    List<Integer> keys=new ArrayList<Integer>(items.keySet());
    Collections.sort(keys);
    int nbKeys=keys.size();
    System.out.println("Min: "+keys.get(0)+", max: "+keys.get(nbKeys-1));
    for(int i=0;i<nbKeys;i++)
    {
      Integer id=keys.get(i);
      HashMap<Object,Object> data=items.get(id);
      Item item=buildItem(id,data);
      item.setIdentifier(id.intValue());
      /*
      ItemsManager mgr=ItemsManager.getInstance();
      mgr.writeItemFile(item);
      System.out.println(item.dump());
      */
      writeItemToDB(item);
    }
  }

  private Item buildItem(Integer id, HashMap<Object,Object> map)
  {
    String name=(String)map.get("Name");
    String slot=(String)map.get("Slot");
    EquipmentLocation loc=EquipmentLocation.getByName(slot);
    Item ret=null;
    @SuppressWarnings("unchecked")
    HashMap<Object,Object> statsMap=(HashMap<Object,Object>)map.get("Stats");
    if (TulkasConstants.isArmor(loc))
    {
      Armour a=new Armour();
      Integer armourValue=(Integer)statsMap.get("Armour");
      if (armourValue!=null)
      {
        a.setArmourValue(armourValue.intValue());
      }
      String armourTypeStr=(String)map.get("Type");
      ArmourType armourType=ArmourType.getArmourTypeByName(armourTypeStr);
      if (loc==EquipmentLocation.SHIELD)
      {
        String subSlot=(String)map.get("SubSlot");
        if ("Heavy".equals(subSlot)) armourType=ArmourType.HEAVY_SHIELD;
        else if ("Light".equals(subSlot)) armourType=ArmourType.SHIELD;
        else if ("Warden".equals(subSlot)) armourType=ArmourType.WARDEN_SHIELD;
        else
        {
          // SubSlots:  
          //Heavy, Warden, Light,
          _logger.warn("Unmanaged shield type ["+subSlot+"]");
        }
      }
      else if (loc==EquipmentLocation.BACK)
      {
        armourType=ArmourType.LIGHT;
      }
      if (armourType==null)
      {
        _logger.warn("Unknown armour type: ["+armourTypeStr+"] (name="+name+")");
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
          Object dpsValue=damageInfo.get("DPS");
          if (dpsValue instanceof Float)
          {
            w.setDPS(((Float)dpsValue).floatValue());
          }
          else if (dpsValue instanceof Integer)
          {
            w.setDPS(((Integer)dpsValue).floatValue());
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
      CharacterClass cClass=CharacterClass.getByName(classStr);
      if (cClass!=null)
      {
        ret.setRequiredClass(cClass);
      }
      else
      {
        _logger.error("Unknown class: "+classStr);
      }
    }
    // Quality
    String colorStr=(String)map.get("Color");
    ItemQuality quality=null;
    if (colorStr!=null)
    {
      quality=ItemQuality.fromColor(colorStr);
    }
    ret.setQuality((quality!=null)?quality:ItemQuality.COMMON);
    
    // Bonus
    if (statsMap!=null)
    {
      BonusManager bonusMgr=ret.getBonusManager();
      final HashMap<String,Object> bonuses=new HashMap<String,Object>();
      loadBonusItemsVersion1(bonuses,statsMap);
      bonuses.remove("Armour");
      for(String bonusName : TulkasConstants.BONUS_NAMES)
      {
        Object bonusValue=bonuses.get(bonusName);
        if (bonusValue!=null)
        {
          BonusType type=BonusType.getByName(bonusName);
          Bonus bonus=new Bonus(type,BONUS_OCCURRENCE.ALWAYS);
          Object value=type.buildValue(bonusValue);
          bonus.setValue(value);
          bonusMgr.add(bonus);
          bonuses.remove(bonusName);
        }
      }
      if (bonuses.size()>0)
      {
        _logger.warn("Unmanaged bonuses: "+bonuses);
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
OK: ["Color"]="Teal";"Purple"
["Stats"] = {
  OK: ["Armour"]=520;
  OK(bonus): ["Vitality"]=152;
  OK(bonus): ["Might"]=54;
  OK(bonus): ["Incoming Healing Rating"]=621;
  OK(bonus): ["Parry Rating"]=310;
  OK(bonus): ["Critical Rating"]=352;
  OK(bonus): ["in-Combat Power Regen"]=113.4;
  OK(bonus): ["Maximum Morale"]=183;
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

Sample item line:
 [1879049233] = {["Name"]="Hengaim";["Level"]=0;["ItemLevel"]=15;["Slot"]="Hand";["SubSlot"]="";
       ["Type"]="Light";["Class"]="";["Color"]="Yellow";["Stats"] = {["Armour"]=55;["Will"]=5;};};
 =>
Name: Hengaim (Hand) (ARMOUR) (Min level=0) (Item level=15) (Value=)
Will : 5
Armour value=55, Armour type=Light Armour
};

[1879049234] = {["Name"]="Cloak of Cardolan";["Level"]=0;["ItemLevel"]=15;
["Slot"]="Back";["SubSlot"]="";["Type"]="";["Class"]="";["Color"]="Yellow";
["Stats"] = {["Armour"]=74;["Maximum Morale"]=14;};};

Name: Cloak of Cardolan (Back) (ARMOUR) (Quality=Uncommon) (Min level=0) (Item level=15) (Value=)
Maximum Morale : 14
Armour value=74, Armour type=Light Armour
*/

  private void loadBonusItemsVersion1(final HashMap<String,Object> bonuses, HashMap<Object,Object> map)
  {
    for(Object key:map.keySet())
    {
      if (key instanceof String)
      {
        String bonusName=(String)key;
        Object value=map.get(key);
        if (value instanceof Integer)
        {
          Integer bonus=(Integer)value;
          bonuses.put(bonusName,bonus);
        }
        else if (value instanceof HashMap)
        {
          // Ignore
        }
        else if (value instanceof Float)
        {
          Float bonus=(Float)value;
          bonuses.put(bonusName,bonus);
        }
        else if (value instanceof String)
        {
          String bonus=(String)value;
          bonuses.put(bonusName,bonus);
        }
        else
        {
          _logger.warn("Unmanaged value: "+value+", class="+value.getClass().getName());
        }
      }
    }
  }

  /**
   * Build items sets from raw data items.
   * @param items Loaded data items.
   */
  public void buildSets(HashMap<Integer,HashMap<Object,Object>> items)
  {
    HashMap<String,ItemsSet> sets=new HashMap<String,ItemsSet>();
    List<Integer> keys=new ArrayList<Integer>(items.keySet());
    Collections.sort(keys);
    int nbKeys=keys.size();
    for(int i=0;i<nbKeys;i++)
    {
      Integer id=keys.get(i);
      HashMap<Object,Object> data=items.get(id);
      @SuppressWarnings("unchecked")
      HashMap<Object,Object> setDef=(HashMap<Object,Object>)data.get("Set");
      if (setDef!=null)
      {
        String setName=(String)setDef.get("Name");
        ItemsSet set=sets.get(setName);
        if (set!=null) continue;
        set=new ItemsSet();
        // Use name as set key
        set.setKey(setName);
        set.setName(setName);
        sets.put(setName,set);
        //System.out.println(setName);
        @SuppressWarnings("unchecked")
        List<Integer> ids=(List<Integer>)setDef.get("IDs");
        //System.out.println(ids);
        for(Integer setItemId : ids)
        {
          set.addItem(setItemId.intValue(),"");
        }
        @SuppressWarnings("unchecked")
        HashMap<Object,Object> bonuses=(HashMap<Object,Object>)setDef.get("Bonus");
        if (bonuses!=null)
        {
          Set<Object> numbers=bonuses.keySet();
          for(Object number : numbers)
          {
            Integer nb=(Integer)number;
            //System.out.println(nb);
            @SuppressWarnings("unchecked")
            HashMap<Object,Object> rawSetBonuses=(HashMap<Object,Object>)bonuses.get(nb);
            HashMap<String,Object> setBonuses=new HashMap<String,Object>();
            loadBonusItemsVersion1(setBonuses,rawSetBonuses);
            for(String bonusName : setBonuses.keySet())
            {
              String bonus=bonusName+" : "+setBonuses.get(bonusName);
              set.addBonus(nb.intValue(),bonus);
            }
          }
        }
      }
    }
    for(ItemsSet set : sets.values())
    {
      System.out.println(set.dump());
    }
      
    //["Set"] = {["Name"]="Daerdúr Armour";["IDs"] = {1879113508,1879113511,1879113514,1879113517,1879113520,1879113523};["Bonus"] = {[3] = {["Might"]="+15";};[6] = {["Vitality"]="+15";["Fire Mitigation"]="+375";};};};};
  }
}
