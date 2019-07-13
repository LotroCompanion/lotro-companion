package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.LabelWithHalo;

/**
 * Controller for the UI gadgets of a LOTRO points reward.
 * @author DAM
 */
public class LotroPointsRewardGadgetsController extends RewardGadgetsController
{
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
}
