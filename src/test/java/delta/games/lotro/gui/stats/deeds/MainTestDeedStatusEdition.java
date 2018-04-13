package delta.games.lotro.gui.stats.deeds;

import java.util.List;

import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.stats.deeds.DeedStatus;

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
      DeedStatus status=new DeedStatus(deed.getKey());
      status.setCompleted(Boolean.TRUE);
      status.setCompletionDate(Long.valueOf(System.currentTimeMillis()));
      DeedStatusEditionDialogController dialog=new DeedStatusEditionDialogController(deed,status,null);
      DeedStatus newStatus=dialog.editModal();
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
