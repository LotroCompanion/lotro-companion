package delta.games.lotro.gui.character.status.tasks.statistics;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.achievables.statistics.reputation.AchievablesFactionStats;
import delta.games.lotro.character.status.tasks.TaskStatus;
import delta.games.lotro.character.status.tasks.statistics.TasksStatistics;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;
import delta.games.lotro.gui.character.status.achievables.statistics.reputation.AchievablesReputationTableController;
import delta.games.lotro.gui.common.statistics.items.ItemsDisplayPanelController;
import delta.games.lotro.gui.common.statistics.reputation.ReputationDisplayPanelController;
import delta.games.lotro.gui.common.statistics.reputation.ReputationTableController;

/**
 * Controller for a panel to show the statistics about some tasks.
 * @author DAM
 */
public class TasksStatisticsPanelController extends AbstractPanelController
{
  // Data
  private TasksStatistics _statistics;
  // Controllers
  private TasksStatisticsSummaryPanelController _summary;
  private ReputationDisplayPanelController<AchievablesFactionStats> _reputation;
  private ItemsDisplayPanelController _consumedItems;
  private ItemsDisplayPanelController _earnedItems;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param statistics Statistics to show toon.
   */
  public TasksStatisticsPanelController(WindowController parent, TasksStatistics statistics)
  {
    super(parent);
    _statistics=statistics;
    _summary=new TasksStatisticsSummaryPanelController(statistics);
    ReputationTableController<AchievablesFactionStats> tableController=new AchievablesReputationTableController(this,statistics.getReputationStats(),AchievableUIMode.QUEST);
    _reputation=new ReputationDisplayPanelController<AchievablesFactionStats>(parent,statistics.getReputationStats(),tableController);
    _consumedItems=new ItemsDisplayPanelController(parent,statistics.getConsumedItemsStats());
    _earnedItems=new ItemsDisplayPanelController(parent,statistics.getEarnedItemsStats());
    JPanel panel=buildPanel();
    setPanel(panel);
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    // Summary panel
    JPanel summaryPanel=_summary.getPanel();
    panel.add(summaryPanel,BorderLayout.NORTH);
    JTabbedPane pane=GuiFactory.buildTabbedPane();
    panel.add(pane,BorderLayout.CENTER);
    // Reputation
    JPanel reputationPanel=_reputation.getPanel();
    pane.add("Reputation",reputationPanel); // I18n
    // Consumed items
    JPanel itemsPanel=_consumedItems.getPanel();
    pane.add("Consumed items",itemsPanel); // I18n
    // Earned items
    JPanel earnedItemsPanel=_earnedItems.getPanel();
    pane.add("Earned items",earnedItemsPanel); // I18n
    return panel;
  }

  /**
   * Update statistics using the given status.
   * @param selectedTaskStatuses Statuses to use.
   */
  public void updateStats(List<TaskStatus> selectedTaskStatuses)
  {
    _statistics.useTasks(selectedTaskStatuses);
    _summary.update();
    _reputation.update();
    _consumedItems.update();
    _earnedItems.update();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    // Data
    _statistics=null;
    // Controllers
    if (_summary!=null)
    {
      _summary.dispose();
      _summary=null;
    }
    if (_reputation!=null)
    {
      _reputation.dispose();
      _reputation=null;
    }
    if (_consumedItems!=null)
    {
      _consumedItems.dispose();
      _consumedItems=null;
    }
    if (_earnedItems!=null)
    {
      _earnedItems.dispose();
      _earnedItems=null;
    }
  }
}
