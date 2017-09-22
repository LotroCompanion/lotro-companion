package delta.games.lotro.gui.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.Weapon;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.comparators.ArmourTypeComparator;
import delta.games.lotro.lore.items.comparators.WeaponTypeComparator;

/**
 * Configuration of an items filter.
 * @author DAM
 */
public class ItemFilterConfiguration
{
  private Set<ArmourType> _armourTypes;
  private Set<ArmourType> _shieldTypes;
  private Set<WeaponType> _weaponTypes;

  /**
   * Constructor.
   */
  public ItemFilterConfiguration() {
    _armourTypes=new HashSet<ArmourType>();
    _shieldTypes=new HashSet<ArmourType>();
    _weaponTypes=new HashSet<WeaponType>();
    defaultInit();
  }

  private void defaultInit() {
    for(ArmourType armourType : ArmourType.ARMOUR_TYPES) 
    {
      _armourTypes.add(armourType);
    }
    for(ArmourType shieldType : ArmourType.SHIELD_ARMOUR_TYPES) 
    {
      _shieldTypes.add(shieldType);
    }
    for(WeaponType weaponType : WeaponType.getAll()) 
    {
      _weaponTypes.add(weaponType);
    }
  }

  /**
   * Init from a collection of items.
   * @param items Items to use.
   */
  public void initFromItems(List<Item> items)
  {
    _armourTypes.clear();
    _shieldTypes.clear();
    _weaponTypes.clear();
    for(Item item : items)
    {
      // Armour
      if (item instanceof Armour)
      {
        Armour weapon=(Armour)item;
        ArmourType type=weapon.getArmourType();
        if (type!=null)
        {
          if (type.isShield()) {
            _shieldTypes.add(type);
          } else {
            _armourTypes.add(type);
          }
        }
      }
      // Weapon
      if (item instanceof Weapon)
      {
        Weapon weapon=(Weapon)item;
        WeaponType type=weapon.getWeaponType();
        if (type!=null)
        {
          _weaponTypes.add(type);
        }
      }
    }
  }

  /**
   * Get the selected weapon types.
   * @return a possibly empty but not <code>null</code> list of sorted weapon types.
   */
  public List<WeaponType> getWeaponTypes() {
    List<WeaponType> weaponTypes=new ArrayList<WeaponType>();
    weaponTypes.addAll(_weaponTypes);
    Collections.sort(weaponTypes,new WeaponTypeComparator());
    return weaponTypes;
  }

  /**
   * Get the selected armour types.
   * @return a possibly empty but not <code>null</code> list of sorted armour types.
   */
  public List<ArmourType> getArmourTypes() {
    List<ArmourType> armourTypes=new ArrayList<ArmourType>();
    armourTypes.addAll(_armourTypes);
    Collections.sort(armourTypes,new ArmourTypeComparator());
    return armourTypes;
  }

  /**
   * Get the selected shield types.
   * @return a possibly empty but not <code>null</code> list of sorted shield types.
   */
  public List<ArmourType> getShieldTypes() {
    List<ArmourType> weaponTypes=new ArrayList<ArmourType>();
    weaponTypes.addAll(_shieldTypes);
    Collections.sort(weaponTypes,new ArmourTypeComparator());
    return weaponTypes;
  }
}
