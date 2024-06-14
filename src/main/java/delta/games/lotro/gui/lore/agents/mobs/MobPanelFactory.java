package delta.games.lotro.gui.lore.agents.mobs;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.agents.mobs.form.MobDisplayPanelController;
import delta.games.lotro.lore.agents.mobs.MobDescription;
import delta.games.lotro.lore.agents.mobs.MobsManager;

/**
 * Factory for mob-related panels.
 * @author DAM
 */
public class MobPanelFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public MobPanelFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.MOB_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildMobPanel(id);
    }
    return ret;
  }

  private MobDisplayPanelController buildMobPanel(int mobID)
  {
    MobsManager mobsMgr=MobsManager.getInstance();
    MobDescription mob=mobsMgr.getMobById(mobID);
    if (mob!=null)
    {
      MobDisplayPanelController panel=new MobDisplayPanelController(_parent,mob);
      return panel;
    }
    return null;
  }
}
