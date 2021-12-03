package delta.games.lotro.gui.character.storage.wallet;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

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
    WalletWindowController ctrl=new WalletWindowController(null,toon);
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
