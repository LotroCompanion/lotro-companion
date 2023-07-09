package delta.games.lotro.gui.account.status.rewardsTracks.summary;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.status.rewardsTrack.RewardsTracksStatusManager;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for a window that displays the status of rewards tracks for an account/server.
 * @author DAM
 */
public class RewardsTracksStatusSummaryWindowController extends DefaultDisplayDialogController<RewardsTracksStatusManager>
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="REWARDS_TRACKS_STATUS";

  // Controllers
  private RewardsTracksStatusSummaryPanelController _statusController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Data to show.
   */
  public RewardsTracksStatusSummaryWindowController(WindowController parent, RewardsTracksStatusManager status)
  {
    super(parent,status);
    _statusController=new RewardsTracksStatusSummaryPanelController(this,status);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle(Labels.getLabel("rewards.tracks.status.summary.title"));
    dialog.pack();
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
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
