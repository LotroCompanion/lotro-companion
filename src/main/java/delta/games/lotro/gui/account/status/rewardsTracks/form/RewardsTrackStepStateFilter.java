package delta.games.lotro.gui.account.status.rewardsTracks.form;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import delta.common.utils.collections.filters.Filter;

/**
 * Filter for allegiance rewards using their state.
 * @author DAM
 */
public class RewardsTrackStepStateFilter implements Filter<RewardsTrackStepState>
{
  private Set<RewardsTrackStepState> _selected;

  /**
   * Constructor.
   */
  public RewardsTrackStepStateFilter()
  {
    _selected=new HashSet<RewardsTrackStepState>();
    _selected.addAll(Arrays.asList(RewardsTrackStepState.values()));
  }

  @Override
  public boolean accept(RewardsTrackStepState state)
  {
    return _selected.contains(state);
  }

  /**
   * Set the selected states.
   * @param selectedStates States to use.
   */
  public void setSelectedStates(Collection<RewardsTrackStepState> selectedStates)
  {
    _selected.clear();
    _selected.addAll(selectedStates);
  }
}
