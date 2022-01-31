package delta.games.lotro.gui.lore.virtues;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.character.virtues.VirtuesManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.virtues.form.VirtueDisplayPanelController;

/**
 * Factory for virtues-related panels.
 * @author DAM
 */
public class VirtuePanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public VirtuePanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.VIRTUE_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildVirtuePanel(id);
    }
    return ret;
  }

  private VirtueDisplayPanelController buildVirtuePanel(int virtueID)
  {
    VirtuesManager virtuesMgr=VirtuesManager.getInstance();
    VirtueDescription virtue=virtuesMgr.getVirtue(virtueID);
    if (virtue!=null)
    {
      VirtueDisplayPanelController traitPanel=new VirtueDisplayPanelController(_parent,virtue);
      return traitPanel;
    }
    return null;
  }
}
