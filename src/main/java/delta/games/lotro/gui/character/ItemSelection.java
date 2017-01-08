package delta.games.lotro.gui.character;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.CharacterData;
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
  public List<Item> getItems(CharacterData c, EQUIMENT_SLOT slot)
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
        || (location==EquipmentLocation.POCKET) || (location==EquipmentLocation.CLASS_SLOT)
        || (location==EquipmentLocation.TOOL))
    {
      return _sorter.getBySlot(location);
    }
    if ((location==EquipmentLocation.MAIN_HAND) || (location==EquipmentLocation.OFF_HAND)
        || (location==EquipmentLocation.RANGED_ITEM))
    {
      List<Item> items=_sorter.buildWeaponsList(cClass,level,location);
      if (location==EquipmentLocation.OFF_HAND)
      {
        List<Item> shields=_sorter.buildShieldsList(cClass,level);
        items.addAll(shields);
      }
      if (location==EquipmentLocation.RANGED_ITEM)
      {
        List<Item> rangedItems=_sorter.getBySlot(location);
        items.addAll(rangedItems);
      }
      return items;
    }
    return new ArrayList<Item>();
  }

  /**
   * Get a list of all essences.
   * @return a list of essence items.
   */
  public List<Item> getEssences()
  {
    return _sorter.buildEssencesList();
  }
}
