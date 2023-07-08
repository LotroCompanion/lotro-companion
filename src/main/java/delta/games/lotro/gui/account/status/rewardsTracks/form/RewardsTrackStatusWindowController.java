package delta.games.lotro.gui.account.status.rewardsTracks.form;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.status.rewardsTrack.RewardsTrackStatus;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.rewardsTrack.RewardsTrack;

/**
 * Controller for a rewards track status display window.
 * @author DAM
 */
public class RewardsTrackStatusWindowController extends DefaultDisplayDialogController<RewardsTrackStatus>
{
  // Controllers
  private RewardsTrackStatusPanelController _statusController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Data to show.
   */
  public RewardsTrackStatusWindowController(WindowController parent, RewardsTrackStatus status)
  {
    super(parent,status);
    _statusController=new RewardsTrackStatusPanelController(this,status);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(800,500));
    String rewardsTrackName=_data.getRewardsTrack().getName();
    String title=Labels.getLabel("rewards.track.details.window.title", new Object[] {rewardsTrackName});
    dialog.setTitle(title);
    dialog.setSize(new Dimension(800,700));
    dialog.setMaximumSize(new Dimension(1000,700));
    return dialog;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return getIdentifier(_data.getRewardsTrack());
  }

  /**
   * Get the window identifier for a rewards track.
   * @param rewardsTrack Rewards track to use.
   * @return An identifier.
   */
  public static final String getIdentifier(RewardsTrack rewardsTrack)
  {
    return String.valueOf(rewardsTrack.getIdentifier());
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=_statusController.getPanel();
    return panel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _data=null;
    // Controllers
    if (_statusController!=null)
    {
      _statusController.dispose();
      _statusController=null;
    }
  }
}
