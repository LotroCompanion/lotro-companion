package delta.games.lotro.gui.lore.trade.vendor;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.lore.trade.vendor.VendorNpc;
import delta.games.lotro.lore.trade.vendor.filters.VendorNameFilter;

/**
 * Vendor filter.
 * @author DAM
 */
public class VendorFilter implements Filter<VendorNpc>
{
  private Filter<VendorNpc> _filter;

  private VendorNameFilter _nameFilter;

  /**
   * Constructor.
   */
  public VendorFilter()
  {
    List<Filter<VendorNpc>> filters=new ArrayList<Filter<VendorNpc>>();
    // Name
    _nameFilter=new VendorNameFilter();
    filters.add(_nameFilter);
    _filter=new CompoundFilter<VendorNpc>(Operator.AND,filters);
  }

  /**
   * Get the filter on vendor name.
   * @return a vendor name filter.
   */
  public VendorNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  @Override
  public boolean accept(VendorNpc item)
  {
    return _filter.accept(item);
  }
}
