package delta.games.lotro.gui.lore.quests;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;
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
      List<QuestDescription> quests=new ArrayList<QuestDescription>();
      quests.add(questsManager.getQuest(1879049768)); // OR requirement
      quests.add(questsManager.getQuest(1879381530)); // Complex requirement
      quests.add(questsManager.getQuest(1879215269)); // Simple requirements
      quests.add(questsManager.getQuest(1879164570)); // No requirements
      for(QuestDescription quest : quests)
      {
        showAchievableWindow(quest);
      }
    }
    // Deeds
    /*
    {
      DeedsManager deedsManager=DeedsManager.getInstance();
      List<DeedDescription> deeds=deedsManager.getAll();
      for(int i=0;i<5;i++)
      {
        DeedDescription deed=deeds.get(i);
        showAchievableWindow(deed);
      }
    }
    */
  }

  private void showAchievableWindow(Achievable achievable)
  {
    NavigatorWindowController window=NavigatorFactory.buildNavigator(null,_index);
    PageIdentifier ref=ReferenceConstants.getAchievableReference(achievable);
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
