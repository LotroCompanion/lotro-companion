package delta.games.lotro.gui.character.storage.wardrobe;

import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.AccountsManager;

/**
 * Test class for the wardrobe display.
 * @author DAM
 */
public class MainTestWardrobeDisplay
{
  private void doIt()
  {
    fetchStorageData();
  }

  private void fetchStorageData()
  {
    String accountName="glorfindel666";
    String server="Landroval";
    Account account=AccountsManager.getInstance().getAccountByAccountName(accountName);
    // Store
    AccountOnServer accountOnServer=account.getServer(server);
    WardrobeDisplayWindowController window=new WardrobeDisplayWindowController(null,accountOnServer);
    window.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestWardrobeDisplay().doIt();
  }
}
