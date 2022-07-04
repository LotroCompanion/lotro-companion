package delta.games.lotro.gui.account.status.rewardsTracks.form;

/**
 * State of a rewards track reward.
 * @author DAM
 */
public enum RewardsTrackStepState
{
  /**
   * Future.
   */
  FUTURE("Future"),
  /**
   * Unlocked (but not claimed).
   */
  UNLOCKED("Unlocked"),
  /**
   * Claimed.
   */
  CLAIMED("Claimed");

  private String _label;

  private RewardsTrackStepState(String label)
  {
    _label=label;
  }

  /**
   * Get the display label.
   * @return the display label.
   */
  public String getLabel()
  {
    return _label;
  }
}
