package delta.games.lotro.gui.items.chooser;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.gui.items.ItemColumnIds;

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
   * Get the default columns to display for the item chooser.
   * @param characterClass Targeted class.
   * @param slot Targeted slot.
   * @return A list of column identifiers for the items chooser table.
   */
  public static List<String> getItemChoiceColumns(CharacterClass characterClass, EQUIMENT_SLOT slot)
  {
    List<String> ret=new ArrayList<String>();
    ret.add(ItemColumnIds.ICON.name());
    ret.add(ItemColumnIds.ID.name());
    ret.add(ItemColumnIds.NAME.name());
    ret.add(ItemColumnIds.ITEM_LEVEL.name());
    if ((slot==EQUIMENT_SLOT.HEAD) || (slot==EQUIMENT_SLOT.BREAST)
        || (slot==EQUIMENT_SLOT.HANDS) || (slot==EQUIMENT_SLOT.LEGS)
        || (slot==EQUIMENT_SLOT.FEET) || (slot==EQUIMENT_SLOT.SHOULDER))
    {
      ret.add(ItemColumnIds.SLOT_COUNT.name());
      ret.add(ItemColumnIds.ARMOUR.name());
      ret.add(ItemColumnIds.ARMOUR_TYPE.name());
    }
    else if (slot==EQUIMENT_SLOT.BACK)
    {
      ret.add(ItemColumnIds.SLOT_COUNT.name());
      ret.add(ItemColumnIds.ARMOUR.name());
    }
    else if (slot==EQUIMENT_SLOT.MAIN_MELEE)
    {
      if (characterClass!=CharacterClass.RUNE_KEEPER)
      {
        ret.add(ItemColumnIds.WEAPON_TYPE.name());
      }
    }
    else if (slot==EQUIMENT_SLOT.OTHER_MELEE)
    {
      ret.add(ItemColumnIds.WEAPON_TYPE.name());
      ret.add(ItemColumnIds.ARMOUR.name());
      ret.add(ItemColumnIds.ARMOUR_TYPE.name());
    }
    else if (slot==EQUIMENT_SLOT.RANGED)
    {
      ret.add(ItemColumnIds.WEAPON_TYPE.name());
    }
    else if ((slot==EQUIMENT_SLOT.NECK) || (slot==EQUIMENT_SLOT.POCKET)
        || (slot==EQUIMENT_SLOT.LEFT_EAR) || (slot==EQUIMENT_SLOT.RIGHT_EAR)
        || (slot==EQUIMENT_SLOT.LEFT_WRIST) || (slot==EQUIMENT_SLOT.RIGHT_WRIST)
        || (slot==EQUIMENT_SLOT.LEFT_FINGER) || (slot==EQUIMENT_SLOT.RIGHT_FINGER))
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
    ret.add(STAT.MORALE.name());
    ret.add(STAT.VITALITY.name());
    if ((characterClass==CharacterClass.LORE_MASTER) || (characterClass==CharacterClass.MINSTREL) || (characterClass==CharacterClass.RUNE_KEEPER))
    {
      // Will
      ret.add(STAT.WILL.name());
      ret.add(STAT.TACTICAL_MASTERY.name());
    }
    else if ((characterClass==CharacterClass.BEORNING) || (characterClass==CharacterClass.CAPTAIN)
        || (characterClass==CharacterClass.CHAMPION) || (characterClass==CharacterClass.GUARDIAN))
    {
      // Might
      ret.add(STAT.MIGHT.name());
      ret.add(STAT.PHYSICAL_MASTERY.name());
    }
    else if ((characterClass==CharacterClass.BURGLAR) || (characterClass==CharacterClass.HUNTER) || (characterClass==CharacterClass.WARDEN))
    {
      // Agility
      ret.add(STAT.AGILITY.name());
      ret.add(STAT.PHYSICAL_MASTERY.name());
    }
    ret.add(STAT.CRITICAL_RATING.name());
    ret.add(STAT.FINESSE.name());
    return ret;
  }
}
