package delta.games.lotro.gui.lore.items.chooser;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.character.BasicCharacterAttributes;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.filters.ArmourTypeFilter;
import delta.games.lotro.lore.items.filters.CharacterProficienciesFilter;
import delta.games.lotro.lore.items.filters.DamageTypeFilter;
import delta.games.lotro.lore.items.filters.ItemCharacterLevelFilter;
import delta.games.lotro.lore.items.filters.ItemClassFilter;
import delta.games.lotro.lore.items.filters.ItemEquipmentLocationFilter;
import delta.games.lotro.lore.items.filters.ItemLevelFilter;
import delta.games.lotro.lore.items.filters.ItemNameFilter;
import delta.games.lotro.lore.items.filters.ItemQualityFilter;
import delta.games.lotro.lore.items.filters.ItemRequiredClassFilter;
import delta.games.lotro.lore.items.filters.ItemRequiredRaceFilter;
import delta.games.lotro.lore.items.filters.ItemStatFilter;
import delta.games.lotro.lore.items.filters.LegendaryItemFilter;
import delta.games.lotro.lore.items.filters.ScalableItemFilter;
import delta.games.lotro.lore.items.filters.TierFilter;
import delta.games.lotro.lore.items.filters.WeaponSlayerFilter;
import delta.games.lotro.lore.items.filters.WeaponTypeFilter;

/**
 * Filter on items for the items chooser.
 * @author DAM
 */
public class ItemChooserFilter implements Filter<Item>
{
  /**
   * Number of stat filters.
   */
  public static final int NB_STATS=3;

  // Data
  private ItemFilterConfiguration _cfg;
  private Filter<Item> _filter;
  // Generic class/race filter
  private ItemRequiredClassFilter _genericClassFilter;
  private ItemRequiredRaceFilter _genericRaceFilter;
  // Class/proficiencies/level filter on CURRENT character (if any)
  private ItemRequiredClassFilter _characterClassFilter;
  private CharacterProficienciesFilter _characterProficienciesFilter;
  private ItemCharacterLevelFilter _characterLevelFilter;
  private TierFilter _tierFilter;
  private ItemNameFilter _nameFilter;
  private ItemQualityFilter _qualityFilter;
  private ItemClassFilter _categoryFilter;
  private LegendaryItemFilter _legendaryFilter;
  private ItemEquipmentLocationFilter _locationFilter;
  private WeaponTypeFilter _weaponTypeFilter;
  private DamageTypeFilter _damageTypeFilter;
  private WeaponSlayerFilter _slayerGenusFilter;
  private ArmourTypeFilter _armourTypeFilter;
  private ArmourTypeFilter _shieldTypeFilter;
  private ItemStatFilter _statFilter;
  private ItemLevelFilter _itemLevelFilter;
  private ScalableItemFilter _scalableFilter;

