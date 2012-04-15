package delta.games.lotro.gui.utils.toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description of a tool bar.
 * @author DAM
 */
public class ToolbarModel
{
  private static final String SEPARATOR_ID_SEED="#separator#";

  /**
   * List of toolbar items.
   */
  private List<ToolbarItem> _items;
  private HashMap<String,ToolbarItem> _itemsMap;

  /**
   * Constructor.
   */
  public ToolbarModel()
  {
    _items=new ArrayList<ToolbarItem>();
    _itemsMap=new HashMap<String,ToolbarItem>();
  }

  private String getAvailableSeparatorId()
  {
    int index=0;
    String ret=null;
    do
    {
      String id=SEPARATOR_ID_SEED+index;
      ToolbarItem item=_itemsMap.get(id);
      if (item==null)
      {
        ret=id;
      }
      else
      {
        index++;
      }
    }
    while(ret==null);
    return ret;
  }

  /**
   * Add a new toolbar icon item.
   * @param item Description of item to add.
   */
  public void addToolbarIconItem(ToolbarIconItem item)
  {
    if (item!=null)
    {
      String id=item.getItemId();
      ToolbarItem old=_itemsMap.get(id);
      if (old==null)
      {
        _items.add(item);
        _itemsMap.put(id,item);
      }
    }
  }

  /**
   * Add a separator.
   */
  public void addSeparator()
  {
    String id=getAvailableSeparatorId();
    ToolbarSeparatorItem item=new ToolbarSeparatorItem(id);
    _items.add(item);
    _itemsMap.put(id,item);
  }

  /**
   * Get the number of items in this toolbar.
   * @return a number of items.
   */
  public int getNumberOfItems()
  {
    return _items.size();
  }

  /**
   * Get the item at the given index.
   * @param index Index of targeted item.
   * @return An item or <code>null</code> if no such index.
   */
  public ToolbarItem getItemAt(int index)
  {
    ToolbarItem ret=null;
    if ((index>=0) && (index<_items.size()))
    {
      ret=_items.get(index);
    }
    return ret;
  }

  /**
   * Get an item by its identifier.
   * @param itemId Identifier of targeted item.
   * @return An item or <code>null</code> if no such item.
   */
  public ToolbarItem getItemById(String itemId)
  {
    ToolbarItem ret=_itemsMap.get(itemId);
    return ret;
  }

  /**
   * Get all items.
   * @return An possibly empty array of items.
   */
  public ToolbarItem[] getItems()
  {
    int nb=_items.size();
    ToolbarItem[] ret=_items.toArray(new ToolbarItem[nb]);
    return ret;
  }

  /**
   * Remove an item.
   * @param item Item to remove.
   */
  public void removeItem(ToolbarItem item)
  {
    if (item!=null)
    {
      if (_items.contains(item))
      {
        String id=item.getItemId();
        _itemsMap.remove(id);
        _items.remove(item);
      }
    }
  }

  /**
   * Remove all items.
   */
  public void removeAll()
  {
    _items.clear();
    _itemsMap.clear();
  }
}
