package delta.games.lotro.gui.character.main.summary;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.details.CharacterDetails;

/**
 * Test for character equipment display.
 * @author DAM
 */
public class MainTestCharacterSummaryPanelController
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    List<CharacterFile> toons=utils.getAllFiles();
    //CharacterFile toon=utils.getMainToon();
    for(CharacterFile toon : toons)
    {
      String name=toon.getName();
      System.out.println("Loading toon ["+name+"]");
      CharacterSummary summary=toon.getSummary();
      CharacterDetails details=toon.getDetails();
      DefaultWindowController c=new DefaultWindowController();
      CharacterSummaryPanelController ctrl=new CharacterSummaryPanelController(c);
      ctrl.setSummary(summary,details);
      JPanel panel=ctrl.getPanel();
      JFrame f=c.getFrame();
      f.setTitle("Summary for "+toon.getName());
      f.add(panel);
      f.pack();
      f.setVisible(true);
    }
  }
}
