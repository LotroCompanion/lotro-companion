package delta.games.lotro.lore.items;

import java.util.HashMap;

/**
 * Equipment location.
 * @author DAM
 */
public class EquipmentLocation
{
  private static HashMap<String,EquipmentLocation> _instances=new HashMap<String,EquipmentLocation>();
  private String _label;

  /**
   * Head.
   */
  public static final EquipmentLocation HEAD=new EquipmentLocation("Head");
  /**
   * Hand (gloves).
   */
  public static final EquipmentLocation HAND=new EquipmentLocation("Hand");
  /**
   * Wrist.
   */
  public static final EquipmentLocation WRIST=new EquipmentLocation("Wrist");
  /**
   * Melee Weapon.
   */
  public static final EquipmentLocation MELEE_WEAPON=new EquipmentLocation("Melee Weapon");
  /**
   * Ranged Weapon.
   */
  public static final EquipmentLocation RANGED_WEAPON=new EquipmentLocation("Ranged Weapon");
  /**
   * Pocket.
   */
  public static final EquipmentLocation POCKET=new EquipmentLocation("Pocket");
  /**
   * Ear.
   */
  public static final EquipmentLocation EAR=new EquipmentLocation("Ear");
  /**
   * Chest.
   */
  public static final EquipmentLocation CHEST=new EquipmentLocation("Chest");
  /**
   * Neck.
   */
  public static final EquipmentLocation NECK=new EquipmentLocation("Neck");
  /**
   * Finger.
   */
  public static final EquipmentLocation FINGER=new EquipmentLocation("Finger");
  /**
   * Back.
   */
  public static final EquipmentLocation BACK=new EquipmentLocation("Back");
  /**
   * Leg.
   */
  public static final EquipmentLocation LEGS=new EquipmentLocation("Leg");
  /**
   * Feet.
   */
  public static final EquipmentLocation FEET=new EquipmentLocation("Feet");
  /**
   * Shield.
   */
  public static final EquipmentLocation SHIELD=new EquipmentLocation("Shield");
  /**
   * Shoulder.
   */
  public static final EquipmentLocation SHOULDER=new EquipmentLocation("Shoulder");
  /**
   * Rune-stone.
   */
  public static final EquipmentLocation RUNE_STONE=new EquipmentLocation("Rune-stone");

  private EquipmentLocation(String label, String... aliases)
  {
    _label=label;
    _instances.put(_label,this);
    for(String alias:aliases)
    {
      _instances.put(alias,this);
    }
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

  @Override
  public String toString()
  {
    return _label;
  }
}
