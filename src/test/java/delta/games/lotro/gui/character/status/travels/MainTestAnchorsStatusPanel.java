package delta.games.lotro.gui.character.status.travels;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.status.travels.AnchorsStatusManager;
import delta.games.lotro.character.status.travels.io.AnchorsStatusIo;

/**
 * Test class for the anchors status panel.
 * @author DAM
 */
public class MainTestAnchorsStatusPanel
{
  private void doIt()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Glumlug");
    AnchorsStatusManager statusMgr=AnchorsStatusIo.load(toon);
    AnchorsStatusDisplayPanelController panelCtrl=new AnchorsStatusDisplayPanelController(null,statusMgr);
    DefaultWindowController w=new DefaultWindowController();
    w.getFrame().add(panelCtrl.getPanel());
    w.getFrame().pack();
    w.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestAnchorsStatusPanel().doIt();
  }
}
