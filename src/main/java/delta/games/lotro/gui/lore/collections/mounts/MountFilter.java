package delta.games.lotro.gui.lore.collections.mounts;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.common.filters.NamedFilter;
import delta.games.lotro.lore.collections.mounts.MountDescription;
import delta.games.lotro.lore.collections.mounts.filters.MountCategoryFilter;
import delta.games.lotro.lore.collections.mounts.filters.MountTypeFilter;

/**
 * Mount filter.
 * @author DAM
 */
public class MountFilter implements Filter<MountDescription>
{
  private Filter<MountDescription> _filter;

  private NamedFilter<MountDescription> _nameFilter;
  private MountCategoryFilter _categoryFilter;
  private MountTypeFilter _typeFilter;

  /**
   * Constructor.
   */
  public MountFilter()
  {
    List<Filter<MountDescription>> filters=new ArrayList<Filter<MountDescription>>();
    // Name
    _nameFilter=new NamedFilter<MountDescription>();
    filters.add(_nameFilter);
    // Category
    _categoryFilter=new MountCategoryFilter(null);
    filters.add(_categoryFilter);
    // Type
    _typeFilter=new MountTypeFilter(null);
    filters.add(_typeFilter);
    _filter=new CompoundFilter<MountDescription>(Operator.AND,filters);
  }

  /**
   * Get the filter on mount name.
   * @return a mount name filter.
   */
  public NamedFilter<MountDescription> getNameFilter()
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

  /**
   * Get the filter on mount type.
   * @return a mount type filter.
   */
  public MountTypeFilter getTypeFilter()
  {
    return _typeFilter;
  }

  @Override
  public boolean accept(MountDescription item)
  {
    return _filter.accept(item);
  }
}
