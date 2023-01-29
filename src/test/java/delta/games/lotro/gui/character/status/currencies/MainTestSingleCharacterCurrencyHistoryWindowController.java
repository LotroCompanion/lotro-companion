package delta.games.lotro.gui.character.status.currencies;

import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

/**
 * Test class for the single character currency history window controller.
 * @author DAM
 */
public class MainTestSingleCharacterCurrencyHistoryWindowController
{
  /**
   * Main method to test this window.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    // Account/server
    Account account=AccountsManager.getInstance().getAccountByAccountName("glorfindel666");
    String serverName="Landroval";
    AccountOnServer accountOnServer=account.getServer(serverName);
    SingleCharacterCurrencyHistoryWindowController controller=new SingleCharacterCurrencyHistoryWindowController(null,accountOnServer);
    controller.show();
    // Character
    CharacterFile toon=CharactersManager.getInstance().getToonById(serverName,"Meva");
    SingleCharacterCurrencyHistoryWindowController controller2=new SingleCharacterCurrencyHistoryWindowController(null,toon);
    controller2.show();
  }
}
