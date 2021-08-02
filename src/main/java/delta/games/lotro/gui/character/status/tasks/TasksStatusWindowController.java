package delta.games.lotro.gui.character.status.tasks;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.tasks.TaskStatus;
import delta.games.lotro.character.status.tasks.filter.TaskStatusFilter;
import delta.games.lotro.gui.character.status.achievables.filter.AchievableStatusFilterController;
import delta.games.lotro.gui.character.status.tasks.filter.TaskFilterController;
import delta.games.lotro.gui.character.status.tasks.table.TaskStatusTableController;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.tasks.Task;
import delta.games.lotro.lore.tasks.TasksRegistry;

/**
 * Controller for a tasks status display window.
 * @author DAM
 */
public class TasksStatusWindowController extends DefaultDisplayDialogController<AchievablesStatusManager> implements FilterUpdateListener
{
  private static final int MAX_HEIGHT=800;

  // Data
  private List<Task> _tasks;
  private TaskStatusFilter _filter;
  // Controllers
  private AchievableStatusFilterController _statusFilterController;
  private TaskFilterController _filterController;
  private TasksStatusPanelController _panelController;
  private TaskStatusTableController _tableController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   * @param toon Parent toon.
   */
  public TasksStatusWindowController(WindowController parent, AchievablesStatusManager status, CharacterFile toon)
  {
    super(parent,status);
    _tasks=new ArrayList<Task>(TasksRegistry.getInstance().getTasks());
    _filter=new TaskStatusFilter();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setTitle("Tasks status");
    dialog.pack();
    Dimension size=dialog.getSize();
    if (size.height>MAX_HEIGHT)
    {
      dialog.setSize(size.width,MAX_HEIGHT);
    }
    return dialog;
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
    // Quest filter
    _filterController=new TaskFilterController(_filter,this,false);
    JPanel taskFilterPanel=_filterController.getPanel();
    taskFilterPanel.setBorder(GuiFactory.buildTitledBorder("Task Filter"));
    // Status filter
    _statusFilterController=new AchievableStatusFilterController(_filter.getQuestStatusFilter(),this);
    JPanel statusFilterPanel=_statusFilterController.getPanel();
    TitledBorder statusFilterBorder=GuiFactory.buildTitledBorder("Status Filter");
    statusFilterPanel.setBorder(statusFilterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(taskFilterPanel,c);
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(statusFilterPanel,c);
    c=new GridBagConstraints(1,1,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(Box.createGlue(),c);
    c=new GridBagConstraints(0,2,2,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  private void initTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("TasksStatus");
    _tableController=new TaskStatusTableController(_data,prefs,_filter,_tasks);
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
  }

  private void showQuest(QuestDescription quest)
  {
    WindowsManager windowsMgr=getWindowsManager();
    int id=windowsMgr.getAll().size();
    NavigatorWindowController window=NavigatorFactory.buildNavigator(TasksStatusWindowController.this,id);
    PageIdentifier ref=ReferenceConstants.getAchievableReference(quest);
    window.navigateTo(ref);
    window.show(false);
    windowsMgr.registerWindow(window);
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
    _tasks=null;
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
  }
}
