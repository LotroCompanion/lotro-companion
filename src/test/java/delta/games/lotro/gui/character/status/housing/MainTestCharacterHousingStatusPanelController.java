package delta.games.lotro.gui.character.status.housing;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.status.housing.AccountHousingData;
import delta.games.lotro.character.status.housing.io.HousingStatusIO;

/**
 * Test class for the character housing status window.
 * @author DAM
 */
class MainTestCharacterHousingStatusPanelController
{
  private void doIt()
  {
    Account account=AccountsManager.getInstance().getAccountByAccountName("glorfindel666");
    AccountOnServer accountOnServer=account.getServer("Landroval");
    AccountHousingData data=HousingStatusIO.loadAccountHousingData(accountOnServer);
    CharacterHousingStatusPanelController ctrl=new CharacterHousingStatusPanelController(null,data);
    DefaultWindowController w=new DefaultWindowController();
    w.getFrame().add(ctrl.getPanel());
    w.getFrame().pack();
    w.show();
  }

  public static void main(String[] args)
  {
    new MainTestCharacterHousingStatusPanelController().doIt();
  }
}
