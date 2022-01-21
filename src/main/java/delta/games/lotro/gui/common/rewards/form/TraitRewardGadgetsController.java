package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconControllerFactory;

/**
 * Controller for the UI gadgets of a trait reward.
 * @author DAM
 */
public class TraitRewardGadgetsController extends RewardGadgetsController
{
  private IconController _itemIcon;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param trait Trait.
   */
  public TraitRewardGadgetsController(WindowController parent, TraitDescription trait)
  {
    // Label
    String text=trait.getName();
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    int id=trait.getIconId();
    Icon icon=LotroIconsManager.getTraitIcon(id);
    _icon=GuiFactory.buildIconLabel(icon);
    // Icon
    _itemIcon=IconControllerFactory.buildTraitIcon(parent,trait);
    _icon=_itemIcon.getIcon();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_itemIcon!=null)
    {
      _itemIcon.dispose();
      _itemIcon=null;
    }
  }
}
