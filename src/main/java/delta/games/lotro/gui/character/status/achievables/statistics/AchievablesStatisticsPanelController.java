package delta.games.lotro.gui.character.status.achievables.statistics;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.statistics.GlobalAchievablesStatistics;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;
import delta.games.lotro.lore.quests.Achievable;

/**
 * Controller for a panel to show the statistics about some achievables.
 * @author DAM
 */
public class AchievablesStatisticsPanelController
{
  // Data
  private GlobalAchievablesStatistics _statistics;
  // UI
  private JPanel _panel;
  // Controllers
  private AchievablesStatisticsSummaryPanelController _summary;
  private AchievablesStatisticsDetailsPanelController _acquired;
  private AchievablesStatisticsDetailsPanelController _toGet;
  private AchievablesStatisticsDetailsPanelController _total;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param statistics Statistics to show toon.
   * @param mode UI mode.
   */
  public AchievablesStatisticsPanelController(WindowController parent, GlobalAchievablesStatistics statistics, AchievableUIMode mode)
  {
    _statistics=statistics;
    _summary=new AchievablesStatisticsSummaryPanelController(statistics,mode);
    _acquired=new AchievablesStatisticsDetailsPanelController(parent,statistics.getAcquiredStats(),mode);
    _toGet=new AchievablesStatisticsDetailsPanelController(parent,statistics.getToGetStats(),mode);
    _total=new AchievablesStatisticsDetailsPanelController(parent,statistics.getTotalStats(),mode);
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
    // Acquired
    JPanel acquiredPanel=_acquired.getPanel();
    pane.add("Acquired",acquiredPanel); // I18n
    // To Get
    JPanel toGetPanel=_toGet.getPanel();
    pane.add("To Get",toGetPanel); // I18n
    // Total
    JPanel totalPanel=_total.getPanel();
    pane.add("Total",totalPanel); // I18n
    return panel;
  }

  /**
   * Update statistics using the given status.
   * @param status Status to use.
   * @param achievables Achievables to use.
   */
  public void updateStats(AchievablesStatusManager status, List<? extends Achievable> achievables)
  {
    _statistics.useAchievables(status,achievables);
    _summary.update();
    _acquired.updateStats();
    _toGet.updateStats();
    _total.updateStats();
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
    if (_acquired!=null)
    {
      _acquired.dispose();
      _acquired=null;
    }
    if (_toGet!=null)
    {
      _toGet.dispose();
      _toGet=null;
    }
    if (_total!=null)
    {
      _total.dispose();
      _total=null;
    }
  }
}
