package delta.games.lotro.gui.character.storage.vaults;

import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.storage.vaults.Vault;
import delta.games.lotro.character.storage.vaults.io.VaultsIo;
import delta.games.lotro.gui.character.storage.vault.VaultDisplayPanelController;

/**
 * Simple test class for the vault display panel controller.
 * @author DAM
 */
public class MainTestDisplayVault
{
  private void doIt()
  {
    {
      CharactersManager mgr=CharactersManager.getInstance();
      for(CharacterFile toon : mgr.getAllToons())
      {
        //CharacterFile toon=mgr.getToonById("Landroval","Meva");
        Vault vault=VaultsIo.load(toon);
        VaultWindow window=new VaultWindow(vault);
        window.setTitle(toon.getName());
        window.pack();
        window.show();
      }
    }
    {
      AccountsManager accountsMgr=AccountsManager.getInstance();
      Account account=accountsMgr.getAccountByName("glorfindel666");
      Vault vault=VaultsIo.load(account,"Landroval");
      VaultWindow window=new VaultWindow(vault);
      window.pack();
      window.show();
    }
  }

  private static class VaultWindow extends DefaultDisplayDialogController<Void>
  {
    private Vault _vault;

    /**
     * Constructor.
     * @param vault Vault.
     */
    public VaultWindow(Vault vault)
    {
      super(null,null);
      _vault=vault;
    }

  @Override
    protected JPanel buildFormPanel()
    {
      VaultDisplayPanelController ctrl=new VaultDisplayPanelController(this,_vault);
      return ctrl.getPanel();
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
