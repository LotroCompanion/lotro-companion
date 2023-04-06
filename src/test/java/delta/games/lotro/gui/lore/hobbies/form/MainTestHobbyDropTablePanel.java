package delta.games.lotro.gui.lore.hobbies.form;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.games.lotro.lore.hobbies.HobbiesManager;
import delta.games.lotro.lore.hobbies.HobbyDescription;
import delta.games.lotro.lore.hobbies.rewards.HobbyDropTable;
import delta.games.lotro.lore.hobbies.rewards.HobbyRewards;
import delta.games.lotro.lore.hobbies.rewards.HobbyRewardsProfile;

/**
 * Test class for the hobby drop table panel.
 * @author DAM
 */
public class MainTestHobbyDropTablePanel
{
  private void doIt()
  {
    final JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    DefaultFormDialogController<Void> dialog=new DefaultFormDialogController<Void>(null,null)
    {
      @Override
      protected JPanel buildFormPanel()
      {
        return panel;
      }
    };
    final HobbyDropTablePanelController panelCtrl=new HobbyDropTablePanelController(dialog);
    HobbiesManager mgr=HobbiesManager.getInstance();
    HobbyDescription hobby=mgr.getAll().get(0);
    HobbyRewards rewards=hobby.getRewards();
    HobbyRewardsProfile profile=rewards.getKnownProfiles().get(0);
    HobbyDropTable table=profile.buildDropTable(1);
    panelCtrl.setEntries(table.getEntries());
    panel.add(panelCtrl.getPanel(),BorderLayout.CENTER);

    dialog.editModal();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestHobbyDropTablePanel().doIt();
  }
}
