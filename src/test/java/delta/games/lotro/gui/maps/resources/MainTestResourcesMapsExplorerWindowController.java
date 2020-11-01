package delta.games.lotro.gui.maps.resources;

import delta.games.lotro.dat.data.DataFacade;

/**
 * Test class for the resources map explorer window controller.
 * @author DAM
 */
public class MainTestResourcesMapsExplorerWindowController
{
  private void doIt()
  {
    DataFacade facade=new DataFacade();
    ResourcesMapsExplorerWindowController windowCtrl=new ResourcesMapsExplorerWindowController(null,facade);
    windowCtrl.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestResourcesMapsExplorerWindowController().doIt();
  }
}
