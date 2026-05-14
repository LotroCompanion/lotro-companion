package delta.games.lotro.gui.lore.portraitFrames;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.portraitFrames.form.PortraitFrameDisplayPanelController;
import delta.games.lotro.lore.portraitFrames.PortraitFrameDescription;
import delta.games.lotro.lore.portraitFrames.PortraitFramesManager;

/**
 * Factory for portrait frames-related panels.
 * @author DAM
 */
public class PortraitFramePanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public PortraitFramePanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.PORTRAIT_FRAME_PAGE))
    {
      int code=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      PortraitFrameDescription portraitFrame=PortraitFramesManager.getInstance().getPortraitFrameByCode(code);
      ret=buildPortraitFramePanel(portraitFrame);
    }
    return ret;
  }

  private PortraitFrameDisplayPanelController buildPortraitFramePanel(PortraitFrameDescription portraitFrame)
  {
    PortraitFrameDisplayPanelController ret=new PortraitFrameDisplayPanelController(_parent,portraitFrame);
    return ret;
  }
}
