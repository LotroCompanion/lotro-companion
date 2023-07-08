package delta.games.lotro.gui.account.status.rewardsTracks.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.account.status.rewardsTrack.RewardsTrackStatus;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.rewardsTrack.RewardsTrack;

/**
 * Controller for a panel to display status summary.
 * @author DAM
 */
public class RewardsTrackStatusSummaryPanelController
{
  // Data
  private RewardsTrackStatus _status;
  // GUI
  private JPanel _panel;

  /**
   * Constructor.
   * @param status Rewards track status.
   */
  public RewardsTrackStatusSummaryPanelController(RewardsTrackStatus status)
  {
    _status=status;
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
    // Components
    RewardsTrack rewardsTrack=_status.getRewardsTrack();
    // - level
    JProgressBar levels=buildProgressBar();
    int currentLevel=_status.getCurrentMilestone();
    int maxLevel=rewardsTrack.getSize();
    setBar(levels,0,maxLevel,currentLevel);
    // - points
    int currentXP=_status.getCurrentExperience();
    int maxXP=rewardsTrack.getMaxPoints();
    JProgressBar points=buildProgressBar();
    setBar(points,0,maxXP,currentXP);
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // - level
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    String levelField=Labels.getFieldLabel("rewards.track.details.summary.level");
    panel.add(GuiFactory.buildLabel(levelField),c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,5),0,0);
    panel.add(levels,c);
    // - points
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    String pointsField=Labels.getFieldLabel("rewards.track.details.summary.points");
    panel.add(GuiFactory.buildLabel(pointsField),c);
    c=new GridBagConstraints(1,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,5),0,0);
    panel.add(points,c);
    return panel;
  }

  private JProgressBar buildProgressBar()
  {
    JProgressBar bar=new JProgressBar(SwingConstants.HORIZONTAL,0,1);
    bar.setBackground(GuiFactory.getBackgroundColor());
    bar.setForeground(Color.BLUE);
    bar.setBorderPainted(true);
    bar.setStringPainted(true);
    bar.setPreferredSize(new Dimension(200,25));
    return bar;
  }

  private void setBar(JProgressBar bar, int min, int max, int current)
  {
    bar.setMinimum(min);
    bar.setMaximum(max);
    int value=(current>max)?max:current;
    bar.setValue(value);
    String label=current+"/"+max;
    bar.setString(label);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _status=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
