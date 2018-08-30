package delta.games.lotro.gui.character.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.location.StorageLocation;
import delta.games.lotro.character.storage.location.comparators.LocationComparator;
import delta.games.lotro.common.owner.Owner;
import delta.games.lotro.common.owner.comparators.OwnerComparator;

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
   * Constructor.
   */
  public StorageFilterConfiguration()
  {
    _owners=new HashSet<Owner>();
    _locations=new HashSet<StorageLocation>();
  }

  /**
   * Set owners/locations from the storage items to display.
   * @param items Items to show.
   */
  public void setItems(List<StoredItem> items)
  {
    _owners.clear();
    _locations.clear();
    for(StoredItem item : items)
    {
      _owners.add(item.getOwner());
      _locations.add(item.getLocation());
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
}
