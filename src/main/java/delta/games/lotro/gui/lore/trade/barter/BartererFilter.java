package delta.games.lotro.gui.lore.trade.barter;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.lore.trade.barter.BarterNpc;
import delta.games.lotro.lore.trade.barter.filters.BartererNameFilter;

/**
 * Barterer filter.
 * @author DAM
 */
public class BartererFilter implements Filter<BarterNpc>
{
  private Filter<BarterNpc> _filter;

  private BartererNameFilter _nameFilter;

  /**
   * Constructor.
   */
  public BartererFilter()
  {
    List<Filter<BarterNpc>> filters=new ArrayList<Filter<BarterNpc>>();
    // Name
    _nameFilter=new BartererNameFilter();
    filters.add(_nameFilter);
    _filter=new CompoundFilter<BarterNpc>(Operator.AND,filters);
  }

  /**
   * Get the filter on barterer name.
   * @return a barterer name filter.
   */
  public BartererNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  @Override
  public boolean accept(BarterNpc item)
  {
    return _filter.accept(item);
  }
}
