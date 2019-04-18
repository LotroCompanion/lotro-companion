package delta.games.lotro.gui.deed;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.gui.rewards.RewardsFilter;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.filters.DeedCategoryFilter;
import delta.games.lotro.lore.deeds.filters.DeedClassRequirementFilter;
import delta.games.lotro.lore.deeds.filters.DeedNameFilter;
import delta.games.lotro.lore.deeds.filters.DeedRaceRequirementFilter;
import delta.games.lotro.lore.deeds.filters.DeedRewardFilter;
import delta.games.lotro.lore.deeds.filters.DeedTypeFilter;

/**
 * Deed filter.
 * @author DAM
 */
public class DeedFilter implements Filter<DeedDescription>
{
  private Filter<DeedDescription> _filter;

  private DeedNameFilter _nameFilter;
  private DeedTypeFilter _typeFilter;
  private DeedCategoryFilter _categoryFilter;
  // Requirements
  private DeedClassRequirementFilter _classFilter;
  private DeedRaceRequirementFilter _raceFilter;
  // Rewards
  private RewardsFilter _rewardsFilter;

  /**
   * Constructor.
   */
  public DeedFilter()
  {
    List<Filter<DeedDescription>> filters=new ArrayList<Filter<DeedDescription>>();
    // Name
    _nameFilter=new DeedNameFilter();
    filters.add(_nameFilter);
    // Type
    _typeFilter=new DeedTypeFilter(null);
    filters.add(_typeFilter);
    // Category
    _categoryFilter=new DeedCategoryFilter(null);
    filters.add(_categoryFilter);
    // Requirements:
    // - class
    _classFilter=new DeedClassRequirementFilter(null);
    filters.add(_classFilter);
    // - race
    _raceFilter=new DeedRaceRequirementFilter(null);
    filters.add(_raceFilter);
    // Rewards
    _rewardsFilter=new RewardsFilter();
    filters.add(new DeedRewardFilter(_rewardsFilter));
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

  /**
   * Get the filter on deed type.
   * @return a deed type filter.
   */
  public DeedTypeFilter getTypeFilter()
  {
    return _typeFilter;
  }

  /**
   * Get the filter on deed category.
   * @return a deed category filter.
   */
  public DeedCategoryFilter getCategoryFilter()
  {
    return _categoryFilter;
  }

  /**
   * Get the filter on class requirement.
   * @return a character class or <code>null</code>.
   */
  public DeedClassRequirementFilter getClassFilter()
  {
    return _classFilter;
  }

  /**
   * Get the filter on race requirement.
   * @return a race or <code>null</code>.
   */
  public DeedRaceRequirementFilter getRaceFilter()
  {
    return _raceFilter;
  }

  /**
   * Get the filter on rewards.
   * @return the rewards filter.
   */
  public RewardsFilter getRewardsFilter()
  {
    return _rewardsFilter;
  }

  @Override
  public boolean accept(DeedDescription item)
  {
    return _filter.accept(item);
  }
}
