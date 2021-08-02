package delta.games.lotro.gui.character.stats.details;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.io.xml.CharacterDataIO;
import delta.games.lotro.character.stats.BasicStatsSet;

/**
 * Test class for the character stats panel controller.
 * @author DAM
 */
public class MainTestDetailedCharacterStatsPanelController
{
  private void doIt()
  {
    CharacterData meva=loadCharacter("meva");
    CharacterData giswald=loadCharacter("giswald");
    BasicStatsSet stats=giswald.getStats();
    BasicStatsSet reference=meva.getStats();

    DetailedCharacterStatsPanelController panelCtrl=new DetailedCharacterStatsPanelController(null);
    JPanel panel=panelCtrl.getPanel();
    panelCtrl.setStats(reference,stats);

    JFrame frame=new JFrame();
    frame.add(panel);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  private CharacterData loadCharacter(String name)
  {
    File from=new File(name+".xml").getAbsoluteFile();
    CharacterData c=CharacterDataIO.getCharacterDescription(from);
    return c;
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestDetailedCharacterStatsPanelController().doIt();
  }
}
