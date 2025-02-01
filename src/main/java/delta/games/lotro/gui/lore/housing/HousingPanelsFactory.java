package delta.games.lotro.gui.lore.housing;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.character.status.housing.HouseAddress;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.housing.form.HouseDisplayPanelController;
import delta.games.lotro.gui.navigation.NavigationParameters;

/**
 * Factory for housing-related panels.
 * @author DAM
 */
public class HousingPanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public HousingPanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.HOUSE_PAGE))
    {
      int houseID=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      Integer neighborhoodID=pageId.getIntParameter(NavigationParameters.NEIGHBORHOOD_ID_PARAMETER);
      HouseAddress houseAddress=new HouseAddress(neighborhoodID.intValue(),houseID);
      ret=buildHousePanel(houseAddress);
    }
    return ret;
  }

  private HouseDisplayPanelController buildHousePanel(HouseAddress address)
  {
    HouseDisplayPanelController ret=new HouseDisplayPanelController(_parent,address);
    return ret;
  }
}
