package delta.games.lotro.gui.house;

import java.util.List;

import delta.common.ui.swing.tables.panel.GenericTablePanelController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.house.HouseEntry;
import delta.games.lotro.house.HousesManager;

/**
 * Test class for the house items table.
 * @author DAM
 */
class MainTestHousesTableController
{
  private void doIt()
  {
    HousesManager mgr=HousesManager.getInstance();
    List<HouseEntry> items=mgr.getAllHouses();

    DefaultWindowController w=new DefaultWindowController();
    HousesTableController t=new HousesTableController(w,null,items,null);
    GenericTablePanelController<HouseEntry> panelCtrl=new GenericTablePanelController<HouseEntry>(w,t.getTableController());
    w.getFrame().add(panelCtrl.getPanel());
    w.getFrame().pack();
    w.show();
  }

  public static void main(String[] args)
  {
    new MainTestHousesTableController().doIt();
  }
}
