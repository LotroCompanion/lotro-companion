package delta.games.lotro.lore.items.index;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Index for items.
 * @author DAM
 */
public class ItemsIndex
{
  private HashMap<String,ItemCategory> _categories;

  /**
   * Constructor.
   */
  public ItemsIndex()
  {
    _categories=new HashMap<String,ItemCategory>();
  }

  /**
   * Get a category by its name. 
   * @param name Name of category to get.
   * @return A category or <code>null</code> if not found.
   */
  public ItemCategory getCategory(String name)
  {
    return _categories.get(name);
  }

  /**
   * Add an item.
   * @param category Item category.
   * @param identifier Item identifier.
   * @param name Item name.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean addItem(String category, String identifier, String name)
  {
    ItemCategory c=_categories.get(category);
    if (c==null)
    {
      c=new ItemCategory(category);
      _categories.put(category,c);
    }
    boolean ok=c.addItem(identifier,name);
    return ok;
  }

  /**
   * Get all the categories.
   * @return A possibly empty array of categories.
   */
  public String[] getCategories()
  {
    String[] ret=null;
    Set<String> ids=_categories.keySet();
    if (ids!=null)
    {
      ret=ids.toArray(new String[ids.size()]);
      Arrays.sort(ret);
    }
    else
    {
      ret=new String[0];
    }
    return ret;
  }
}
