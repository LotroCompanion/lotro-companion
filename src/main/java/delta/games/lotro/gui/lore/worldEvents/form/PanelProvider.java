package delta.games.lotro.gui.lore.worldEvents.form;

import javax.swing.JPanel;

import delta.common.ui.swing.misc.Disposable;

/**
 * Interface of a panel provider.
 * @author DAM
 */
public interface PanelProvider extends Disposable
{
  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  JPanel getPanel();
}
