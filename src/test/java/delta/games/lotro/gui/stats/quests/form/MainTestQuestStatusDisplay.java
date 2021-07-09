package delta.games.lotro.gui.stats.quests.form;

import java.util.List;

import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;

/**
 * Simple test class for the quest status display dialog.
 * @author DAM
 */
public class MainTestQuestStatusDisplay
{
  private void doIt()
  {
    QuestsManager questsManager=QuestsManager.getInstance();
    List<QuestDescription> quests=questsManager.getAll();
    {
      QuestDescription quest=quests.get(0);
      AchievableStatus status=new AchievableStatus(quest);
      status.setCompleted(true);
      status.setCompletionDate(Long.valueOf(System.currentTimeMillis()));
      status.updateInternalState();
      QuestStatusDialogController dialog=new QuestStatusDialogController(status,null);
      dialog.show();
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestQuestStatusDisplay().doIt();
  }
}
