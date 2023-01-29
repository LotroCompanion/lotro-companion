package delta.games.lotro.gui.character.storage;

import java.util.List;

import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.AccountUtils;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.gui.character.storage.account.AccountStorageDisplayWindowController;
import delta.games.lotro.gui.character.storage.own.CharacterStorageDisplayWindowController;

/**
 * Test class to show the storage for account/characters.
 * @author DAM
 */
public class MainTestShowCharacterStorage
{
  private void doIt()
  {
    fetchStorageData();
  }

  private void fetchStorageData()
  {
    String accountName="glorfindel666";
    String server="Landroval";
    //String toon="Meva";
    boolean showShared=true;
    Account account=AccountsManager.getInstance().getAccountByAccountName(accountName);
    List<CharacterFile> characters=AccountUtils.getCharacters(account.getSummary(),server);
    for(CharacterFile character : characters)
    {
      CharacterStorageDisplayWindowController window=new CharacterStorageDisplayWindowController(null,character);
      window.show();

      if (showShared)
      {
        // Store
        AccountOnServer accountOnServer=account.getServer(server);
        AccountStorageDisplayWindowController accountWindow=new AccountStorageDisplayWindowController(null,accountOnServer);
        accountWindow.show();
        showShared=false;
      }
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestShowCharacterStorage().doIt();
  }
}
