package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconWithText;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.common.rewards.VirtueReward;
import delta.games.lotro.gui.LotroIconsManager;

/**
 * Controller for the UI gadgets of a virtue reward.
 * @author DAM
 */
public class VirtueRewardGadgetsController extends RewardGadgetsController
{
  /**
   * Constructor.
   * @param virtueReward Virtue reward.
   */
  public VirtueRewardGadgetsController(VirtueReward virtueReward)
  {
    VirtueDescription virtue=virtueReward.getVirtue();
    // Label
    String text=virtue.getName();
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    Icon virtueIcon=LotroIconsManager.getVirtueIcon(virtue);
    int count=virtueReward.getCount();
    Icon decoratedVirtueIcon=new IconWithText(virtueIcon,String.valueOf(count),Color.WHITE);
    _icon=GuiFactory.buildIconLabel(decoratedVirtueIcon);
  }
}
