package delta.games.lotro.lore.items.bonus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import delta.common.utils.NumericTools;

/**
 * A bonus type.
 * @author DAM
 */
public class BonusType
{
  /**
   * Bonus value type.
   * @author DAM
   */
  public enum VALUE_CLASS
  {
    /**
     * Integer value.
     */
    INTEGER,
    /**
     * Float value.
     */
    FLOAT,
    /**
     * String value.
     */
    STRING,
    /**
     * Percentage value (as a Float).
     */
    PERCENTAGE
  }
  private static HashMap<String,BonusType> _instances=new HashMap<String,BonusType>();
  private static HashMap<String,BonusType> _instancesByKey=new HashMap<String,BonusType>();
  private static List<BonusType> _all=new ArrayList<BonusType>();
  private String _key;
  private String _label;
  private VALUE_CLASS _valueClass;

  /**
   * Maximum morale.
   */
  public static BonusType MAX_MORALE=new BonusType("MAX_MORALE","Maximum Morale",VALUE_CLASS.FLOAT);
  /**
   * In-combat morale regen.
   */
  public static BonusType ICMR=new BonusType("ICMR","in-Combat Morale Regen",VALUE_CLASS.FLOAT);
  /**
   * Non-combat morale regen.
   */
  public static BonusType NCMR=new BonusType("NCMR","non-Combat Morale Regen",VALUE_CLASS.FLOAT);
  /**
   * Maximum power.
   */
  public static BonusType MAX_POWER=new BonusType("MAX_POWER","Maximum Power",VALUE_CLASS.INTEGER);
  /**
   * In-combat power regen.
   */
  public static BonusType ICPR=new BonusType("ICPR","in-Combat Power Regen",VALUE_CLASS.FLOAT);
  /**
   * Non-combat power regen.
   */
  public static BonusType NCPR=new BonusType("NCPR","non-Combat Power Regen",VALUE_CLASS.FLOAT);
  /**
   * Fellowship manoeuvre damage from Might.
   */
  public static BonusType FELLOWSHIP_MANOEUVRE_MIGHT=new BonusType("FELLOWSHIP_MANOEUVRE_MIGHT","Fellowship Manoeuvre Damage from Might",VALUE_CLASS.PERCENTAGE);
  /**
   * Fellowship manoeuvre damage from Guile.
   */
  public static BonusType FELLOWSHIP_MANOEUVRE_GUILE=new BonusType("FELLOWSHIP_MANOEUVRE_GUILE","Fellowship Manoeuvre Damage from Guile",VALUE_CLASS.PERCENTAGE);
  /**
   * Might.
   */
  public static BonusType MIGHT=new BonusType("MIGHT","Might",VALUE_CLASS.INTEGER);
  /**
   * Agility.
   */
  public static BonusType AGILITY=new BonusType("AGILITY","Agility",VALUE_CLASS.INTEGER);
  /**
   * Vitality.
   */
  public static BonusType VITALITY=new BonusType("VITALITY","Vitality",VALUE_CLASS.INTEGER);
  /**
   * Will.
   */
  public static BonusType WILL=new BonusType("WILL","Will",VALUE_CLASS.INTEGER);
  /**
   * Fate.
   */
  public static BonusType FATE=new BonusType("FATE","Fate",VALUE_CLASS.INTEGER);
  /**
   * Melee critical rating.
   */
  public static BonusType MELEE_CRIT_RATING=new BonusType("MELEE_CRIT_RATING","Melee Critical Rating",VALUE_CLASS.INTEGER);
  /**
   * Ranged critical rating.
   */
  public static BonusType RANGED_CRIT_RATING=new BonusType("RANGED_CRIT_RATING","Ranged Critical Rating",VALUE_CLASS.INTEGER);
  /**
   * Critical rating.
   */
  public static BonusType CRIT_RATING=new BonusType("CRIT_RATING","Critical Rating",VALUE_CLASS.INTEGER);
  /**
   * Finesse.
   */
  public static BonusType FINESSE=new BonusType("FINESSE","Finesse Rating",VALUE_CLASS.INTEGER);
  /**
   * Physical mastery.
   */
  public static BonusType PHYSICAL_MASTERY=new BonusType("PHYSICAL_MASTERY","Physical Mastery Rating",VALUE_CLASS.INTEGER);
  /**
   * Tactical mastery.
   */
  public static BonusType TACTICAL_MASTERY=new BonusType("TACTICAL_MASTERY","Tactical Mastery Rating",VALUE_CLASS.INTEGER);
  /**
   * Resistance rating.
   */
  public static BonusType RESISTANCE=new BonusType("RESISTANCE","Resistance Rating",VALUE_CLASS.FLOAT);
  /**
   * Critical defence.
   */
  public static BonusType CRITICAL_DEFENCE=new BonusType("CRITICAL_DEFENCE","Critical Defence",VALUE_CLASS.INTEGER);
  /**
   * Incoming healing rating.
   */
  public static BonusType INCOMING_HEALING=new BonusType("INCOMING_HEALING","Incoming Healing Rating",VALUE_CLASS.FLOAT);
  /**
   * Block rating.
   */
  public static BonusType BLOCK=new BonusType("BLOCK","Block Rating",VALUE_CLASS.INTEGER);
  /**
   * Parry rating.
   */
  public static BonusType PARRY=new BonusType("PARRY","Parry Rating",VALUE_CLASS.INTEGER);
  /**
   * Evade rating.
   */
  public static BonusType EVADE=new BonusType("EVADE","Evade Rating",VALUE_CLASS.INTEGER);
  /**
   * Melee defence rating.
   */
  public static BonusType MELEE_DEFENCE=new BonusType("MELEE_DEFENCE","Melee Defence Rating",VALUE_CLASS.INTEGER);
  /**
   * Ranged defence.
   */
  public static BonusType RANGED_DEFENCE=new BonusType("RANGED_DEFENCE","Ranged Defence",VALUE_CLASS.INTEGER);
  /**
   * Tactical defence.
   */
  public static BonusType TACTICAL_DEFENCE=new BonusType("TACTICAL_DEFENCE","Tactical Defence",VALUE_CLASS.INTEGER);
  /**
   * Physical mitigation.
   */
  public static BonusType PHYSICAL_MIT=new BonusType("PHYSICAL_MIT","Physical Mitigation",VALUE_CLASS.INTEGER);
  /**
   * Tactical mitigation.
   */
  public static BonusType TACTICAL_MIT=new BonusType("TACTICAL_MIT","Tactical Mitigation",VALUE_CLASS.INTEGER);
  /**
   * Audacity.
   */
  public static BonusType AUDACITY=new BonusType("AUDACITY","Audacity",VALUE_CLASS.INTEGER);
  /**
   * Stealth level.
   */
  public static BonusType STEALTH_LEVEL=new BonusType("STEALTH_LEVEL","Stealth Level",VALUE_CLASS.INTEGER);
  /**
   * Tactical critical multiplier.
   */
  public static BonusType TACTICAL_CRIT_MULTIPLIER=new BonusType("TACTICAL_CRIT_MULTIPLIER","Tactical Critical Multiplier",VALUE_CLASS.PERCENTAGE);
  /**
   * Melee offence.
   */
  public static BonusType MELEE_OFFENCE=new BonusType("MELEE_OFFENCE","Melee Offence Rating",VALUE_CLASS.INTEGER);
  /**
   * Ranged offence.
   */
  public static BonusType RANGED_OFFENCE=new BonusType("RANGED_OFFENCE","Ranged Offence Rating",VALUE_CLASS.INTEGER);
  /**
   * All skill inductions.
   */
  public static BonusType ALL_SKILL_INDUCTIONS=new BonusType("ALL_SKILL_INDUCTIONS","All Skill Inductions",VALUE_CLASS.PERCENTAGE);
  /**
   * Other.
   */
  public static BonusType OTHER=new BonusType("OTHER","Other",VALUE_CLASS.STRING);

