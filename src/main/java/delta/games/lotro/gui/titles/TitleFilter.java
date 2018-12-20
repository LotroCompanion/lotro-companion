package delta.games.lotro.gui.titles;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.lore.titles.filters.TitleCategoryFilter;
import delta.games.lotro.lore.titles.filters.TitleNameFilter;

/**
 * Title filter.
 * @author DAM
 */
public class TitleFilter implements Filter<TitleDescription>
{
  private Filter<TitleDescription> _filter;

  private TitleNameFilter _nameFilter;
  private TitleCategoryFilter _categoryFilter;

  /**
   * Constructor.
   */
  public TitleFilter()
  {
    List<Filter<TitleDescription>> filters=new ArrayList<Filter<TitleDescription>>();
    // Name
    _nameFilter=new TitleNameFilter();
    filters.add(_nameFilter);
    // Category
    _categoryFilter=new TitleCategoryFilter(null);
    filters.add(_categoryFilter);
    _filter=new CompoundFilter<TitleDescription>(Operator.AND,filters);
  }

  /**
   * Get the filter on title name.
   * @return a title name filter.
   */
  public TitleNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  /**
   * Get the filter on title category.
   * @return a title category filter.
   */
  public TitleCategoryFilter getCategoryFilter()
  {
    return _categoryFilter;
  }

  @Override
  public boolean accept(TitleDescription item)
  {
    return _filter.accept(item);
  }
}
