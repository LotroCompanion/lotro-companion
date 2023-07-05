package delta.games.lotro.gui.lore.trade.barter.form;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.trade.barter.BarterEntry;
import delta.games.lotro.lore.trade.barter.ItemBarterEntryElement;

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
  public static List<CountedItem<Item>> getToGiveItems(BarterEntry entry)
  {
    List<CountedItem<Item>> items=new ArrayList<CountedItem<Item>>();
    for(ItemBarterEntryElement element : entry.getElementsToGive())
    {
      Item item=element.getItem();
      int quantity=element.getQuantity();
      items.add(new CountedItem<Item>(item,quantity));
    }
    return items;
  }
}
