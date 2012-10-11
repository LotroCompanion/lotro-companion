package delta.games.lotro.lore.deeds.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Deed category.
 * @author DAM
 */
public class DeedCategory
{
  private String _name;
  private HashMap<String,List<DeedSummary>> _idsToSummary;

  /**
   * Constructor.
   * @param name Category name.
   */
  public DeedCategory(String name)
  {
    _name=name;
    _idsToSummary=new HashMap<String,List<DeedSummary>>();
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
   * Add a deed.
   * @param identifier Deed identifier.
   * @param key Deed key.
   * @param name Deed name.
   */
  public void addDeed(int identifier, String key, String name)
  {
    List<DeedSummary> list=_idsToSummary.get(key);
    if (list==null)
    {
      list=new ArrayList<DeedSummary>();
      _idsToSummary.put(key,list);
    }
    DeedSummary summary=new DeedSummary(identifier,key,name);
    list.add(summary);
  }

  /**
   * Get all the deeds of this category.
   * @return A possibly empty array of deed summaries.
   */
  public DeedSummary[] getDeeds()
  {
    List<String> ids=new ArrayList<String>(_idsToSummary.keySet());
    Collections.sort(ids);
    List<DeedSummary> summaries=new ArrayList<DeedSummary>();
    for(String id : ids)
    {
      summaries.addAll(_idsToSummary.get(id));
    }
    DeedSummary[] ret=summaries.toArray(new DeedSummary[summaries.size()]);
    return ret;
  }

  /**
   * Get all the keys of this category.
   * @return A possibly empty array of deed keys.
   */
  public String[] getKeys()
  {
    List<String> ids=new ArrayList<String>(_idsToSummary.keySet());
    Collections.sort(ids);
    String[] ret=ids.toArray(new String[ids.size()]);
    return ret;
  }
  
  @Override
  public String toString()
  {
    return _name;
  }
}
