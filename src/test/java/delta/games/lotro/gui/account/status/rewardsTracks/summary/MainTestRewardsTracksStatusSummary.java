package delta.games.lotro.gui.account.status.rewardsTracks.summary;

import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.account.status.rewardsTrack.RewardsTracksStatusManager;
import delta.games.lotro.account.status.rewardsTrack.io.RewardsTracksStatusIo;

/**
 * Test class for the rewards tracks status summary.
 * @author DAM
 */
public class MainTestRewardsTracksStatusSummary
{
  private void doIt()
  {
    AccountsManager accountsMgr=AccountsManager.getInstance();
    Account account=accountsMgr.getAccountByAccountName("glorfindel666");
    AccountOnServer accountOnServer=account.getServer("Landroval");
    RewardsTracksStatusManager rewardsTracksStatusMgr=RewardsTracksStatusIo.load(accountOnServer);
    RewardsTracksStatusSummaryWindowController ctrl=new RewardsTracksStatusSummaryWindowController(null,rewardsTracksStatusMgr);
    ctrl.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestRewardsTracksStatusSummary().doIt();
  }
}
