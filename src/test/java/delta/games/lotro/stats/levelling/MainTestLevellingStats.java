package delta.games.lotro.stats.levelling;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.gui.stats.levelling.CharacterLevelChartController;
import delta.games.lotro.stats.level.MultipleToonsLevellingStats;

/**
 * Test for character levelling graph.
 * @author DAM
 */
public class MainTestLevellingStats
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    List<CharacterFile> toons=utils.getAllFiles();
    MultipleToonsLevellingStats stats=new MultipleToonsLevellingStats();
    //String[] names={"Glumlug","Feroce","Tilmogrim","Beleganth"};
    //for(String name : names)
    //CharacterFile toon=utils.getMainToon();
    for(CharacterFile toon : toons)
    {
      stats.addToon(toon);
    }
    
    JFrame f=new JFrame();
    CharacterLevelChartController controller=new CharacterLevelChartController(stats);
    JPanel panel=controller.getPanel();
    f.getContentPane().add(panel);
    f.pack();
    f.setVisible(true);
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }
}
