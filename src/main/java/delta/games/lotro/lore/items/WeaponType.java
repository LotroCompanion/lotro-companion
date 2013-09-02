package delta.games.lotro.lore.items;

import java.util.HashMap;

/**
 * Weapon type.
 * @author DAM
 */
public class WeaponType
{
  private static HashMap<String,WeaponType> _map=new HashMap<String,WeaponType>();

  /**
   * Two-handed Sword.
   */
  public static final WeaponType TWO_HANDED_SWORD=new WeaponType("Two-handed Sword",false,true);
  /**
   * Staff.
   */
  public static final WeaponType STAFF=new WeaponType("Staff",false,true);
  /**
   * Halberd.
   */
  public static final WeaponType HALBERD=new WeaponType("Halberd",false,true);
  /**
   * Two-handed Hammer.
   */
  public static final WeaponType TWO_HANDED_HAMMER=new WeaponType("Two-handed Hammer",false,true);
  /**
   * Bow.
   */
  public static final WeaponType BOW=new WeaponType("Bow",false,false);
  /**
   * Javelin.
   */
  public static final WeaponType JAVELIN=new WeaponType("Javelin",false,true); // TODO check
  /**
   * Two-handed Club.
   */
  public static final WeaponType TWO_HANDED_CLUB=new WeaponType("Two-handed Club",false,true);
  /**
   * One-handed Hammer.
   */
  public static final WeaponType ONE_HANDED_HAMMER=new WeaponType("One-handed Hammer",false,false);
  /**
   * Spear.
   */
  public static final WeaponType SPEAR=new WeaponType("Spear",false,false); // TODO check
  /**
   * One-handed Club.
   */
  public static final WeaponType ONE_HANDED_CLUB=new WeaponType("One-handed Club",false,false);
  /**
   * One-handed Mace.
   */
  public static final WeaponType ONE_HANDED_MACE=new WeaponType("One-handed Mace",false,false);
  /**
   * Crossbow.
   */
  public static final WeaponType CROSSBOW=new WeaponType("Crossbow",true,false);
  /**
   * Dagger.
   */
  public static final WeaponType DAGGER=new WeaponType("Dagger",false,false);
  /**
   * One-handed Axe.
   */
  public static final WeaponType ONE_HANDED_AXE=new WeaponType("One-handed Axe",false,false);
  /**
   * One-handed Sword.
   */
  public static final WeaponType ONE_HANDED_SWORD=new WeaponType("One-handed Sword",false,false);
  /**
   * Two-handed Axe.
   */
  public static final WeaponType TWO_HANDED_AXE=new WeaponType("Two-handed Axe",false,true);
  /**
   * Rune-stone.
   */
  public static final WeaponType RUNE_STONE=new WeaponType("Rune-stone",false,false);
  /**
   * Thrown weapon.
   */
  public static final WeaponType THROWN_WEAPON=new WeaponType("Thrown Weapon",true,false); // TODO CHECK
  
  private String _name;
  private boolean _ranged;
  private boolean _twoHanded;
  
  private WeaponType(String name, boolean ranged, boolean twoHanded)
  {
    _name=name;
    _ranged=ranged;
    _twoHanded=twoHanded;
    _map.put(name,this);
  }
  
  /**
   * Get the weapon type name.
   * @return A name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Indicates if this weapon type is a ranged weapon or not.
   * @return <code>true</code> if it is ranged, <code>false</code> otherwise.
   */
  public boolean isRanged()
  {
    return _ranged;
  }

  /**
   * Indicates if this weapon type is a two-handed weapon or not.
   * @return <code>true</code> if it is two-handed, <code>false</code> otherwise.
   */
  public boolean isTwoHanded()
  {
    return _twoHanded;
  }

  @Override
  public String toString()
  {
    return _name;
  }

  /**
   * Get a weapon type using its name.
   * @param name Name of weapon type.
   * @return A weapon type instance or <code>null</code> if not found.
   */
  public static WeaponType getWeaponTypeByName(String name)
  {
    return _map.get(name);
  }
}
