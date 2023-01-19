package delta.games.lotro.gui.lore.races;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RacesManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.races.form.RaceDisplayPanelController;

/**
 * Factory for race-related panels.
 * @author DAM
 */
public class RacePanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public RacePanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.RACE_PAGE))
    {
      String key=pageId.getStringParameter(PageIdentifier.ID_PARAMETER);
      RaceDescription race=RacesManager.getInstance().getByKey(key);
      ret=buildRacePanel(race);
    }
    return ret;
  }

  private RaceDisplayPanelController buildRacePanel(RaceDescription race)
  {
    RaceDisplayPanelController traitPanel=new RaceDisplayPanelController(_parent,race);
    return traitPanel;
  }
}
