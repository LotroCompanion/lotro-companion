package delta.games.lotro.gui.rewards;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconWithText;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.common.rewards.VirtueReward;
import delta.games.lotro.gui.LotroIconsManager;

/**
 * Controller for the UI gadgets of a virtue reward.
 * @author DAM
 */
public class VirtueRewardGadgetsController
{
  private JLabel _labelIcon;
  private JLabel _label;

  /**
   * Constructor.
   * @param virtue Virtue.
   */
  public VirtueRewardGadgetsController(VirtueReward virtue)
  {
    // Label
    String text=virtue.getIdentifier().getLabel();
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    VirtueId virtueId=virtue.getIdentifier();
    Icon virtueIcon=LotroIconsManager.getVirtueIcon(virtueId.name());
    int count=virtue.getCount();
    Icon decoratedVirtueIcon=new IconWithText(virtueIcon,String.valueOf(count),Color.WHITE);
    _labelIcon=GuiFactory.buildIconLabel(decoratedVirtueIcon);
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
