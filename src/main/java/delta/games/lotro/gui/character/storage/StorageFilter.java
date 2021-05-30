package delta.games.lotro.gui.character.storage;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.common.utils.collections.filters.ProxyFilter;
import delta.common.utils.collections.filters.ProxyValueResolver;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.filters.StoredItemLocationFilter;
import delta.games.lotro.character.storage.filters.StoredItemOwnerFilter;
import delta.games.lotro.common.filters.NamedFilter;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemProvider;
import delta.games.lotro.lore.items.filters.ItemQualityFilter;

/**
 * Storage filter.
 * @author DAM
 */
public class StorageFilter implements Filter<StoredItem>
{
  // Data
  private StorageFilterConfiguration _cfg;
  private Filter<StoredItem> _filter;

  private NamedFilter<Item> _nameFilter;
  private StoredItemOwnerFilter _ownerFilter;
  private StoredItemLocationFilter _locationFilter;
  private ItemQualityFilter _qualityFilter;

  /**
   * Constructor.
   */
  public StorageFilter()
  {
    _cfg=new StorageFilterConfiguration();
    List<Filter<StoredItem>> filters=new ArrayList<Filter<StoredItem>>();
    // Name
    _nameFilter=new NamedFilter<Item>();
    ProxyValueResolver<StoredItem,Item> itemResolver=new ProxyValueResolver<StoredItem,Item>()
    {
      public Item getValue(StoredItem source)
      {
        ItemProvider itemProvider=source.getItem();
        if (itemProvider!=null)
        {
          return itemProvider.getItem();
        }
        return null;
      }
    };
    ProxyFilter<StoredItem,Item> nameFilter=new ProxyFilter<StoredItem,Item>(itemResolver,_nameFilter);
    filters.add(nameFilter);
    // Owner
    _ownerFilter=new StoredItemOwnerFilter(null);
    filters.add(_ownerFilter);
    // Location
    _locationFilter=new StoredItemLocationFilter(null);
    filters.add(_locationFilter);
    // Quality
    _qualityFilter=new ItemQualityFilter(null);
    ProxyFilter<StoredItem,Item> qualityFilter=new ProxyFilter<StoredItem,Item>(itemResolver,_qualityFilter);
    filters.add(qualityFilter);
    _filter=new CompoundFilter<StoredItem>(Operator.AND,filters);
  }

  /**
   * Get the filter configuration.
   * @return the filter configuration.
   */
  public StorageFilterConfiguration getConfiguration()
  {
    return _cfg;
  }

  /**
   * Get the filter on item name.
   * @return a stored item name filter.
   */
  public NamedFilter<Item> getNameFilter()
  {
    return _nameFilter;
  }

  /**
   * Get the filter on owner.
   * @return a stored item owner filter.
   */
  public StoredItemOwnerFilter getOwnerFilter()
  {
    return _ownerFilter;
  }

  /**
   * Get the filter on location.
   * @return a stored item location filter.
   */
  public StoredItemLocationFilter getLocationFilter()
  {
    return _locationFilter;
  }

  /**
   * Get the filter on quality.
   * @return A quality filter.
   */
  public ItemQualityFilter getQualityFilter()
  {
    return _qualityFilter;
  }

  @Override
  public boolean accept(StoredItem item)
  {
    return _filter.accept(item);
  }
}
