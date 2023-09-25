package delta.games.lotro.gui.lore.items.chooser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.common.enums.comparator.LotroEnumEntryNameComparator;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.ArmourTypes;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ShieldTypes;
import delta.games.lotro.lore.items.Weapon;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.comparators.ArmourTypeComparator;

/**
 * Configuration of an items filter.
 * @author DAM
 */
public class ItemFilterConfiguration
{
  private Set<ArmourType> _armourTypes;
  private Set<ArmourType> _shieldTypes;
  private Set<WeaponType> _weaponTypes;
  private Set<DamageType> _damageTypes;
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
    _damageTypes=new HashSet<DamageType>();
    _itemLevels=new HashSet<Integer>();
    defaultInit();
  }

  private void defaultInit()
  {
    int nbComponents=ItemChooserFilterComponent.values().length;
    _components=new boolean[nbComponents];
    for(ArmourType armourType : ArmourTypes.ARMOUR_TYPES) 
    {
      _armourTypes.add(armourType);
    }
    for(ArmourType shieldType : ShieldTypes.SHIELD_TYPES) 
    {
      _shieldTypes.add(shieldType);
    }
    for(WeaponType weaponType : WeaponType.getAll()) 
    {
      _weaponTypes.add(weaponType);
    }
    for(DamageType damageType : DamageType.getAll()) 
    {
      _damageTypes.add(damageType);
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
    _components[ItemChooserFilterComponent.CURRENT_CHAR_CLASS.ordinal()]=true;
    _components[ItemChooserFilterComponent.CURRENT_CHAR_LEVEL.ordinal()]=true;
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
    _components[ItemChooserFilterComponent.WEAPON_TYPE.ordinal()]=true;
    _components[ItemChooserFilterComponent.DAMAGE_TYPE.ordinal()]=true;
    _components[ItemChooserFilterComponent.SLAYER_GENUS.ordinal()]=true;
    _components[ItemChooserFilterComponent.ARMOUR_TYPE.ordinal()]=true;
    _components[ItemChooserFilterComponent.SHIELD_TYPE.ordinal()]=true;
    // Requirements for the current character: class, proficiencies, level
    // TODO: add faction requirements
    _components[ItemChooserFilterComponent.CURRENT_CHAR_CLASS.ordinal()]=true;
    _components[ItemChooserFilterComponent.CURRENT_CHAR_PROFICIENCIES.ordinal()]=true;
    _components[ItemChooserFilterComponent.CURRENT_CHAR_LEVEL.ordinal()]=true;
    // Item level range
    _components[ItemChooserFilterComponent.ITEM_LEVEL.ordinal()]=true;
  }

  /**
   * Configure the filter for the items database explorer.
   */
  public void forItemExplorerFilter()
  {
    boolean isLive=LotroCoreConfig.isLive();
    if (isLive)
    {
      _components[ItemChooserFilterComponent.TIER.ordinal()]=true;
    }
    _components[ItemChooserFilterComponent.NAME.ordinal()]=true;
    _components[ItemChooserFilterComponent.QUALITY.ordinal()]=true;
    _components[ItemChooserFilterComponent.CATEGORY.ordinal()]=true;
    if (isLive)
    {
      _components[ItemChooserFilterComponent.LEGENDARY.ordinal()]=true;
    }
    _components[ItemChooserFilterComponent.STAT.ordinal()]=true;
    _components[ItemChooserFilterComponent.LOCATION.ordinal()]=true;
    _components[ItemChooserFilterComponent.WEAPON_TYPE.ordinal()]=true;
    _components[ItemChooserFilterComponent.DAMAGE_TYPE.ordinal()]=true;
    _components[ItemChooserFilterComponent.SLAYER_GENUS.ordinal()]=true;
    _components[ItemChooserFilterComponent.ARMOUR_TYPE.ordinal()]=true;
    _components[ItemChooserFilterComponent.SHIELD_TYPE.ordinal()]=true;
    _components[ItemChooserFilterComponent.GENERIC_CHARACTER_CLASS.ordinal()]=true;
    _components[ItemChooserFilterComponent.GENERIC_CHARACTER_RACE.ordinal()]=true;
    _components[ItemChooserFilterComponent.ITEM_LEVEL.ordinal()]=true;
    if (isLive)
    {
      _components[ItemChooserFilterComponent.SCALABLE.ordinal()]=true;
    }
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
  public void initFromItems(List<? extends Item> items)
  {
    _armourTypes.clear();
    _shieldTypes.clear();
    _weaponTypes.clear();
    _damageTypes.clear();
    _itemLevels.clear();
    Set<ArmourType> shieldTypes=new HashSet<ArmourType>();
    for(ArmourType shieldType : ShieldTypes.SHIELD_TYPES)
    {
      shieldTypes.add(shieldType);
    }
    for(Item item : items)
    {
      // Armour
      if (item instanceof Armour)
      {
        Armour armour=(Armour)item;
        ArmourType type=armour.getArmourType();
        if (type!=null)
        {
          if (shieldTypes.contains(type))
          {
            _shieldTypes.add(type);
          }
          else
          {
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
        DamageType damageType=weapon.getDamageType();
        if (damageType!=null)
        {
          _damageTypes.add(damageType);
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
    Collections.sort(weaponTypes,new LotroEnumEntryNameComparator<WeaponType>());
    return weaponTypes;
  }

  /**
   * Get the selected damage types.
   * @return a possibly empty but not <code>null</code> list of sorted damage types.
   */
  public List<DamageType> getDamageTypes()
  {
    List<DamageType> damageTypes=new ArrayList<DamageType>();
    damageTypes.addAll(_damageTypes);
    Collections.sort(damageTypes,new LotroEnumEntryNameComparator<DamageType>());
    return damageTypes;
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
    List<ArmourType> shieldTypes=new ArrayList<ArmourType>();
    shieldTypes.addAll(_shieldTypes);
    Collections.sort(shieldTypes,new ArmourTypeComparator());
    return shieldTypes;
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
