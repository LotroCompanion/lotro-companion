package delta.games.lotro.gui.character.tomes;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.tomes.TomesSet;
import delta.games.lotro.common.stats.WellKnownStat;

/**
 * Test for tomes edition panel.
 * @author DAM
 */
public class MainTestTomesEditionPanel
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    CharacterData c=new CharacterData();
    TomesSet tomes=c.getTomes();
    tomes.setTomeRank(WellKnownStat.MIGHT,5);
    TomesEditionPanelController controller=new TomesEditionPanelController(c);
    JFrame frame=new JFrame("Tomes edition");
    frame.add(controller.getPanel());
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
