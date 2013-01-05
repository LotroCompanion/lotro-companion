package delta.games.lotro.gui.stats.warbands;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.LotroTestUtils;
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
    LotroTestUtils utils=new LotroTestUtils();
    CharacterFile allyriel=utils.getToonByName("Allyriel");
    stats.addToon(allyriel);
    CharacterFile allurwyn=utils.getToonByName("Allurwyn");
    stats.addToon(allurwyn);
    CharacterFile serilis=utils.getToonByName("Serilis");
    stats.addToon(serilis);
    CharacterFile glumlug=utils.getToonByName("Glumlug");
    stats.addToon(glumlug);
    WarbandsWindowController controller=new WarbandsWindowController(stats);
    controller.show();
  }
}
