package delta.games.lotro.gui.character.storage.statistics;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.utils.l10n.L10n;
import delta.games.lotro.character.storage.statistics.StorageStatistics;
import delta.games.lotro.character.storage.statistics.reputation.StorageReputationStats;
import delta.games.lotro.gui.common.money.MoneyDisplayController;

/**
 * Controller for a panel to show the summary of the statistics about stored items.
 * @author DAM
 */
public class StorageStatisticsSummaryPanelController
{
  // Data
  private StorageStatistics _statistics;
  // UI
  private JPanel _panel;
  private JLabel _reputation;
  private MoneyDisplayController _priceDisplay;
  private JLabel _totalItemXP;
  private JLabel _totalVirtueXP;

  /**
   * Constructor.
   * @param statistics Statistics to show.
   */
  public StorageStatisticsSummaryPanelController(StorageStatistics statistics)
  {
    _statistics=statistics;
    _panel=buildPanel();
    update();
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    // Stats panel
    JPanel statsPanel=GuiFactory.buildPanel(new GridBagLayout());
    TitledBorder border=GuiFactory.buildTitledBorder("Statistics"); // I18n
    statsPanel.setBorder(border);
    panel.add(statsPanel,BorderLayout.CENTER);
    GridBagConstraints cLabels=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,0),0,0);
    GridBagConstraints cValues=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);

    // Reputation
    statsPanel.add(GuiFactory.buildLabel("Reputation:"),cLabels); // I18n
    _reputation=GuiFactory.buildLabel("");
    statsPanel.add(_reputation,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Price
    statsPanel.add(GuiFactory.buildLabel("Total Value:"),cLabels); // I18n
    _priceDisplay=new MoneyDisplayController();
    statsPanel.add(_priceDisplay.getPanel(),cValues);
    cLabels.gridy++;cValues.gridy++;
    // Item XP
    statsPanel.add(GuiFactory.buildLabel("Total Item XP:"),cLabels); // I18n
    _totalItemXP=GuiFactory.buildLabel("");
    statsPanel.add(_totalItemXP,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Virtue XP
    statsPanel.add(GuiFactory.buildLabel("Total Virtue XP:"),cLabels); // I18n
    _totalVirtueXP=GuiFactory.buildLabel("");
    statsPanel.add(_totalVirtueXP,cValues);
    cLabels.gridy++;cValues.gridy++;

    return panel;
  }

  /**
   * Update display.
   */
  public void update()
  {
    // Reputation
    StorageReputationStats reputation=_statistics.getReputationStats();
    int nbFactions=reputation.getFactionsCount();
    int nbReputationPoints=reputation.getTotalReputationPoints();
    String nbReputationPointsStr=L10n.getString(nbReputationPoints);
    String nbFactionsStr=L10n.getString(nbFactions);
    String reputationStr=String.format("%s points, %s factions",nbReputationPointsStr,nbFactionsStr);
    _reputation.setText(reputationStr);
    // Price
    _priceDisplay.setMoney(_statistics.getTotalValue());
    // Item XP
    long totalItemXP=_statistics.getTotalItemXP();
    _totalItemXP.setText(L10n.getString(totalItemXP));
    // Virtue XP
    long totalVirtueXP=_statistics.getTotalVirtueXP();
    _totalVirtueXP.setText(L10n.getString(totalVirtueXP));
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
    _reputation=null;
    if (_priceDisplay!=null)
    {
      _priceDisplay.dispose();
      _priceDisplay=null;
    }
    _totalItemXP=null;
    _totalVirtueXP=null;
  }
}
