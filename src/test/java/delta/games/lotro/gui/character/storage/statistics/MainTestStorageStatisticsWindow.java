package delta.games.lotro.gui.character.storage.statistics;

import java.util.List;

import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.StorageUtils;
import delta.games.lotro.character.storage.StoragesIO;
import delta.games.lotro.character.storage.StoredItem;

/**
 * Simple tool class to compute and show storage statistics for a character.
 * @author DAM
 */
public class MainTestStorageStatisticsWindow
{
  private List<StoredItem> loadForAccountServer(String accountName, String serverName)
  {
    Account account=AccountsManager.getInstance().getAccountByAccountName(accountName);
    AccountOnServer accountOnServer=account.getServer(serverName);
    List<StoredItem> items=StorageUtils.buildAccountItems(accountOnServer);
    return items;
  }

  private List<StoredItem> loadForCharacter(String serverName, String characterName)
  {
    CharacterFile toon=CharactersManager.getInstance().getToonById(serverName,characterName);
    CharacterStorage characterStorage=StoragesIO.loadCharacterStorage(toon);
    List<StoredItem> items=StorageUtils.buildCharacterItems(toon,characterStorage);
    return items;
  }

  private void doIt(boolean account)
  {
    List<StoredItem> items;
    if (account)
    {
      items=loadForAccountServer("glorfindel666","Landroval");
    }
    else
    {
      items=loadForCharacter("Landroval","Kargarth");
    }
    StorageStatisticsWindowController ctrl=new StorageStatisticsWindowController(null);
    ctrl.updateDisplay(items);
    ctrl.show();
  }

  /**
   * Main method for this tool.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestStorageStatisticsWindow().doIt(true);
  }
}
