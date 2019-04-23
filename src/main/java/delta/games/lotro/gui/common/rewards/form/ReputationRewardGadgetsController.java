package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.games.lotro.common.rewards.ReputationReward;
import delta.games.lotro.lore.reputation.Faction;

/**
 * Controller for the UI gadgets of a reputation reward.
 * @author DAM
 */
public class ReputationRewardGadgetsController
{
  private JLabel _labelIcon;
  private JLabel _label;

  /**
   * Constructor.
   * @param reputation Reputation.
   */
  public ReputationRewardGadgetsController(ReputationReward reputation)
  {
    // Label
    int amount=reputation.getAmount();
    Faction faction=reputation.getFaction();
    String text=((amount>0)?"+":"")+amount+" "+faction.getName();
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    Icon icon=IconsManager.getIcon("/resources/gui/icons/reputation.png");
    _labelIcon=GuiFactory.buildIconLabel(icon);
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
