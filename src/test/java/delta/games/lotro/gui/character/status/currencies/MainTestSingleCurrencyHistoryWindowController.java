package delta.games.lotro.gui.character.status.currencies;

import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

/**
 * Test class for the single currency history window controller.
 * @author DAM
 */
public class MainTestSingleCurrencyHistoryWindowController
{
  /**
   * Main method to test this window.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    // Account/server
    Account account=AccountsManager.getInstance().getAccountByName("glorfindel666");
    String serverName="Landroval";
    SingleCurrencyHistoryWindowController controller=SingleCurrencyHistoryWindowController.buildAccountServerWindow(null,account,serverName);
    controller.show();
    // Character
    CharacterFile toon=CharactersManager.getInstance().getToonById(serverName,"Meva");
    SingleCurrencyHistoryWindowController controller2=SingleCurrencyHistoryWindowController.buildCharacterWindow(null,toon);
    controller2.show();
  }
}
