package delta.games.lotro.gui.items;

import java.util.Comparator;

import delta.games.lotro.character.storage.StoredItem;

/**
 * Compares stored items using their name.
 * @author DAM
 */
public class StoredItemNameComparator implements Comparator<StoredItem>
{
  @Override
  public int compare(StoredItem o1, StoredItem o2)
  {
    String name1=o1.getName();
    String name2=o2.getName();
    return name1.compareTo(name2);
  }
}
