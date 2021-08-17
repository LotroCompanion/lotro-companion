package delta.games.lotro.gui.character.status.tasks.statistics;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.status.statistics.items.ItemsStats;
import delta.games.lotro.character.status.statistics.reputation.ReputationStats;
import delta.games.lotro.character.status.tasks.statistics.TasksStatistics;
import delta.games.lotro.gui.common.money.MoneyDisplayController;

/**
 * Controller for a panel to show the summary of the statistics about some achievables.
 * @author DAM
 */
public class TasksStatisticsSummaryPanelController
{
  // Data
  private TasksStatistics _statistics;
  // UI
  private JPanel _panel;
  private JPanel _statsPanel;
  private JLabel _distinctTasksCount;
  private JLabel _completionsCount;
  private JLabel _reputation;
  private JLabel _consumedItemsCount;
  private MoneyDisplayController _priceDisplay;
  private JLabel _earnedItemsCount;
  private JLabel _totalXP;
  private JLabel _totalItemXP;
  private JLabel _totalMountXP;

  /**
   * Constructor.
   * @param statistics Statistics to show.
   */
  public TasksStatisticsSummaryPanelController(TasksStatistics statistics)
  {
    _statistics=statistics;
    _panel=buildPanel();
    update();
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    // Stats panel
    _statsPanel=GuiFactory.buildPanel(new GridBagLayout());
    TitledBorder border=GuiFactory.buildTitledBorder("Statistics");
    _statsPanel.setBorder(border);
    panel.add(_statsPanel,BorderLayout.CENTER);
    GridBagConstraints cLabels=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,0),0,0);
    GridBagConstraints cValues=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);

    // Distinct tasks count
    _statsPanel.add(GuiFactory.buildLabel("Distinct tasks count:"),cLabels);
    _distinctTasksCount=GuiFactory.buildLabel("");
    _statsPanel.add(_distinctTasksCount,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Completions count
    _statsPanel.add(GuiFactory.buildLabel("Completions count:"),cLabels);
    _completionsCount=GuiFactory.buildLabel("");
    _statsPanel.add(_completionsCount,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Reputation
    _statsPanel.add(GuiFactory.buildLabel("Reputation:"),cLabels);
    _reputation=GuiFactory.buildLabel("");
    _statsPanel.add(_reputation,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Consumed items
    _statsPanel.add(GuiFactory.buildLabel("Consumed items:"),cLabels);
    _consumedItemsCount=GuiFactory.buildLabel("");
    _statsPanel.add(_consumedItemsCount,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Price
    _statsPanel.add(GuiFactory.buildLabel("Value:"),cLabels);
    _priceDisplay=new MoneyDisplayController();
    _statsPanel.add(_priceDisplay.getPanel(),cValues);
    cLabels.gridy++;cValues.gridy++;
    // Earned items
    _statsPanel.add(GuiFactory.buildLabel("Earned items:"),cLabels);
    _earnedItemsCount=GuiFactory.buildLabel("");
    _statsPanel.add(_earnedItemsCount,cValues);
    cLabels.gridy++;cValues.gridy++;
    // XP
    _statsPanel.add(GuiFactory.buildLabel("Total XP:"),cLabels);
    _totalXP=GuiFactory.buildLabel("");
    _statsPanel.add(_totalXP,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Item XP
    _statsPanel.add(GuiFactory.buildLabel("Total Item XP:"),cLabels);
    _totalItemXP=GuiFactory.buildLabel("");
    _statsPanel.add(_totalItemXP,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Mount XP
    _statsPanel.add(GuiFactory.buildLabel("Total Mount XP:"),cLabels);
    _totalMountXP=GuiFactory.buildLabel("");
    _statsPanel.add(_totalMountXP,cValues);
    cLabels.gridy++;cValues.gridy++;

    return panel;
  }

  /**
   * Update display.
   */
  public void update()
  {
    // Distinct tasks count
    int distinctTasksCount=_statistics.getDistinctCompletedTasksCount();
    _distinctTasksCount.setText(String.valueOf(distinctTasksCount));
    // Completions count
    int completionsCount=_statistics.getTaskCompletionsCount();
    _completionsCount.setText(String.valueOf(completionsCount));
    // Reputation
    ReputationStats reputation=_statistics.getReputationStats();
    int nbFactions=reputation.getFactionsCount();
    int nbReputationPoints=reputation.getTotalReputationPoints();
    String reputationStr=String.format("%d points, %d factions",Integer.valueOf(nbReputationPoints),Integer.valueOf(nbFactions));
    _reputation.setText(reputationStr);
    // Consumed items
    ItemsStats consumedItemsStats=_statistics.getConsumedItemsStats();
    _consumedItemsCount.setText(getItemsLabel(consumedItemsStats));
    // Price
    _priceDisplay.setMoney(_statistics.getConsumedItemsPrice());
    // Earned items
    ItemsStats earnedItemsStats=_statistics.getEarnedItemsStats();
    _earnedItemsCount.setText(getItemsLabel(earnedItemsStats));
    // XP
    _totalXP.setText(String.valueOf(_statistics.getTotalXP()));
    _totalItemXP.setText(String.valueOf(_statistics.getTotalItemXP()));
    _totalMountXP.setText(String.valueOf(_statistics.getTotalMountXP()));
  }

  private String getItemsLabel(ItemsStats stats)
  {
    int nbDistinctItems=stats.getDistinctItemsCount();
    int nbItems=stats.getItemsCount();
    String ret=String.format("%d items, (%d distinct)",Integer.valueOf(nbItems),Integer.valueOf(nbDistinctItems));
    return ret;
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
    _distinctTasksCount=null;
    _completionsCount=null;
    _reputation=null;
    _consumedItemsCount=null;
    if (_priceDisplay!=null)
    {
      _priceDisplay.dispose();
      _priceDisplay=null;
    }
    _earnedItemsCount=null;
    _totalXP=null;
    _totalItemXP=null;
    _totalMountXP=null;
  }
}