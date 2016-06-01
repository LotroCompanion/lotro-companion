package delta.games.lotro.gui.character;

import java.util.List;

import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.sort.ItemsSorter;

/**
 * Select items that fit in equipment slots.
 * @author DAM
 */
public class ItemSelection
{
  private ItemsSorter _sorter;

  /**
   * Constructor.
   */
  public ItemSelection()
  {
    _sorter=new ItemsSorter();
    List<Item> items=ItemsManager.getInstance().getAllItems();
    _sorter.sortItems(items);
  }

  /**
   * Get items that fit a slot for a given character.
   * @param c Character.
   * @param slot Targeted slot.
   * @return A list of items.
   */
  public List<Item> getItems(Character c, EQUIMENT_SLOT slot)
  {
    CharacterClass cClass=c.getCharacterClass();
    int level=c.getLevel();
    EquipmentLocation location=slot.getLocation();
    if ((location==EquipmentLocation.HEAD) || (location==EquipmentLocation.SHOULDER)
        || (location==EquipmentLocation.CHEST) || (location==EquipmentLocation.BACK)
        || (location==EquipmentLocation.LEGS) || (location==EquipmentLocation.FEET)
        || (location==EquipmentLocation.HAND))
    {
      return _sorter.buildArmoursList(cClass,level,location);
    }
    if ((location==EquipmentLocation.EAR) || (location==EquipmentLocation.NECK)
        || (location==EquipmentLocation.WRIST) || (location==EquipmentLocation.FINGER)
        || (location==EquipmentLocation.POCKET))
    {
      return _sorter.getJewels(location);
    }
    if ((location==EquipmentLocation.MAIN_HAND) || (location==EquipmentLocation.OFF_HAND)
        || (location==EquipmentLocation.RANGED_ITEM))
    {
      List<Item> weapons=_sorter.buildWeaponsList(cClass,level,location);
      if (location==EquipmentLocation.OFF_HAND)
      {
        List<Item> shields=_sorter.buildShieldsList(cClass,level);
        weapons.addAll(shields);
      }
      return weapons;
    }
    // TODO tool, class slot
    return null;
  }
}
