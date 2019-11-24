package delta.games.lotro.gui.utils;

/**
 * Configuration of the user interface.
 * @author DAM
 */
public class UiConfiguration
{
  /**
   * Indicates if 'technical' columns shall be available or not.
   * @return <code>true</code> to show them, <code>false</code> otherwise.
   */
  public static boolean showTechnicalColumns()
  {
    String env=System.getenv("LC_SHOW_TECHNICAL_COLUMNS");
    return "true".equals(env);
  }
}
