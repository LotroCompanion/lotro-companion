package delta.games.lotro.gui.character.status.achievables.statistics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.utils.l10n.L10n;
import delta.games.lotro.character.status.achievables.statistics.AchievablesStatistics;
import delta.games.lotro.character.status.achievables.statistics.reputation.AchievablesReputationStats;
import delta.games.lotro.character.status.achievables.statistics.virtues.VirtueXPStatsFromAchievables;
import delta.games.lotro.common.statistics.items.ItemsStats;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;

/**
 * Controller for a panel to show the detailed summary of the statistics about some achievables.
 * @author DAM
 */
public class AchievablesStatisticsDetailedSummaryPanelController
{
  // Data
  private AchievablesStatistics _statistics;
  private AchievableUIMode _mode;
  // UI
  private JPanel _panel;
  private JLabel _lotroPoints;
  private JLabel _classPoints;
  private JLabel _marks;
  private JLabel _medallions;
  private JLabel _titlesCount;
  private JLabel _reputation;
  private JLabel _virtues;
  private JLabel _virtueXP;
  private JLabel _itemsCount;
  private JLabel _emotesCount;
  private JLabel _traitsCount;

  /**
   * Constructor.
   * @param statistics Statistics to show.
   * @param mode UI mode.
   */
  public AchievablesStatisticsDetailedSummaryPanelController(AchievablesStatistics statistics, AchievableUIMode mode)
  {
    _statistics=statistics;
    _mode=mode;
    _panel=buildPanel();
    update();
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    TitledBorder border=GuiFactory.buildTitledBorder("Statistics"); // I18n
    ret.setBorder(border);
    GridBagConstraints cLabels=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,0),0,0);
    GridBagConstraints cValues=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);

    // LOTRO points
    ret.add(GuiFactory.buildLabel("LOTRO Points:"),cLabels); // I18n
    _lotroPoints=GuiFactory.buildLabel("");
    ret.add(_lotroPoints,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Class points
    ret.add(GuiFactory.buildLabel("Class Points:"),cLabels); // I18n
    _classPoints=GuiFactory.buildLabel("");
    ret.add(_classPoints,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Marks
    ret.add(GuiFactory.buildLabel("Marks:"),cLabels); // I18n
    _marks=GuiFactory.buildLabel("");
    ret.add(_marks,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Medallions
    ret.add(GuiFactory.buildLabel("Medallions:"),cLabels); // I18n
    _medallions=GuiFactory.buildLabel("");
    ret.add(_medallions,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Titles
    ret.add(GuiFactory.buildLabel("Titles:"),cLabels); // I18n
    _titlesCount=GuiFactory.buildLabel("");
    ret.add(_titlesCount,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Reputation
    ret.add(GuiFactory.buildLabel("Reputation:"),cLabels); // I18n
    _reputation=GuiFactory.buildLabel("");
    ret.add(_reputation,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Virtues
    ret.add(GuiFactory.buildLabel("Virtues:"),cLabels); // I18n
    _virtues=GuiFactory.buildLabel("");
    ret.add(_virtues,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Virtue XP
    ret.add(GuiFactory.buildLabel("Virtue XP:"),cLabels); // I18n
    _virtueXP=GuiFactory.buildLabel("");
    ret.add(_virtueXP,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Items
    ret.add(GuiFactory.buildLabel("Items:"),cLabels); // I18n
    _itemsCount=GuiFactory.buildLabel("");
    ret.add(_itemsCount,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Emotes
    ret.add(GuiFactory.buildLabel("Emotes:"),cLabels); // I18n
    _emotesCount=GuiFactory.buildLabel("");
    ret.add(_emotesCount,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Traits
    ret.add(GuiFactory.buildLabel("Traits:"),cLabels); // I18n
    _traitsCount=GuiFactory.buildLabel("");
    ret.add(_traitsCount,cValues);
    cLabels.gridy++;cValues.gridy++;

    return ret;
  }

  /**
   * Update display.
   */
  public void update()
  {
    // LOTRO points
    int lotroPoints=_statistics.getAcquiredLP();
    _lotroPoints.setText(L10n.getString(lotroPoints));
    // Class points
    int classPoints=_statistics.getClassPoints();
    _classPoints.setText(L10n.getString(classPoints));
    // Marks
    int marks=_statistics.getMarksCount();
    _marks.setText(L10n.getString(marks));
    // Medallions
    int medallions=_statistics.getMedallionsCount();
    _medallions.setText(L10n.getString(medallions));
    // Titles count
    int nbTitles=_statistics.getTitles().size();
    _titlesCount.setText(L10n.getString(nbTitles));
    // Reputation
    AchievablesReputationStats reputation=_statistics.getReputationStats();
    int nbFactions=reputation.getFactionsCount();
    String nbFactionsStr=L10n.getString(nbFactions);
    int nbReputationPoints=reputation.getTotalReputationPoints();
    String nbReputationPointsStr=L10n.getString(nbReputationPoints);
    String reputationStr=String.format("%s points, %s factions",nbReputationPointsStr,nbFactionsStr); // I18n
    _reputation.setText(reputationStr);
    // Virtues
    int nbVirtues=_statistics.getVirtues().size();
    String nbVirtuesStr=L10n.getString(nbVirtues);
    int nbVirtuePoints=_statistics.getTotalVirtuePoints();
    String nbVirtuePointsStr=L10n.getString(nbVirtuePoints);
    String virtuesStr=String.format("%s points, %s virtues",nbVirtuePointsStr,nbVirtuesStr); // I18n
    _virtues.setText(virtuesStr);
    // Virtue XP
    {
      VirtueXPStatsFromAchievables virtueXPStats=_statistics.getVirtueXPStats();
      int totalVirtueXP=virtueXPStats.getTotalVirtueXP();
      String totalVirtueXPStr=L10n.getString(totalVirtueXP);
      int achievablesCount=virtueXPStats.getEntriesCount();
      String achievablesCountStr=L10n.getString(achievablesCount);
      int completionsCount=virtueXPStats.getTotalCompletions();
      String completionsCountStr=L10n.getString(completionsCount);
      String virtueXPLabel;
      if (_mode==AchievableUIMode.DEED)
      {
        virtueXPLabel=totalVirtueXPStr+" points from "+achievablesCountStr+" deeds"; // I18n
      }
      else
      {
        virtueXPLabel=totalVirtueXPStr+" points from "+completionsCountStr+" completions ("+achievablesCountStr+" unique quests)"; // I18n
      }
      _virtueXP.setText(virtueXPLabel);
    }
    // Items count
    ItemsStats itemsStats=_statistics.getItemsStats();
    int nbItems=itemsStats.getDistinctItemsCount();
    _itemsCount.setText(L10n.getString(nbItems));
    // Emotes count
    int nbEmotes=_statistics.getEmotes().size();
    _emotesCount.setText(L10n.getString(nbEmotes));
    // Traits count
    int nbTraits=_statistics.getTraits().size();
    _traitsCount.setText(L10n.getString(nbTraits));
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
    _mode=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _lotroPoints=null;
    _classPoints=null;
    _marks=null;
    _medallions=null;
    _titlesCount=null;
    _reputation=null;
    _virtues=null;
    _virtueXP=null;
    _itemsCount=null;
    _emotesCount=null;
    _traitsCount=null;
  }
}
