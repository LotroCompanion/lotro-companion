package delta.games.lotro.gui.character.storage.vaults;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
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
    VaultWindowController ctrl=new VaultWindowController(null,toon,false);
    ctrl.show();
    VaultWindowController sharedCtrl=new VaultWindowController(null,toon,true);
    sharedCtrl.show();
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
