package delta.games.lotro.gui.stats.crafting;

import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.stats.crafting.CraftingStats;
import delta.games.lotro.stats.crafting.ProfessionStat;

/**
 * Test for crafting history graph.
 * @author DAM
 */
public class MainTestCraftingHistoryChart
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    Locale.setDefault(Locale.US);
    LotroTestUtils utils=new LotroTestUtils();
    //for(CharacterFile toon : utils.getAllFiles())
    {
      CharacterFile toon=utils.getMainToon();
      //CharacterFile toon=utils.getToonByName("Glumlug");
      CharacterLog log=toon.getLastCharacterLog();
      if (log!=null)
      {
        CraftingStats stats=new CraftingStats(log.getName(),log);
        stats.dump(System.out);
        String[] professions=stats.getProfessions();
        for(String profession : professions)
        {
          ProfessionStat stat=stats.getProfessionStat(profession);
          JFrame f=new JFrame();
          CraftingHistoryChartController controller=new CraftingHistoryChartController(stat,true);
          JPanel panel=controller.getPanel();
          f.getContentPane().add(panel);
          f.pack();
          f.setVisible(true);
          f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }
      }
    }
  }
}
