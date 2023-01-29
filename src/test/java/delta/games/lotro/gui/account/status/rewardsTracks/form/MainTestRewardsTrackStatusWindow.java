package delta.games.lotro.gui.account.status.rewardsTracks.form;

import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.account.status.rewardsTrack.RewardsTrackStatus;
import delta.games.lotro.account.status.rewardsTrack.RewardsTracksStatusManager;
import delta.games.lotro.account.status.rewardsTrack.io.RewardsTracksStatusIo;
import delta.games.lotro.lore.rewardsTrack.RewardsTrack;
import delta.games.lotro.lore.rewardsTrack.RewardsTracksManager;

/**
 * Test class for the rewards track status window.
 * @author DAM
 */
public class MainTestRewardsTrackStatusWindow
{
  private void doIt()
  {
    AccountsManager accountsMgr=AccountsManager.getInstance();
    Account account=accountsMgr.getAccountByAccountName("glorfindel666");
    AccountOnServer accountOnServer=account.getServer("Landroval");
    RewardsTracksStatusManager rewardsTracksStatusMgr=RewardsTracksStatusIo.load(accountOnServer);
    for(RewardsTrack rewardsTrack : RewardsTracksManager.getInstance().getAllRewardsTracks())
    {
      RewardsTrackStatus status=rewardsTracksStatusMgr.getStatus(rewardsTrack,true);
      RewardsTrackStatusWindowController ctrl=new RewardsTrackStatusWindowController(null,status);
      ctrl.show(false);
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestRewardsTrackStatusWindow().doIt();
  }
}
