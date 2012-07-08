package delta.games.lotro.lore.deeds.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.utils.LotroLoggers;

/**
 * Deed category.
 * @author DAM
 */
public class DeedCategory
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private String _name;
  private HashMap<String,DeedSummary> _idsToSummary;

  /**
   * Constructor.
   * @param name Category name.
   */
  public DeedCategory(String name)
  {
    _name=name;
    _idsToSummary=new HashMap<String,DeedSummary>();
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
   * @param name Deed name.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean addDeed(String identifier, String name)
  {
    boolean ret;
    DeedSummary old=_idsToSummary.get(identifier);
    if (old==null)
    {
      DeedSummary summary=new DeedSummary(identifier,name);
      _idsToSummary.put(identifier,summary);
      ret=true;
    }
    else
    {
      _logger.warn("Duplicate deed identifier ["+identifier+"] in category ["+_name+"]!");
      ret=false;
    }
    return ret;
  }

  /**
   * Get all the deeds of this category.
   * @return A possibly empty array of deed summaries.
   */
  public DeedSummary[] getDeeds()
  {
    List<String> ids=new ArrayList<String>(_idsToSummary.keySet());
    Collections.sort(ids);
    DeedSummary[] ret=new DeedSummary[ids.size()];
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
