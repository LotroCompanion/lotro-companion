package delta.games.lotro.gui.character.status.pvp;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.pvp.PVPStatus;
import delta.games.lotro.character.pvp.io.PVPStatusIO;

/**
 * Test class for the PVP status display panel.
 * @author DAM
 */
public class MainTestPvpStatusDisplayPanelController
{
  private void doIt()
  {
    DefaultWindowController w=new DefaultWindowController();

    LotroTestUtils utils=new LotroTestUtils();
    CharacterFile toon=utils.getToonByName("Reddeif");
    PVPStatus status=PVPStatusIO.loadPVPStatus(toon);
    if (status==null)
    {
      status=new PVPStatus();
    }
    PvpStatusDisplayPanelController panelCtrl=new PvpStatusDisplayPanelController();
    w.getFrame().add(panelCtrl.getPanel());
    panelCtrl.setData(status);
    w.setTitle(toon.getName());
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
    new MainTestPvpStatusDisplayPanelController().doIt();
  }
}
