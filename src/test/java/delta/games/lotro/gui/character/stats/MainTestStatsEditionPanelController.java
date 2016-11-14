package delta.games.lotro.gui.character.stats;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.STAT;

/**
 * Test class for the stats edition panel controller.
 * @author DAM
 */
public class MainTestStatsEditionPanelController
{
  private BasicStatsSet buildStats()
  {
    BasicStatsSet stats=new BasicStatsSet();
    stats.setStat(STAT.AGILITY,224);
    stats.setStat(STAT.PHYSICAL_MASTERY,1180);
    stats.setStat(STAT.DEVASTATE_MAGNITUDE_PERCENTAGE, 2.5f);
    stats.setStat(STAT.AUDACITY, 132);
    stats.setStat(STAT.MORALE, 12);
    return stats;
  }

  private JPanel buildPanel(BasicStatsSet stats)
  {
    StatsEditionPanelController ctrl=new StatsEditionPanelController();
    ctrl.initFromStats(stats);
    JPanel panel=ctrl.getPanel();
    return panel;
  }

  private void doIt()
  {
    JFrame frame=new JFrame();
    BasicStatsSet stats=buildStats();
    JPanel panel=buildPanel(stats);
    frame.add(panel);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestStatsEditionPanelController().doIt();
  }
}
