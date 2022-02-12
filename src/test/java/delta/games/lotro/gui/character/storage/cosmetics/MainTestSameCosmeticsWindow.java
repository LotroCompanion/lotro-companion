package delta.games.lotro.gui.character.storage.cosmetics;

import java.util.List;

import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.StorageUtils;
import delta.games.lotro.character.storage.StoragesIO;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.cosmetics.CosmeticItemsGroup;
import delta.games.lotro.character.storage.cosmetics.SameCosmeticsFinder;

/**
 * Test class for the 'same cosmetics' window.
 * @author DAM
 */
public class MainTestSameCosmeticsWindow
{
  private void doIt()
  {
    CharacterFile toon=CharactersManager.getInstance().getToonById("Landroval","Lorewyne");
    CharacterStorage characterStorage=StoragesIO.loadCharacterStorage(toon);
    List<StoredItem> items=StorageUtils.buildCharacterItems(toon,characterStorage);
    doIt(items);

    Account account=AccountsManager.getInstance().getAccountByName("glorfindel666");
    List<StoredItem> items2=StorageUtils.buildAccountItems(account,"Landroval");
    doIt(items2);
  }

  private void doIt(List<StoredItem> items)
  {
    SameCosmeticsFinder finder=new SameCosmeticsFinder();
    List<CosmeticItemsGroup> groups=finder.findGroups(items);
    SameCosmeticsWindowController windowCtrl=new SameCosmeticsWindowController(null,groups);
    windowCtrl.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestSameCosmeticsWindow().doIt();
  }
}
