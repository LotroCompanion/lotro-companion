package delta.games.lotro.gui.rewards;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.LabelWithHalo;

/**
 * Controller for the UI gadgets of a LOTRO points reward.
 * @author DAM
 */
public class LotroPointsRewardGadgetsController
{
  private JLabel _labelIcon;
  private JLabel _label;

  /**
   * Constructor.
   * @param count LOTRO points count.
   */
  public LotroPointsRewardGadgetsController(int count)
  {
    // Label
    String text=count+" LOTRO Points";
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    Icon lpIcon=IconsManager.getIcon("/resources/gui/icons/LP.png");
    _labelIcon=GuiFactory.buildIconLabel(lpIcon);
  }

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
}
