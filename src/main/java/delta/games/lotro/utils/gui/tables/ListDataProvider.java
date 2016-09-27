package delta.games.lotro.utils.gui.tables;

import java.util.List;

/**
 * Data provider that uses a Java list.
 * @param <POJO> Type of managed data.
 * @author DAM
 */
public class ListDataProvider<POJO> implements DataProvider<POJO>
{
  private List<POJO> _list;

  /**
   * Constructor.
   * @param list Wrapped list.
   */
  public ListDataProvider(List<POJO> list)
  {
    _list=list;
  }

  /**
   * Get a data item.
   * @param index Targeted index, starting at 0.
   * @return A data item.
   */
  public POJO getAt(int index)
  {
    return _list.get(index);
  }

  /**
   * Get the number of items managed by this provider.
   * @return An item count.
   */
  public int getCount()
  {
    return _list.size();
  }
}
