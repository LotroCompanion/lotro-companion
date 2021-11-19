package delta.games.lotro.gui.character.status.allegiances.summary;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.status.allegiances.AllegiancesStatusManager;
import delta.games.lotro.character.status.allegiances.io.AllegiancesStatusIo;

/**
 * Test class for the allegiances status summary.
 * @author DAM
 */
public class MainTestAllegiancesStatusSummary
{
  private void doIt()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Giswald");
    AllegiancesStatusManager allegiancesStatusMgr=AllegiancesStatusIo.load(toon);
    AllegiancesStatusSummaryPanelController panelCtrl=new AllegiancesStatusSummaryPanelController();
    panelCtrl.setStatus(allegiancesStatusMgr);
    JPanel panel=panelCtrl.getPanel();
    JFrame f=new JFrame();
    f.getContentPane().add(panel);
    f.pack();
    f.setVisible(true);
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestAllegiancesStatusSummary().doIt();
  }
}
