package delta.games.lotro.gui.character.status.achievables.statistics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.utils.l10n.L10n;
import delta.games.lotro.character.status.achievables.AchievableElementState;
import delta.games.lotro.character.status.achievables.statistics.GlobalAchievablesStatistics;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;

/**
 * Controller for a panel to show the summary of the statistics about some achievables.
 * @author DAM
 */
public class AchievablesStatisticsSummaryPanelController
{
  // Data
  private GlobalAchievablesStatistics _statistics;
  private AchievableUIMode _mode;
  // UI
  private JPanel _panel;
  private JLabel _notStarted;
  private JLabel _underway;
  private JLabel _completed;
  private JLabel _completionsCount;

  /**
   * Constructor.
   * @param statistics Statistics to show.
   * @param mode UI mode.
   */
  public AchievablesStatisticsSummaryPanelController(GlobalAchievablesStatistics statistics, AchievableUIMode mode)
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

    // Counts by state:
    // - Completed
    ret.add(GuiFactory.buildLabel("Completed:"),cLabels); // I18n
    _completed=GuiFactory.buildLabel("");
    ret.add(_completed,cValues);
    cLabels.gridy++;cValues.gridy++;
    // - Underway
    ret.add(GuiFactory.buildLabel("Underway:"),cLabels); // I18n
    _underway=GuiFactory.buildLabel("");
    ret.add(_underway,cValues);
    cLabels.gridy++;cValues.gridy++;
    // - Not started
    ret.add(GuiFactory.buildLabel("Not started:"),cLabels); // I18n
    _notStarted=GuiFactory.buildLabel("");
    ret.add(_notStarted,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Completions count (quests only)
    if (_mode==AchievableUIMode.QUEST)
    {
      ret.add(GuiFactory.buildLabel("Completions count:"),cLabels); // I18n
      _completionsCount=GuiFactory.buildLabel("");
      ret.add(_completionsCount,cValues);
      cLabels.gridy++;cValues.gridy++;
    }
    return ret;
  }

  /**
   * Update display.
   */
  public void update()
  {
    // Counts by state
    updateStateCount(AchievableElementState.COMPLETED,_completed);
    updateStateCount(AchievableElementState.UNDERWAY,_underway);
    updateStateCount(AchievableElementState.UNDEFINED,_notStarted);
    // Completions count
    if (_completionsCount!=null)
    {
      int completionsCount=_statistics.getCompletionsCount();
      _completionsCount.setText(L10n.getString(completionsCount));
    }
  }

  private void updateStateCount(AchievableElementState state, JLabel gadget)
  {
    int count=_statistics.getCountForState(state);
    int total=_statistics.getTotalCount();
    String displayStr="0";
    if (total>0)
    {
      double percentage=(100.0*count)/total;
      String percentageStr=L10n.getString(percentage,1);
      displayStr=String.format("%s / %s (%s%%)",L10n.getString(count),L10n.getString(total),percentageStr);
    }
    gadget.setText(displayStr);
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
    _completed=null;
    _underway=null;
    _notStarted=null;
    _completionsCount=null;
  }
}
