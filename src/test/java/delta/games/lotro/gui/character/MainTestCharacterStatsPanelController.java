package delta.games.lotro.gui.character;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import delta.games.lotro.character.Character;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.CharacterGenerationTools;
import delta.games.lotro.character.stats.CharacterGeneratorMeva;
import delta.games.lotro.character.stats.CharacterStatsComputer;

/**
 * Test for character stats panel window.
 * @author DAM
 */
public class MainTestCharacterStatsPanelController
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    CharacterGenerationTools tools=new CharacterGenerationTools();
    CharacterGeneratorMeva mevaGenerator=new CharacterGeneratorMeva(tools);
    Character meva=mevaGenerator.buildCharacter();
    CharacterStatsComputer statsComputer=new CharacterStatsComputer();
    BasicStatsSet stats=statsComputer.getStats(meva);
    meva.getStats().setStats(stats);
    ChararacterStatsPanelController panelCtrl=new ChararacterStatsPanelController(meva);
    JFrame frame=new JFrame();
    frame.add(panelCtrl.getPanel());
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
