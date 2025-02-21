package delta.games.lotro.gui.character.status.housing;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.character.status.housing.House;
import delta.games.lotro.character.status.housing.HouseIdentifier;
import delta.games.lotro.character.status.housing.io.HousingStatusIO;

/**
 * Utility methods related to housing UI.
 * @author DAM
 */
public class HousingUiUtils
{
  /**
   * Show a house window.
   * @param houseIdentifier House identifier.
   * @param parent Parent window.
   */
  public static void showHouse(HouseIdentifier houseIdentifier, WindowController parent)
  {
    House house=HousingStatusIO.loadHouse(houseIdentifier);
    if (house==null)
    {
      GuiFactory.showInformationDialog(parent.getWindow(),"House not found!\nUse import to get it.","Warning!");
      return;
    }
    String windowId=HouseDisplayWindowController.getWindowIdentifier(houseIdentifier);
    WindowsManager mgr=parent.getWindowsManager();
    WindowController houseWindow=mgr.getWindow(windowId);
    if (houseWindow==null)
    {
      houseWindow=new HouseDisplayWindowController(parent,house);
    }
    houseWindow.bringToFront();
  }
}
