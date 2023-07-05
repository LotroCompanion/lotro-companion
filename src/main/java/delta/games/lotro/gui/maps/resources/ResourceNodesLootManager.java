package delta.games.lotro.gui.maps.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import delta.games.lotro.common.IdentifiableComparator;
import delta.games.lotro.common.enums.ItemClass;
import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.containers.ContainerInspector;
import delta.games.lotro.lore.maps.resources.ResourcesMapDescriptor;
import delta.games.lotro.lore.maps.resources.ResourcesMapsManager;

/**
 * Provides access to the items that can be found
 * in the resource nodes.
 * @author DAM
 */
public class ResourceNodesLootManager
{
  private static final Logger LOGGER=Logger.getLogger(ResourceNodesLootManager.class);

  private CraftingLevel _level;
  private List<Item> _sourceItems;
  private Map<Integer,List<Item>> _lootItems;

  /**
   * Constructor.
   * @param level Crafting level.
   */
  public ResourceNodesLootManager(CraftingLevel level)
  {
    _level=level;
    _sourceItems=new ArrayList<Item>();
    _lootItems=new HashMap<Integer,List<Item>>();
    init();
  }

  private void init()
  {
    ResourcesMapsManager mgr=ResourcesMapsManager.getInstance();
    ResourcesMapDescriptor descriptor=mgr.getMapForLevel(_level);
    if (descriptor==null)
    {
      return;
    }
    List<Item> items=descriptor.getItems();
    for(Item resourceNode : items)
    {
      _sourceItems.add(resourceNode);
      List<Item> lootItems=ContainerInspector.getContainerContents(resourceNode);
      Integer key=Integer.valueOf(resourceNode.getIdentifier());
      _lootItems.put(key,lootItems);
    }
  }

  /**
   * Get the items associated with the given resource nodes.
   * @param sourceItems Source items.
   * @return a list of items.
   */
  public List<Item> getGlobalLoots(List<Item> sourceItems)
  {
    List<Item> ret=new ArrayList<Item>();
    Set<Item> items=new HashSet<Item>();
    for(Item sourceItem : sourceItems)
    {
      List<Item> lootItems=getLoots(sourceItem.getIdentifier());
      if (lootItems.size()>0)
      {
        items.addAll(lootItems);
      }
      else
      {
        items.add(sourceItem);
      }
    }
    List<Item> sortedItems=sortItems(new ArrayList<Item>(items));
    ret.addAll(sortedItems);
    return ret;
  }

  /**
   * Get the 'source' items (the ones found as markers).
   * @return a list of items.
   */
  public List<Item> getSourceItems()
  {
    return _sourceItems;
  }

  /**
   * Get the loot items for a given item.
   * @param itemId Source item identifier.
   * @return A list of loot items.
   */
  public List<Item> getLoots(int itemId)
  {
    List<Item> lootItems=_lootItems.get(Integer.valueOf(itemId));
    if (lootItems!=null)
    {
      return sortItems(lootItems);
    }
    return new ArrayList<Item>();
  }

  /**
   * Build an items list from a collection of item IDs.
   * @param itemIds Item identifiers to use.
   * @return A list of items.
   */
  public List<Item> buildItemsList(Collection<Integer> itemIds)
  {
    List<Item> items=new ArrayList<Item>();
    for(Integer itemId : itemIds)
    {
      Item item=ItemsManager.getInstance().getItem(itemId.intValue());
      if (item!=null)
      {
        items.add(item);
      }
    }
    return items;
  }

  /**
   * Sort items.
   * @param items Items to sort.
   * @return a new list of sorted items.
   */
  public List<Item> sortItems(List<Item> items)
  {
    //System.out.println("Sorting items: "+items);
    List<Item> ret=new ArrayList<Item>();
    Map<String,List<Item>> sortedItemsMap=new HashMap<String,List<Item>>();
    for(Item item : items)
    {
      String category=item.getSubCategory();
      List<Item> itemsForCategory=sortedItemsMap.get(category);
      if (itemsForCategory==null)
      {
        itemsForCategory=new ArrayList<Item>();
        sortedItemsMap.put(category,itemsForCategory);
      }
      itemsForCategory.add(item);
    }
    String[] categories=getCategories();
    for(String category : categories)
    {
      List<Item> itemsForCategory=sortedItemsMap.get(category);
      if (itemsForCategory!=null)
      {
        Collections.sort(itemsForCategory,new IdentifiableComparator<Item>());
        //System.out.println("\tFound items: "+itemsForCategory+" for category: "+category);
        ret.addAll(itemsForCategory);
        sortedItemsMap.remove(category);
      }
    }
    if (sortedItemsMap.size()>0)
    {
      LOGGER.warn("Unmanaged categories: "+sortedItemsMap.keySet());
      List<String> missingCategories=new ArrayList<String>(sortedItemsMap.keySet());
      Collections.sort(missingCategories);
      for(String missingCategory : missingCategories)
      {
        List<Item> itemsForCategory=sortedItemsMap.get(missingCategory);
        if (itemsForCategory!=null)
        {
          Collections.sort(itemsForCategory,new IdentifiableComparator<Item>());
          //System.out.println("\tFound items: "+itemsForCategory+" for missing category: "+missingCategory);
          ret.addAll(itemsForCategory);
        }
      }
    }
    return ret;
  }

  private String[] getCategories()
  {
    int[] categoryCodes;
    boolean live=LotroCoreConfig.isLive();
    if (live)
    {
      categoryCodes=new int[]
      {
        56,  // "Craft: Resource"
        37,  // "Craft: Component"
        38,  // "Craft: Ingredient"
        188, // "Craft: Optional Ingredient"
        164  // "Misc."
      };
    }
    else
    {
      categoryCodes=new int[]
      {
        53, // "NonInventory" for resource nodes
        38, // "Ingredient"
        56  // "Resource"
      };
    }
    return getCategories(categoryCodes);
  }

  private String[] getCategories(int[] codes)
  {
    LotroEnum<ItemClass> classEnum=LotroEnumsRegistry.getInstance().get(ItemClass.class);
    List<String> categories=new ArrayList<String>();
    for(int code : codes)
    {
      ItemClass entry=classEnum.getEntry(code);
      if (entry!=null)
      {
        String category=entry.getLabel();
        categories.add(category);
      }
    }
    return categories.toArray(new String[categories.size()]);
  }
}
