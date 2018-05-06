package delta.games.lotro.gui.items;

import delta.games.lotro.lore.items.Item;

/**
 * Item with an associated count.
 * @author DAM
 */
public class CountedItem
{
  private int _count;
  private Item _item;

  /**
   * Constructor.
   * @param item Item.
   * @param count Count.
   */
  public CountedItem(Item item, int count)
  {
    _item=item;
    _count=count;
  }

  /**
   * Add some items.
   * @param count Number of items to add.
   */
  public void add(int count)
  {
    _count+=count;
  }

  /**
   * Get the number of items.
   * @return A count.
   */
  public int getCount()
  {
    return _count;
  }

  /**
   * Get the targeted item.
   * @return An item.
   */
  public Item getItem()
  {
    return _item;
  }
}
