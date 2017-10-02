package delta.games.lotro.gui.stats.reputation.synopsis;

import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

/**
 * Test for the reputation synopsis.
 * @author DAM
 */
public class MainTestReputationSynopsis
{
  private void doIt()
  {
    CharactersManager mgr=CharactersManager.getInstance();
    List<CharacterFile> allToons=mgr.getAllToons();
    MultipleToonsReputationStats stats=new MultipleToonsReputationStats();
    int i=0;
    for(CharacterFile toon : allToons)
    {
      if (i<60)
        stats.addToon(toon);
      i++;
    }
    ReputationSynopsisPanelController panelCtrl=new ReputationSynopsisPanelController(null,stats);
    final JPanel panel=panelCtrl.getPanel();
    DefaultWindowController w=new DefaultWindowController();
    w.getFrame().add(panel);
    w.getFrame().pack();
    w.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestReputationSynopsis().doIt();
  }
}
