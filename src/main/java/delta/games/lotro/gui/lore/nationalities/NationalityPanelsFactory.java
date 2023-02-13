package delta.games.lotro.gui.lore.nationalities;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.character.races.NationalitiesManager;
import delta.games.lotro.character.races.NationalityDescription;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.nationalities.form.NationalityDisplayPanelController;

/**
 * Factory for nationality-related panels.
 * @author DAM
 */
public class NationalityPanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public NationalityPanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.NATIONALITY_PAGE))
    {
      int code=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      NationalityDescription nationality=NationalitiesManager.getInstance().getNationalityDescription(code);
      ret=buildNationalityPanel(nationality);
    }
    return ret;
  }

  private NationalityDisplayPanelController buildNationalityPanel(NationalityDescription nationality)
  {
    NationalityDisplayPanelController traitPanel=new NationalityDisplayPanelController(_parent,nationality);
    return traitPanel;
  }
}
