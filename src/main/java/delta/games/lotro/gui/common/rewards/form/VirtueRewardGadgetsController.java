package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.common.rewards.VirtueReward;
import delta.games.lotro.gui.lore.virtues.VirtueUiTools;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconControllerFactory;

/**
 * Controller for the UI gadgets of a virtue reward.
 * @author DAM
 */
public class VirtueRewardGadgetsController extends RewardGadgetsController
{
  private HyperLinkController _virtueLink;
  private IconController _virtueIcon;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param virtueReward Virtue reward.
   */
  public VirtueRewardGadgetsController(WindowController parent, VirtueReward virtueReward)
  {
    super(parent);
    VirtueDescription virtue=virtueReward.getVirtue();
    // Label
    String text=virtue.getName();
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(Color.WHITE);
    // Link
    _virtueLink=VirtueUiTools.buildVirtueLink(parent,virtue,_label);
    // Icon
    int count=virtueReward.getCount();
    _virtueIcon=IconControllerFactory.buildVirtueIcon(parent,virtue,count);
    _icon=_virtueIcon.getIcon();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_virtueLink!=null)
    {
      _virtueLink.dispose();
      _virtueLink=null;
    }
    if (_virtueIcon!=null)
    {
      _virtueIcon.dispose();
      _virtueIcon=null;
    }
  }
}
