package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.games.lotro.common.rewards.ReputationReward;
import delta.games.lotro.lore.reputation.Faction;

/**
 * Controller for the UI gadgets of a reputation reward.
 * @author DAM
 */
public class ReputationRewardGadgetsController extends RewardGadgetsController
{
  /**
   * Constructor.
   * @param reputation Reputation.
   */
  public ReputationRewardGadgetsController(ReputationReward reputation)
  {
    // Label
    int amount=reputation.getAmount();
    String iconName=(amount>0)?"reputation":"reputation-decrease";
    Faction faction=reputation.getFaction();
    String text=((amount>0)?"+":"")+amount+" "+faction.getName();
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    Icon icon=IconsManager.getIcon("/resources/gui/icons/"+iconName+".png");
    _icon=GuiFactory.buildIconLabel(icon);
  }
}
