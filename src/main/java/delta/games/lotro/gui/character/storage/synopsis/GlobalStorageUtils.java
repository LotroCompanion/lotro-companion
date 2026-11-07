package delta.games.lotro.gui.character.storage.synopsis;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.AccountUtils;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.storage.StorageUtils;
import delta.games.lotro.character.storage.StoredItem;

/**
 * Utility methods to aggregate storage across all managed accounts.
 * @author DAM
 */
public class GlobalStorageUtils
{
  private GlobalStorageUtils()
  {
    // Prevent instantiation
  }

  /**
   * Build the list of stored items for all characters on all accounts/servers.
   * @return A possibly empty but not <code>null</code> list of stored items.
   */
  public static List<StoredItem> buildAllAccountsItems()
  {
    List<StoredItem> items=new ArrayList<StoredItem>();
    List<Account> accounts=AccountsManager.getInstance().getAllAccounts();
    for(Account account : accounts)
    {
      List<String> servers=AccountUtils.getServers(account.getSummary());
      for(String serverName : servers)
      {
        AccountOnServer accountOnServer=account.getServer(serverName);
        items.addAll(StorageUtils.buildAccountItems(accountOnServer));
      }
    }
    return items;
  }
}
