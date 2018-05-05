package delta.games.lotro.gui.stats.deeds.statistics;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.stats.deeds.DeedsStatistics;
import delta.games.lotro.stats.deeds.DeedsStatusManager;

/**
 * Controller for a panel to show the statistics about some deeds.
 * @author DAM
 */
public class DeedStatisticsPanelController
{
  // Data
  private DeedsStatistics _statistics;
  // UI
  private JPanel _panel;
  // Controllers
  private DeedStatisticsSummaryPanelController _summary;
  private TitlesDisplayPanelController _titles;
  private ReputationDisplayPanel _reputation;
  private VirtuesDisplayPanel _virtues;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param statistics Statistics to show toon.
   */
  public DeedStatisticsPanelController(WindowController parent, DeedsStatistics statistics)
  {
    _statistics=statistics;
    _summary=new DeedStatisticsSummaryPanelController(statistics);
    _titles=new TitlesDisplayPanelController(parent,statistics);
    _reputation=new ReputationDisplayPanel(parent,statistics);
    _virtues=new VirtuesDisplayPanel(parent,statistics);
    _panel=buildPanel();
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    // Summary panel
    JPanel summaryPanel=_summary.getPanel();
    panel.add(summaryPanel,BorderLayout.NORTH);
    JTabbedPane pane=GuiFactory.buildTabbedPane();
    panel.add(pane,BorderLayout.CENTER);
    // Titles
    JPanel titlesPanel=_titles.getPanel();
    pane.add("Titles",titlesPanel);
    // Reputation
    JPanel reputationPanel=_reputation.getPanel();
    pane.add("Reputation",reputationPanel);
    // Virtues
    JPanel virtuesPanel=_virtues.getPanel();
    pane.add("Virtues",virtuesPanel);
    return panel;
  }

  /**
   * Update statistics using the given deeds status.
   * @param status Deeds status to use.
   */
  public void updateStats(DeedsStatusManager status)
  {
    List<DeedDescription> deeds=DeedsManager.getInstance().getAll();
    _statistics.useDeeds(status,deeds);
    _summary.update();
    _titles.update();
    _reputation.update();
    _virtues.update();
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
    if (_titles!=null)
    {
      _titles.dispose();
      _titles=null;
    }
    if (_reputation!=null)
    {
      _reputation.dispose();
      _reputation=null;
    }
    if (_virtues!=null)
    {
      _virtues.dispose();
      _virtues=null;
    }
  }
}
