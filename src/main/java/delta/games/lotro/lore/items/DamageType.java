package delta.games.lotro.lore.items;

import java.util.HashMap;

/**
 * Damage type.
 * @author DAM
 */
public class DamageType
{
  private static HashMap<String,DamageType> _map=new HashMap<String,DamageType>();
  private static HashMap<String,DamageType> _keyMap=new HashMap<String,DamageType>();

  /**
   * Common.
   */
  public static final DamageType COMMON=new DamageType("COMMON","Common");
  /**
   * Westernesse.
   */
  public static final DamageType WESTERNESSE=new DamageType("WESTERNESSE","Westernesse");
  /**
   * Ancient dwarf.
   */
  public static final DamageType ANCIENT_DWARF=new DamageType("ANCIENT_DWARF","Ancient Dwarf-make");
  /**
   * Beleriand.
   */
  public static final DamageType BELERIAND=new DamageType("BELERIAND","Beleriand");
  /**
   * Fire.
   */
  public static final DamageType FIRE=new DamageType("FIRE","Fire");
  /**
   * Shadow.
   */
  public static final DamageType SHADOW=new DamageType("SHADOW","Shadow");
  /**
   * Light.
   */
  public static final DamageType LIGHT=new DamageType("LIGHT","Light");
  /**
   * Lightning.
   */
  public static final DamageType LIGHTNING=new DamageType("LIGHTNING","Lightning");
  /**
   * Frost.
   */
  public static final DamageType FROST=new DamageType("FROST","Frost");
  
  private String _key;
  private String _name;
  
  private DamageType(String key, String name)
  {
    _key=key;
    _keyMap.put(key,this);
    _name=name;
    _map.put(name,this);
  }
  
  /**
   * Get the damage type key.
   * @return A key.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Get the damage type name.
   * @return A name.
   */
  public String getName()
  {
    return _name;
  }

  @Override
  public String toString()
  {
    return _name;
  }

  /**
   * Get a damage type using its name.
   * @param name Name of damage type.
   * @return A damage type instance or <code>null</code> if not found.
   */
  public static DamageType getDamageTypeByName(String name)
  {
    return _map.get(name);
  }

  /**
   * Get a damage type using its key.
   * @param key Key of damage type.
   * @return A damage type instance or <code>null</code> if not found.
   */
  public static DamageType getDamageTypeByKey(String key)
  {
    return _keyMap.get(key);
  }
}
