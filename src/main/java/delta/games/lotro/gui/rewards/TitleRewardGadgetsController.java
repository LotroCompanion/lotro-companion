package delta.games.lotro.gui.rewards;

import java.awt.Color;

import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.games.lotro.common.rewards.TitleReward;

/**
 * Controller for the UI gadgets of a title reward.
 * @author DAM
 */
public class TitleRewardGadgetsController
{
  private JLabel _labelIcon;
  private JLabel _label;

  /**
   * Constructor.
   * @param title Title.
   */
  public TitleRewardGadgetsController(TitleReward title)
  {
    // Label
    String text=title.getName();
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    _labelIcon=GuiFactory.buildTransparentIconlabel(32);
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
