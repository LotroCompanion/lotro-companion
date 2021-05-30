package delta.games.lotro.gui.lore.trade.barter.form;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemProxy;
import delta.games.lotro.lore.trade.barter.BarterEntry;
import delta.games.lotro.lore.trade.barter.ItemBarterEntryElement;
import delta.games.lotro.utils.Proxy;

/**
 * Utility methods for barter-related UIs.
 * @author DAM
 */
public class BarterUiUtils
{
  /**
   * Get a list of all items to give in a barter entry.
   * @param entry To use.
   * @return a list of all items.
   */
  public static List<CountedItem<ItemProxy>> getToGiveItems(BarterEntry entry)
  {
    List<CountedItem<ItemProxy>> items=new ArrayList<CountedItem<ItemProxy>>();
    for(ItemBarterEntryElement element : entry.getElementsToGive())
    {
      Proxy<Item> item=element.getItemProxy();
      ItemProxy proxy=new ItemProxy();
      proxy.setItem(item.getObject());
      int quantity=element.getQuantity();
      items.add(new CountedItem<ItemProxy>(proxy,quantity));
    }
    return items;
  }
}
