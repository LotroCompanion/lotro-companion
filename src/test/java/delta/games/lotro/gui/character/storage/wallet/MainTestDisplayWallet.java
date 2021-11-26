package delta.games.lotro.gui.character.storage.wallet;

import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.storage.wallet.Wallet;
import delta.games.lotro.character.storage.wallet.io.xml.WalletsIO;

/**
 * Simple test class for the wallet display panel controller.
 * @author DAM
 */
public class MainTestDisplayWallet
{
  private void doIt()
  {
    {
      CharactersManager mgr=CharactersManager.getInstance();
      //for(CharacterFile toon : mgr.getAllToons())
      //{
      CharacterFile toon=mgr.getToonById("Landroval","Meva");
      Wallet wallet=WalletsIO.loadCharacterWallet(toon);
      WalletWindow window=new WalletWindow(wallet);
      window.pack();
      window.show();
      //}
    }
    {
      AccountsManager accountsMgr=AccountsManager.getInstance();
      Account account=accountsMgr.getAccountByName("glorfindel666");
      Wallet wallet=WalletsIO.loadAccountSharedWallet(account,"Landroval");
      WalletWindow window=new WalletWindow(wallet);
      window.pack();
      window.show();
    }
  }

  private static class WalletWindow extends DefaultDisplayDialogController<Void>
  {
    private Wallet _wallet;

    /**
     * Constructor.
     * @param wallet Wallet.
     */
    public WalletWindow(Wallet wallet)
    {
      super(null,null);
      _wallet=wallet;
    }

  @Override
    protected JPanel buildFormPanel()
    {
      WalletDisplayPanelController ctrl=new WalletDisplayPanelController(this,_wallet);
      return ctrl.getPanel();
    }
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
