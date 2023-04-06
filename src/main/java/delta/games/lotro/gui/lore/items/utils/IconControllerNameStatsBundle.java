package delta.games.lotro.gui.lore.items.utils;

import javax.swing.JButton;

import delta.games.lotro.gui.utils.AbstractIconController;

/**
 * Controller for the UI items to display an icon, a name, and some stats.
 * @param <T> Type of managed icon controller.
 * @author DAM
 */
public class IconControllerNameStatsBundle<T extends AbstractIconController> extends NameStatsBundle
{
  // UI
  protected T _icon;

  /**
   * Constructor.
   */
  public IconControllerNameStatsBundle()
  {
    super();
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
  public T getIconController()
  {
    return _icon;
  }

  /**
   * Set the icon controller.
   * @param icon Icon controller.
   */
  public void setIconController(T icon)
  {
    _icon=icon;
  }
}
