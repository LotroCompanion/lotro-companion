package delta.games.lotro.gui.character.storage;

import java.util.Set;

import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.storage.AccountServerStorage;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.io.xml.StorageIO;
import delta.games.lotro.plugins.StorageLoader;

/**
 * Test class to show the storage for a single character.
 * @author DAM
 */
public class MainTestShowCharacterStorage
{
  private void doIt()
  {
    fetchStorageData();
  }

  private void fetchStorageData()
  {
    String accountName="glorfindel666";
    String server="Landroval";
    //String toon="Meva";
    boolean showShared=true;
    StorageLoader loader=new StorageLoader();
    AccountServerStorage storage=loader.loadStorage(accountName,server);
    if (storage!=null)
    {
      Set<String> toons=storage.getCharacters();
      for(String toon : toons)
      {
        // Store/reload
        CharacterStorage characterStorage=storage.getStorage(toon,false);
        CharactersManager manager=CharactersManager.getInstance();
        CharacterFile character=manager.getToonById(server,toon);
        if (character==null)
        {
          System.out.println("Character not found: "+toon);
          continue;
        }
        // Store
        StorageIO.writeCharacterStorage(characterStorage,character);
        CharacterStorageDisplayWindowController window=new CharacterStorageDisplayWindowController(null,character);
        window.show();

        if (showShared)
        {
          // Store
          Account account=AccountsManager.getInstance().getAccountByName(accountName);
          StorageIO.writeAccountStorage(storage,account);
          AccountStorageDisplayWindowController accountWindow=new AccountStorageDisplayWindowController(null,account,server);
          accountWindow.show();
          showShared=false;
        }
      }
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestShowCharacterStorage().doIt();
  }
}
