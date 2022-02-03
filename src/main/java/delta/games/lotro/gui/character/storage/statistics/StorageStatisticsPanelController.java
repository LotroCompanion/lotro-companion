package delta.games.lotro.gui.character.storage.statistics;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.statistics.StorageStatistics;
import delta.games.lotro.character.storage.statistics.StorageStatisticsComputer;
import delta.games.lotro.character.storage.statistics.reputation.StorageFactionStats;
import delta.games.lotro.gui.common.statistics.reputation.ReputationDisplayPanelController;
import delta.games.lotro.gui.common.statistics.reputation.ReputationTableController;

/**
 * Controller for a panel to show the statistics about stored items.
 * @author DAM
 */
public class StorageStatisticsPanelController
{
  // Data
  private StorageStatistics _statistics;
  // UI
  private JPanel _panel;
  // Controllers
  private StorageStatisticsSummaryPanelController _summary;
  private ReputationDisplayPanelController<StorageFactionStats> _reputation;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param statistics Statistics to show.
   */
  public StorageStatisticsPanelController(WindowController parent, StorageStatistics statistics)
  {
    _statistics=statistics;
    _summary=new StorageStatisticsSummaryPanelController(statistics);
    ReputationTableController<StorageFactionStats> tableController=new StorageReputationTableController(statistics.getReputationStats());
    _reputation=new ReputationDisplayPanelController<StorageFactionStats>(parent,statistics.getReputationStats(),tableController);
    _panel=buildPanel();
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
    pane.add("Reputation",reputationPanel);
    return panel;
  }

  /**
   * Update statistics using the given items.
   * @param storedItems Items to use.
   */
  public void updateStats(List<StoredItem> storedItems)
  {
    StorageStatisticsComputer computer=new StorageStatisticsComputer();
    computer.computeStatistics(storedItems,_statistics);
    _summary.update();
    _reputation.update();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _statistics=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
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
  }
}
