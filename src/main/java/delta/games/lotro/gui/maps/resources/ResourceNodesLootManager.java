package delta.games.lotro.gui.maps.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import delta.games.lotro.common.IdentifiableComparator;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.items.Container;
import delta.games.lotro.lore.items.ContainersManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsContainer;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.maps.resources.ResourcesMapDescriptor;
import delta.games.lotro.lore.maps.resources.ResourcesMapsManager;
import delta.games.lotro.utils.Proxy;

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
    List<Proxy<Item>> items=descriptor.getItems();
    for(Proxy<Item> resourceNodeProxy : items)
    {
      Item resourceNode=resourceNodeProxy.getObject();
      _sourceItems.add(resourceNode);
      List<Item> lootItems=handleResourceNode(resourceNode);
      Integer key=Integer.valueOf(resourceNode.getIdentifier());
      _lootItems.put(key,lootItems);
    }
  }

  /**
   * Get the items associated to the resource nodes of the managed crafting level.
   * @return a list of items.
   */
  public List<Item> getGlobalLoots()
  {
    List<Item> ret=new ArrayList<Item>();
    Set<Item> items=new HashSet<Item>();
    for(Item sourceItem : _sourceItems)
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

  private List<Item> handleResourceNode(Item resourceNode)
  {
    List<Item> ret=new ArrayList<Item>();
    Set<Integer> itemIds=new HashSet<Integer>();
    ContainersManager containersMgr=ContainersManager.getInstance();
    Container container=containersMgr.getContainerById(resourceNode.getIdentifier());
    if (container instanceof ItemsContainer)
    {
      ItemsContainer itemsContainer=(ItemsContainer)container;
      itemIds.addAll(itemsContainer.getItemIds());
    }
    for(Integer itemId : itemIds)
    {
      Item item=ItemsManager.getInstance().getItem(itemId.intValue());
      if (item!=null)
      {
        ret.add(item);
      }
    }
    return ret;
  }

  private List<Item> sortItems(List<Item> items)
  {
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
    String[] categories={"Craft: Resource", "Craft: Component", "Craft: Ingredient", "Craft: Optional Ingredient"};
    for(String category : categories)
    {
      List<Item> itemsForCategory=sortedItemsMap.get(category);
      if (itemsForCategory!=null)
      {
        Collections.sort(itemsForCategory,new IdentifiableComparator<Item>());
        ret.addAll(itemsForCategory);
        sortedItemsMap.remove(category);
      }
    }
    if (sortedItemsMap.size()>0)
    {
      LOGGER.warn("Unmanaged categories: "+sortedItemsMap.keySet());
    }
    return ret;
  }
}
