package delta.games.lotro.gui.character.storage.vaults;

import delta.games.lotro.account.Account;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.storage.vaults.Vault;
import delta.games.lotro.character.storage.vaults.io.VaultsIo;
import delta.games.lotro.character.utils.CharacterUtils;
import delta.games.lotro.gui.character.storage.vault.VaultWindowController;

/**
 * Simple test class for the vault display panel controller.
 * @author DAM
 */
public class MainTestDisplayVault
{
  private void doIt()
  {
    CharactersManager mgr=CharactersManager.getInstance();
    CharacterFile toon=mgr.getToonById("Landroval","Backstaba");

    Vault sharedVault=null;
    String serverName=toon.getServerName();
    Account account=CharacterUtils.getAccount(toon);
    if (account!=null)
    {
      sharedVault=VaultsIo.load(account,serverName);
    }
    Vault ownVault=VaultsIo.load(toon);

    VaultWindowController ctrl=new VaultWindowController(null,toon,false,ownVault);
    ctrl.show();
    if (sharedVault!=null)
    {
      VaultWindowController sharedCtrl=new VaultWindowController(null,toon,true,sharedVault);
      sharedCtrl.show();
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestDisplayVault().doIt();
  }
}
