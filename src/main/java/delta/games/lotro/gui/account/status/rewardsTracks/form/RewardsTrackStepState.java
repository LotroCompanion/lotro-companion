package delta.games.lotro.gui.account.status.rewardsTracks.form;

import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * State of a rewards track reward.
 * @author DAM
 */
public enum RewardsTrackStepState
{
  /**
   * Future.
   */
  FUTURE("future"),
  /**
   * Unlocked (but not claimed).
   */
  UNLOCKED("unlocked"),
  /**
   * Claimed.
   */
  CLAIMED("claimed");

  private String _i18nKey;

  private RewardsTrackStepState(String i18nKey)
  {
    _i18nKey=i18nKey;
  }

  /**
   * Get the display label.
   * @return the display label.
   */
  public String getLabel()
  {
    return Labels.getLabel("rewards.track.step."+_i18nKey);
  }
}
