package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.lore.traits.TraitUiUtils;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconControllerFactory;

/**
 * Controller for the UI gadgets of a trait reward.
 * @author DAM
 */
public class TraitRewardGadgetsController extends RewardGadgetsController
{
  private HyperLinkController _traitLink;
  private IconController _traitIcon;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param trait Trait.
   */
  public TraitRewardGadgetsController(WindowController parent, TraitDescription trait)
  {
    super(parent);
    // Label
    _label=new LabelWithHalo();
    _label.setOpaque(false);
    _label.setForeground(Color.WHITE);
    // Link
    _traitLink=TraitUiUtils.buildTraitLink(parent,trait,_label);
    // Icon
    _traitIcon=IconControllerFactory.buildTraitIcon(parent,trait);
    _icon=_traitIcon.getIcon();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_traitLink!=null)
    {
      _traitLink.dispose();
      _traitLink=null;
    }
    if (_traitIcon!=null)
    {
      _traitIcon.dispose();
      _traitIcon=null;
    }
  }
}
