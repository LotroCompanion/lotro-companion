package delta.games.lotro.gui.character.status.housing;

import delta.games.lotro.character.status.housing.House;
import delta.games.lotro.character.status.housing.HouseAddress;
import delta.games.lotro.character.status.housing.HouseIdentifier;
import delta.games.lotro.character.status.housing.io.HousingStatusIO;

/**
 * Test class for the house display window.
 * @author DAM
 */
class MainTestHouseDisplayWindowController
{
  private void doIt()
  {
    HouseAddress address=new HouseAddress(1879094517,1879094572);
    HouseIdentifier id=new HouseIdentifier("Landroval",address);
    House house=HousingStatusIO.loadHouse(id);
    HouseDisplayWindowController w=new HouseDisplayWindowController(null,house);
    w.show();
  }

  public static void main(String[] args)
  {
    new MainTestHouseDisplayWindowController().doIt();
  }
}
