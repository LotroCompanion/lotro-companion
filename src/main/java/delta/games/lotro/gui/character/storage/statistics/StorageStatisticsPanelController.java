package delta.games.lotro.gui.character.storage.statistics;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.statistics.StorageStatistics;
import delta.games.lotro.character.storage.statistics.StorageStatisticsComputer;
import delta.games.lotro.character.storage.statistics.reputation.StorageFactionStats;
import delta.games.lotro.gui.common.statistics.items.ItemsDisplayPanelController;
import delta.games.lotro.gui.common.statistics.reputation.ReputationDisplayPanelController;
import delta.games.lotro.gui.common.statistics.reputation.ReputationTableController;

/**
 * Controller for a panel to show the statistics about stored items.
 * @author DAM
 */
public class StorageStatisticsPanelController extends AbstractPanelController
{
  // Data
  private StorageStatistics _statistics;
  // Controllers
  private StorageStatisticsSummaryPanelController _summary;
  private ItemsDisplayPanelController _items;
  private ReputationDisplayPanelController<StorageFactionStats> _reputation;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param statistics Statistics to show.
   */
  public StorageStatisticsPanelController(WindowController parent, StorageStatistics statistics)
  {
    super(parent);
    _statistics=statistics;
    _summary=new StorageStatisticsSummaryPanelController(statistics);
    _items=new ItemsDisplayPanelController(parent,statistics.getItemStats());
    ReputationTableController<StorageFactionStats> tableController=new StorageReputationTableController(this,statistics.getReputationStats());
    _reputation=new ReputationDisplayPanelController<StorageFactionStats>(parent,statistics.getReputationStats(),tableController);
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
    // Items
    JPanel itemsPanel=_items.getPanel();
    pane.add("Disenchantment",itemsPanel); // I18n
    // Reputation
    JPanel reputationPanel=_reputation.getPanel();
    pane.add("Reputation",reputationPanel); // I18n
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
    _items.update();
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
    if (_items!=null)
    {
      _items.dispose();
      _items=null;
    }
    if (_reputation!=null)
    {
      _reputation.dispose();
      _reputation=null;
    }
  }
}
