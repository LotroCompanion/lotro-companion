package delta.games.lotro.gui.items;

import java.util.Comparator;

/**
 * Compares counted items using their name.
 * @author DAM
 */
public class CountedItemNameComparator implements Comparator<CountedItem>
{
  @Override
  public int compare(CountedItem o1, CountedItem o2)
  {
    String name1=o1.getItem().getName();
    String name2=o2.getItem().getName();
    return name1.compareTo(name2);
  }
}
