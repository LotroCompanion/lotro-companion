package delta.games.lotro.utils.gui.tables;

/**
 * Interface of an object that can access to indexed POJO objects.
 * @param <POJO> Type of managed data.
 * @author DAM
 */
public interface DataProvider<POJO>
{
  /**
   * Get a data item.
   * @param index Targeted index, starting at 0.
   * @return A data item.
   */
  POJO getAt(int index);

  /**
   * Get the number of items managed by this provider.
   * @return An item count.
   */
  int getCount();
}
