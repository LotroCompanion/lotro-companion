package delta.games.lotro.gui.character.status.tasks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lore.tasks.deeds.TaskDeed;
import delta.games.lotro.lore.tasks.deeds.TaskDeedsBuilder;
import delta.games.lotro.lore.tasks.deeds.TaskDeedsManager;

/**
 * Controller for a panel to display a summary of task deeds status.
 * @author DAM
 */
public class TaskDeedsStatusPanelController
{
  // Data
  private TaskDeedsManager _taskDeedsMgr;
  // GUI
  private JPanel _panel;
  private JProgressBar _global;
  private JLabel _achievedDeed;
  private JPanel _deedInProgressPanel;
  private JLabel _deedInProgressLabel;
  private JProgressBar _deedInProgressProgress;

  /**
   * Constructor.
   */
  public TaskDeedsStatusPanelController()
  {
    _taskDeedsMgr=new TaskDeedsBuilder().build();
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
    // Components
    _global=buildProgressBar();
    _deedInProgressProgress=buildProgressBar();
    _achievedDeed=GuiFactory.buildLabel("");
    _deedInProgressLabel=GuiFactory.buildLabel("");
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Achieved deed
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,2,5),0,0);
    JPanel achievedPanel=buildLabeledBar("Achieved deed:",_achievedDeed,_global); // I18n
    panel.add(achievedPanel,c);
    // Deed in progress 
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,5,5),0,0);
    _deedInProgressPanel=buildLabeledBar("Next deed:",_deedInProgressLabel,_deedInProgressProgress); // I18n
    panel.add(_deedInProgressPanel,c);
    return panel;
  }

  private JPanel buildLabeledBar(String text, JLabel label, JProgressBar bar)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Line 1: text + label
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(GuiFactory.buildLabel(text),c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,0),0,0);
    panel.add(label,c);
    // Line 2: bar
    c=new GridBagConstraints(0,1,2,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(bar,c);
    return panel;
  }

  private JProgressBar buildProgressBar()
  {
    JProgressBar bar=new JProgressBar(SwingConstants.HORIZONTAL,0,1);
    bar.setBackground(GuiFactory.getBackgroundColor());
    bar.setForeground(Color.BLUE);
    bar.setBorderPainted(true);
    bar.setStringPainted(true);
    bar.setPreferredSize(new Dimension(200,25));
    return bar;
  }

  /**
   * Update the UI for the given tasks count.
   * @param tasksCount A tasks count.
   */
  public void update(int tasksCount)
  {
    // Global progress
    int maxTasks=_taskDeedsMgr.getMaxTasks();
    setBar(_global,0,maxTasks,tasksCount);
    // Achieved deed
    TaskDeed achieved=_taskDeedsMgr.getAchievedTaskDeed(tasksCount);
    String achievedLabel="-";
    if (achieved!=null)
    {
      achievedLabel=achieved.getDeed().getName();
    }
    _achievedDeed.setText(achievedLabel);
    // Deed in progress
    TaskDeed inProgress=_taskDeedsMgr.getTaskDeedInProgress(tasksCount);
    if (inProgress==null)
    {
      _deedInProgressPanel.setVisible(false);
    }
    else
    {
      _deedInProgressPanel.setVisible(true);
      String deedName=inProgress.getDeed().getName();
      _deedInProgressLabel.setText(deedName);
      int min=_taskDeedsMgr.getBaseTasksCount(inProgress);
      int max=inProgress.getTasksCount();
      setBar(_deedInProgressProgress,0,max-min,tasksCount-min);
    }
  }

  private void setBar(JProgressBar bar, int min, int max, int current)
  {
    bar.setMinimum(min);
    bar.setMaximum(max);
    int value=(current>max)?max:current;
    bar.setValue(value);
    String label=current+"/"+max;
    bar.setString(label);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _taskDeedsMgr=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _global=null;
    _achievedDeed=null;
    _deedInProgressPanel=null;
    _deedInProgressProgress=null;
  }
}
