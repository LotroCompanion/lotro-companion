package delta.games.lotro.gui.character.status.tasks.statistics;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.tasks.TaskStatus;
import delta.games.lotro.character.status.tasks.TasksStatusManager;
import delta.games.lotro.character.status.tasks.statistics.TasksStatistics;

/**
 * Controller for a "tasks statistics" window.
 * @author DAM
 */
public class TasksStatisticsWindowController extends DefaultDialogController
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="TASKS_STATISTICS";

  // Data
  private TasksStatistics _statistics;
  private TasksStatusManager _statusMgr;
  private Filter<TaskStatus> _filter;
  // Controllers
  private TasksStatisticsPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param toon Character file.
   * @param statusMgr Status manager.
   * @param filter Current filter.
   */
  public TasksStatisticsWindowController(WindowController parent, CharacterFile toon, TasksStatusManager statusMgr, Filter<TaskStatus> filter)
  {
    super(parent);
    _statistics=new TasksStatistics();
    _panelController=new TasksStatisticsPanelController(this,_statistics);
    _statusMgr=statusMgr;
    _filter=filter;
    updateStats();
    updateTitle(toon);
  }

  private void updateTitle(CharacterFile toon)
  {
    String title=buildTitle(toon);
    getDialog().setTitle(title);
  }

  private String buildTitle(CharacterFile toon)
  {
    String name=toon.getName();
    String serverName=toon.getServerName();
    String title="Statistics for "+name+" @ "+serverName; // I18n
    return title;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel summaryPanel=_panelController.getPanel();
    return summaryPanel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setSize(700,700);
    dialog.setResizable(true);
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  /**
   * Update statistics.
   */
  public void updateStats()
  {
    // Update status
    List<TaskStatus> selectedTaskStatuses=new ArrayList<TaskStatus>();
    for(TaskStatus task : _statusMgr.getTasksStatuses())
    {
      if (_filter.accept(task))
      {
        selectedTaskStatuses.add(task);
      }
    }
    _panelController.updateStats(selectedTaskStatuses);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    // Data
    _statistics=null;
    _statusMgr=null;
    _filter=null;
    // Controllers
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    super.dispose();
  }
}
