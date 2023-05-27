package delta.games.lotro.gui.character.storage.wardrobe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.character.storage.wardrobe.WardrobeItem;
import delta.games.lotro.common.enums.ItemClass;
import delta.games.lotro.common.enums.comparator.LotroEnumEntryCodeComparator;
import delta.games.lotro.common.enums.comparator.LotroEnumEntryNameComparator;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;

/**
 * Configuration of the wardrobe filter.
 * @author DAM
 */
public class WardrobeFilterConfiguration
{
  /**
   * Possible slots.
   */
  private Set<EquipmentLocation> _slots;
  /**
   * Possible categories.
   */
  private Set<ItemClass> _categories;

  /**
   * Constructor.
   */
  public WardrobeFilterConfiguration()
  {
    _slots=new HashSet<EquipmentLocation>();
    _categories=new HashSet<ItemClass>();
  }

  /**
   * Set slots/categories from the wardrobe items to display.
   * @param wardrobeItems Items to show.
   */
  public void setItems(List<WardrobeItem> wardrobeItems)
  {
    _slots.clear();
    _categories.clear();
    for(WardrobeItem wardrobeItem : wardrobeItems)
    {
      Item item=wardrobeItem.getItem().getItem();
      _slots.add(item.getEquipmentLocation());
      _categories.add(item.getItemClass());
    }
  }

  /**
   * Get an ordered list of known slots.
   * @return A list of slots.
   */
  public List<EquipmentLocation> getSlots()
  {
    List<EquipmentLocation> locations=new ArrayList<EquipmentLocation>();
    locations.addAll(_slots);
    Collections.sort(locations,new LotroEnumEntryCodeComparator<EquipmentLocation>());
    return locations;
  }

  /**
   * Get an ordered list of known categories.
   * @return A list of categories.
   */
  public List<ItemClass> getCategories()
  {
    List<ItemClass> sortedCategories=new ArrayList<ItemClass>();
    sortedCategories.addAll(_categories);
    Collections.sort(sortedCategories,new LotroEnumEntryNameComparator<ItemClass>());
    return sortedCategories;
  }
}
