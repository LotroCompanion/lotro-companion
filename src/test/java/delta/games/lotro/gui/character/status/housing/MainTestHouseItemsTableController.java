package delta.games.lotro.gui.character.status.housing;

import java.util.List;

import delta.common.ui.swing.tables.panel.GenericTablePanelController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.status.housing.House;
import delta.games.lotro.character.status.housing.HouseAddress;
import delta.games.lotro.character.status.housing.HouseIdentifier;
import delta.games.lotro.character.status.housing.HousingItem;
import delta.games.lotro.character.status.housing.io.HousingStatusIO;

/**
 * Test class for the house items table.
 * @author DAM
 */
class MainTestHouseItemsTableController
{
  private void doIt()
  {
    HouseAddress address=new HouseAddress(1879094517,1879094597);
    HouseIdentifier id=new HouseIdentifier("Landroval",address);
    House house=HousingStatusIO.loadHouse(id);
    List<HousingItem> items=house.getInterior().getItems();

    DefaultWindowController w=new DefaultWindowController();
    HouseItemsTableController t=new HouseItemsTableController(w,null,items,null);
    GenericTablePanelController<HousingItem> panelCtrl=new GenericTablePanelController<HousingItem>(w,t.getTableController());
    w.getFrame().add(panelCtrl.getPanel());
    w.getFrame().pack();
    w.show();
  }

  public static void main(String[] args)
  {
    new MainTestHouseItemsTableController().doIt();
  }
}
