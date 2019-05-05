package delta.games.lotro.gui.common.navigator;

import javax.swing.JPanel;

/**
 * Interface of a navigable panel controller.
 * @author DAM
 */
public interface NavigablePanelController
{
  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  JPanel getPanel();

  /**
   * Release all managed resources.
   */
  void dispose();

  /**
   * Get the window title for this panel.
   * @return a window title.
   */
  String getTitle();
}
