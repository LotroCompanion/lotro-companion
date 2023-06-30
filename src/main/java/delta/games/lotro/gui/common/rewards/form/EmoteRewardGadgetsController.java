package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.lore.emotes.EmoteUiUtils;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconControllerFactory;
import delta.games.lotro.lore.emotes.EmoteDescription;

/**
 * Controller for the UI gadgets of a emote reward.
 * @author DAM
 */
public class EmoteRewardGadgetsController extends RewardGadgetsController
{
  private HyperLinkController _emoteLink;
  private IconController _emoteIcon;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param emote Emote.
   */
  public EmoteRewardGadgetsController(WindowController parent, EmoteDescription emote)
  {
    super(parent);
    // Label
    _label=new LabelWithHalo();
    _label.setForeground(Color.WHITE);
    // Link
    _emoteLink=EmoteUiUtils.buildEmoteLink(parent,emote,_label);
    // Icon
    _emoteIcon=IconControllerFactory.buildEmoteIcon(parent,emote);
    _icon=_emoteIcon.getIcon();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_emoteLink!=null)
    {
      _emoteLink.dispose();
      _emoteLink=null;
    }
    if (_emoteIcon!=null)
    {
      _emoteIcon.dispose();
      _emoteIcon=null;
    }
  }
}
