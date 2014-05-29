package delta.games.lotro.lore.items.io.tulkas;

import delta.games.lotro.lore.items.EquipmentLocation;

/**
 * Constants for Tulkas database.
 * @author DAM
 */
public class TulkasConstants
{
  /**
   * Name of bonuses.
   */
  public static String[] BONUS_NAMES=new String[] {
      "???", // 0
      "Maximum Morale", // 1
      "in-Combat Morale Regen", // 2
      "non-Combat Morale Regen", // 3
      "Maximum Power", // 4
      "in-Combat Power Regen", // 5
      "non-Combat Power Regen", // 6
      "Might", // 7
      "Agility",  // 8
      "Vitality", // 9
      "Will",     // 10
      "Fate",     // 11
      "Critical Rating", // 12
      "Finesse Rating", // 13
      "Physical Mastery Rating", // 14
      "Tactical Mastery Rating", // 15
      "Resistance Rating", // 16
      "Critical Defence", // 17
      "Incoming Healing Rating",
      "Block Rating",
      "Parry Rating",
      "Evade Rating",
      "Melee Defence",
      "Ranged Defence", // 23
      "Tactical Defence",
      "Physical Mitigation",
      "Tactical Mitigation",
      "Audacity", // 27
      "Stealth Level",
      "Tactical Critical Multiplier", // 29
      // Not in version 2:
      "Ranged Offence Rating",
      "All Skill Inductions"
    };

  /**
   * Indicates if this equipment location is for an armor.
   * @param loc Location to test.
   * @return <code>true</code> if it is, <code>false</code> otherwise.
   */
  public static  boolean isArmor(EquipmentLocation loc)
  {
    if (loc==EquipmentLocation.HEAD) return true;
    if (loc==EquipmentLocation.HAND) return true;
    if (loc==EquipmentLocation.CHEST) return true;
    if (loc==EquipmentLocation.BACK) return true;
    if (loc==EquipmentLocation.LEGS) return true;
    if (loc==EquipmentLocation.FEET) return true;
    if (loc==EquipmentLocation.SHIELD) return true;
    if (loc==EquipmentLocation.SHOULDER) return true;
    return false;
  }
}
