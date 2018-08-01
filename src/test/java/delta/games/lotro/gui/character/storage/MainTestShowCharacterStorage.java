package delta.games.lotro.gui.character.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.utils.collections.filters.Filter;
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
import delta.games.lotro.gui.items.CountedItemsTableController;
import delta.games.lotro.gui.items.chooser.ItemFilterConfiguration;
import delta.games.lotro.gui.items.chooser.ItemFilterController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.plugins.StorageLoader;

/**
 * Test class to show the storage for a single character.
 * @author DAM
 */
public class MainTestShowCharacterStorage
{
  private void doIt()
  {
    String account="glorfindel666";
    String server="Landroval";
    //String toon="Meva";
    boolean showShared=true;
    StorageLoader loader=new StorageLoader();
    AccountServerStorage storage=loader.loadStorage(account,server);
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
        // Reload
        characterStorage=StorageIO.loadCharacterStorage(character);
  
        // Own bags
        {
          Vault container=characterStorage.getBags();
          List<StoredItem> storedItems=getAllItems(container);
          show("Bags ("+toon+")",storedItems);
        }
        // Own vault
        {
          Vault container=characterStorage.getOwnVault();
          List<StoredItem> storedItems=getAllItems(container);
          show("Vault ("+toon+")",storedItems);
        }
        // Own wallet
        {
          Wallet ownWallet=characterStorage.getWallet();
          List<StoredItem> storedItems=ownWallet.getAllItemsByName();
          show("Wallet ("+toon+")",storedItems);
        }
        if (showShared)
        {
          // Shared wallet
          {
            ItemsContainer container=storage.getSharedWallet();
            List<StoredItem> storedItems=container.getAllItemsByName();
            show("Shared wallet",storedItems);
          }
          // Shared vault
          {
            Vault sharedVault=storage.getSharedVault();
            List<StoredItem> storedItems=getAllItems(sharedVault);
            show("Shared vault",storedItems);
          }
          showShared=false;
        }
      }
    }
  }

  private void show(String title, List<StoredItem> storedItems)
  {
    ItemFilterConfiguration cfg=new ItemFilterConfiguration();
    cfg.forStashFilter();
    ItemFilterController filterController=new ItemFilterController(cfg,null,null);
    Filter<Item> filter=filterController.getFilter();
    final CountedItemsTableController tableController=new CountedItemsTableController(null,storedItems,filter);
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

  private List<StoredItem> getAllItems(Vault container)
  {
    List<StoredItem> items=new ArrayList<StoredItem>();
    int chests=container.getChestCount();
    //int itemsCount=0;
    for(int i=0;i<chests;i++)
    {
      Chest chest=container.getChest(i);
      if (chest!=null)
      {
        List<StoredItem> chestItems=chest.getAllItemsByName();
        //itemsCount+=chestItems.size();
        items.addAll(chestItems);
      }
    }
    //System.out.println(itemsCount);
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
