package delta.games.lotro.gui.common.rewards.form;

import java.awt.Component;

import javax.swing.JLabel;

import delta.common.ui.swing.area.AbstractAreaController;
import delta.common.ui.swing.area.AreaController;

/**
 * Base class for controllers of reward display gadgets.
 * @author DAM
 */
public class RewardGadgetsController extends AbstractAreaController
{
  protected Component _icon;
  protected JLabel _label;

  /**
   * Constructor.
   * @param parent Parent controller.
   */
  public RewardGadgetsController(AreaController parent)
  {
    super(parent);
  }

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
    super.dispose();
    _icon=null;
    _label=null;
  }
}
