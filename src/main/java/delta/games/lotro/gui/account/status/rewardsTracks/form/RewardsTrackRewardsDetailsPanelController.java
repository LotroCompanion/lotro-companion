package delta.games.lotro.gui.account.status.rewardsTracks.form;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.status.rewardsTrack.RewardsTrackStatus;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.rewards.form.RewardsPanelController;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.rewardsTrack.RewardsTrack;
import delta.games.lotro.lore.rewardsTrack.RewardsTrackStep;

/**
 * Controller for a panel to display rewards details for a rewards track.
 * @author DAM
 */
public class RewardsTrackRewardsDetailsPanelController
{
  // Data
  private RewardsTrackStatus _status;
  private RewardsTrackStepStateFilter _filter;
  // Controllers
  private List<RewardsPanelController> _rewards;
  // UI
  private List<JLabel> _largeIcons;
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param status Status to display.
   * @param filter Rewards track step state filter.
   */
  public RewardsTrackRewardsDetailsPanelController(WindowController parent, RewardsTrackStatus status, RewardsTrackStepStateFilter filter)
  {
    _status=status;
    _filter=filter;
    _rewards=new ArrayList<RewardsPanelController>();
    _largeIcons=new ArrayList<JLabel>();
    _panel=GuiFactory.buildPanel(new GridBagLayout());
    buildRewardsPanels(parent,status.getRewardsTrack());
    updatePanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void buildRewardsPanels(WindowController parent, RewardsTrack rewardsTrack)
  {
    List<RewardsTrackStep> steps=rewardsTrack.getSteps();
    for(RewardsTrackStep step : steps)
    {
      // Rewards
      Rewards rewards=RewardsTracksUtils.buildRewards(step);
      RewardsPanelController panel=new RewardsPanelController(parent,rewards);
      _rewards.add(panel);
      // Large icon
      JLabel iconLabel=null;
      int largeIconID=step.getLargeIconID();
      if (largeIconID!=0)
      {
        ImageIcon icon=LotroIconsManager.getLargeItemIcon(largeIconID);
        iconLabel=GuiFactory.buildIconLabel(icon);
      }
      _largeIcons.add(iconLabel);
    }
  }

  /**
   * Update the contents of the managed panel according to the filter status.
   */
  public void updatePanel()
  {
    _panel.removeAll();
    RewardsTrack allegiance=_status.getRewardsTrack();
    int nbSteps=allegiance.getSize();
    int y=0;
    for(int i=1;i<=nbSteps;i++)
    {
      RewardsTrackStepState state=RewardsTracksUtils.getState(_status,i);
      boolean ok=_filter.accept(state);
      if (!ok)
      {
        continue;
      }
      // Label
      String text=getLabel(i,state);
      JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
      {
        GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
        // Large icon
        JLabel largeIconLabel=_largeIcons.get(i-1);
        if (largeIconLabel!=null)
        {
          panel.add(largeIconLabel,c);
        }
        // Rewards
        JPanel rewardsPanel=_rewards.get(i-1).getPanel();
        c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
        panel.add(rewardsPanel,c);
        panel.setBorder(GuiFactory.buildTitledBorder(text));
      }
      int top=(y==0)?5:0;
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(top,5,5,5),0,0);
      _panel.add(panel,c);
      y++;
    }
    // Push everything on left
    Component glue=Box.createGlue();
    GridBagConstraints c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    _panel.add(glue,c);
    _panel.revalidate();
    _panel.repaint();
  }

  private String getLabel(int level, RewardsTrackStepState state)
  {
    String levelStr=Labels.getFieldLabel("rewards.track.details.level",new Object[] {Integer.valueOf(level)});
    String stateStr="";
    if (state==RewardsTrackStepState.CLAIMED)
    {
      stateStr=Labels.getLabel("rewards.track.details.state.claimed");
    }
    else if (state==RewardsTrackStepState.UNLOCKED)
    {
      stateStr=Labels.getLabel("rewards.track.details.state.unlocked");
    }
    else if (state==RewardsTrackStepState.FUTURE)
    {
      int currentMilestone=_status.getCurrentMilestone();
      if (level==(currentMilestone+1))
      {
        int currentXP=_status.getCurrentExperience();
        int nextGoalXP=_status.getNextExperienceGoal();
        int neededXP=nextGoalXP-currentXP;
        stateStr=Labels.getLabel("rewards.track.details.state.need", new Object[] {Integer.valueOf(neededXP)});
      }
      else
      {
        stateStr=Labels.getLabel("rewards.track.details.state.notStarted");
      }
    }
    return levelStr+stateStr;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _status=null;
    _filter=null;
    // Controllers
    if (_rewards!=null)
    {
      for(RewardsPanelController reward : _rewards)
      {
        reward.dispose();
      }
      _rewards.clear();
      _rewards=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _largeIcons=null;
  }
}
