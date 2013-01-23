package delta.games.lotro.gui.stats.warbands;

import delta.games.lotro.stats.warbands.MultipleToonsWarbandsStats;

/**
 * Test for the warbands statistics window.
 * @author DAM
 */
public class MainTestShowWarbandsStatistics
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    MultipleToonsWarbandsStats stats=new MultipleToonsWarbandsStats();
    WarbandsWindowController controller=new WarbandsWindowController(stats);
    controller.show();
  }
}
