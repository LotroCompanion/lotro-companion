package delta.games.lotro.gui.common.rewards.form;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.lore.quests.AchievablesUiUtils;
import delta.games.lotro.lore.quests.Achievable;

/**
 * Controller for the UI gadgets of a quest complete reward.
 * @author DAM
 */
public class QuestCompleteRewardGadgetsController extends RewardGadgetsController
{
  private HyperLinkController _achievableLink;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param achievable Achievable to show.
   */
  public QuestCompleteRewardGadgetsController(WindowController parent, Achievable achievable)
  {
    super(parent);
    // Label
    _label=GuiFactory.buildLabel("");
    // Link
    _achievableLink=AchievablesUiUtils.buildAchievableLink(parent,achievable,_label);
    // Icon
    _icon=GuiFactory.buildTransparentIconlabel(1);
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_achievableLink!=null)
    {
      _achievableLink.dispose();
      _achievableLink=null;
    }
  }
}
