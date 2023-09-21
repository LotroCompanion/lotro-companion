package delta.games.lotro.gui.lore.items.chooser;

import java.util.List;

import delta.common.utils.BooleanTools;
import delta.common.utils.NumericTools;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.classes.AbstractClassDescription;
import delta.games.lotro.character.classes.ClassesManager;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RacesManager;
import delta.games.lotro.common.enums.Genus;
import delta.games.lotro.common.enums.ItemClass;
import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatsRegistry;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.ItemQuality;
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
 * I/O methods for the item chooser filter.
 * @author DAM
 */
public class ItemChooserFilterIo
{
  // Current character class/proficiences/level filters
  private static final String CURRENT_CHAR_CLASS_FILTER_ENABLED="classFilterEnabled";
  private static final String CURRENT_CHAR_PROFICIENCIES_FILTER_ENABLED="proficienciesFilterEnabled";
  private static final String CURRENT_CHAR_LEVEL_FILTER_ENABLED="levelFilterEnabled";
  private static final String TIER="tier";
  private static final String NAME_PATTERN="namePattern";
  private static final String CATEGORY="category";
  private static final String QUALITY="quality";
  private static final String LEGENDARY="legendary";
  private static final String LOCATION="location";
  private static final String WEAPON_TYPE="weaponType";
  private static final String DAMAGE_TYPE="damageType";
  private static final String SLAYER_GENUS="slayerGenus";
  private static final String ARMOUR_TYPE="armourType";
  private static final String SHIELD_TYPE="shieldType";
  private static final String STAT_SEED="stat.";
  private static final String MIN_ITEM_LEVEL="minItemLevel";
  private static final String MAX_ITEM_LEVEL="maxItemLevel";
  private static final String SCALABLE="scalable";
  // Generic class/race filters
  private static final String GENERIC_CLASS_FILTER="classFilter";
  private static final String GENERIC_RACE_FILTER="raceFilter";

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
    ItemRequiredClassFilter classFilter=filter.getCurrentCharacterClassFilter();
    if (classFilter!=null)
    {
      boolean enabled=props.getBooleanProperty(CURRENT_CHAR_CLASS_FILTER_ENABLED,classFilter.isEnabled());
      classFilter.setEnabled(enabled);
    }
    // Proficiencies
    CharacterProficienciesFilter proficienciesFilter=filter.getCurrentCharacterProficienciesFilter();
    if (proficienciesFilter!=null)
    {
      boolean enabled=props.getBooleanProperty(CURRENT_CHAR_PROFICIENCIES_FILTER_ENABLED,proficienciesFilter.isEnabled());
      proficienciesFilter.setEnabled(enabled);
    }
    // Level
    ItemCharacterLevelFilter levelFilter=filter.getCurrentCharacterLevelFilter();
    if (levelFilter!=null)
    {
      boolean enabled=props.getBooleanProperty(CURRENT_CHAR_LEVEL_FILTER_ENABLED,levelFilter.isEnabled());
      levelFilter.setEnabled(enabled);
    }
    // Tier
    TierFilter tierFilter=filter.getTierFilter();
    if (tierFilter!=null)
    {
      Integer tier=props.getIntegerProperty(TIER);
      tierFilter.setTier(tier);
    }
    // Name
    ItemNameFilter nameFilter=filter.getNameFilter();
    if (nameFilter!=null)
    {
      String namePattern=props.getStringProperty(NAME_PATTERN,null);
      nameFilter.setPattern(namePattern);
    }
    // Category
    ItemClassFilter categoryFilter=filter.getCategoryFilter();
    if (categoryFilter!=null)
    {
      String category=props.getStringProperty(CATEGORY,null);
      ItemClass itemClass=null;
      if (category!=null)
      {
        LotroEnum<ItemClass> itemClassEnum=LotroEnumsRegistry.getInstance().get(ItemClass.class);
        Integer itemClassCode=NumericTools.parseInteger(category,false);
        if (itemClassCode!=null)
        {
          itemClass=itemClassEnum.getEntry(itemClassCode.intValue());
        }
        else
        {
          itemClass=itemClassEnum.getByLabel(category);
        }
        categoryFilter.setItemClass(itemClass);
      }
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
    // Location
    ItemEquipmentLocationFilter locationFilter=filter.getLocationFilter();
    if (locationFilter!=null)
    {
      List<String> locationKeys=props.getStringList(LOCATION);
      if (locationKeys!=null)
      {
        locationFilter.loadFromString(locationKeys);
      }
    }
    // Weapon type
    WeaponTypeFilter weaponTypeFilter=filter.getWeaponTypeFilter();
    if (weaponTypeFilter!=null)
    {
      String weaponTypeKey=props.getStringProperty(WEAPON_TYPE,null);
      WeaponType weaponType=WeaponType.getWeaponTypeByKey(weaponTypeKey);
      weaponTypeFilter.setWeaponType(weaponType);
    }
    // Damage type
    DamageTypeFilter damageTypeFilter=filter.getDamageTypeFilter();
    if (damageTypeFilter!=null)
    {
      String damageTypeKey=props.getStringProperty(DAMAGE_TYPE,null);
      DamageType damageType=DamageType.getDamageTypeByKey(damageTypeKey);
      damageTypeFilter.setDamageType(damageType);
    }
    // Slayer genus
    WeaponSlayerFilter slayerGenusFilter=filter.getSlayerGenusFilter();
    if (slayerGenusFilter!=null)
    {
      int slayerGenusCode=props.getIntProperty(SLAYER_GENUS,-1);
      LotroEnum<Genus> genusEnum=LotroEnumsRegistry.getInstance().get(Genus.class);
      Genus genus=genusEnum.getEntry(slayerGenusCode);
      slayerGenusFilter.setGenus(genus);
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
    // Scalable
    ScalableItemFilter scalableFilter=filter.getScalableFilter();
    if (scalableFilter!=null)
    {
      String scalableKey=props.getStringProperty(SCALABLE,null);
      Boolean scalable=null;
      if (scalableKey!=null)
      {
        scalable=BooleanTools.parseBoolean(scalableKey);
      }
      scalableFilter.setScalable(scalable);
    }
    // Character/monster class
    ItemRequiredClassFilter genericClassFilter=filter.getGenericClassFilter();
    if (genericClassFilter!=null)
    {
      String classFilterKey=props.getStringProperty(GENERIC_CLASS_FILTER,null);
      AbstractClassDescription clazz=ClassesManager.getInstance().getClassByKey(classFilterKey);
      genericClassFilter.setClass(clazz);
    }
    // Race
    ItemRequiredRaceFilter genericRaceFilter=filter.getGenericRaceFilter();
    if (genericRaceFilter!=null)
    {
      String raceFilterKey=props.getStringProperty(GENERIC_RACE_FILTER,null);
      RaceDescription race=RacesManager.getInstance().getByKey(raceFilterKey);
      genericRaceFilter.setRace(race);
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
    ItemRequiredClassFilter classFilter=filter.getCurrentCharacterClassFilter();
    if (classFilter!=null)
    {
      props.setStringProperty(CURRENT_CHAR_CLASS_FILTER_ENABLED,Boolean.toString(classFilter.isEnabled()));
    }
    // Proficiencies
    CharacterProficienciesFilter proficienciesFilter=filter.getCurrentCharacterProficienciesFilter();
    if (proficienciesFilter!=null)
    {
      props.setStringProperty(CURRENT_CHAR_PROFICIENCIES_FILTER_ENABLED,Boolean.toString(proficienciesFilter.isEnabled()));
    }
    // Level
    ItemCharacterLevelFilter levelFilter=filter.getCurrentCharacterLevelFilter();
    if (levelFilter!=null)
    {
      props.setStringProperty(CURRENT_CHAR_LEVEL_FILTER_ENABLED,Boolean.toString(levelFilter.isEnabled()));
    }
    // Tier
    TierFilter tierFilter=filter.getTierFilter();
    if (tierFilter!=null)
    {
      Integer tier=tierFilter.getTier();
      if (tier!=null)
      {
        props.setIntProperty(TIER,tier.intValue());
      }
      else
      {
        props.removeProperty(TIER);
      }
    }
    // Name
    ItemNameFilter nameFilter=filter.getNameFilter();
    if (nameFilter!=null)
    {
      String namePattern=nameFilter.getPattern();
      props.setStringProperty(NAME_PATTERN,namePattern);
    }
    // Category
    ItemClassFilter categoryFilter=filter.getCategoryFilter();
    if (categoryFilter!=null)
    {
      ItemClass category=categoryFilter.getItemClass();
      if (category!=null)
      {
        props.setStringProperty(CATEGORY,String.valueOf(category.getCode()));
      }
      else
      {
        props.removeProperty(CATEGORY);
      }
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
    // Location
    ItemEquipmentLocationFilter locationFilter=filter.getLocationFilter();
    if (locationFilter!=null)
    {
      props.setStringList(LOCATION,locationFilter.asString());
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
    // Damage type
    DamageTypeFilter damageTypeFilter=filter.getDamageTypeFilter();
    if (damageTypeFilter!=null)
    {
      DamageType damageType=damageTypeFilter.getDamageType();
      if (damageType!=null)
      {
        props.setStringProperty(DAMAGE_TYPE,damageType.getKey());
      }
      else
      {
        props.removeProperty(DAMAGE_TYPE);
      }
    }
    // Damage type
    WeaponSlayerFilter slayerGenusFilter=filter.getSlayerGenusFilter();
    if (slayerGenusFilter!=null)
    {
      Genus genus=slayerGenusFilter.getGenus();
      if (genus!=null)
      {
        props.setIntProperty(SLAYER_GENUS,genus.getCode());
      }
      else
      {
        props.removeProperty(SLAYER_GENUS);
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
    // Scalable
    ScalableItemFilter scalableFilter=filter.getScalableFilter();
    if (scalableFilter!=null)
    {
      Boolean scalable=scalableFilter.getScalable();
      if (scalable!=null)
      {
        props.setStringProperty(SCALABLE,scalable.toString());
      }
      else
      {
        props.removeProperty(SCALABLE);
      }
    }
    // Character/monster class
    ItemRequiredClassFilter genericClassFilter=filter.getGenericClassFilter();
    if (genericClassFilter!=null)
    {
      AbstractClassDescription clazz=genericClassFilter.getCharacterClass();
      if (clazz!=null)
      {
        props.setStringProperty(GENERIC_CLASS_FILTER,clazz.getKey());
      }
      else
      {
        props.removeProperty(GENERIC_CLASS_FILTER);
      }
    }
    // Race
    ItemRequiredRaceFilter genericRaceFilter=filter.getGenericRaceFilter();
    if (genericRaceFilter!=null)
    {
      RaceDescription race=genericRaceFilter.getRace();
      if (race!=null)
      {
        props.setStringProperty(GENERIC_RACE_FILTER,race.getKey());
      }
      else
      {
        props.removeProperty(GENERIC_RACE_FILTER);
      }
    }
  }
}
