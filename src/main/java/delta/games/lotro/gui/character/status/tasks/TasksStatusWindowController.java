package delta.games.lotro.gui.character.status.tasks;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.tasks.TaskStatus;
import delta.games.lotro.character.status.tasks.TasksStatusManager;
import delta.games.lotro.character.status.tasks.filter.TaskStatusFilter;
import delta.games.lotro.gui.character.status.achievables.filter.AchievableStatusFilterController;
import delta.games.lotro.gui.character.status.tasks.filter.TaskFilterController;
import delta.games.lotro.gui.character.status.tasks.statistics.TasksStatisticsWindowController;
import delta.games.lotro.gui.character.status.tasks.table.TaskStatusTableController;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.utils.NavigationUtils;
import delta.games.lotro.lore.quests.QuestDescription;

/**
 * Controller for a tasks status display window.
 * @author DAM
 */
public class TasksStatusWindowController extends DefaultDisplayDialogController<TasksStatusManager> implements FilterUpdateListener
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="TASKS_STATUS";

  private static final int MAX_HEIGHT=800;

  // Data
  private CharacterFile _toon;
  private AchievablesStatusManager _questsStatus;
  private TaskStatusFilter _filter;
  // Controllers
  private AchievableStatusFilterController _statusFilterController;
  private TaskFilterController _filterController;
  private TasksStatusPanelController _panelController;
  private TaskStatusTableController _tableController;
  private TaskDeedsStatusPanelController _taskDeedsController;
  private TaskQuestsStatusPanelController _taskQuestsController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   * @param questsStatus Quests status.
   * @param toon Parent toon.
   */
  public TasksStatusWindowController(WindowController parent, TasksStatusManager status, AchievablesStatusManager questsStatus, CharacterFile toon)
  {
    super(parent,status);
    _toon=toon;
    _questsStatus=questsStatus;
    _filter=new TaskStatusFilter();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(1000,300));
    dialog.setTitle("Tasks status"); // I18n
    dialog.pack();
    Dimension size=dialog.getSize();
    if (size.height>MAX_HEIGHT)
    {
      dialog.setSize(size.width,MAX_HEIGHT);
    }
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    initTable();
    _panelController=new TasksStatusPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Build child controllers
    _filterController=new TaskFilterController(this,_filter,this);
    _statusFilterController=new AchievableStatusFilterController(_filter.getQuestStatusFilter(),this);
    _taskDeedsController=new TaskDeedsStatusPanelController();
    _taskDeedsController.update(_data.getCompletedTasksCount());
    _taskQuestsController=new TaskQuestsStatusPanelController();
    _taskQuestsController.update(_questsStatus);
    // Whole panel
    // - filter
    JPanel filterPanel=buildFilterPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    // - deeds and quests status
    JPanel statusPanel=GuiFactory.buildPanel(new GridBagLayout());
    // - deeds status
    JPanel taskDeedsPanel=_taskDeedsController.getPanel();
    taskDeedsPanel.setBorder(GuiFactory.buildTitledBorder("Task Deeds")); // I18n
    c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    statusPanel.add(taskDeedsPanel,c);
    // - quests status
    JPanel taskQuestsPanel=_taskQuestsController.getPanel();
    taskQuestsPanel.setBorder(GuiFactory.buildTitledBorder("Task Quests")); // I18n
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    statusPanel.add(taskQuestsPanel,c);

    c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(statusPanel,c);
    // - table
    c=new GridBagConstraints(0,1,2,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  private JPanel buildFilterPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,3,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Task filter
    JPanel taskFilterPanel=_filterController.getPanel();
    taskFilterPanel.setBorder(GuiFactory.buildTitledBorder("Task Filter")); // I18n
    panel.add(taskFilterPanel,c);
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Status filter
    JPanel statusFilterPanel=_statusFilterController.getPanel();
    statusFilterPanel.setBorder(GuiFactory.buildTitledBorder("Status Filter")); // I18n
    panel.add(statusFilterPanel,c);
    // Stats button
    JButton b=GuiFactory.buildButton("Stats");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showStatistics();
      }
    };
    b.addActionListener(al);
    c=new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(b,c);
    c=new GridBagConstraints(2,1,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(Box.createGlue(),c);
    return panel;
  }

  private void initTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("TasksStatus");
    _tableController=new TaskStatusTableController(this,_data,prefs,_filter);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          TaskStatus status=(TaskStatus)event.getSource();
          showQuest(status.getTask().getQuest());
        }
      }
    };
    _tableController.getTableController().addActionListener(al);
  }

  @Override
  public void filterUpdated()
  {
    _panelController.filterUpdated();
    TasksStatisticsWindowController statisticsController=getStatisticsWindow();
    if (statisticsController!=null)
    {
      statisticsController.updateStats();
    }
  }

  private void showQuest(QuestDescription quest)
  {
    PageIdentifier ref=ReferenceConstants.getAchievableReference(quest);
    NavigationUtils.navigateTo(ref,this);
  }

  private TasksStatisticsWindowController getStatisticsWindow()
  {
    WindowsManager windowsMgr=getWindowsManager();
    TasksStatisticsWindowController ret=(TasksStatisticsWindowController)windowsMgr.getWindow(TasksStatisticsWindowController.IDENTIFIER);
    return ret;
  }

  private void showStatistics()
  {
    TasksStatisticsWindowController statisticsController=getStatisticsWindow();
    if (statisticsController==null)
    {
      statisticsController=new TasksStatisticsWindowController(this,_toon,_data,_filter);
      WindowsManager windowsMgr=getWindowsManager();
      windowsMgr.registerWindow(statisticsController);
      statisticsController.getWindow().setLocationRelativeTo(getWindow());
    }
    statisticsController.bringToFront();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _data=null;
    _toon=null;
    _filter=null;
    // Controllers
    if (_statusFilterController!=null)
    {
      _statusFilterController.dispose();
      _statusFilterController=null;
    }
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    if (_taskDeedsController!=null)
    {
      _taskDeedsController.dispose();
      _taskDeedsController=null;
    }
    if (_taskQuestsController!=null)
    {
      _taskQuestsController.dispose();
      _taskQuestsController=null;
    }
  }
}
