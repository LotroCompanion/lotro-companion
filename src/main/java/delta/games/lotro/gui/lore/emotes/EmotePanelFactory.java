package delta.games.lotro.gui.lore.emotes;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.emotes.form.EmoteDisplayPanelController;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.emotes.EmotesManager;

/**
 * Factory for emote-related panels.
 * @author DAM
 */
public class EmotePanelFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public EmotePanelFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.EMOTE_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildEmotePanel(id);
    }
    return ret;
  }

  private EmoteDisplayPanelController buildEmotePanel(int emoteID)
  {
    EmotesManager emotesMgr=EmotesManager.getInstance();
    EmoteDescription emote=emotesMgr.getEmote(emoteID);
    if (emote!=null)
    {
      EmoteDisplayPanelController emotePanel=new EmoteDisplayPanelController(_parent,emote);
      return emotePanel;
    }
    return null;
  }
}
