package delta.games.lotro.gui.lore.birds;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.birds.form.BirdDisplayPanelController;
import delta.games.lotro.lore.collections.birds.BirdDescription;
import delta.games.lotro.lore.collections.birds.BirdsManager;

/**
 * Factory for birds-related panels.
 * @author DAM
 */
public class BirdsPanelFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public BirdsPanelFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.BIRD_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildBirdPanel(id);
    }
    return ret;
  }

  private BirdDisplayPanelController buildBirdPanel(int birdID)
  {
    BirdsManager birdsMgr=BirdsManager.getInstance();
    BirdDescription bird=birdsMgr.getBird(birdID);
    if (bird!=null)
    {
      BirdDisplayPanelController birdPanel=new BirdDisplayPanelController(_parent,bird);
      return birdPanel;
    }
    return null;
  }
}
