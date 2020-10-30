package delta.games.lotro.gui.maps.resources;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import delta.games.lotro.dat.data.DataFacade;

/**
 * Test class for the resources map explorer panel controller.
 * @author DAM
 */
public class MainTestResourcesMapsExplorerPanelController
{
  private void doIt()
  {
    DataFacade facade=new DataFacade();
    ResourcesMapsExplorerPanelController panelCtrl=new ResourcesMapsExplorerPanelController(null,facade);
    JFrame f=new JFrame();
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    f.getContentPane().add(panelCtrl.getPanel(),BorderLayout.CENTER);
    f.pack();
    f.setVisible(true);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestResourcesMapsExplorerPanelController().doIt();
  }
}
