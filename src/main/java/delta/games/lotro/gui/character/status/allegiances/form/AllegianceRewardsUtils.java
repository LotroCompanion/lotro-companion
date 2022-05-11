package delta.games.lotro.gui.character.status.allegiances.form;

import java.util.BitSet;

import delta.games.lotro.character.status.allegiances.AllegianceStatus;

/**
 * Utility methods for the display of allegiance rewards.
 * @author DAM
 */
public class AllegianceRewardsUtils
{
  /**
   * Get the state of an allegiance reward.
   * @param status Status to use.
   * @param level Level to use.
   * @return A state.
   */
  public static AllegianceRewardState getState(AllegianceStatus status, int level)
  {
    Integer currentLevel=status.getCurrentLevel();
    if (currentLevel==null)
    {
      return AllegianceRewardState.FUTURE;
    }
    if (level>currentLevel.intValue())
    {
      return AllegianceRewardState.FUTURE;
    }
    BitSet claimedRewards=status.getClaimedRewards();
    boolean claimed=claimedRewards.get(level-1);
    return claimed?AllegianceRewardState.CLAIMED:AllegianceRewardState.UNLOCKED;
  }
}
