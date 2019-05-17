package delta.games.lotro.gui.common.rewards.filter;

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
import delta.games.lotro.common.rewards.filters.ItemXpRewardFilter;
import delta.games.lotro.common.rewards.filters.LotroPointsRewardFilter;
import delta.games.lotro.common.rewards.filters.MountXpRewardFilter;
import delta.games.lotro.common.rewards.filters.RelicRewardFilter;
import delta.games.lotro.common.rewards.filters.ReputationRewardFilter;
import delta.games.lotro.common.rewards.filters.TitleRewardFilter;
import delta.games.lotro.common.rewards.filters.TraitRewardFilter;
import delta.games.lotro.common.rewards.filters.VirtueRewardFilter;
import delta.games.lotro.common.rewards.filters.XpRewardFilter;

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
  private XpRewardFilter _xpFilter;
  private ItemXpRewardFilter _itemXpFilter;
  private MountXpRewardFilter _mountXpFilter;
  private TraitRewardFilter _traitFilter;
  private TitleRewardFilter _titleFilter;
  private VirtueRewardFilter _virtueFilter;
  private EmoteRewardFilter _emoteFilter;
  private ItemRewardFilter _itemFilter;
  private RelicRewardFilter _relicFilter;

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
    // - XP
    _xpFilter=new XpRewardFilter(null);
    filters.add(_xpFilter);
    // - item XP
    _itemXpFilter=new ItemXpRewardFilter(null);
    filters.add(_itemXpFilter);
    // - mount XP
    _mountXpFilter=new MountXpRewardFilter(null);
    filters.add(_mountXpFilter);
    // - trait
    _traitFilter=new TraitRewardFilter(null);
    filters.add(_traitFilter);
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
    // - relic
    _relicFilter=new RelicRewardFilter(null);
    filters.add(_relicFilter);
    _filter=new CompoundFilter<Rewards>(Operator.AND,filters);
  }

  /**
   * Get the filter on reputation reward.
   * @return a reputation reward filter.
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
   * Get the filter on XP reward.
   * @return a filter.
   */
  public XpRewardFilter getXpFilter()
  {
    return _xpFilter;
  }

  /**
   * Get the filter on item XP reward.
   * @return a filter.
   */
  public ItemXpRewardFilter getItemXpFilter()
  {
    return _itemXpFilter;
  }

  /**
   * Get the filter on mount XP reward.
   * @return a filter.
   */
  public MountXpRewardFilter getMountXpFilter()
  {
    return _mountXpFilter;
  }

  /**
   * Get the filter on trait reward.
   * @return a trait reward filter.
   */
  public TraitRewardFilter getTraitFilter()
  {
    return _traitFilter;
  }

  /**
   * Get the filter on title reward.
   * @return a title reward filter.
   */
  public TitleRewardFilter getTitleFilter()
  {
    return _titleFilter;
  }

  /**
   * Get the filter on virtue reward.
   * @return a virtue reward filter.
   */
  public VirtueRewardFilter getVirtueFilter()
  {
    return _virtueFilter;
  }

  /**
   * Get the filter on emote reward.
   * @return an emote reward filter.
   */
  public EmoteRewardFilter getEmoteFilter()
  {
    return _emoteFilter;
  }

  /**
   * Get the filter on item reward.
   * @return an item reward filter.
   */
  public ItemRewardFilter getItemFilter()
  {
    return _itemFilter;
  }

  /**
   * Get the filter on relic reward.
   * @return a relic reward filter.
   */
  public RelicRewardFilter getRelicFilter()
  {
    return _relicFilter;
  }

  @Override
  public boolean accept(Rewards item)
  {
    return _filter.accept(item);
  }
}
