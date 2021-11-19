package delta.games.lotro.gui.character.status.allegiances.summary;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.status.allegiances.AllegiancesStatusManager;
import delta.games.lotro.character.status.allegiances.io.AllegiancesStatusIo;

/**
 * @author dm
 */
public class MainTestAllegiancesStatusSummary
{
  private void doIt()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Giswald");
    AllegiancesStatusManager allegiancesStatusMgr=AllegiancesStatusIo.load(toon);
    AllegiancesGroupStatusPanelController panelCtrl=new AllegiancesGroupStatusPanelController("Mordor");
    panelCtrl.setStatus(allegiancesStatusMgr);
    JPanel panel=panelCtrl.getPanel();
    JFrame f=new JFrame();
    f.getContentPane().add(panel);
    f.pack();
    f.setVisible(true);
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    new MainTestAllegiancesStatusSummary().doIt();
  }
}
