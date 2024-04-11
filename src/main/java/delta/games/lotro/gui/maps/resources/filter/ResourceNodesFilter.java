package delta.games.lotro.gui.maps.resources.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.maps.data.Marker;

/**
 * Filter on resource nodes.
 * @author DAM
 */
public class ResourceNodesFilter implements Filter<Marker>
{
  private Map<Integer,Boolean> _selectedItems;

  /**
   * Constructor.
   */
  public ResourceNodesFilter()
  {
    _selectedItems=new HashMap<Integer,Boolean>();
  }

  /**
   * Set the items to use.
   * @param sourceItems Items to use.
   */
  public void setItems(List<Item> sourceItems)
  {
    _selectedItems.clear();
    for(Item sourceItem : sourceItems)
    {
      setSelected(sourceItem.getIdentifier(),true);
    }
  }

  /**
   * Indicates if the given item is selected or not.
   * @param itemId Item identifier.
   * @return <code>true</code> if it is, <code>false</code> otherwise.
   */
  public boolean isSelected(int itemId)
  {
    Integer key=Integer.valueOf(itemId);
    Boolean ok=_selectedItems.get(key);
    return ((ok!=null) && (ok.booleanValue()));
  }

  /**
   * Set the selected flag for an item.
   * @param itemId Item identifier.
   * @param selected Selection flag.
   */
  public void setSelected(int itemId, boolean selected)
  {
    Integer key=Integer.valueOf(itemId);
    _selectedItems.put(key,Boolean.valueOf(selected));
  }

  @Override
  public boolean accept(Marker item)
  {
    return isSelected(item.getDid());
  }
}
