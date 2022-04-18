package delta.games.lotro.gui.lore.items.chooser;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.stats.WellKnownStat;
import delta.games.lotro.gui.lore.items.ItemColumnIds;
import delta.games.lotro.gui.lore.items.ItemInstanceColumnIds;

/**
 * Manages the columns to show for item/essence choice table.
 * @author DAM
 */
public class ItemChoiceTableColumnsManager
{
  /**
   * Get the default columns to display for the essence chooser.
   * @param characterClass Targeted class.
   * @return A list of column identifiers for the items chooser table.
   */
  public static List<String> getEssenceChoiceColumns(CharacterClass characterClass)
  {
    List<String> ret=new ArrayList<String>();
    ret.add(ItemColumnIds.ICON.name());
    ret.add(ItemColumnIds.ID.name());
    ret.add(ItemColumnIds.NAME.name());
    ret.add(ItemColumnIds.ITEM_LEVEL.name());
    ret.add(ItemColumnIds.TIER.name());
    ret.addAll(getStatColumns(characterClass));
    return ret;
  }

  /**
   * Get the default item columns to display for the item chooser.
   * @return A list of column identifiers for the items chooser table.
   */
  public static List<String> getItemChoiceItemColumns()
  {
    List<String> ret=new ArrayList<String>();
    ret.add(ItemColumnIds.ICON.name());
    ret.add(ItemColumnIds.ID.name());
    ret.add(ItemColumnIds.NAME.name());
    ret.add(ItemColumnIds.ITEM_LEVEL.name());
    return ret;
  }

  /**
   * Get the default item columns to display for the item chooser.
   * @return A list of column identifiers for the items chooser table.
   */
  public static List<String> getItemInstanceChoiceItemColumns()
  {
    List<String> ret=new ArrayList<String>();
    ret.add(ItemColumnIds.ICON.name());
    ret.add(ItemColumnIds.ID.name());
    ret.add(ItemColumnIds.NAME.name());
    ret.add(ItemInstanceColumnIds.INSTANCE_ITEM_LEVEL.name());
    return ret;
  }

  /**
   * Get the default columns to display for the item chooser.
   * @param characterClass Targeted class.
   * @param slot Targeted slot.
   * @return A list of column identifiers for the items chooser table.
   */
  public static List<String> getItemChoiceColumnsUsingClassAndSlot(CharacterClass characterClass, GearSlot slot)
  {
    List<String> ret=new ArrayList<String>();
    if ((slot==GearSlot.HEAD) || (slot==GearSlot.BREAST)
        || (slot==GearSlot.HANDS) || (slot==GearSlot.LEGS)
        || (slot==GearSlot.FEET) || (slot==GearSlot.SHOULDER))
    {
      ret.add(ItemColumnIds.SLOT_COUNT.name());
      ret.add(WellKnownStat.ARMOUR.getLegacyKey());
      ret.add(ItemColumnIds.ARMOUR_TYPE.name());
    }
    else if (slot==GearSlot.BACK)
    {
      ret.add(ItemColumnIds.SLOT_COUNT.name());
      ret.add(WellKnownStat.ARMOUR.getLegacyKey());
    }
    else if (slot==GearSlot.MAIN_MELEE)
    {
      if (characterClass!=CharacterClass.RUNE_KEEPER)
      {
        ret.add(ItemColumnIds.WEAPON_TYPE.name());
      }
    }
    else if (slot==GearSlot.OTHER_MELEE)
    {
      ret.add(ItemColumnIds.WEAPON_TYPE.name());
      ret.add(WellKnownStat.ARMOUR.getLegacyKey());
      ret.add(ItemColumnIds.ARMOUR_TYPE.name());
    }
    else if (slot==GearSlot.RANGED)
    {
      ret.add(ItemColumnIds.WEAPON_TYPE.name());
    }
    else if ((slot==GearSlot.NECK) || (slot==GearSlot.POCKET)
        || (slot==GearSlot.LEFT_EAR) || (slot==GearSlot.RIGHT_EAR)
        || (slot==GearSlot.LEFT_WRIST) || (slot==GearSlot.RIGHT_WRIST)
        || (slot==GearSlot.LEFT_FINGER) || (slot==GearSlot.RIGHT_FINGER))
    {
      ret.add(ItemColumnIds.SLOT_COUNT.name());
    }
    ret.addAll(getStatColumns(characterClass));
    return ret;
  }

  private static List<String> getStatColumns(CharacterClass characterClass)
  {
    List<String> ret=new ArrayList<String>();
    // Stats
    ret.add(WellKnownStat.MORALE.getLegacyKey());
    ret.add(WellKnownStat.VITALITY.getLegacyKey());
    if ((characterClass==CharacterClass.LORE_MASTER) || (characterClass==CharacterClass.MINSTREL) || (characterClass==CharacterClass.RUNE_KEEPER))
    {
      // Will
      ret.add(WellKnownStat.WILL.getLegacyKey());
      ret.add(WellKnownStat.TACTICAL_MASTERY.getLegacyKey());
    }
    else if ((characterClass==CharacterClass.BEORNING) || (characterClass==CharacterClass.CAPTAIN)
        || (characterClass==CharacterClass.CHAMPION) || (characterClass==CharacterClass.GUARDIAN)
        || (characterClass==CharacterClass.BRAWLER))
    {
      // Might
      ret.add(WellKnownStat.MIGHT.getLegacyKey());
      ret.add(WellKnownStat.PHYSICAL_MASTERY.getLegacyKey());
    }
    else if ((characterClass==CharacterClass.BURGLAR) || (characterClass==CharacterClass.HUNTER) || (characterClass==CharacterClass.WARDEN))
    {
      // Agility
      ret.add(WellKnownStat.AGILITY.getLegacyKey());
      ret.add(WellKnownStat.PHYSICAL_MASTERY.getLegacyKey());
    }
    ret.add(WellKnownStat.CRITICAL_RATING.getLegacyKey());
    ret.add(WellKnownStat.FINESSE.getLegacyKey());
    return ret;
  }
}
