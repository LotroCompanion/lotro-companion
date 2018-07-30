package delta.games.lotro.gui.items;

import java.util.Comparator;

import delta.games.lotro.lore.items.CountedItem;

/**
 * Compares counted items using their name.
 * @author DAM
 */
public class CountedItemNameComparator implements Comparator<CountedItem>
{
  @Override
  public int compare(CountedItem o1, CountedItem o2)
  {
    String name1=o1.getName();
    String name2=o2.getName();
    return name1.compareTo(name2);
  }
}