  private BonusType(String key, String label, VALUE_CLASS valueClass)
  {
    this(key,label,null,valueClass);
  }
  
  private BonusType(String key, String label, String[] aliases, VALUE_CLASS valueClass)
  {
    _key=key;
    _label=label;
    _valueClass=valueClass;
    _instancesByKey.put(key,this);
    _instances.put(label,this);
    if (aliases!=null)
    {
      for(String alias : aliases)
      {
        _instances.put(alias,this);
      }
    }
    _all.add(this);
  }

  /**
   * Get the key for this class.
   * @return An internal key.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Get the displayable name of this class.
   * @return A displayable label.
   */
  public String getLabel()
  {
    return _label;
  }

  /**
   * Get the class of the bonus value.
   * @return a bonus value class.
   */
  public VALUE_CLASS getValueClass()
  {
    return _valueClass;
  }

  /**
   * Build a bonus value from an object value.
   * @param value Value to use.
   * @return A value or <code>null</code> if <code>value</code> is <code>null</code>.
   */
  public Object buildValue(Object value)
  {
    Object ret=null;
    if (value!=null)
    {
      if (value instanceof String)
      {
        ret=buildValue(value);
      }
      else if (value instanceof Integer)
      {
        Integer integer=(Integer)value;
        if (_valueClass==VALUE_CLASS.INTEGER)
        {
          ret=integer;
        }
        else if (_valueClass==VALUE_CLASS.FLOAT)
        {
          ret=Float.valueOf(integer.intValue());
        }
        else if (_valueClass==VALUE_CLASS.PERCENTAGE)
        {
          ret=Float.valueOf(integer.intValue());
        }
      }
      else if (value instanceof Float)
      {
        Float floatValue=(Float)value;
        if (_valueClass==VALUE_CLASS.INTEGER)
        {
          ret=Integer.valueOf(floatValue.intValue());
        }
        else if (_valueClass==VALUE_CLASS.FLOAT)
        {
          ret=floatValue;
        }
        else if (_valueClass==VALUE_CLASS.PERCENTAGE)
        {
          ret=floatValue;
        }
        else
        {
          ret=value.toString();
        }
      }
    }
    return ret;
  }

