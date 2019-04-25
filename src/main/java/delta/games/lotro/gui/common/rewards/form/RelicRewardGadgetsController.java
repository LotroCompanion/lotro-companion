package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.legendary.relics.Relic;

/**
 * Controller for the UI gadgets of a relic reward.
 * @author DAM
 */
public class RelicRewardGadgetsController
{
  private JLabel _labelIcon;
  private JLabel _label;

  /**
   * Constructor.
   * @param relic Relic to display.
   * @param count Count of that relic.
   */
  public RelicRewardGadgetsController(Relic relic, int count)
  {
    // Label
    String text=relic.getName();
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(Color.WHITE);
    // Icon
    String iconPath=relic.getIconFilename();
    ImageIcon icon=LotroIconsManager.getRelicIcon(iconPath);
    _labelIcon=GuiFactory.buildIconLabel(icon);
    _labelIcon.setSize(icon.getIconWidth(),icon.getIconHeight());
  }

  /**
   * Get the managed (label) icon for relic icon.
   * @return a icon label.
   */
  public JLabel getLabelIcon()
  {
    return _labelIcon;
  }

  /**
   * Get the managed label (for relic name).
   * @return a halo label.
   */
  public JLabel getLabel()
  {
    return _label;
  }
}
