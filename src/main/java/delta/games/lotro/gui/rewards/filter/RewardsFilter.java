package delta.games.lotro.gui.rewards.filter;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.common.rewards.filters.ClassPointRewardFilter;
import delta.games.lotro.common.rewards.filters.DestinyPointsRewardFilter;
import delta.games.lotro.common.rewards.filters.EmoteRewardFilter;
import delta.games.lotro.common.rewards.filters.ItemRewardFilter;
import delta.games.lotro.common.rewards.filters.LotroPointsRewardFilter;
import delta.games.lotro.common.rewards.filters.ReputationRewardFilter;
import delta.games.lotro.common.rewards.filters.SkillRewardFilter;
import delta.games.lotro.common.rewards.filters.TitleRewardFilter;
import delta.games.lotro.common.rewards.filters.TraitRewardFilter;
import delta.games.lotro.common.rewards.filters.VirtueRewardFilter;

/**
 * Filter on rewards.
 * @author DAM
 */
public class RewardsFilter implements Filter<Rewards>
{
  private Filter<Rewards> _filter;

  private ReputationRewardFilter _reputationFilter;
  private LotroPointsRewardFilter _lotroPointsFilter;
  private DestinyPointsRewardFilter _destinyPointsFilter;
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
  public RewardsFilter()
  {
    List<Filter<Rewards>> filters=new ArrayList<Filter<Rewards>>();
    // - reputation
    _reputationFilter=new ReputationRewardFilter(null);
    filters.add(_reputationFilter);
    // - LOTRO points
    _lotroPointsFilter=new LotroPointsRewardFilter(null);
    filters.add(_lotroPointsFilter);
    // - destiny points
    _destinyPointsFilter=new DestinyPointsRewardFilter(null);
    filters.add(_destinyPointsFilter);
    // - class points
    _classPointsFilter=new ClassPointRewardFilter(null);
    filters.add(_classPointsFilter);
    // - trait
    _traitFilter=new TraitRewardFilter(null);
    filters.add(_traitFilter);
    // - skill
    _skillFilter=new SkillRewardFilter(null);
    filters.add(_skillFilter);
    // - title
    _titleFilter=new TitleRewardFilter(null);
    filters.add(_titleFilter);
    // - virtue
    _virtueFilter=new VirtueRewardFilter(null);
    filters.add(_virtueFilter);
    // - emote
    _emoteFilter=new EmoteRewardFilter(null);
    filters.add(_emoteFilter);
    // - item
    _itemFilter=new ItemRewardFilter(null);
    filters.add(_itemFilter);
    _filter=new CompoundFilter<Rewards>(Operator.AND,filters);
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
   * Get the filter on destiny points reward.
   * @return a filter.
   */
  public DestinyPointsRewardFilter getDestinyPointsFilter()
  {
    return _destinyPointsFilter;
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
  public boolean accept(Rewards item)
  {
    return _filter.accept(item);
  }
}