  /**
   * Build a bonus value from a string.
   * @param valueStr Value string to use.
   * @return A value or <code>null</code> if <code>valueStr</code> is <code>null</code>.
   */
  public Object buildValueFromString(String valueStr)
  {
    Object ret=null;
    if (_valueClass==VALUE_CLASS.INTEGER)
    {
      if (valueStr.startsWith("+")) valueStr=valueStr.substring(1);
      ret=NumericTools.parseInteger(valueStr,true);
    }
    else if (_valueClass==VALUE_CLASS.FLOAT)
    {
      ret=NumericTools.parseFloat(valueStr,true);
    }
    else if (_valueClass==VALUE_CLASS.PERCENTAGE)
    {
      if (valueStr.endsWith("%")) valueStr=valueStr.substring(0,valueStr.length()-1);
      ret=NumericTools.parseFloat(valueStr,true);
    }
    else
    {
      ret=valueStr;
    }
    return ret;
  }

  /**
   * Get a bonus type class instance by its key.
   * @param key Key to search.
   * @return A bonus type or <code>null</code> if not found.
   */
  public static BonusType getByKey(String key)
  {
    BonusType ret=_instancesByKey.get(key);
    return ret;
  }

  /**
   * Get a bonus type instance by a name.
   * @param name Label to search.
   * @return A bonus type or <code>null</code> if not found.
   */
  public static BonusType getByName(String name)
  {
    BonusType ret=_instances.get(name);
    return ret;
  }

  /**
   * Get all bonus types.
   * @return A list of all bonus types.
   */
  public static List<BonusType> getAll()
  {
    return _all;
  }

  @Override
  public String toString()
  {
    return _key;
  }
}
