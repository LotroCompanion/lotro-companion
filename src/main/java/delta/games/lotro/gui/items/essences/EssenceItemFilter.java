package delta.games.lotro.gui.items.essences;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.filters.EssenceTierFilter;
import delta.games.lotro.lore.items.filters.ItemFilter;
import delta.games.lotro.lore.items.filters.ItemIsEssenceFilter;
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
  private Filter<Item> _filter;

  /**
   * Constructor.
   */
  public EssenceItemFilter()
  {
    _essenceFilter=new ItemIsEssenceFilter();
    _qualityFilter=new ItemQualityFilter(null);
    _tierFilter=null;
  }

  /**
   * Set the quality to select.
   * @param quality A quality or <code>null</code> to accept all.
   */
  public void setQuality(ItemQuality quality)
  {
    _qualityFilter.setQuality(quality);
    _filter=buildFilter();
  }

  /**
   * Set the tier to select.
   * @param tier A tier or <code>null</code> to accept all.
   */
  public void setTier(Integer tier)
  {
    if (tier!=null)
    {
      _tierFilter=new EssenceTierFilter(tier.intValue());
    }
    else
    {
      _tierFilter=null;
    }
    _filter=buildFilter();
  }

  private Filter<Item> buildFilter()
  {
    if ((_tierFilter!=null) || (_qualityFilter!=null))
    {
      List<Filter<Item>> filters=new ArrayList<Filter<Item>>();
      filters.add(_essenceFilter);
      if (_qualityFilter.getQuality()!=null)
      {
        filters.add(_qualityFilter);
      }
      if (_tierFilter!=null)
      {
        filters.add(_tierFilter);
      }
      CompoundFilter<Item> filter=new CompoundFilter<Item>(Operator.AND,filters);
      return filter;
    }
    return _essenceFilter;
  }

  public boolean accept(Item item)
  {
    return _filter.accept(item);
  }
}
