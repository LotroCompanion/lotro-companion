package delta.games.lotro.common.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of object items.
 * @author DAM
 */
public class ObjectsSet
{
  private List<ObjectItem> _items;
  private List<Integer> _quantities;

  /**
   * Constructor.
   */
  public ObjectsSet()
  {
    _items=new ArrayList<ObjectItem>();
    _quantities=new ArrayList<Integer>();
  }

  /**
   * Add an object item in this set.
   * @param item Item to add.
   * @param quantity Quantity.
   */
  public void addObject(ObjectItem item, int quantity)
  {
    if ((item!=null) && (quantity>0))
    {
      _items.add(item);
      _quantities.add(Integer.valueOf(quantity));
    }
  }

  /**
   * Get the number of object items.
   * @return a positive integer. 
   */
  public int getNbObjectItems()
  {
    return _items.size();
  }

  /**
   * Get the quantity of a given object item.
   * @param index Index of the targeted object item.
   * @return a strictly positive integer.
   */
  public int getQuantity(int index)
  {
    return _quantities.get(index).intValue();
  }

  /**
   * Get the object item at specified index.
   * @param index Index of the targeted object item.
   * @return An object item.
   */
  public ObjectItem getItem(int index)
  {
    return _items.get(index);
  }

  @Override
  public String toString()
  {
    String ret="";
    int size=_items.size();
    if (size>0)
    {
      StringBuilder sb=new StringBuilder();
      for(int i=0;i<size;i++)
      {
        if (i>0)
        {
          sb.append(", ");
        }
        ObjectItem item=getItem(i);
        sb.append(item);
        int quantity=getQuantity(i);
        if (quantity>1)
        {
          sb.append(" (x").append(quantity).append(')');
        }
      }
      ret=sb.toString();
    }
    return ret;
  }
}
