package delta.games.lotro.gui.character.storage.wardrobe;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.common.utils.collections.filters.ProxyFilter;
import delta.common.utils.collections.filters.ProxyValueResolver;
import delta.games.lotro.character.storage.wardrobe.WardrobeItem;
import delta.games.lotro.common.filters.NamedFilter;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.filters.ItemClassFilter;
import delta.games.lotro.lore.items.filters.ItemSlotFilter;

/**
 * Wardrobe filter.
 * @author DAM
 */
public class WardrobeFilter implements Filter<WardrobeItem>
{
  // Data
  private WardrobeFilterConfiguration _cfg;
  private Filter<WardrobeItem> _filter;

  private NamedFilter<Item> _nameFilter;
  private ItemSlotFilter _slotFilter;
  private ItemClassFilter _categoryFilter;

  /**
   * Constructor.
   */
  public WardrobeFilter()
  {
    _cfg=new WardrobeFilterConfiguration();
    List<Filter<Item>> filters=new ArrayList<Filter<Item>>();
    // Name
    _nameFilter=new NamedFilter<Item>();
    filters.add(_nameFilter);
    // Slot filter
    _categoryFilter=new ItemClassFilter(null);
    filters.add(_categoryFilter);
    // Category
    _slotFilter=new ItemSlotFilter((EquipmentLocation)null);
    filters.add(_slotFilter);
    CompoundFilter<Item> filter=new CompoundFilter<Item>(Operator.AND,filters);

    ProxyValueResolver<WardrobeItem,Item> itemResolver=new ProxyValueResolver<WardrobeItem,Item>()
    {
      @Override
      public Item getValue(WardrobeItem source)
      {
        return source.getItem();
      }
    };
    _filter=new ProxyFilter<WardrobeItem,Item>(itemResolver,filter);
  }

  /**
   * Get the filter configuration.
   * @return the filter configuration.
   */
  public WardrobeFilterConfiguration getConfiguration()
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
   * Get the filter on slot.
   * @return A category filter.
   */
  public ItemSlotFilter getSlotFilter()
  {
    return _slotFilter;
  }

  /**
   * Get the filter on category.
   * @return A category filter.
   */
  public ItemClassFilter getCategoryFilter()
  {
    return _categoryFilter;
  }

  @Override
  public boolean accept(WardrobeItem item)
  {
    return _filter.accept(item);
  }
}
