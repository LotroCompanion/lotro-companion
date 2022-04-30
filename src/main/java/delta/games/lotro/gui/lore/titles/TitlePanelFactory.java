package delta.games.lotro.gui.lore.titles;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.titles.form.TitleDisplayPanelController;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.lore.titles.TitlesManager;

/**
 * Factory for title-related panels.
 * @author DAM
 */
public class TitlePanelFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public TitlePanelFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.TITLE_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildTitlePanel(id);
    }
    return ret;
  }

  private TitleDisplayPanelController buildTitlePanel(int titleID)
  {
    TitlesManager titlesMgr=TitlesManager.getInstance();
    TitleDescription title=titlesMgr.getTitle(titleID);
    if (title!=null)
    {
      TitleDisplayPanelController titlePanel=new TitleDisplayPanelController(_parent,title);
      return titlePanel;
    }
    return null;
  }
}
