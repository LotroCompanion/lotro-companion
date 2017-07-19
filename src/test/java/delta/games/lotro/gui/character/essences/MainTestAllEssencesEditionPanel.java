package delta.games.lotro.gui.character.essences;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.CharacterGenerationTools;
import delta.games.lotro.character.stats.CharacterGeneratorMeva;

/**
 * Test for all essences edition panel.
 * @author DAM
 */
public class MainTestAllEssencesEditionPanel
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    CharacterGenerationTools tools=new CharacterGenerationTools();
    CharacterGeneratorMeva mevaGenerator=new CharacterGeneratorMeva(tools);
    CharacterData meva=mevaGenerator.buildCharacter();
    meva.setDate(Long.valueOf(System.currentTimeMillis()));
    AllEssencesEditionPanelController panelCtrl=new AllEssencesEditionPanelController(null,meva);
    JFrame frame=new JFrame();
    frame.add(panelCtrl.getPanel());
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
