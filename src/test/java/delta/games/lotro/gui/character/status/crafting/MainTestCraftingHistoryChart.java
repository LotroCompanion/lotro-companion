package delta.games.lotro.gui.character.status.crafting;

import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.crafting.CraftingStatus;
import delta.games.lotro.character.status.crafting.ProfessionStatus;
import delta.games.lotro.lore.crafting.Profession;

/**
 * Test for crafting history graph.
 * @author DAM
 */
public class MainTestCraftingHistoryChart
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    Locale.setDefault(Locale.US);
    LotroTestUtils utils=new LotroTestUtils();
    //for(CharacterFile toon : utils.getAllFiles())
    {
      CharacterFile toon=utils.getMainToon();
      CraftingStatus stats=toon.getCraftingMgr().getCraftingStatus();
      stats.dump(System.out);
      List<Profession> professions=stats.getKnownProfessions();
      for(Profession profession : professions)
      {
        ProfessionStatus stat=stats.getProfessionStatus(profession);
        JFrame f=new JFrame();
        ProfessionStatusPanelController controller=new ProfessionStatusPanelController(stat);
        JPanel panel=controller.getPanel();
        f.getContentPane().add(panel);
        f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      }
    }
  }
}
