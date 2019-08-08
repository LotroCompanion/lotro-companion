package delta.games.lotro.gui.quests;

import java.util.List;

import delta.games.lotro.gui.common.navigator.NavigatorWindowController;
import delta.games.lotro.gui.common.navigator.ReferenceConstants;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;

/**
 * Simple test class to show some quest/deed windows.
 * @author DAM
 */
public class MainTestAchievableFormWindow
{
  private int _index;

  private void doIt()
  {
    // Quests
    {
      QuestsManager questsManager=QuestsManager.getInstance();
      List<QuestDescription> quests=questsManager.getAll();
      for(int i=0;i<5;i++)
      {
        QuestDescription quest=quests.get(i);
        showAchievableWindow(quest);
      }
    }
    // Deeds
    {
      DeedsManager deedsManager=DeedsManager.getInstance();
      List<DeedDescription> deeds=deedsManager.getAll();
      for(int i=0;i<5;i++)
      {
        DeedDescription deed=deeds.get(i);
        showAchievableWindow(deed);
      }
    }
  }

  private void showAchievableWindow(Achievable achievable)
  {
    NavigatorWindowController window=new NavigatorWindowController(null,_index);
    String ref=ReferenceConstants.getAchievableReference(achievable);
    window.navigateTo(ref);
    window.show(false);
    _index++;
  }

  /**
   * Main method for this test class.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestAchievableFormWindow().doIt();
  }
}
