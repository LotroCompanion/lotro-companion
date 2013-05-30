package delta.games.lotro.lore.items;

import java.util.HashMap;

/**
 * Armour type.
 * @author DAM
 */
public class ArmourType
{
  private static HashMap<String,ArmourType> _map=new HashMap<String,ArmourType>();
  private static HashMap<String,ArmourType> _keyMap=new HashMap<String,ArmourType>();

  /**
   * Heavy.
   */
  public static final ArmourType HEAVY=new ArmourType("HEAVY","Heavy Armour","Heavy");
  /**
   * Medium.
   */
  public static final ArmourType MEDIUM=new ArmourType("MEDIUM","Medium Armour","Medium");
  /**
   * Light.
   */
  public static final ArmourType LIGHT=new ArmourType("LIGHT","Light Armour","Light","Cloak");
  /**
   * Warden's Shield.
   */
  public static final ArmourType WARDEN_SHIELD=new ArmourType("WARDEN_SHIELD","Warden's Shield");
  /**
   * Heavy Shield.
   */
  public static final ArmourType HEAVY_SHIELD=new ArmourType("HEAVY_SHIELD","Heavy Shield");
  /**
   * Shield.
   */
  public static final ArmourType SHIELD=new ArmourType("SHIELD","Shield");
  
  private String _key;
  private String _name;
  
  private ArmourType(String key, String name, String... aliases)
  {
    _key=key;
    _keyMap.put(key,this);
    _name=name;
    _map.put(name,this);
    for(String alias : aliases) _map.put(alias,this);
  }
  
  /**
   * Get the armour type key.
   * @return A key.
   */
  public String getKey()
  {
    return _key;
  }
  
  /**
   * Get the armour type name.
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
   * Get an armour type using its name.
   * @param name Name of armour type.
   * @return An armour type instance or <code>null</code> if not found.
   */
  public static ArmourType getArmourTypeByName(String name)
  {
    return _map.get(name);
  }

  /**
   * Get an armour type using its key.
   * @param key Key of armour type.
   * @return An armour type instance or <code>null</code> if not found.
   */
  public static ArmourType getDamageTypeByKey(String key)
  {
    return _keyMap.get(key);
  }
}
