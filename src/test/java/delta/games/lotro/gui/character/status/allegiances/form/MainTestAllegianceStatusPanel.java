package delta.games.lotro.gui.character.status.allegiances.form;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.status.allegiances.AllegianceStatus;
import delta.games.lotro.character.status.allegiances.AllegiancesStatusManager;
import delta.games.lotro.character.status.allegiances.io.AllegiancesStatusIo;
import delta.games.lotro.lore.allegiances.AllegianceDescription;
import delta.games.lotro.lore.allegiances.AllegiancesManager;

/**
 * Test class for the allegiance status panel.
 * @author DAM
 */
public class MainTestAllegianceStatusPanel
{
  private void doIt()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Utharr");
    AllegiancesStatusManager allegiancesStatusMgr=AllegiancesStatusIo.load(toon);
    for(AllegianceDescription allegiance : AllegiancesManager.getInstance().getAll())
    {
      AllegianceStatus status=allegiancesStatusMgr.get(allegiance,true);
      AllegianceStatusFormController panelCtrl=new AllegianceStatusFormController(status);
      JPanel panel=panelCtrl.getPanel();
      JFrame f=new JFrame();
      f.getContentPane().add(panel);
      //f.pack();
      f.setSize(700,300);
      f.setVisible(true);
      f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestAllegianceStatusPanel().doIt();
  }
}
