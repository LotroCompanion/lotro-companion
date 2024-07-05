package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.common.utils.l10n.L10n;

/**
 * Controller for the UI gadgets of a glory (=renown or infamy) reward.
 * @author DAM
 */
public class GloryRewardGadgetsController extends RewardGadgetsController
{
  /**
   * Constructor.
   * @param parent Parent controller.
   * @param glory Amount.
   * @param renown Renown or Infamy.
   */
  public GloryRewardGadgetsController(AreaController parent, int glory, boolean renown)
  {
    super(parent);
    // Label
    String nature=(renown)?"Renown":"Infamy"; // I18n
    String text=L10n.getString(glory)+" "+nature;
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    String iconName=(renown)?"reputation.png":"reputation-decrease.png";
    Icon icon=IconsManager.getIcon("/resources/gui/icons/"+iconName);
    _icon=GuiFactory.buildIconLabel(icon);
  }
}
