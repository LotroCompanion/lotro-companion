package delta.games.lotro.utils.gui;

import javax.swing.JFrame;

/**
 * Interface of a window controller.
 * @author DAM
 */
public interface WindowController
{
  /**
   * Get the managed frame.
   * @return the managed frame.
   */
  public JFrame getFrame();

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
   * Release all managed resources.
   */
  public void dispose();
}
