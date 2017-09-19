package delta.games.lotro.gui.character.storage;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.storage.AccountServerStorage;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.Chest;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.Vault;
import delta.games.lotro.gui.items.ItemChoiceWindowController;
import delta.games.lotro.gui.items.ItemFilterController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemPropertyNames;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.filters.ItemNameFilter;
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
    StorageLoader loader=new StorageLoader();
    AccountServerStorage storage=loader.loadStorage(account,server);
    if (storage!=null)
    {
      //ItemsContainer container=storage.getSharedWallet();
      //List<StoredItem> storedItems=container.getAllItemsByName();
      List<StoredItem> storedItems=getAllItems("Meva",storage,false);
      List<Item> items=getItems(storedItems);

      ItemNameFilter filter=new ItemNameFilter();
      ItemFilterController filterController=new ItemFilterController(filter);
      ItemChoiceWindowController choiceCtrl=new ItemChoiceWindowController(null,items,filter,filterController);
      Item ret=choiceCtrl.editModal();
      if (ret!=null)
      {
        System.out.println(ret.dump());
      }
    }
  }

  private List<StoredItem> getAllItems(String character, AccountServerStorage storage, boolean bags)
  {
    CharacterStorage characterStorage=storage.getStorage(character,true);
    List<StoredItem> items=new ArrayList<StoredItem>();
    Vault container=(bags?characterStorage.getBags():characterStorage.getOwnVault());
    int chests=container.getChestCount();
    int itemsCount=0;
    for(int i=0;i<chests;i++)
    {
      Chest chest=container.getChest(i);
      if (chest!=null)
      {
        List<StoredItem> chestItems=chest.getAllItemsByName();
        itemsCount+=chestItems.size();
        items.addAll(chestItems);
      }
    }
    System.out.println(itemsCount);
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
      selection.add(selectedItem);
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
