package delta.games.lotro.gui.lore.items.utils;

import javax.swing.JButton;

import delta.common.ui.swing.GuiFactory;

/**
 * Controller for the UI items to display an icon, a name, and some stats.
 * @author DAM
 */
public class IconNameStatsBundle extends NameStatsBundle
{
  // UI
  protected JButton _icon;

  /**
   * Constructor.
   */
  public IconNameStatsBundle()
  {
    super();
    // Icon
    _icon=GuiFactory.buildIconButton();
  }

  /**
   * Get the managed icon.
   * @return the managed icon.
   */
  public JButton getIcon()
  {
    return _icon;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // UI
    _icon=null;
  }
}
