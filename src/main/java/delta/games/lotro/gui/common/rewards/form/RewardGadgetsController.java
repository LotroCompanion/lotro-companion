package delta.games.lotro.gui.common.rewards.form;

import java.awt.Component;

import javax.swing.JLabel;

/**
 * Base class for controllers of reward display gadgets.
 * @author DAM
 */
public class RewardGadgetsController
{
  protected Component _icon;
  protected JLabel _label;

  /**
   * Get the managed icon component.
   * @return a icon component.
   */
  public Component getIcon()
  {
    return _icon;
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
    _icon=null;
    _label=null;
  }
}