  /**
   * Constructor.
   * @param cfg Configuration.
   * @param attrs Attributes of toon to use (may be <code>null</code>).
   */
  public ItemChooserFilter(ItemFilterConfiguration cfg, BasicCharacterAttributes attrs)
  {
    _cfg=cfg;
    List<Filter<Item>> filters=new ArrayList<Filter<Item>>();
    // Current character requirements
    if (attrs!=null)
    {
      // - class
      ClassDescription characterClass=attrs.getCharacterClass();
      boolean useCurrentCharClass=cfg.hasComponent(ItemChooserFilterComponent.CURRENT_CHAR_CLASS);
      if (useCurrentCharClass)
      {
        _characterClassFilter=new ItemRequiredClassFilter(characterClass,false);
        filters.add(_characterClassFilter);
      }
      // - proficiencies
      int level=attrs.getLevel();
      boolean useProficiencies=cfg.hasComponent(ItemChooserFilterComponent.CURRENT_CHAR_PROFICIENCIES);
      if (useProficiencies)
      {
        _characterProficienciesFilter=new CharacterProficienciesFilter(characterClass,level);
        filters.add(_characterProficienciesFilter);
      }
      // Level
      boolean useCurrentCharLevel=cfg.hasComponent(ItemChooserFilterComponent.CURRENT_CHAR_LEVEL);
      if (useCurrentCharLevel)
      {
        _characterLevelFilter=new ItemCharacterLevelFilter(level);
        filters.add(_characterLevelFilter);
      }
    }
    // Tier
    boolean useTier=cfg.hasComponent(ItemChooserFilterComponent.TIER);
    if (useTier)
    {
      _tierFilter=new TierFilter();
      filters.add(_tierFilter);
    }
    // Name
    boolean useName=cfg.hasComponent(ItemChooserFilterComponent.NAME);
    if (useName)
    {
      _nameFilter=new ItemNameFilter();
      filters.add(_nameFilter);
    }
    // Quality
    boolean useQuality=cfg.hasComponent(ItemChooserFilterComponent.QUALITY);
    if (useQuality)
    {
      _qualityFilter=new ItemQualityFilter(null);
      filters.add(_qualityFilter);
    }
    // Category
    boolean useCategory=cfg.hasComponent(ItemChooserFilterComponent.CATEGORY);
    if (useCategory)
    {
      _categoryFilter=new ItemClassFilter(null);
      filters.add(_categoryFilter);
    }
    // Legendary
    _legendaryFilter=new LegendaryItemFilter(null);
    filters.add(_legendaryFilter);
    // Location
    boolean useLocation=cfg.hasComponent(ItemChooserFilterComponent.LOCATION);
    if (useLocation)
    {
      _locationFilter=new ItemEquipmentLocationFilter();
      filters.add(_locationFilter);
    }
    // Weapon type
    boolean useWeaponType=cfg.hasComponent(ItemChooserFilterComponent.WEAPON_TYPE);
    if (useWeaponType)
    {
      List<WeaponType> weaponTypes=cfg.getWeaponTypes();
      if (weaponTypes.size()>0)
      {
        _weaponTypeFilter=new WeaponTypeFilter(null);
        filters.add(_weaponTypeFilter);
      }
    }
    // Damage type
    boolean useDamageType=cfg.hasComponent(ItemChooserFilterComponent.DAMAGE_TYPE);
    if (useDamageType)
    {
      List<DamageType> damageTypes=cfg.getDamageTypes();
      if (!damageTypes.isEmpty())
      {
        _damageTypeFilter=new DamageTypeFilter(null);
        filters.add(_damageTypeFilter);
      }
    }
    // Slayer genus
    boolean useSlayerGenus=cfg.hasComponent(ItemChooserFilterComponent.SLAYER_GENUS);
    if (useSlayerGenus)
    {
      _slayerGenusFilter=new WeaponSlayerFilter(null);
      filters.add(_slayerGenusFilter);
    }
    // Armour type
    boolean useArmourType=cfg.hasComponent(ItemChooserFilterComponent.ARMOUR_TYPE);
    if (useArmourType)
    {
      List<ArmourType> armourTypes=cfg.getArmourTypes();
      if (armourTypes.size()>0)
      {
        _armourTypeFilter=new ArmourTypeFilter(null);
        filters.add(_armourTypeFilter);
      }
    }
    // Shield type
    boolean useShieldType=cfg.hasComponent(ItemChooserFilterComponent.SHIELD_TYPE);
    if (useShieldType)
    {
      List<ArmourType> shieldTypes=cfg.getShieldTypes();
      if (shieldTypes.size()>0)
      {
        _shieldTypeFilter=new ArmourTypeFilter(null);
        filters.add(_shieldTypeFilter);
      }
    }
    // Stat contribution
    boolean useStat=cfg.hasComponent(ItemChooserFilterComponent.STAT);
    if (useStat)
    {
      _statFilter=new ItemStatFilter(NB_STATS);
      filters.add(_statFilter);
    }
    // Item level
    boolean useItemLevel=cfg.hasComponent(ItemChooserFilterComponent.ITEM_LEVEL);
    if (useItemLevel)
    {
      _itemLevelFilter=new ItemLevelFilter();
      filters.add(_itemLevelFilter);
    }
    // Scalable
    boolean useScalable=cfg.hasComponent(ItemChooserFilterComponent.SCALABLE);
    if (useScalable)
    {
      _scalableFilter=new ScalableItemFilter(null);
      filters.add(_scalableFilter);
    }
    // Character class
    boolean useCharacterClass=cfg.hasComponent(ItemChooserFilterComponent.GENERIC_CHARACTER_CLASS);
    if (useCharacterClass)
    {
      _genericClassFilter=new ItemRequiredClassFilter(null,false);
      filters.add(_genericClassFilter);
    }
    // Race
    boolean useCharacterRace=cfg.hasComponent(ItemChooserFilterComponent.GENERIC_CHARACTER_RACE);
    if (useCharacterRace)
    {
      _genericRaceFilter=new ItemRequiredRaceFilter(null,false);
      filters.add(_genericRaceFilter);
    }
    _filter=new CompoundFilter<Item>(Operator.AND,filters);
  }

