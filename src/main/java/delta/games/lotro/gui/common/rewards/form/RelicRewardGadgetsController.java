package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.ImageIcon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.legendary.relics.Relic;

/**
 * Controller for the UI gadgets of a relic reward.
 * @author DAM
 */
public class RelicRewardGadgetsController extends RewardGadgetsController
{
  /**
   * Constructor.
   * @param parent Parent controller.
   * @param relic Relic to display.
   * @param count Count of that relic.
   */
  public RelicRewardGadgetsController(AreaController parent, Relic relic, int count)
  {
    super(parent);
    // Label
    String text=relic.getName();
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(Color.WHITE);
    // Icon
    String iconPath=relic.getIconFilename();
    ImageIcon icon=LotroIconsManager.getRelicIcon(iconPath);
    _icon=GuiFactory.buildIconLabel(icon);
    _icon.setSize(icon.getIconWidth(),icon.getIconHeight());
  }
}
