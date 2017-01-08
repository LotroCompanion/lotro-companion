package delta.games.lotro.gui.items.essences;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.filters.EssenceTierFilter;
import delta.games.lotro.lore.items.filters.ItemFilter;
import delta.games.lotro.lore.items.filters.ItemIsEssenceFilter;
import delta.games.lotro.lore.items.filters.ItemNameFilter;
import delta.games.lotro.lore.items.filters.ItemQualityFilter;

/**
 * Item to filter essence items.
 * @author DAM
 */
public class EssenceItemFilter implements ItemFilter
{
  private ItemIsEssenceFilter _essenceFilter;
  private ItemQualityFilter _qualityFilter;
  private EssenceTierFilter _tierFilter;
  private ItemNameFilter _nameFilter;
  private Filter<Item> _filter;

  /**
   * Constructor.
   */
  public EssenceItemFilter()
  {
    _essenceFilter=new ItemIsEssenceFilter();
    _qualityFilter=new ItemQualityFilter(null);
    _tierFilter=new EssenceTierFilter();
    _nameFilter=new ItemNameFilter();
    List<Filter<Item>> filters=new ArrayList<Filter<Item>>();
    filters.add(_essenceFilter);
    filters.add(_qualityFilter);
    filters.add(_tierFilter);
    filters.add(_nameFilter);
    _filter=new CompoundFilter<Item>(Operator.AND,filters);
  }

  /**
   * Get the managed item name filter.
   * @return a filter.
   */
  public ItemNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  /**
   * Get the managed item quality filter.
   * @return a filter.
   */
  public ItemQualityFilter getQualityFilter()
  {
    return _qualityFilter;
  }

  /**
   * Get the managed essence tier filter.
   * @return a filter.
   */
  public EssenceTierFilter getEssenceTierFilter()
  {
    return _tierFilter;
  }

  public boolean accept(Item item)
  {
    return _filter.accept(item);
  }
}
