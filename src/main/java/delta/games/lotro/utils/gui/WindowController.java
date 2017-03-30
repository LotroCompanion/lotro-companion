package delta.games.lotro.utils.gui;

import java.awt.Window;

/**
 * Interface of a window controller.
 * @author DAM
 */
public interface WindowController
{
  /**
   * Get the managed window.
   * @return the managed window.
   */
  public Window getWindow();

  /**
   * Compute a window identifier.
   * @return A string that uniquely identifies the managed frame.
   */
  public String getWindowIdentifier();

  /**
   * Show the managed window.
   */
  public void show();

  /**
   * Bring the managed window to front.
   */
  public void bringToFront();

  /**
   * Set window title.
   * @param title Title to set.
   */
  public void setTitle(String title);

  /**
   * Release all managed resources.
   */
  public void dispose();
}
