package delta.games.lotro.gui.lore.items.chooser;

import delta.common.utils.BooleanTools;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatsRegistry;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.filters.ArmourTypeFilter;
import delta.games.lotro.lore.items.filters.CharacterProficienciesFilter;
import delta.games.lotro.lore.items.filters.EssenceTierFilter;
import delta.games.lotro.lore.items.filters.ItemCharacterLevelFilter;
import delta.games.lotro.lore.items.filters.ItemLevelFilter;
import delta.games.lotro.lore.items.filters.ItemNameFilter;
import delta.games.lotro.lore.items.filters.ItemQualityFilter;
import delta.games.lotro.lore.items.filters.ItemRequiredClassFilter;
import delta.games.lotro.lore.items.filters.ItemStatFilter;
import delta.games.lotro.lore.items.filters.LegendaryItemFilter;
import delta.games.lotro.lore.items.filters.WeaponTypeFilter;

/**
 * I/O methods for the item chooser filter.
 * @author DAM
 */
public class ItemChooserFilterIo
{
  private static final String CLASS_FILTER_ENABLED="classFilterEnabled";
  private static final String PROFICIENCIES_FILTER_ENABLED="proficienciesFilterEnabled";
  private static final String LEVEL_FILTER_ENABLED="levelFilterEnabled";
  private static final String ESSENCE_TIER="essenceTier";
  private static final String NAME_PATTERN="namePattern";
  private static final String QUALITY="quality";
  private static final String LEGENDARY="legendary";
  private static final String WEAPON_TYPE="weaponType";
  private static final String ARMOUR_TYPE="armourType";
  private static final String SHIELD_TYPE="shieldType";
  private static final String STAT_SEED="stat.";
  private static final String MIN_ITEM_LEVEL="minItemLevel";
  private static final String MAX_ITEM_LEVEL="maxItemLevel";

  /**
   * Load filter data from the given properties.
   * @param filter Filter to update.
   * @param props Properties to load from.
   */
  public static void loadFrom(ItemChooserFilter filter, TypedProperties props)
  {
    if (props==null)
    {
      return;
    }
    // Class
    ItemRequiredClassFilter classFilter=filter.getClassFilter();
    if (classFilter!=null)
    {
      boolean enabled=props.getBooleanProperty(CLASS_FILTER_ENABLED,false);
      classFilter.setEnabled(enabled);
    }
    // Proficiencies
    CharacterProficienciesFilter proficienciesFilter=filter.getProficienciesFilter();
    if (proficienciesFilter!=null)
    {
      boolean enabled=props.getBooleanProperty(PROFICIENCIES_FILTER_ENABLED,false);
      proficienciesFilter.setEnabled(enabled);
    }
    // Level
    ItemCharacterLevelFilter levelFilter=filter.getLevelFilter();
    if (levelFilter!=null)
    {
      boolean enabled=props.getBooleanProperty(LEVEL_FILTER_ENABLED,false);
      levelFilter.setEnabled(enabled);
    }
    // Essence Tier
    EssenceTierFilter tierFilter=filter.getEssenceTierFilter();
    if (tierFilter!=null)
    {
      Integer tier=props.getIntegerProperty(ESSENCE_TIER);
      tierFilter.setTier(tier);
    }
    // Name
    ItemNameFilter nameFilter=filter.getNameFilter();
    if (nameFilter!=null)
    {
      String namePattern=props.getStringProperty(NAME_PATTERN,null);
      nameFilter.setPattern(namePattern);
    }
    // Quality
    ItemQualityFilter qualityFilter=filter.getQualityFilter();
    if (qualityFilter!=null)
    {
      String qualityKey=props.getStringProperty(QUALITY,null);
      ItemQuality quality=ItemQuality.fromCode(qualityKey);
      qualityFilter.setQuality(quality);
    }
    // Legendary
    LegendaryItemFilter legendaryFilter=filter.getLegendaryFilter();
    if (legendaryFilter!=null)
    {
      String legendaryKey=props.getStringProperty(LEGENDARY,null);
      Boolean legendary=null;
      if (legendaryKey!=null)
      {
        legendary=BooleanTools.parseBoolean(legendaryKey);
      }
      legendaryFilter.setLegendary(legendary);
    }
    // Weapon type
    WeaponTypeFilter weaponTypeFilter=filter.getWeaponTypeFilter();
    if (weaponTypeFilter!=null)
    {
      String weaponTypeKey=props.getStringProperty(WEAPON_TYPE,null);
      WeaponType weaponType=WeaponType.getWeaponTypeByKey(weaponTypeKey);
      weaponTypeFilter.setWeaponType(weaponType);
    }
    // Armour type
    ArmourTypeFilter armourTypeFilter=filter.getArmourTypeFilter();
    if (armourTypeFilter!=null)
    {
      String armourTypeKey=props.getStringProperty(ARMOUR_TYPE,null);
      ArmourType armourType=ArmourType.getArmourTypeByKey(armourTypeKey);
      armourTypeFilter.setArmourType(armourType);
    }
    // Shield type
    ArmourTypeFilter shieldTypeFilter=filter.getShieldTypeFilter();
    if (shieldTypeFilter!=null)
    {
      String shieldTypeKey=props.getStringProperty(SHIELD_TYPE,null);
      ArmourType shieldType=ArmourType.getArmourTypeByKey(shieldTypeKey);
      shieldTypeFilter.setArmourType(shieldType);
    }
    // Stats
    ItemStatFilter statFilter=filter.getStatFilter();
    if (statFilter!=null)
    {
      int nbStats=statFilter.getNbItems();
      for(int i=0;i<nbStats;i++)
      {
        String statKey=STAT_SEED+i;
        String statStr=props.getStringProperty(statKey,null);
        StatDescription stat=StatsRegistry.getInstance().getByKey(statStr);
        statFilter.setStat(i,stat);
      }
    }
    // Item level
    ItemLevelFilter itemLevelFilter=filter.getItemLevelFilter();
    if (itemLevelFilter!=null)
    {
      Integer minLevel=props.getIntegerProperty(MIN_ITEM_LEVEL);
      Integer maxLevel=props.getIntegerProperty(MAX_ITEM_LEVEL);
      itemLevelFilter.setRange(minLevel,maxLevel);
    }
  }

