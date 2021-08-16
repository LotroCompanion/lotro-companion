package delta.games.lotro.gui.character.status.tasks;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.tasks.quests.TaskQuest;
import delta.games.lotro.lore.tasks.quests.TaskQuestsBuilder;
import delta.games.lotro.lore.tasks.quests.TaskQuestsManager;

/**
 * Controller for a panel to display a summary of task quests status.
 * @author DAM
 */
public class TaskQuestsStatusPanelController
{
  // Data
  private TaskQuestsManager _taskQuestsManager;
  // GUI
  private JPanel _panel;
  private List<JLabel> _counts;

  /**
   * Constructor.
   */
  public TaskQuestsStatusPanelController()
  {
    _taskQuestsManager=new TaskQuestsBuilder().build();
    _panel=build();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    _counts=new ArrayList<JLabel>();
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    List<TaskQuest> taskQuests=_taskQuestsManager.getTaskQuests();
    int y=0;
    for(TaskQuest taskQuest : taskQuests)
    {
      // Quest name
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      String name=taskQuest.getQuest().getName();
      JLabel questName=GuiFactory.buildLabel(name+": ");
      panel.add(questName,c);
      // Completion count 
      c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,0,2,5),0,0);
      JLabel count=GuiFactory.buildLabel("");
      _counts.add(count);
      panel.add(count,c);
      y++;
    }
    return panel;
  }

  /**
   * Update the UI for the given quests status.
   * @param questsStatus Quests status.
   */
  public void update(AchievablesStatusManager questsStatus)
  {
    List<TaskQuest> quests=_taskQuestsManager.getTaskQuests();
    int nb=quests.size();
    for(int i=0;i<nb;i++)
    {
      TaskQuest taskQuest=quests.get(i);
      QuestDescription quest=taskQuest.getQuest();
      AchievableStatus questStatus=questsStatus.get(quest,false);
      Integer completionsCount=(questStatus!=null)?questStatus.getCompletionCount():null;
      int completionsCountInt=(completionsCount!=null)?completionsCount.intValue():0;
      int tasksCount=taskQuest.getTasksCount();
      String label="0";
      if (completionsCountInt>0)
      {
        label=completionsCountInt+"x"+tasksCount+"="+(completionsCountInt*tasksCount);
      }
      _counts.get(i).setText(label);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _taskQuestsManager=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _counts=null;
  }
}
