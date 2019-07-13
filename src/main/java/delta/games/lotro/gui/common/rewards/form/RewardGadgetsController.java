package delta.games.lotro.gui.common.rewards.form;

import javax.swing.JLabel;

/**
 * Base class for controllers of reward display gadgets.
 * @author DAM
 */
public class RewardGadgetsController
{
  protected JLabel _labelIcon;
  protected JLabel _label;

  /**
   * Get the managed (label) icon.
   * @return a icon label.
   */
  public JLabel getLabelIcon()
  {
    return _labelIcon;
  }

  /**
   * Get the managed label.
   * @return a halo label.
   */
  public JLabel getLabel()
  {
    return _label;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _labelIcon=null;
    _label=null;
  }
}
