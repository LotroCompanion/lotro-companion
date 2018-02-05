package delta.games.lotro.gui.deed;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.filters.DeedNameFilter;

/**
 * Deed filter.
 * @author DAM
 */
public class DeedFilter implements Filter<DeedDescription>
{
  private Filter<DeedDescription> _filter;

  private DeedNameFilter _nameFilter;

  /**
   * Constructor.
   */
  public DeedFilter()
  {
    List<Filter<DeedDescription>> filters=new ArrayList<Filter<DeedDescription>>();
    // Name
    _nameFilter=new DeedNameFilter();
    filters.add(_nameFilter);
    _filter=new CompoundFilter<DeedDescription>(Operator.AND,filters);
  }

  /**
   * Get the filter on deed name.
   * @return a deed name filter.
   */
  public DeedNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  @Override
  public boolean accept(DeedDescription item)
  {
    return _filter.accept(item);
  }
}
