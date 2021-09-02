package delta.games.lotro.gui.lore.items.chooser;

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
  private Set<Integer> _itemLevels;
  private boolean[] _components;

  /**
   * Constructor.
   */
  public ItemFilterConfiguration()
  {
    _armourTypes=new HashSet<ArmourType>();
    _shieldTypes=new HashSet<ArmourType>();
    _weaponTypes=new HashSet<WeaponType>();
    _itemLevels=new HashSet<Integer>();
    defaultInit();
  }

  private void defaultInit()
  {
    int nbComponents=ItemChooserFilterComponent.values().length;
    _components=new boolean[nbComponents];
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
    for(int i=1;i<400;i++)
    {
      _itemLevels.add(Integer.valueOf(i));
    }
  }

  /**
   * Configure the filter for choosing an item from stash.
   */
  public void forStashFilter()
  {
    _components[ItemChooserFilterComponent.NAME.ordinal()]=true;
    _components[ItemChooserFilterComponent.QUALITY.ordinal()]=true;
  }

  /**
   * Configure the filter for choosing an essence.
   */
  public void forEssenceFilter()
  {
    _components[ItemChooserFilterComponent.TIER.ordinal()]=true;
    _components[ItemChooserFilterComponent.NAME.ordinal()]=true;
    _components[ItemChooserFilterComponent.QUALITY.ordinal()]=true;
    _components[ItemChooserFilterComponent.STAT.ordinal()]=true;
    _components[ItemChooserFilterComponent.CHAR_CLASS.ordinal()]=true;
    _components[ItemChooserFilterComponent.CHAR_LEVEL.ordinal()]=true;
    _components[ItemChooserFilterComponent.ITEM_LEVEL.ordinal()]=true;
  }

  /**
   * Configure the filter for choosing an item from the whole items database.
   */
  public void forItemFilter()
  {
    _components[ItemChooserFilterComponent.NAME.ordinal()]=true;
    _components[ItemChooserFilterComponent.QUALITY.ordinal()]=true;
    _components[ItemChooserFilterComponent.LEGENDARY.ordinal()]=true;
    _components[ItemChooserFilterComponent.STAT.ordinal()]=true;
    _components[ItemChooserFilterComponent.ARMOUR_TYPE.ordinal()]=true;
    _components[ItemChooserFilterComponent.WEAPON_TYPE.ordinal()]=true;
    _components[ItemChooserFilterComponent.SHIELD_TYPE.ordinal()]=true;
    // Requirements for the current character: class, proficiencies, level
    // TODO: add faction requirements
    _components[ItemChooserFilterComponent.CHAR_CLASS.ordinal()]=true;
    _components[ItemChooserFilterComponent.CHAR_PROFICIENCIES.ordinal()]=true;
    _components[ItemChooserFilterComponent.CHAR_LEVEL.ordinal()]=true;
    // Item level range
    _components[ItemChooserFilterComponent.ITEM_LEVEL.ordinal()]=true;
  }

  /**
   * Configure the filter for the items database explorer.
   */
  public void forItemExplorerFilter()
  {
    _components[ItemChooserFilterComponent.NAME.ordinal()]=true;
    _components[ItemChooserFilterComponent.QUALITY.ordinal()]=true;
    _components[ItemChooserFilterComponent.CATEGORY.ordinal()]=true;
    _components[ItemChooserFilterComponent.LEGENDARY.ordinal()]=true;
    _components[ItemChooserFilterComponent.STAT.ordinal()]=true;
    _components[ItemChooserFilterComponent.ARMOUR_TYPE.ordinal()]=true;
    _components[ItemChooserFilterComponent.WEAPON_TYPE.ordinal()]=true;
    _components[ItemChooserFilterComponent.SHIELD_TYPE.ordinal()]=true;
    _components[ItemChooserFilterComponent.CHARACTER_CLASS.ordinal()]=true;
    _components[ItemChooserFilterComponent.CHARACTER_RACE.ordinal()]=true;
    _components[ItemChooserFilterComponent.ITEM_LEVEL.ordinal()]=true;
  }

  /**
   * Indicates if the specified component is enabled or not.
   * @param component Targeted component.
   * @return <code>true</code> if it is, <code>false</code> otherwise.
   */
  public boolean hasComponent(ItemChooserFilterComponent component)
  {
    return _components[component.ordinal()];
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
    _itemLevels.clear();
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
      // Item Level
      Integer itemLevel=item.getItemLevel();
      if (itemLevel!=null)
      {
        _itemLevels.add(itemLevel);
      }
    }
  }

  /**
   * Get the selected weapon types.
   * @return a possibly empty but not <code>null</code> list of sorted weapon types.
   */
  public List<WeaponType> getWeaponTypes()
  {
    List<WeaponType> weaponTypes=new ArrayList<WeaponType>();
    weaponTypes.addAll(_weaponTypes);
    Collections.sort(weaponTypes,new WeaponTypeComparator());
    return weaponTypes;
  }

  /**
   * Get the selected armour types.
   * @return a possibly empty but not <code>null</code> list of sorted armour types.
   */
  public List<ArmourType> getArmourTypes()
  {
    List<ArmourType> armourTypes=new ArrayList<ArmourType>();
    armourTypes.addAll(_armourTypes);
    Collections.sort(armourTypes,new ArmourTypeComparator());
    return armourTypes;
  }

  /**
   * Get the selected shield types.
   * @return a possibly empty but not <code>null</code> list of sorted shield types.
   */
  public List<ArmourType> getShieldTypes()
  {
    List<ArmourType> weaponTypes=new ArrayList<ArmourType>();
    weaponTypes.addAll(_shieldTypes);
    Collections.sort(weaponTypes,new ArmourTypeComparator());
    return weaponTypes;
  }

  /**
   * Get the possible item levels.
   * @return A set of item levels.
   */
  public Set<Integer> getItemLevels()
  {
    return _itemLevels;
  }
}
