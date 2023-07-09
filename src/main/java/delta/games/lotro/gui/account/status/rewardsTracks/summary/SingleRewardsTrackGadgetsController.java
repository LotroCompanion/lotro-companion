package delta.games.lotro.gui.account.status.rewardsTracks.summary;

import javax.swing.JButton;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.account.status.rewardsTrack.RewardsTrackStatus;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.rewardsTrack.RewardsTrack;

/**
 * Controller for the UI items to display a single rewards track status summary.
 * @author DAM
 */
public class SingleRewardsTrackGadgetsController
{
  // Data
  private RewardsTrackStatus _status;
  // UI
  // - rewards track state
  private JLabel _state;
  // - details button
  private JButton _button;

  /**
   * Constructor.
   * @param status Rewards track status to use.
   */
  public SingleRewardsTrackGadgetsController(RewardsTrackStatus status)
  {
    _status=status;
    // State
    _state=GuiFactory.buildLabel("?");
    // Button
    _button=GuiFactory.buildButton(Labels.getLabel("rewards.tracks.status.summary.details"));
    // Init
    setRewardsTrackStatus(status);
  }

  /**
   * Get the managed rewards track status.
   * @return a rewards track status.
   */
  public RewardsTrackStatus getStatus()
  {
    return _status;
  }

  /**
   * Get the gadget for the state.
   * @return a label.
   */
  public JLabel getStateGadget()
  {
    return _state;
  }

  /**
   * Get the details button.
   * @return a button.
   */
  public JButton getDetailsButton()
  {
    return _button;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    _state=null;
  }

  /**
   * Set the data to display.
   * @param status Status to display.
   */
  private void setRewardsTrackStatus(RewardsTrackStatus status)
  {
    // State
    String stateLabel=buildStateLabel(status);
    _state.setText(stateLabel);
  }

  private String buildStateLabel(RewardsTrackStatus status)
  {
    if (status==null)
    {
      return Labels.getLabel("rewards.tracks.status.summary.state.unknown");
    }
    int currentMilestone=status.getCurrentMilestone();
    if (currentMilestone==0)
    {
      return Labels.getLabel("rewards.tracks.status.summary.state.notStarted");
    }
    RewardsTrack rewardsTrack=status.getRewardsTrack();
    String claimedComplement="";
    int claimed=status.getClaimedMilestones();
    if (claimed<currentMilestone)
    {
      int toClaim=currentMilestone-claimed;
      claimedComplement=Labels.getLabel("rewards.tracks.status.summary.state.complement",new Object[] {Integer.valueOf(toClaim)});
    }
    int maxLevel=rewardsTrack.getSize();
    if (currentMilestone>=maxLevel)
    {
      int nbRepeats=currentMilestone-maxLevel+1;
      if (nbRepeats>1)
      {
        Object[] params={Integer.valueOf(nbRepeats),claimedComplement};
        return Labels.getLabel("rewards.tracks.status.summary.state.finished.withRepeats",params);
      }
      Object[] params={claimedComplement};
      return Labels.getLabel("rewards.tracks.status.summary.state.finished",params);
    }
    Object[] params={Integer.valueOf(currentMilestone), Integer.valueOf(maxLevel), claimedComplement};
    return Labels.getLabel("rewards.tracks.status.summary.state.notFinished",params);
  }
}
