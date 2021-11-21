package delta.games.lotro.gui.character.status.allegiances.form;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import delta.common.utils.collections.filters.Filter;

/**
 * Filter for allegiance rewards using their state.
 * @author DAM
 */
public class AllegianceRewardsFilter implements Filter<AllegianceRewardState>
{
  private Set<AllegianceRewardState> _selected;

  /**
   * Constructor.
   */
  public AllegianceRewardsFilter()
  {
    _selected=new HashSet<AllegianceRewardState>();
    for(AllegianceRewardState state : AllegianceRewardState.values())
    {
      _selected.add(state);
    }
  }

  @Override
  public boolean accept(AllegianceRewardState state)
  {
    return _selected.contains(state);
  }

  /**
   * Set the selected states.
   * @param selectedStates States to use.
   */
  public void setSelectedStates(Collection<AllegianceRewardState> selectedStates)
  {
    _selected.clear();
    _selected.addAll(selectedStates);
  }
}
