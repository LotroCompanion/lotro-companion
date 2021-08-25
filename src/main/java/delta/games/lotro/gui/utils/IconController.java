package delta.games.lotro.gui.utils;

import javax.swing.JButton;

import delta.common.ui.swing.misc.Disposable;

/**
 * Interface for icon controllers (items+relics).
 * @author DAM
 */
public interface IconController extends Disposable
{
  /**
   * Get the managed icon button.
   * @return the managed icon button.
   */
  JButton getIcon();
}
