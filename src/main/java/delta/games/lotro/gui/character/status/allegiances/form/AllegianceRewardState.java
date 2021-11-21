package delta.games.lotro.gui.character.status.allegiances.form;

/**
 * State of an allegiance reward.
 * @author DAM
 */
public enum AllegianceRewardState
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

  private AllegianceRewardState(String label)
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
