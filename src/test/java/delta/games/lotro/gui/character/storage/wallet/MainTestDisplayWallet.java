package delta.games.lotro.gui.character.storage.wallet;

import delta.games.lotro.account.Account;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.storage.wallet.Wallet;
import delta.games.lotro.character.storage.wallet.io.xml.WalletsIO;
import delta.games.lotro.character.utils.CharacterUtils;

/**
 * Simple test class for the wallet display panel controller.
 * @author DAM
 */
public class MainTestDisplayWallet
{
  private void doIt()
  {
    CharactersManager mgr=CharactersManager.getInstance();
    CharacterFile toon=mgr.getToonById("Landroval","Meva");
    Wallet ownWallet=WalletsIO.loadCharacterWallet(toon);
    Wallet sharedWallet=null;
    Account account=CharacterUtils.getAccount(toon);
    String serverName=toon.getServerName();
    if (account!=null)
    {
      sharedWallet=WalletsIO.loadAccountSharedWallet(account,serverName);
    }
    WalletWindowController ctrl=new WalletWindowController(null,toon,ownWallet,sharedWallet);
    ctrl.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestDisplayWallet().doIt();
  }
}
