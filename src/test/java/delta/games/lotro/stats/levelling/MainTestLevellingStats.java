package delta.games.lotro.stats.levelling;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.LotroTestUtils;

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
    List<LevellingStats> stats=new ArrayList<LevellingStats>();
    //String[] names={"Glumlug","Feroce","Tilmogrim","Beleganth"};
    //for(String name : names)
    for(CharacterFile toon : toons)
    {
      CharacterLog log=toon.getLastCharacterLog();
      if (log!=null)
      {
        LevellingStats toonStats=new LevellingStats(log);
        stats.add(toonStats);
      }
    }
    System.out.println(stats);
    
    JFrame f=new JFrame();
    CharacterLevelChartController controller=new CharacterLevelChartController(stats);
    JPanel panel=controller.getPanel();
    f.getContentPane().add(panel);
    f.pack();
    f.setVisible(true);
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }
}
