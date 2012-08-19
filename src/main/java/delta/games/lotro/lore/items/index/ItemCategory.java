package delta.games.lotro.lore.items.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.utils.LotroLoggers;

/**
 * Item category.
 * @author DAM
 */
public class ItemCategory
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private String _name;
  private HashMap<String,ItemSummary> _idsToSummary;

  /**
   * Constructor.
   * @param name Category name.
   */
  public ItemCategory(String name)
  {
    _name=name;
    _idsToSummary=new HashMap<String,ItemSummary>();
  }
  
  /**
   * Get the name of this category.
   * @return a category name.
   */
  public String getName()
  {
    return _name;
  }
  
  /**
   * Add a item.
   * @param identifier Item identifier.
   * @param name Item name.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean addItem(String identifier, String name)
  {
    boolean ret;
    ItemSummary old=_idsToSummary.get(identifier);
    if (old==null)
    {
      ItemSummary summary=new ItemSummary(identifier,name);
      _idsToSummary.put(identifier,summary);
      ret=true;
    }
    else
    {
      _logger.warn("Duplicate item identifier ["+identifier+"] in category ["+_name+"]!");
      ret=false;
    }
    return ret;
  }

  /**
   * Get all the items of this category.
   * @return A possibly empty array of item summaries.
   */
  public ItemSummary[] getItems()
  {
    List<String> ids=new ArrayList<String>(_idsToSummary.keySet());
    Collections.sort(ids);
    ItemSummary[] ret=new ItemSummary[ids.size()];
    int index=0;
    for(String id : ids)
    {
      ret[index]=_idsToSummary.get(id);
      index++;
    }
    return ret;
  }

  @Override
  public String toString()
  {
    return _name;
  }
}
