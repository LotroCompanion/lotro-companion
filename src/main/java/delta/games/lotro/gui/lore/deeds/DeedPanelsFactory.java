package delta.games.lotro.gui.lore.deeds;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.deeds.form.DeedDisplayPanelController;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.quests.AchievableProxiesResolver;

/**
 * Factory for deed panels.
 * @author DAM
 */
public class DeedPanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public DeedPanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.DEED_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildDeedPanel(id);
    }
    return ret;
  }

  private DeedDisplayPanelController buildDeedPanel(int deedId)
  {
    DeedsManager deedsMgr=DeedsManager.getInstance();
    DeedDescription deed=deedsMgr.getDeed(deedId);
    if (deed!=null)
    {
      AchievableProxiesResolver.resolve(deed);
      DeedDisplayPanelController deedPanel=new DeedDisplayPanelController(_parent,deed);
      return deedPanel;
    }
    return null;
  }
}
