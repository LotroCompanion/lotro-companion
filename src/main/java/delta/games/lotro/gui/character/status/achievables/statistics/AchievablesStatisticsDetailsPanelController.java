package delta.games.lotro.gui.character.status.achievables.statistics;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.achievables.statistics.AchievablesStatistics;
import delta.games.lotro.character.status.achievables.statistics.emotes.EmoteEvent;
import delta.games.lotro.character.status.achievables.statistics.reputation.AchievablesFactionStats;
import delta.games.lotro.character.status.achievables.statistics.titles.TitleEvent;
import delta.games.lotro.character.status.achievables.statistics.traits.TraitEvent;
import delta.games.lotro.character.status.achievables.statistics.virtues.VirtueXPStatsFromAchievable;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;
import delta.games.lotro.gui.character.status.achievables.statistics.traits.TraitEventsTableController;
import delta.games.lotro.gui.common.statistics.ReputationTableController;
import delta.games.lotro.gui.lore.items.CountedItemsTableController;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for a panel to show the detailed statistics about some achievables.
 * @author DAM
 */
public class AchievablesStatisticsDetailsPanelController extends AbstractPanelController
{
  // Controllers
  private AchievablesStatisticsDetailedSummaryPanelController _summary;
  private AchievableStatisticsTabPanelController<TitleEvent> _titles;
  private AchievableStatisticsTabPanelController<AchievablesFactionStats> _reputation;
  private AchievableStatisticsTabPanelController<VirtueXPStatsFromAchievable> _virtueXP;
  private AchievableStatisticsTabPanelController<CountedItem<Item>> _items;
  private AchievableStatisticsTabPanelController<EmoteEvent> _emotes;
  private AchievableStatisticsTabPanelController<TraitEvent> _traits;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param statistics Statistics to show toon.
   * @param mode UI mode.
   */
  public AchievablesStatisticsDetailsPanelController(WindowController parent, AchievablesStatistics statistics, AchievableUIMode mode)
  {
    super(parent);
    _summary=new AchievablesStatisticsDetailedSummaryPanelController(statistics,mode);
    // TODO I18n:
    // Titles
    TitleEventsTableController titlesTable=new TitleEventsTableController(statistics);
    _titles=new AchievableStatisticsTabPanelController<TitleEvent>(parent,titlesTable.getTableController());
    _titles.configure(null,"Titles(s)");
    // Reputation
    ReputationTableController<AchievablesFactionStats> tableController=new AchievablesReputationTableController(this,statistics.getReputationStats(),mode);
    _reputation=new AchievableStatisticsTabPanelController<AchievablesFactionStats>(parent,tableController.getTableController());
    _reputation.configure(null,"Faction(s)");
    // Virtues
    VirtueXPFromAchievablesTableController virtuesTable=new VirtueXPFromAchievablesTableController(statistics,mode);
    _virtueXP=new AchievableStatisticsTabPanelController<VirtueXPStatsFromAchievable>(parent,virtuesTable.getTableController());
    _virtueXP.configure(null,"Source(s)");
    // Items
    CountedItemsTableController<Item> itemsTable=new CountedItemsTableController<Item>(null,statistics.getItemsStats().getItems(),null);
    _items=new AchievableStatisticsTabPanelController<CountedItem<Item>>(parent,itemsTable.getTableController());
    _items.configure(null,"Item(s)");
    // Emotes
    EmoteEventsTableController emotesTable=new EmoteEventsTableController(statistics);
    _emotes=new AchievableStatisticsTabPanelController<EmoteEvent>(parent,emotesTable.getTableController());
    _emotes.configure(null,"Emote(s)");
    // Traits
    TraitEventsTableController traitsTable=new TraitEventsTableController(statistics);
    _traits=new AchievableStatisticsTabPanelController<TraitEvent>(parent,traitsTable.getTableController());
    _traits.configure(null,"Trait(s)");
    JPanel panel=buildPanel();
    setPanel(panel);
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
    pane.add("Titles",titlesPanel); // I18n
    // Reputation
    JPanel reputationPanel=_reputation.getPanel();
    pane.add("Reputation",reputationPanel); // I18n
    // Virtue XP
    JPanel virtueXpPanel=_virtueXP.getPanel();
    pane.add("Virtue XP",virtueXpPanel); // I18n
    // Items
    JPanel itemsPanel=_items.getPanel();
    pane.add("Items",itemsPanel); // I18n
    // Emotes
    JPanel emotesPanel=_emotes.getPanel();
    pane.add("Emotes",emotesPanel); // I18n
    // Traits
    JPanel traitsPanel=_traits.getPanel();
    pane.add("Traits",traitsPanel); // I18n
    return panel;
  }

  /**
   * Update displayed statistics.
   */
  public void updateStats()
  {
    _summary.update();
    _titles.update();
    _reputation.update();
    _virtueXP.update();
    _items.update();
    _emotes.update();
    _traits.update();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
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
