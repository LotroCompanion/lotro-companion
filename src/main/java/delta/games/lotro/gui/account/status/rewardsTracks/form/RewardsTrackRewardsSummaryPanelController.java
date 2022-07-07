package delta.games.lotro.gui.account.status.rewardsTracks.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.status.rewardsTrack.RewardsTrackStatus;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.common.rewards.RewardsUtils;
import delta.games.lotro.gui.common.rewards.form.RewardsPanelController;
import delta.games.lotro.lore.rewardsTrack.RewardsTrack;
import delta.games.lotro.lore.rewardsTrack.RewardsTrackStep;

/**
 * Controller for a panel to display rewards summary.
 * @author DAM
 */
public class RewardsTrackRewardsSummaryPanelController
{
  // Data
  private RewardsTrackStatus _status;
  private RewardsTrackStepStateFilter _filter;
  // Controllers
  private WindowController _parent;
  private RewardsPanelController _rewards;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param status Status to display.
   * @param filter Rewards track steps filter.
   */
  public RewardsTrackRewardsSummaryPanelController(WindowController parent, RewardsTrackStatus status, RewardsTrackStepStateFilter filter)
  {
    _parent=parent;
    _status=status;
    _filter=filter;
    _rewards=new RewardsPanelController(parent,new Rewards(),100);
    _panel=GuiFactory.buildPanel(new GridBagLayout());
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

  /**
   * Update the contents of the managed panel according to the filter status.
   */
  public void updatePanel()
  {
    _panel.removeAll();
    List<Rewards> selectedRewards=new ArrayList<Rewards>(); 
    RewardsTrack rewardsTrack=_status.getRewardsTrack();
    int nbSteps=rewardsTrack.getSize();
    List<RewardsTrackStep> steps=rewardsTrack.getSteps();
    for(int i=1;i<=nbSteps;i++)
    {
      RewardsTrackStepState state=RewardsTracksUtils.getState(_status,i);
      boolean ok=_filter.accept(state);
      if (ok)
      {
        RewardsTrackStep step=steps.get(i-1);
        Rewards stepRewards=RewardsTracksUtils.buildRewards(step);
        selectedRewards.add(stepRewards);
      }
    }
    Rewards total=RewardsUtils.buildTotalRewards(selectedRewards);
    _rewards.dispose();
    _rewards=new RewardsPanelController(_parent,total,100);
    // Assembly
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    _panel.add(_rewards.getPanel(),c);
    // Push everything on top/left
    c=new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    _panel.add(Box.createGlue(),c);
    _panel.revalidate();
    _panel.repaint();
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
    _parent=null;
    if (_rewards!=null)
    {
      _rewards.dispose();
      _rewards=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
