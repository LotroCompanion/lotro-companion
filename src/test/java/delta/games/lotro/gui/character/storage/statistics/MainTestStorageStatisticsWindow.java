package delta.games.lotro.gui.character.storage.statistics;

import java.util.List;

import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.storage.StorageUtils;
import delta.games.lotro.character.storage.StoredItem;

/**
 * Simple tool class to compute and show storage statistics for a character.
 * @author DAM
 */
public class MainTestStorageStatisticsWindow
{
  /**
   * Main method for this tool.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    String serverName="Landroval";
    /*
    CharacterFile toon=CharactersManager.getInstance().getToonById(serverName,"Kargarth");
    CharacterStorage characterStorage=StoragesIO.loadCharacterStorage(toon);
    List<StoredItem> items=StorageUtils.buildCharacterItems(toon,characterStorage);
    */
    String accountName="glorfindel666";
    Account account=AccountsManager.getInstance().getAccountByAccountName(accountName);
    AccountOnServer accountOnServer=account.getServer(serverName);
    List<StoredItem> items=StorageUtils.buildAccountItems(accountOnServer);

    StorageStatisticsWindowController ctrl=new StorageStatisticsWindowController(null);
    ctrl.updateDisplay(items);
    ctrl.show();
  }
}
