package delta.games.lotro.gui.lore.agents.mobs;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lore.agents.mobs.MobDescription;
import delta.games.lotro.lore.agents.mobs.MobsManager;

/**
 * Test class for the non-imbued legendary attributes display panel.
 * @author DAM
 */
public class MainTestMobsTableController
{
  private void doIt()
  {
    List<MobDescription> mobs=MobsManager.getInstance().getMobs();
    MobsTableController ctrl=new MobsTableController(null,null,mobs);
    JTable table=ctrl.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    JFrame frame=new JFrame();
    frame.add(scroll);
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestMobsTableController().doIt();
  }
}
