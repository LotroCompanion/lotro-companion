package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.games.lotro.common.rewards.TitleReward;
import delta.games.lotro.gui.common.rewards.RewardsUiUtils;

/**
 * Controller for the UI gadgets of a title reward.
 * @author DAM
 */
public class TitleRewardGadgetsController extends RewardGadgetsController
{
  /**
   * Constructor.
   * @param title Title.
   */
  public TitleRewardGadgetsController(TitleReward title)
  {
    // Label
    String text=RewardsUiUtils.getDisplayedTitle(title.getName());
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    _labelIcon=GuiFactory.buildTransparentIconlabel(32);
  }
}
