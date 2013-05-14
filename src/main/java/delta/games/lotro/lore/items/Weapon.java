package delta.games.lotro.lore.items;

import delta.common.utils.text.EndOfLine;

/**
 * Weapon description.
 * @author DAM
 */
public class Weapon extends Item
{
  //197 - 359 Common Damage
  private int _minDamage;
  private int _maxDamage;
  private DamageType _damageType;
  private float _dps;
  private WeaponType _type;

  /**
   * Constructor.
   */
  public Weapon()
  {
    super();
    setCategory(ItemCategory.WEAPON);
    _minDamage=0;
    _maxDamage=0;
    _damageType=DamageType.COMMON;
    _dps=0.0f;
  }

  /**
   * Get the minimum damage.
   * @return the minimum damage.
   */
  public int getMinDamage()
  {
    return _minDamage;
  }

  /**
   * Set the minimum damage.
   * @param minDamage the value to set.
   */
  public void setMinDamage(int minDamage)
  {
    _minDamage=minDamage;
  }

  /**
   * Get the maximum damage.
   * @return the maximum damage.
   */
  public int getMaxDamage()
  {
    return _maxDamage;
  }

  /**
   * Set the maximum damage.
   * @param maxDamage the value to set.
   */
  public void setMaxDamage(int maxDamage)
  {
    _maxDamage=maxDamage;
  }

  /**
   * Get the damage type.
   * @return the damage type.
   */
  public DamageType getDamageType()
  {
    return _damageType;
  }

  /**
   * Set the damage type.
   * @param damageType the damage type to set.
   */
  public void setDamageType(DamageType damageType)
  {
    _damageType=damageType;
  }

  /**
   * Get the DPS value.
   * @return the DPS value.
   */
  public float getDPS()
  {
    return _dps;
  }

  /**
   * Set the DPS value.
   * @param dps value to set.
   */
  public void setDPS(float dps)
  {
    _dps=dps;
  }
  
  /**
   * Get weapon type.
   * @return a weapon type.
   */
  public WeaponType getWeaponType()
  {
    return _type;
  }

  /**
   * Set weapon type.
   * @param type Weapon type to set.
   */
  public void setWeaponType(WeaponType type)
  {
    _type=type;
  }

  /**
   * Dump the contents of this quest as a string.
   * @return A readable string.
   */
  public String dump()
  {
    StringBuilder sb=new StringBuilder();
    String itemDump=super.dump();
    sb.append(itemDump);
    sb.append(EndOfLine.NATIVE_EOL);
    sb.append(' ').append(_type);
    sb.append(" (DPS=").append(_dps).append(')');
    sb.append(" (damage=").append(_minDamage).append('-').append(_maxDamage);
    sb.append(' ').append(_damageType).append(')');
    return sb.toString();
  }
}
