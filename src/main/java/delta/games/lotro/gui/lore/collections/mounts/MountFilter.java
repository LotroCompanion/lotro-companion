package delta.games.lotro.gui.lore.collections.mounts;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.lore.collections.mounts.MountDescription;
import delta.games.lotro.lore.collections.mounts.filters.MountCategoryFilter;
import delta.games.lotro.lore.collections.mounts.filters.MountNameFilter;

/**
 * Mount filter.
 * @author DAM
 */
public class MountFilter implements Filter<MountDescription>
{
  private Filter<MountDescription> _filter;

  private MountNameFilter _nameFilter;
  private MountCategoryFilter _categoryFilter;

  /**
   * Constructor.
   */
  public MountFilter()
  {
    List<Filter<MountDescription>> filters=new ArrayList<Filter<MountDescription>>();
    // Name
    _nameFilter=new MountNameFilter();
    filters.add(_nameFilter);
    // Category
    _categoryFilter=new MountCategoryFilter(null);
    filters.add(_categoryFilter);
    _filter=new CompoundFilter<MountDescription>(Operator.AND,filters);
  }

  /**
   * Get the filter on mount name.
   * @return a mount name filter.
   */
  public MountNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  /**
   * Get the filter on mount category.
   * @return a mount category filter.
   */
  public MountCategoryFilter getCategoryFilter()
  {
    return _categoryFilter;
  }

  @Override
  public boolean accept(MountDescription item)
  {
    return _filter.accept(item);
  }
}
