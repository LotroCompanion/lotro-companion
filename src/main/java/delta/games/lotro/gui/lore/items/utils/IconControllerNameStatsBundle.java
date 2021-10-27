package delta.games.lotro.gui.lore.items.utils;

import javax.swing.JButton;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.utils.IconController;

/**
 * Controller for the UI items to display an icon, a name, and some stats.
 * @author DAM
 */
public class IconControllerNameStatsBundle extends NameStatsBundle
{
  // UI
  protected IconController _icon;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public IconControllerNameStatsBundle(WindowController parent)
  {
    super();
    // Icon
    _icon=new IconController(parent);
  }

  /**
   * Get the managed icon.
   * @return the managed icon.
   */
  public JButton getIcon()
  {
    return _icon.getIcon();
  }

  /**
   * Get the managed icon controller.
   * @return the managed icon controller.
   */
  public IconController getIconController()
  {
    return _icon;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    // UI
    _icon=null;
  }
}
