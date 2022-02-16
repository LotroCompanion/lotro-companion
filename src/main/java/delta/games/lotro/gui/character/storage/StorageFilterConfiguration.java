package delta.games.lotro.gui.character.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.location.StorageLocation;
import delta.games.lotro.character.storage.location.comparators.LocationComparator;
import delta.games.lotro.common.enums.ItemClass;
import delta.games.lotro.common.enums.comparator.LotroEnumEntryNameComparator;
import delta.games.lotro.common.owner.Owner;
import delta.games.lotro.common.owner.comparators.OwnerComparator;
import delta.games.lotro.lore.items.Item;

/**
 * Configuration of the storage filter.
 * @author DAM
 */
public class StorageFilterConfiguration
{
  /**
   * Possible owners.
   */
  private Set<Owner> _owners;
  /**
   * Possible locations.
   */
  private Set<StorageLocation> _locations;
  /**
   * Possible categories.
   */
  private Set<ItemClass> _categories;

  /**
   * Constructor.
   */
  public StorageFilterConfiguration()
  {
    _owners=new HashSet<Owner>();
    _locations=new HashSet<StorageLocation>();
    _categories=new HashSet<ItemClass>();
  }

  /**
   * Set owners/locations from the storage items to display.
   * @param storedItems Items to show.
   */
  public void setItems(List<StoredItem> storedItems)
  {
    _owners.clear();
    _locations.clear();
    _categories.clear();
    for(StoredItem storedItem : storedItems)
    {
      _owners.add(storedItem.getOwner());
      _locations.add(storedItem.getLocation());
      Item item=storedItem.getItem().getItem();
      _categories.add(item.getItemClass());
    }
  }

  /**
   * Get an ordered list of known owners.
   * @return A list of owners.
   */
  public List<Owner> getOwners()
  {
    List<Owner> owners=new ArrayList<Owner>();
    owners.addAll(_owners);
    Collections.sort(owners,new OwnerComparator());
    return owners;
  }

  /**
   * Get an ordered list of known locations.
   * @return A list of locations.
   */
  public List<StorageLocation> getLocations()
  {
    List<StorageLocation> locations=new ArrayList<StorageLocation>();
    locations.addAll(_locations);
    Collections.sort(locations,new LocationComparator());
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
