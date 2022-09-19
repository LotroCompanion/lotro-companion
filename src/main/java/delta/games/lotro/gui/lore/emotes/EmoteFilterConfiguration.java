package delta.games.lotro.gui.lore.emotes;

/**
 * Configuration of the emote filter UI.
 * @author DAM
 */
public class EmoteFilterConfiguration
{
  /**
   * State of filter elements.
   * @author DAM
   */
  public enum State
  {
    /**
     * Visible but disabled.
     */
    VISIBLE,
    /**
     * Enabled.
     */
    ENABLED,
    /**
     * Hidden.
     */
    HIDDEN
  }

  private State _autoState;

  /**
   * Constructor.
   */
  public EmoteFilterConfiguration()
  {
    _autoState=State.ENABLED;
  }

  /**
   * Full constructor.
   * @param autoState State of the auto filter element.
   */
  public EmoteFilterConfiguration(State autoState)
  {
    _autoState=autoState;
  }

  /**
   * Get the state of the auto filter element.
   * @return the state of the auto filter element.
   */
  public State getAutoState()
  {
    return _autoState;
  }
}
