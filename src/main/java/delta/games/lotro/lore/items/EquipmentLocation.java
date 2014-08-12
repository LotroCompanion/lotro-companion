package delta.games.lotro.lore.items;

import java.util.HashMap;

/**
 * Equipment location.
 * @author DAM
 */
public class EquipmentLocation
{
  private static HashMap<String,EquipmentLocation> _instances=new HashMap<String,EquipmentLocation>();
  private static HashMap<String,EquipmentLocation> _keyMap=new HashMap<String,EquipmentLocation>();
  private String _key;
  private String _label;

  /**
   * Head.
   */
  public static final EquipmentLocation HEAD=new EquipmentLocation("HEAD","Head");
  /**
   * Hand (gloves).
   */
  public static final EquipmentLocation HAND=new EquipmentLocation("HAND","Hand");
  /**
   * Wrist.
   */
  public static final EquipmentLocation WRIST=new EquipmentLocation("WRIST","Wrist");
  /**
   * Melee Weapon.
   */
  public static final EquipmentLocation MELEE_WEAPON=new EquipmentLocation("MELEE_WEAPON","Melee Weapon");
  /**
   * Ranged Weapon.
   */
  public static final EquipmentLocation RANGED_WEAPON=new EquipmentLocation("RANGED_WEAPON","Ranged Weapon");
  /**
   * Pocket.
   */
  public static final EquipmentLocation POCKET=new EquipmentLocation("POCKET","Pocket");
  /**
   * Ear.
   */
  public static final EquipmentLocation EAR=new EquipmentLocation("EAR","Ear");
  /**
   * Chest.
   */
  public static final EquipmentLocation CHEST=new EquipmentLocation("CHEST","Chest");
  /**
   * Neck.
   */
  public static final EquipmentLocation NECK=new EquipmentLocation("NECK","Neck");
  /**
   * Finger.
   */
  public static final EquipmentLocation FINGER=new EquipmentLocation("FINGER","Finger");
  /**
   * Back.
   */
  public static final EquipmentLocation BACK=new EquipmentLocation("BACK","Back");
  /**
   * Leg.
   */
  public static final EquipmentLocation LEGS=new EquipmentLocation("LEGS","Leg");
  /**
   * Feet.
   */
  public static final EquipmentLocation FEET=new EquipmentLocation("FEET","Feet");
  /**
   * Shield.
   */
  public static final EquipmentLocation SHIELD=new EquipmentLocation("SHIELD","Shield");
  /**
   * Shoulder.
   */
  public static final EquipmentLocation SHOULDER=new EquipmentLocation("SHOULDER","Shoulder");
  /**
   * Rune-stone.
   */
  public static final EquipmentLocation RUNE_STONE=new EquipmentLocation("RUNE_STONE","Rune-stone");

  private EquipmentLocation(String key, String label, String... aliases)
  {
    _key=key;
    _keyMap.put(_key,this);
    _label=label;
    _instances.put(_label,this);
    for(String alias:aliases)
    {
      _instances.put(alias,this);
    }
  }

  /**
   * Get an internal key for this location.
   * @return A string key.
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
   * Get an equipment location instance by its name or alias.
   * @param name Name/alias to search.
   * @return An equipment location instance or <code>null</code> if not found.
   */
  public static EquipmentLocation getByName(String name)
  {
    EquipmentLocation ret=_instances.get(name);
    return ret;
  }

  /**
   * Get an equipment location using its key.
   * @param key Key of equipment location to get.
   * @return An equipment location instance or <code>null</code> if not found.
   */
  public static EquipmentLocation getByKey(String key)
  {
    return _keyMap.get(key);
  }

  @Override
  public String toString()
  {
    return _label;
  }
}
