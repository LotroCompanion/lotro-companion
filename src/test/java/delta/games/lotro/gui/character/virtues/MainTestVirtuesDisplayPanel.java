package delta.games.lotro.gui.character.virtues;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.CharacterGenerationTools;
import delta.games.lotro.character.stats.CharacterGeneratorMeva;
import delta.games.lotro.character.stats.virtues.VirtuesSet;

/**
 * Test for virtues display panel.
 * @author DAM
 */
public class MainTestVirtuesDisplayPanel
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
    VirtuesDisplayPanelController panelCtrl=new VirtuesDisplayPanelController();
    VirtuesSet virtues=meva.getVirtues();
    virtues.setSelectedVirtue(null,1);
    panelCtrl.setVirtues(virtues);
    JFrame frame=new JFrame();
    frame.add(panelCtrl.getPanel());
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
