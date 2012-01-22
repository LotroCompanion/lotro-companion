package delta.games.lotro.utils.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import delta.common.utils.NumericTools;

/**
 * Resources mapping.
 * Maps resource integer identifiers to Wiki string identifiers. 
 * @author DAM
 */
public class ResourcesMapping
{
  private HashMap<Integer,String> _identifiers;

  /**
   * Constructor.
   */
  public ResourcesMapping()
  {
    _identifiers=new HashMap<Integer,String>();
  }

  /**
   * Get the identifier for a resource.
   * @param resource Targeted resource.
   * @return An identifier or <code>null</code> if not found.
   */
  public String getIdentifier(int resource)
  {
    Integer key=Integer.valueOf(resource);
    String ret=_identifiers.get(key);
    return ret;
  }

  /**
   * Register a new resource mapping.
   * @param resource Resource integer identifier.
   * @param identifier String identifier.
   */
  public void registerMapping(int resource, String identifier)
  {
    Integer key=Integer.valueOf(resource);
    _identifiers.put(key,identifier);
  }

  /**
   * Register a new resource mapping.
   * @param resourceURL Resource URL.
   * @param identifier String identifier.
   */
  public void registerMapping(String resourceURL, String identifier)
  {
    int resource=getResourceIdentifierFromURL(resourceURL);
    if (resource!=-1)
    {
      Integer key=Integer.valueOf(resource);
      _identifiers.put(key,identifier);
    }
  }

  /**
   * Extract a resource identifier from a LOTRO resource URL.
   * @param url URL to use.
   * @return An identifier or <code>-1</code>.
   */
  public int getResourceIdentifierFromURL(String url)
  {
    int ret=-1;
    if (url!=null)
    {
      // http://lorebook.lotro.com/wiki/Special:LotroResource?id=1879198643
      int index=url.indexOf("?id=");
      if (index!=-1)
      {
        ret=NumericTools.parseInt(url.substring(index+4),-1);
      }
    }
    return ret;
  }

  /**
   * Get a sorted list of all resource keys.
   * @return an array of integers.
   */
  public Integer[] getKeys()
  {
    List<Integer> keys=new ArrayList<Integer>();
    keys.addAll(_identifiers.keySet());
    Collections.sort(keys);
    Integer[] ret=keys.toArray(new Integer[keys.size()]);
    return ret;
  }
}
