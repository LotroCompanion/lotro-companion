package delta.games.lotro.gui.character.status.achievables.form;

import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;

/**
 * Configuration of the achievable form UI.
 * @author DAM
 */
public class AchievableFormConfig
{
  private AchievableUIMode _mode;
  private boolean _editable;

  /**
   * Constructor.
   * @param mode Mode to use.
   * @param editable Editable or not.
   */
  public AchievableFormConfig(AchievableUIMode mode, boolean editable)
  {
    _mode=mode;
    _editable=editable;
  }

  /**
   * Get the achievable mode.
   * @return the achievable mode.
   */
  public AchievableUIMode getMode()
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
