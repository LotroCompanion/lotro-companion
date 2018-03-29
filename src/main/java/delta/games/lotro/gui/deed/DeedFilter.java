package delta.games.lotro.gui.deed;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.common.rewards.filters.ClassPointRewardFilter;
import delta.games.lotro.common.rewards.filters.EmoteRewardFilter;
import delta.games.lotro.common.rewards.filters.ItemRewardFilter;
import delta.games.lotro.common.rewards.filters.LotroPointsRewardFilter;
import delta.games.lotro.common.rewards.filters.ReputationRewardFilter;
import delta.games.lotro.common.rewards.filters.SkillRewardFilter;
import delta.games.lotro.common.rewards.filters.TitleRewardFilter;
import delta.games.lotro.common.rewards.filters.TraitRewardFilter;
import delta.games.lotro.common.rewards.filters.VirtueRewardFilter;
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
  private ReputationRewardFilter _reputationFilter;
  private LotroPointsRewardFilter _lotroPointsFilter;
  private ClassPointRewardFilter _classPointsFilter;
  private TraitRewardFilter _traitFilter;
  private SkillRewardFilter _skillFilter;
  private TitleRewardFilter _titleFilter;
  private VirtueRewardFilter _virtueFilter;
  private EmoteRewardFilter _emoteFilter;
  private ItemRewardFilter _itemFilter;

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
    // Rewards:
    // - reputation
    _reputationFilter=new ReputationRewardFilter(null);
    filters.add(new DeedRewardFilter(_reputationFilter));
    // - LOTRO points
    _lotroPointsFilter=new LotroPointsRewardFilter(null);
    filters.add(new DeedRewardFilter(_lotroPointsFilter));
    // - class points
    _classPointsFilter=new ClassPointRewardFilter(null);
    filters.add(new DeedRewardFilter(_classPointsFilter));
    // - trait
    _traitFilter=new TraitRewardFilter(null);
    filters.add(new DeedRewardFilter(_traitFilter));
    // - skill
    _skillFilter=new SkillRewardFilter(null);
    filters.add(new DeedRewardFilter(_skillFilter));
    // - title
    _titleFilter=new TitleRewardFilter(null);
    filters.add(new DeedRewardFilter(_titleFilter));
    // - virtue
    _virtueFilter=new VirtueRewardFilter(null);
    filters.add(new DeedRewardFilter(_virtueFilter));
    // - emote
    _emoteFilter=new EmoteRewardFilter(null);
    filters.add(new DeedRewardFilter(_emoteFilter));
    // - item
    _itemFilter=new ItemRewardFilter(null);
    filters.add(new DeedRewardFilter(_itemFilter));
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
   * Get the filter on deed reputation.
   * @return a deed reputation filter.
   */
  public ReputationRewardFilter getReputationFilter()
  {
    return _reputationFilter;
  }

  /**
   * Get the filter on LOTRO points reward.
   * @return a filter.
   */
  public LotroPointsRewardFilter getLotroPointsFilter()
  {
    return _lotroPointsFilter;
  }

  /**
   * Get the filter on class point reward.
   * @return a filter.
   */
  public ClassPointRewardFilter getClassPointsFilter()
  {
    return _classPointsFilter;
  }

  /**
   * Get the filter on deed trait.
   * @return a deed trait filter.
   */
  public TraitRewardFilter getTraitFilter()
  {
    return _traitFilter;
  }

  /**
   * Get the filter on deed skill.
   * @return a deed skill filter.
   */
  public SkillRewardFilter getSkillFilter()
  {
    return _skillFilter;
  }

  /**
   * Get the filter on deed title.
   * @return a deed title filter.
   */
  public TitleRewardFilter getTitleFilter()
  {
    return _titleFilter;
  }

  /**
   * Get the filter on deed virtue.
   * @return a deed virtue filter.
   */
  public VirtueRewardFilter getVirtueFilter()
  {
    return _virtueFilter;
  }

  /**
   * Get the filter on deed emote.
   * @return a deed emote filter.
   */
  public EmoteRewardFilter getEmoteFilter()
  {
    return _emoteFilter;
  }

  /**
   * Get the filter on deed item.
   * @return a deed item filter.
   */
  public ItemRewardFilter getItemFilter()
  {
    return _itemFilter;
  }

  @Override
  public boolean accept(DeedDescription item)
  {
    return _filter.accept(item);
  }
}
