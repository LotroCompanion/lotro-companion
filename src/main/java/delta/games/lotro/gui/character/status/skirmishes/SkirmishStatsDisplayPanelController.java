package delta.games.lotro.gui.character.status.skirmishes;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.utils.l10n.L10n;
import delta.games.lotro.character.status.skirmishes.SkirmishStats;
import delta.games.lotro.common.Duration;

/**
 * Controller for a "skirmish stats" display panel.
 * @author DAM
 */
public class SkirmishStatsDisplayPanelController
{
  // GUI
  private JPanel _panel;

  private JLabel _monsterKills;
  private JLabel _lieutenantKills;
  private JLabel _bossKills;
  private JLabel _bossResets;
  private JLabel _defendersLost;
  private JLabel _defendersSaved;
  private JLabel _soldiersDeaths;
  private JLabel _controlPointsTaken;
  private JLabel _encountersCompleted;
  private JLabel _playTime;
  private JLabel _skirmishesCompleted;
  private JLabel _skirmishesAttempted;
  private JLabel _bestTime;
  private JLabel _totalMarksEarned;

  /**
   * Constructor.
   */
  public SkirmishStatsDisplayPanelController()
  {
    _panel=build();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Monster kills
    _monsterKills=buildLabelLine(panel,c,"Monster kills: "); // I18n
    // Lieutenant kills
    _lieutenantKills=buildLabelLine(panel,c,"Lieutenant kills: "); // I18n
    // Boss kills
    _bossKills=buildLabelLine(panel,c,"Boss kills: "); // I18n
    // Boss resets
    _bossResets=buildLabelLine(panel,c,"Boss resets: "); // I18n
    // Defenders lost
    _defendersLost=buildLabelLine(panel,c,"Defenders lost: "); // I18n
    // Defenders saved
    _defendersSaved=buildLabelLine(panel,c,"Defenders saved: "); // I18n
    // Soldier deaths
    _soldiersDeaths=buildLabelLine(panel,c,"Soldier deaths: "); // I18n
    // Control points taken
    _controlPointsTaken=buildLabelLine(panel,c,"Control Points taken: "); // I18n
    // Encounters completed
    _encountersCompleted=buildLabelLine(panel,c,"Encounters completed: "); // I18n
    // Play time
    _playTime=buildLabelLine(panel,c,"Play time: "); // I18n
    // Skirmishes completed
    _skirmishesCompleted=buildLabelLine(panel,c,"Skirmishes completed: "); // I18n
    // Skirmishes attempted
    _skirmishesAttempted=buildLabelLine(panel,c,"Skirmishes attempted: "); // I18n
    // Best time
    _bestTime=buildLabelLine(panel,c,"Best time: "); // I18n
    // Total marks earned
    _totalMarksEarned=buildLabelLine(panel,c,"Total marks earned: "); // I18n
    return panel;
  }

  private JLabel buildLabelLine(JPanel parent, GridBagConstraints c, String fieldName)
  {
    // Build line panel
    FlowLayout flowLayout=new FlowLayout(FlowLayout.LEFT,5,2);
    JPanel panelLine=GuiFactory.buildPanel(flowLayout);
    // Build field label
    panelLine.add(GuiFactory.buildLabel(fieldName));
    // Build value label
    JLabel label=GuiFactory.buildLabel("");
    panelLine.add(label);
    // Add line panel to parent
    parent.add(panelLine,c);
    c.gridy++;
    if (c.gridy==7)
    {
      c.gridx++;
      c.gridy=0;
    }
    return label;
  }

  /**
   * Update UI to reflect data.
   * @param stats Statistics to show.
   */
  public void updateUI(SkirmishStats stats)
  {
    // Monster kills
    _monsterKills.setText(L10n.getString(stats.getMonsterKills()));
    // Lieutenant kills
    _lieutenantKills.setText(L10n.getString(stats.getLieutenantKills()));
    // Boss kills
    _bossKills.setText(L10n.getString(stats.getBossKills()));
    // Boss resets
    _bossResets.setText(L10n.getString(stats.getBossResets()));
    // Defenders lost
    _defendersLost.setText(L10n.getString(stats.getDefendersLost()));
    // Defenders saved
    _defendersSaved.setText(L10n.getString(stats.getDefendersSaved()));
    // Soldier deaths
    _soldiersDeaths.setText(L10n.getString(stats.getSoldiersDeaths()));
    // Control points taken
    _controlPointsTaken.setText(L10n.getString(stats.getControlPointsTaken()));
    // Encounters completed
    _encountersCompleted.setText(L10n.getString(stats.getEncountersCompleted()));
    // Play time
    _playTime.setText(Duration.getDurationString((int)stats.getPlayTime()));
    // Skirmishes completed
    _skirmishesCompleted.setText(L10n.getString(stats.getSkirmishesCompleted()));
    // Skirmishes attempted
    _skirmishesAttempted.setText(L10n.getString(stats.getSkirmishesAttempted()));
    // Best time
    _bestTime.setText(Duration.getDurationString((int)stats.getBestTime()));
    // Total marks earned
    _totalMarksEarned.setText(L10n.getString(stats.getTotalMarksEarned()));
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _monsterKills=null;
    _lieutenantKills=null;
    _bossKills=null;
    _bossResets=null;
    _defendersLost=null;
    _defendersSaved=null;
    _soldiersDeaths=null;
    _controlPointsTaken=null;
    _encountersCompleted=null;
    _playTime=null;
    _skirmishesCompleted=null;
    _skirmishesAttempted=null;
    _bestTime=null;
    _totalMarksEarned=null;
  }
}
