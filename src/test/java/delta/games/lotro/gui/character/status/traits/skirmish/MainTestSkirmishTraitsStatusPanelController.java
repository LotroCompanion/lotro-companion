package delta.games.lotro.gui.character.status.traits.skirmish;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.traits.skirmish.SkirmishTraitsStatus;
import delta.games.lotro.character.status.traits.skirmish.io.SkirmishTraitsStatusIo;

/**
 * Test class for the skirmish traits status panel.
 * @author DAM
 */
public class MainTestSkirmishTraitsStatusPanelController
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    CharacterFile file=new LotroTestUtils().getToonByName("Giswald");
    SkirmishTraitsStatus status=SkirmishTraitsStatusIo.load(file);
    DefaultWindowController c=new DefaultWindowController();
    JFrame f=c.getFrame();
    SkirmishTraitsStatusPanelController s=new SkirmishTraitsStatusPanelController(c,status);
    JPanel panel=s.getPanel();
    f.add(panel);
    f.pack();
    f.setVisible(true);
  }
}
