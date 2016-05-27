package delta.games.lotro.gui.character.stats;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Test class for the single stat display controller.
 * @author DAM
 */
public class MainTestSingleStatWidgetsController
{
  private void doIt()
  {
    BasicStatsSet value=new BasicStatsSet();
    value.addStat(STAT.MORALE,new FixedDecimalsInteger(100));
    value.addStat(STAT.CRITICAL_MELEE_PERCENTAGE,new FixedDecimalsInteger(12.3f));
    BasicStatsSet reference=new BasicStatsSet();
    reference.addStat(STAT.MORALE,new FixedDecimalsInteger(67));
    reference.addStat(STAT.CRITICAL_MELEE_PERCENTAGE,new FixedDecimalsInteger(13.3f));

    SingleStatWidgetsController moraleCtrl=new SingleStatWidgetsController("Morale",STAT.MORALE,false);
    moraleCtrl.updateStats(reference,value);
    showFrameForStat(moraleCtrl);
    SingleStatWidgetsController critCtrl=new SingleStatWidgetsController("Crit Melee %",STAT.CRITICAL_MELEE_PERCENTAGE,true);
    critCtrl.updateStats(reference,value);
    showFrameForStat(critCtrl);
  }

  private void showFrameForStat(SingleStatWidgetsController singleCtrl)
  {
    JPanel panel=new JPanel(new FlowLayout());
    panel.add(singleCtrl.getLabel());
    panel.add(singleCtrl.getValueLabel());
    panel.add(singleCtrl.getDeltaValueLabel());
    JFrame frame=new JFrame();
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
    new MainTestSingleStatWidgetsController().doIt();
  }
}
