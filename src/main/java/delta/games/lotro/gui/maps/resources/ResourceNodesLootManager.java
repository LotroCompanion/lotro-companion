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

  /**
   * Get the items associated to the resource nodes of a given crafting level.
   * @param level Crafting level to use.
   * @return a list of items.
   */
  public List<Item> getLoots(CraftingLevel level)
  {
    List<Item> ret=new ArrayList<Item>();
    //System.out.println("Using: "+level);
    ResourcesMapsManager mgr=ResourcesMapsManager.getInstance();
    ResourcesMapDescriptor descriptor=mgr.getMapForLevel(level);
    if (descriptor!=null)
    {
      Set<Integer> itemIds=new HashSet<Integer>();
      List<Proxy<Item>> items=descriptor.getItems();
      //System.out.println("\tNb nodes: "+items.size());
      for(Proxy<Item> resourceNodeProxy : items)
      {
        Item resourceNode=resourceNodeProxy.getObject();
        handleResourceNode(resourceNode,itemIds);
      }
      List<Item> sortedItems=sortItems(itemIds);
      /*
      for(Item item : sortedItems)
      {
        System.out.println("\t"+item.getName()+" - "+item.getSubCategory());
      }
      */
      ret.addAll(sortedItems);
    }
    return ret;
  }

  private void handleResourceNode(Item resourceNode, Set<Integer> itemIds)
  {
    //System.out.println("Resource node: "+resourceNode);
    ContainersManager containersMgr=ContainersManager.getInstance();
    Container container=containersMgr.getContainerById(resourceNode.getIdentifier());
    if (container instanceof ItemsContainer)
    {
      ItemsContainer itemsContainer=(ItemsContainer)container;
      itemIds.addAll(itemsContainer.getItemIds());
    }
  }

  private List<Item> sortItems(Set<Integer> itemIds)
  {
    List<Item> ret=new ArrayList<Item>();
    Map<String,List<Item>> sortedItemsMap=new HashMap<String,List<Item>>();
    for(Integer itemId : itemIds)
    {
      Item item=ItemsManager.getInstance().getItem(itemId.intValue());
      if (item==null)
      {
        continue;
      }
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
      List<Item> items=sortedItemsMap.get(category);
      if (items!=null)
      {
        Collections.sort(items,new IdentifiableComparator<Item>());
        ret.addAll(items);
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
