package delta.games.lotro.gui.account.status.rewardsTracks.form;

import delta.games.lotro.account.status.rewardsTrack.RewardsTrackStatus;
import delta.games.lotro.common.rewards.ItemReward;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.rewardsTrack.RewardsTrackStep;

/**
 * Utility methods for the display of rewards tracks.
 * @author DAM
 */
public class RewardsTracksUtils
{
  /**
   * Get the state of a rewards track step.
   * @param status Status to use.
   * @param level Level to use.
   * @return A state.
   */
  public static RewardsTrackStepState getState(RewardsTrackStatus status, int level)
  {
    int currentLevel=status.getCurrentMilestone();
    if (level>currentLevel)
    {
      return RewardsTrackStepState.FUTURE;
    }
    int claimedMilestones=status.getClaimedMilestones();
    boolean claimed=(claimedMilestones>=level);
    return claimed?RewardsTrackStepState.CLAIMED:RewardsTrackStepState.UNLOCKED;
  }

  /**
   * Build rewards for a step.
   * @param step Step to use.
   * @return the rewards.
   */
  public static Rewards buildRewards(RewardsTrackStep step)
  {
    Rewards ret=new Rewards();
    Item rewardItem=step.getReward();
    if (rewardItem!=null)
    {
      ItemReward reward=new ItemReward(rewardItem,1);
      ret.addRewardElement(reward);
    }
    return ret;
  }
}
