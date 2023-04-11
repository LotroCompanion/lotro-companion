package delta.games.lotro.gui.lore.hobbies;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.hobbies.form.HobbyDisplayPanelController;
import delta.games.lotro.lore.hobbies.HobbiesManager;
import delta.games.lotro.lore.hobbies.HobbyDescription;

/**
 * Factory for hobby-related panels.
 * @author DAM
 */
public class HobbyPanelFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public HobbyPanelFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.HOBBY_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildHobbyPanel(id);
    }
    return ret;
  }

  private HobbyDisplayPanelController buildHobbyPanel(int hobbyID)
  {
    HobbiesManager hobbiesMgr=HobbiesManager.getInstance();
    HobbyDescription hobby=hobbiesMgr.getHobby(hobbyID);
    if (hobby!=null)
    {
      HobbyDisplayPanelController hobbyPanel=new HobbyDisplayPanelController(_parent,hobby);
      return hobbyPanel;
    }
    return null;
  }
}