  /**
   * Get the filter configuration.
   * @return the filter configuration.
   */
  public ItemFilterConfiguration getConfiguration()
  {
    return _cfg;
  }

  /**
   * Get the managed tier filter.
   * @return a filter.
   */
  public TierFilter getTierFilter()
  {
    return _tierFilter;
  }

  /**
   * Get the managed generic class filter.
   * @return a class filter or <code>null</code>.
   */
  public ItemRequiredClassFilter getGenericClassFilter()
  {
    return _genericClassFilter;
  }

  /**
   * Get the managed generic race filter.
   * @return a race filter or <code>null</code>.
   */
  public ItemRequiredRaceFilter getGenericRaceFilter()
  {
    return _genericRaceFilter;
  }

  /**
   * Get the managed current character class filter.
   * @return a class filter or <code>null</code>.
   */
  public ItemRequiredClassFilter getCurrentCharacterClassFilter()
  {
    return _characterClassFilter;
  }

  /**
   * Get the managed current character proficiencies filter.
   * @return a proficiencies filter or <code>null</code>.
   */
  public CharacterProficienciesFilter getCurrentCharacterProficienciesFilter()
  {
    return _characterProficienciesFilter;
  }

  /**
   * Get the managed current character level filter.
   * @return a level filter or <code>null</code>.
   */
  public ItemCharacterLevelFilter getCurrentCharacterLevelFilter()
  {
    return _characterLevelFilter;
  }

  /**
   * Get the name filter.
   * @return a name filter.
   */
  public ItemNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  /**
   * Get the item quality filter.
   * @return A filter for item quality.
   */
  public ItemQualityFilter getQualityFilter()
  {
    return _qualityFilter;
  }

  /**
   * Get the category filter.
   * @return A filter for item category.
   */
  public ItemClassFilter getCategoryFilter()
  {
    return _categoryFilter;
  }

  /**
   * Get the filter on the 'legendary' attribute of items.
   * @return A filter for the 'legendary' attribute of items.
   */
  public LegendaryItemFilter getLegendaryFilter()
  {
    return _legendaryFilter;
  }

  /**
   * Get the location filter.
   * @return a location filter or <code>null</code>.
   */
  public ItemEquipmentLocationFilter getLocationFilter()
  {
    return _locationFilter;
  }

  /**
   * Get the weapon type filter.
   * @return a weapon type filter or <code>null</code>.
   */
  public WeaponTypeFilter getWeaponTypeFilter()
  {
    return _weaponTypeFilter;
  }

  /**
   * Get the damage type filter.
   * @return a damage type filter or <code>null</code>.
   */
  public DamageTypeFilter getDamageTypeFilter()
  {
    return _damageTypeFilter;
  }

  /**
   * Get the slayer genus filter.
   * @return a slayer genus filter or <code>null</code>.
   */
  public WeaponSlayerFilter getSlayerGenusFilter()
  {
    return _slayerGenusFilter;
  }

  /**
   * Get the armour type filter.
   * @return a armour type filter or <code>null</code>.
   */
  public ArmourTypeFilter getArmourTypeFilter()
  {
    return _armourTypeFilter;
  }

  /**
   * Get the shield type filter.
   * @return a armour type or <code>null</code>.
   */
  public ArmourTypeFilter getShieldTypeFilter()
  {
    return _shieldTypeFilter;
  }

  /**
   * Get the stat filter.
   * @return A stat filter.
   */
  public ItemStatFilter getStatFilter()
  {
    return _statFilter;
  }

  /**
   * Get the item level filter.
   * @return An item level filter.
   */
  public ItemLevelFilter getItemLevelFilter()
  {
    return _itemLevelFilter;
  }

  /**
   * Get the scalable item filter.
   * @return A filter for scalable items.
   */
  public ScalableItemFilter getScalableFilter()
  {
    return _scalableFilter;
  }

  @Override
  public boolean accept(Item item)
  {
    return _filter.accept(item);
  }
}
