package delta.games.lotro.gui.stats.achievables.form;

/**
 * Configuration of the achievable form UI.
 * @author DAM
 */
public class AchievableFormConfig
{
  /**
   * UI mode.
   * @author DAM
   */
  public enum MODE
  {
    /**
     * Deed mode: show completion date, hide completion count.
     */
    DEED,
    /**
     * Quest: hide completion date, show completion count.
     */
    QUEST
  }

  private MODE _mode;
  private boolean _editable;

  /**
   * Constructor.
   * @param mode Mode to use.
   * @param editable Editable or not.
   */
  public AchievableFormConfig(MODE mode, boolean editable)
  {
    _mode=mode;
    _editable=editable;
  }

  /**
   * Get the achievable mode.
   * @return the achievable mode.
   */
  public MODE getMode()
  {
    return _mode;
  }

  /**
   * Indicates if this component is editable or not.
   * @return <code>true</code> if it is, <code>false</code> otherwise.
   */
  public boolean isEditable()
  {
    return _editable;
  }

  @Override
  public String toString()
  {
    return "Mode: "+_mode+", editable="+_editable;
  }
}
