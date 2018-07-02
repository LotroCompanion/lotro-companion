package delta.games.lotro.gui.items.chooser;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.filters.ArmourTypeFilter;
import delta.games.lotro.lore.items.filters.CharacterProficienciesFilter;
import delta.games.lotro.lore.items.filters.EssenceTierFilter;
import delta.games.lotro.lore.items.filters.ItemLevelFilter;
import delta.games.lotro.lore.items.filters.ItemNameFilter;
import delta.games.lotro.lore.items.filters.ItemQualityFilter;
import delta.games.lotro.lore.items.filters.ItemRequiredClassFilter;
import delta.games.lotro.lore.items.filters.ItemRequiredLevelFilter;
import delta.games.lotro.lore.items.filters.ItemStatFilter;
import delta.games.lotro.lore.items.filters.LegendaryItemFilter;
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
  private ItemRequiredClassFilter _classFilter;
  private CharacterProficienciesFilter _proficienciesFilter;
  private ItemRequiredLevelFilter _levelFilter;
  private EssenceTierFilter _essenceTierFilter;
  private ItemNameFilter _nameFilter;
  private ItemQualityFilter _qualityFilter;
  private LegendaryItemFilter _legendaryFilter;
  private WeaponTypeFilter _weaponTypeFilter;
  private ArmourTypeFilter _armourTypeFilter;
  private ArmourTypeFilter _shieldTypeFilter;
  private ItemStatFilter _statFilter;
  private ItemLevelFilter _itemLevelFilter;

  /**
   * Constructor.
   * @param cfg Configuration.
   * @param character Targeted character (may be <code>null</code>).
   */
  public ItemChooserFilter(ItemFilterConfiguration cfg, CharacterSummary character)
  {
    _cfg=cfg;
    List<Filter<Item>> filters=new ArrayList<Filter<Item>>();
    // Character proficiencies
    if (character!=null)
    {
      CharacterClass characterClass=character.getCharacterClass();
      int level=character.getLevel();
      // Class
      boolean useClass=cfg.hasComponent(ItemChooserFilterComponent.CHAR_CLASS);
      if (useClass)
      {
        _classFilter=new ItemRequiredClassFilter(characterClass,false);
        filters.add(_classFilter);
      }
      // Proficiencies
      boolean useProficiencies=cfg.hasComponent(ItemChooserFilterComponent.CHAR_PROFICIENCIES);
      if (useProficiencies)
      {
        _proficienciesFilter=new CharacterProficienciesFilter(characterClass,level);
        filters.add(_proficienciesFilter);
      }
      // Level
      boolean useLevel=cfg.hasComponent(ItemChooserFilterComponent.CHAR_LEVEL);
      if (useLevel)
      {
        _levelFilter=new ItemRequiredLevelFilter(level);
        filters.add(_levelFilter);
      }
    }
    // Tier
    boolean useTier=cfg.hasComponent(ItemChooserFilterComponent.TIER);
    if (useTier)
    {
      _essenceTierFilter=new EssenceTierFilter();
      filters.add(_essenceTierFilter);
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
    // Legendary
    _legendaryFilter=new LegendaryItemFilter(null);
    filters.add(_legendaryFilter);
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
   * Get the managed essence tier filter.
   * @return a filter.
   */
  public EssenceTierFilter getEssenceTierFilter()
  {
    return _essenceTierFilter;
  }

  /**
   * Get the managed class filter.
   * @return a class filter or <code>null</code>.
   */
  public ItemRequiredClassFilter getClassFilter()
  {
    return _classFilter;
  }

  /**
   * Get the managed proficiencies filter.
   * @return a proficiencies filter or <code>null</code>.
   */
  public CharacterProficienciesFilter getProficienciesFilter()
  {
    return _proficienciesFilter;
  }

  /**
   * Get the managed level filter.
   * @return a level filter or <code>null</code>.
   */
  public ItemRequiredLevelFilter getLevelFilter()
  {
    return _levelFilter;
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
   * Get the filter on the 'legendary' attribute of items.
   * @return A filter for the 'legendary' attribute of items.
   */
  public LegendaryItemFilter getLegendaryFilter()
  {
    return _legendaryFilter;
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

  @Override
  public boolean accept(Item item)
  {
    return _filter.accept(item);
  }
}
