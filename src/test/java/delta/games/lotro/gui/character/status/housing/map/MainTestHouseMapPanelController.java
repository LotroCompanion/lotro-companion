package delta.games.lotro.gui.character.status.housing.map;

import java.awt.Component;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.status.housing.House;
import delta.games.lotro.character.status.housing.HouseAddress;
import delta.games.lotro.character.status.housing.HouseContents;
import delta.games.lotro.character.status.housing.HouseIdentifier;
import delta.games.lotro.character.status.housing.io.HousingStatusIO;
import delta.games.lotro.dat.data.DataFacade;

/**
 * Test class for the house map panel controller.
 * @author DAM
 */
public class MainTestHouseMapPanelController
{
  private DataFacade _facade=new DataFacade();

  private void doIt()
  {
    HouseAddress address=new HouseAddress(1879094517,1879094572);
    HouseIdentifier id=new HouseIdentifier("Landroval",address);
    House house=HousingStatusIO.loadHouse(id);
    doHouseMap(house.getInterior());
    doHouseMap(house.getExterior());
  }

  private void doHouseMap(HouseContents contents)
  {
    DefaultWindowController w=new DefaultWindowController();

    HouseMapPanelController ctrl=new HouseMapPanelController(w,_facade,contents);
    Component mapComponent=ctrl.getMapComponent();
    String title=ctrl.getMapTitle();
    w.getFrame().add(mapComponent);
    w.setTitle(title);
    w.getFrame().setResizable(false);
    w.pack();
    w.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestHouseMapPanelController().doIt();
  }
}
