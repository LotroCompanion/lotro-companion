package delta.games.lotro.gui.character.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.storage.AccountServerStorage;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.Chest;
import delta.games.lotro.character.storage.ItemsContainer;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.Vault;
import delta.games.lotro.character.storage.Wallet;
import delta.games.lotro.character.storage.io.xml.StorageIO;
import delta.games.lotro.character.storage.location.BagLocation;
import delta.games.lotro.character.storage.location.StorageLocation;
import delta.games.lotro.character.storage.location.VaultLocation;
import delta.games.lotro.character.storage.location.WalletLocation;
import delta.games.lotro.common.owner.AccountOwner;
import delta.games.lotro.common.owner.AccountServerOwner;
import delta.games.lotro.common.owner.Owner;
import delta.games.lotro.gui.items.chooser.ItemFilterConfiguration;
import delta.games.lotro.lore.items.CountedItem;
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
      List<StoredItem> allItems=new ArrayList<StoredItem>();
      AccountOwner accountOwner=new AccountOwner(accountName);
      AccountServerOwner accountServer=new AccountServerOwner(accountOwner,server);
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
        StorageDisplayWindowController window=new StorageDisplayWindowController(null,character);
        window.show();

        if (showShared)
        {
          // Store
          Account account=AccountsManager.getInstance().getAccountByName(accountName);
          StorageIO.writeAccountStorage(storage,account);
          // Reload
          Vault sharedVault=StorageIO.loadAccountSharedVault(account,server);
          Wallet sharedWallet=StorageIO.loadAccountSharedWallet(account,server);

          // Shared wallet
          {
            WalletLocation location=new WalletLocation();
            List<StoredItem> storedItems=getAllItems(accountServer,location,sharedWallet);
            allItems.addAll(storedItems);
            show("Shared wallet",storedItems);
          }
          // Shared vault
          {
            List<StoredItem> storedItems=getAllItems(accountServer,sharedVault,false);
            allItems.addAll(storedItems);
            show("Shared vault",storedItems);
          }

          showShared=false;
        }
      }
      show("All",allItems);
    }
  }

  private void show(String title, List<StoredItem> storedItems)
  {
    ItemFilterConfiguration cfg=new ItemFilterConfiguration();
    cfg.forStashFilter();
    final StoredItemsTableController tableController=new StoredItemsTableController(null,storedItems);
    DefaultWindowController c=new DefaultWindowController()
    {
      @Override
      protected JComponent buildContents()
      {
        // Table
        JTable table=tableController.getTable();
        JScrollPane scroll=GuiFactory.buildScrollPane(table);
        return scroll;
      }
    };
    c.setTitle(title);
    c.getWindow().pack();
    c.show();
  }

  private List<StoredItem> getAllItems(Owner owner, Vault container, boolean isBag)
  {
    List<StoredItem> items=new ArrayList<StoredItem>();
    int chests=container.getChestCount();
    for(int i=0;i<chests;i++)
    {
      Chest chest=container.getChest(i);
      if (chest!=null)
      {
        String chestName=chest.getName();
        StorageLocation location=isBag?new BagLocation(chestName):new VaultLocation(chestName);
        List<CountedItem> chestItems=chest.getAllItemsByName();
        for(CountedItem chestItem : chestItems)
        {
          StoredItem storedItem=new StoredItem(chestItem.getProxy(),chestItem.getQuantity());
          storedItem.setOwner(owner);
          storedItem.setLocation(location);
          items.add(storedItem);
        }
      }
    }
    return items;
  }

  private List<StoredItem> getAllItems(Owner owner, StorageLocation location, ItemsContainer container)
  {
    List<StoredItem> items=new ArrayList<StoredItem>();
    for(CountedItem countedItem : container.getAllItemsByName())
    {
      StoredItem storedItem=new StoredItem(countedItem.getProxy(),countedItem.getQuantity());
      storedItem.setOwner(owner);
      storedItem.setLocation(location);
      items.add(storedItem);
    }
    return items;
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
