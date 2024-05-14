package delta.games.lotro.gui.character.status.levelling;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.levelling.LevelHistory;
import delta.games.lotro.character.status.levelling.MultipleToonsLevellingStats;
import delta.games.lotro.gui.character.status.curves.DatedCurvesChartConfiguration;
import delta.games.lotro.gui.character.status.curves.DatedCurvesChartController;
import delta.games.lotro.gui.character.status.curves.MultipleToonsDatedCurvesProvider;

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
    for(CharacterFile toon : toons)
    {
      stats.addToon(toon);
    }

    JFrame f=new JFrame();
    DatedCurvesChartConfiguration configuration=new DatedCurvesChartConfiguration();
    configuration.setChartTitle("Characters levelling");
    configuration.setValueAxisLabel("Level");
    configuration.setValueAxisTicks(new double[]{1,5,10});
    LevelCurveProvider curveProvider=new LevelCurveProvider();
    MultipleToonsDatedCurvesProvider<LevelHistory> provider=new MultipleToonsDatedCurvesProvider<LevelHistory>(stats,curveProvider);
    DatedCurvesChartController controller=new DatedCurvesChartController(provider,configuration);
    JPanel panel=controller.getPanel();
    f.getContentPane().add(panel);
    f.pack();
    f.setVisible(true);
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }
}
