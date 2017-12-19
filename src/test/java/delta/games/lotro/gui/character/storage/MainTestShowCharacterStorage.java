package delta.games.lotro.gui.character.storage;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.storage.AccountServerStorage;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.Chest;
import delta.games.lotro.character.storage.ItemsContainer;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.Vault;
import delta.games.lotro.character.storage.Wallet;
import delta.games.lotro.gui.items.ItemChoiceWindowController;
import delta.games.lotro.gui.items.ItemFilterController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemPropertyNames;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.plugins.StorageLoader;

/**
 * Test class to show the storage for a single character.
 * @author DAM
 */
public class MainTestShowCharacterStorage
{
  private static final Logger LOGGER=Logger.getLogger(MainTestShowCharacterStorage.class);

  private void doIt()
  {
    String account="glorfindel666";
    String server="Landroval";
    String toon="Meva";
    boolean showShared=false;
    StorageLoader loader=new StorageLoader();
    AccountServerStorage storage=loader.loadStorage(account,server);
    if (storage!=null)
    {
      // Own bags
      {
        List<StoredItem> storedItems=getAllItems(toon,storage,true);
        show(storedItems);
      }
      // Own vault
      {
        List<StoredItem> storedItems=getAllItems(toon,storage,false);
        show(storedItems);
      }
      // Own wallet
      {
        CharacterStorage characterStorage=storage.getStorage(toon,true);
        Wallet ownWallet=characterStorage.getWallet();
        List<StoredItem> storedItems=ownWallet.getAllItemsByName();
        show(storedItems);
      }
      if (showShared)
      {
        // Shared wallet
        {
          ItemsContainer container=storage.getSharedWallet();
          List<StoredItem> storedItems=container.getAllItemsByName();
          show(storedItems);
        }
        // Shared vault
        {
          Vault sharedVault=storage.getSharedVault();
          List<StoredItem> storedItems=getAllItems(sharedVault);
          show(storedItems);
        }
      }
    }
  }

  private void show(List<StoredItem> storedItems)
  {
    List<Item> items=getItems(storedItems);
    ItemFilterController filterController=new ItemFilterController();
    Filter<Item> filter=filterController.getFilter();
    ItemChoiceWindowController choiceCtrl=new ItemChoiceWindowController(null,null,items,filter,filterController);
    choiceCtrl.show();
  }

  private List<StoredItem> getAllItems(String character, AccountServerStorage storage, boolean bags)
  {
    CharacterStorage characterStorage=storage.getStorage(character,true);
    Vault container=(bags?characterStorage.getBags():characterStorage.getOwnVault());
    return getAllItems(container);
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

  private List<Item> getItems(List<StoredItem> storedItems)
  {
    ItemsManager itemsMgr=ItemsManager.getInstance();
    List<Item> allItems=itemsMgr.getAllItems();
    List<Item> selection=new ArrayList<Item>();
    for(StoredItem storedItem : storedItems)
    {
      Item selectedItem=null;
      for(Item item : allItems)
      {
        Item match=match(item,storedItem);
        if (match!=null)
        {
          selectedItem=item;
          break;
        }
      }
      if (selectedItem!=null)
      {
        selection.add(selectedItem);
      }
      else
      {
        LOGGER.warn("Could not find item: "+storedItem.getName());
      }
    }
    return selection;
  }

  private Item match(Item item, StoredItem storedItem)
  {
    String itemName=item.getName();
    String storedItemName=storedItem.getName();
    if (storedItemName.equals(itemName))
    {
      String itemIconId=item.getProperty(ItemPropertyNames.ICON_ID);
      String storedIconId=storedItem.getIconId();
      if (itemIconId.equals(storedIconId))
      {
        String itemBackground=item.getProperty(ItemPropertyNames.BACKGROUND_ICON_ID);
        String storedBackgroundIconId=storedItem.getBackgroundIconId();
        if (storedBackgroundIconId==null)
        {
          storedBackgroundIconId=itemBackground;
          return item;
        }
        if (itemBackground.equals(storedBackgroundIconId))
        {
          return item;
        }
      }
    }
    return null;
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
