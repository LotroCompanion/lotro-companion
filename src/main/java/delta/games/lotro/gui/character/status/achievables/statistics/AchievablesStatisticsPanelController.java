package delta.games.lotro.gui.character.status.achievables.statistics;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.statistics.AchievablesStatistics;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;
import delta.games.lotro.lore.quests.Achievable;

/**
 * Controller for a panel to show the statistics about some achievables.
 * @author DAM
 */
public class AchievablesStatisticsPanelController
{
  // Data
  private AchievablesStatistics _statistics;
  // UI
  private JPanel _panel;
  // Controllers
  private AchievablesStatisticsSummaryPanelController _summary;
  private TitlesDisplayPanelController _titles;
  private ReputationDisplayPanelController _reputation;
  private VirtuesDisplayPanelController _virtues;
  private VirtueXPDisplayPanelController _virtueXP;
  private ItemsDisplayPanelController _items;
  private EmotesDisplayPanelController _emotes;
  private TraitsDisplayPanelController _traits;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param statistics Statistics to show toon.
   * @param mode UI mode.
   */
  public AchievablesStatisticsPanelController(WindowController parent, AchievablesStatistics statistics, AchievableUIMode mode)
  {
    _statistics=statistics;
    _summary=new AchievablesStatisticsSummaryPanelController(statistics,mode);
    _titles=new TitlesDisplayPanelController(parent,statistics);
    _reputation=new ReputationDisplayPanelController(parent,statistics,mode);
    _virtues=new VirtuesDisplayPanelController(parent,statistics,mode);
    _virtueXP=new VirtueXPDisplayPanelController(parent,statistics,mode);
    _items=new ItemsDisplayPanelController(parent,statistics);
    _emotes=new EmotesDisplayPanelController(parent,statistics);
    _traits=new TraitsDisplayPanelController(parent,statistics);
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
    // Titles
    JPanel titlesPanel=_titles.getPanel();
    pane.add("Titles",titlesPanel);
    // Reputation
    JPanel reputationPanel=_reputation.getPanel();
    pane.add("Reputation",reputationPanel);
    // Virtues
    JPanel virtuesPanel=_virtues.getPanel();
    pane.add("Virtues",virtuesPanel);
    // Virtue XP
    JPanel virtueXpPanel=_virtueXP.getPanel();
    pane.add("Virtue XP",virtueXpPanel);
    // Items
    JPanel itemsPanel=_items.getPanel();
    pane.add("Items",itemsPanel);
    // Emotes
    JPanel emotesPanel=_emotes.getPanel();
    pane.add("Emotes",emotesPanel);
    // Traits
    JPanel traitsPanel=_traits.getPanel();
    pane.add("Traits",traitsPanel);
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
    _titles.update();
    _reputation.update();
    _virtues.update();
    _virtueXP.update();
    _items.update();
    _emotes.update();
    _traits.update();
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
    if (_virtueXP!=null)
    {
      _virtueXP.dispose();
      _virtueXP=null;
    }
    if (_items!=null)
    {
      _items.dispose();
      _items=null;
    }
    if (_emotes!=null)
    {
      _emotes.dispose();
      _emotes=null;
    }
    if (_traits!=null)
    {
      _traits.dispose();
      _traits=null;
    }
  }
}
