package delta.games.lotro.gui.house;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.panel.GenericTablePanelController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.status.housing.House;
import delta.games.lotro.character.status.housing.HouseAddress;
import delta.games.lotro.character.status.housing.HouseIdentifier;
import delta.games.lotro.character.status.housing.io.HousingStatusIO;

/**
 * Test class for the house items table.
 * @author DAM
 */
class MainTestHousesTableController
{
  private void doIt()
  {
    HouseAddress address=new HouseAddress(1879094517,1879094597);
    HouseIdentifier id=new HouseIdentifier("Landroval",address);
    House house=HousingStatusIO.loadHouse(id);
    HouseTableEntry entry=new HouseTableEntry(house);
    List<HouseTableEntry> items=new ArrayList<HouseTableEntry>();
    items.add(entry);

    DefaultWindowController w=new DefaultWindowController();
    HousesTableController t=new HousesTableController(w,null,items,null);
    GenericTablePanelController<HouseTableEntry> panelCtrl=new GenericTablePanelController<HouseTableEntry>(w,t.getTableController());
    w.getFrame().add(panelCtrl.getPanel());
    w.getFrame().pack();
    w.show();
  }

  public static void main(String[] args)
  {
    new MainTestHousesTableController().doIt();
  }
}
