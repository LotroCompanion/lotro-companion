package delta.games.lotro.gui.character.stats;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.character.Character;
import delta.games.lotro.character.io.xml.CharacterXMLParser;
import delta.games.lotro.character.stats.BasicStatsSet;

/**
 * Test class for the character stats panel controller.
 * @author DAM
 */
public class MainTestCharacterStatsPanelController
{
  private void doIt()
  {
    Character meva=loadCharacter("meva");
    Character giswald=loadCharacter("giswald");
    BasicStatsSet stats=giswald.getStats();
    BasicStatsSet reference=meva.getStats();

    CharacterStatsPanelController panelCtrl=new CharacterStatsPanelController();
    JPanel panel=panelCtrl.getPanel();
    panelCtrl.setStats(reference,stats);

    JFrame frame=new JFrame();
    frame.add(panel);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  private Character loadCharacter(String name)
  {
    CharacterXMLParser parser=new CharacterXMLParser();
    File from=new File(name+".xml").getAbsoluteFile();
    Character c=parser.parseXML(from);
    return c;
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestCharacterStatsPanelController().doIt();
  }
}
