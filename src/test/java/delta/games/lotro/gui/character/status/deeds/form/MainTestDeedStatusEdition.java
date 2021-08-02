package delta.games.lotro.gui.character.status.deeds.form;

import java.util.List;

import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;

/**
 * Simple test class for the deed status edition dialog.
 * @author DAM
 */
public class MainTestDeedStatusEdition
{
  private void doIt()
  {
    DeedsManager deedsManager=DeedsManager.getInstance();
    List<DeedDescription> deeds=deedsManager.getAll();
    {
      DeedDescription deed=deeds.get(0);
      AchievableStatus status=new AchievableStatus(deed);
      status.setCompleted(true);
      status.setCompletionDate(Long.valueOf(System.currentTimeMillis()));
      status.updateInternalState();
      DeedStatusDialogController dialog=new DeedStatusDialogController(status,null);
      AchievableStatus newStatus=dialog.editModal();
      if (newStatus!=null)
      {
        System.out.println(newStatus);
      }
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestDeedStatusEdition().doIt();
  }
}
