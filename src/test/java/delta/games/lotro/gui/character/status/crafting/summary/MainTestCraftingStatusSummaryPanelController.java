package delta.games.lotro.gui.character.status.crafting.summary;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.crafting.CraftingStatus;
import delta.games.lotro.character.status.crafting.CraftingStatusSummaryBuilder;
import delta.games.lotro.character.status.crafting.summary.CraftingStatusSummary;

/**
 * Test class for the known skirmish traits status panel.
 * @author DAM
 */
public class MainTestCraftingStatusSummaryPanelController
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    for(CharacterFile file : new LotroTestUtils().getAllFiles())
    {
      CraftingStatus status=file.getCraftingMgr().getCraftingStatus();
      CraftingStatusSummaryBuilder b=new CraftingStatusSummaryBuilder();
      CraftingStatusSummary summary=b.buildSummary(status);
      if (summary.getProfessionStatuses().isEmpty())
      {
        continue;
      }
      DefaultWindowController c=new DefaultWindowController();
      JFrame f=c.getFrame();
      CraftingStatusSummaryPanelController ctrl=new CraftingStatusSummaryPanelController();
      ctrl.setStatus(summary);
      JPanel panel=ctrl.getPanel();
      f.add(panel);
      f.pack();
      f.setTitle(file.getName());
      f.setVisible(true);
    }
  }
}