  /**
   * Save filter data to the given properties.
   * @param filter Source filter.
   * @param props Properties to update.
   */
  public static void saveTo(ItemChooserFilter filter, TypedProperties props)
  {
    // Class
    ItemRequiredClassFilter classFilter=filter.getClassFilter();
    if (classFilter!=null)
    {
      props.setStringProperty(CLASS_FILTER_ENABLED,Boolean.toString(classFilter.isEnabled()));
    }
    // Proficiencies
    CharacterProficienciesFilter proficienciesFilter=filter.getProficienciesFilter();
    if (proficienciesFilter!=null)
    {
      props.setStringProperty(PROFICIENCIES_FILTER_ENABLED,Boolean.toString(proficienciesFilter.isEnabled()));
    }
    // Level
    ItemCharacterLevelFilter levelFilter=filter.getLevelFilter();
    if (levelFilter!=null)
    {
      props.setStringProperty(LEVEL_FILTER_ENABLED,Boolean.toString(levelFilter.isEnabled()));
    }
    // Essence Tier
    EssenceTierFilter tierFilter=filter.getEssenceTierFilter();
    if (tierFilter!=null)
    {
      Integer tier=tierFilter.getTier();
      if (tier!=null)
      {
        props.setIntProperty(ESSENCE_TIER,tier.intValue());
      }
      else
      {
        props.removeProperty(ESSENCE_TIER);
      }
    }
    // Name
    ItemNameFilter nameFilter=filter.getNameFilter();
    if (nameFilter!=null)
    {
      String namePattern=nameFilter.getPattern();
      props.setStringProperty(NAME_PATTERN,namePattern);
    }
    // Quality
    ItemQualityFilter qualityFilter=filter.getQualityFilter();
    if (qualityFilter!=null)
    {
      ItemQuality quality=qualityFilter.getQuality();
      if (quality!=null)
      {
        props.setStringProperty(QUALITY,quality.getKey());
      }
      else
      {
        props.removeProperty(QUALITY);
      }
    }
    // Legendary
    LegendaryItemFilter legendaryFilter=filter.getLegendaryFilter();
    if (legendaryFilter!=null)
    {
      Boolean legendary=legendaryFilter.getLegendary();
      if (legendary!=null)
      {
        props.setStringProperty(LEGENDARY,legendary.toString());
      }
      else
      {
        props.removeProperty(LEGENDARY);
      }
    }
    // Weapon type
    WeaponTypeFilter weaponTypeFilter=filter.getWeaponTypeFilter();
    if (weaponTypeFilter!=null)
    {
      WeaponType weaponType=weaponTypeFilter.getWeaponType();
      if (weaponType!=null)
      {
        props.setStringProperty(WEAPON_TYPE,weaponType.getKey());
      }
      else
      {
        props.removeProperty(WEAPON_TYPE);
      }
    }
    // Armour type
    ArmourTypeFilter armourTypeFilter=filter.getArmourTypeFilter();
    if (armourTypeFilter!=null)
    {
      ArmourType armourType=armourTypeFilter.getArmourType();
      if (armourType!=null)
      {
        props.setStringProperty(ARMOUR_TYPE,armourType.getKey());
      }
      else
      {
        props.removeProperty(ARMOUR_TYPE);
      }
    }
    // Shield type
    ArmourTypeFilter shieldTypeFilter=filter.getShieldTypeFilter();
    if (shieldTypeFilter!=null)
    {
      ArmourType shieldType=shieldTypeFilter.getArmourType();
      if (shieldType!=null)
      {
        props.setStringProperty(SHIELD_TYPE,shieldType.getKey());
      }
      else
      {
        props.removeProperty(SHIELD_TYPE);
      }
    }
    // Stats
    ItemStatFilter statFilter=filter.getStatFilter();
    if (statFilter!=null)
    {
      int nbStats=statFilter.getNbItems();
      for(int i=0;i<nbStats;i++)
      {
        StatDescription stat=statFilter.getStat(i);
        String statKey=STAT_SEED+i;
        if (stat!=null)
        {
          props.setStringProperty(statKey,stat.getKey());
        }
        else
        {
          props.removeProperty(statKey);
        }
      }
    }
    // Item level
    ItemLevelFilter itemLevelFilter=filter.getItemLevelFilter();
    if (itemLevelFilter!=null)
    {
      Integer minLevel=itemLevelFilter.getMinItemLevel();
      if (minLevel!=null)
      {
        props.setIntProperty(MIN_ITEM_LEVEL,minLevel.intValue());
      }
      else
      {
        props.removeProperty(MIN_ITEM_LEVEL);
      }
      Integer maxLevel=itemLevelFilter.getMaxItemLevel();
      if (maxLevel!=null)
      {
        props.setIntProperty(MAX_ITEM_LEVEL,maxLevel.intValue());
      }
      else
      {
        props.removeProperty(MAX_ITEM_LEVEL);
      }
    }
  }
}
