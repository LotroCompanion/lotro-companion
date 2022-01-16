package delta.games.lotro.gui.lore.traits;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.character.traits.TraitsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.traits.form.TraitDisplayPanelController;

/**
 * Factory for trait-related panels.
 * @author DAM
 */
public class TraitPanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public TraitPanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.TRAIT_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildTraitPanel(id);
    }
    return ret;
  }

  private TraitDisplayPanelController buildTraitPanel(int traitID)
  {
    TraitsManager traitsMgr=TraitsManager.getInstance();
    TraitDescription trait=traitsMgr.getTrait(traitID);
    if (trait!=null)
    {
      TraitDisplayPanelController traitPanel=new TraitDisplayPanelController(_parent,trait);
      return traitPanel;
    }
    return null;
  }
}
