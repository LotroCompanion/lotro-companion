package delta.games.lotro.gui.items;

import java.util.Comparator;

import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.ItemProvider;

/**
 * Compares counted items using their name.
 * @author DAM
 * @param <T> Type of managed items.
 */
public class CountedItemNameComparator<T extends ItemProvider> implements Comparator<CountedItem<T>>
{
  @Override
  public int compare(CountedItem<T> o1, CountedItem<T> o2)
  {
    String name1=o1.getName();
    String name2=o2.getName();
    return name1.compareTo(name2);
  }
}
