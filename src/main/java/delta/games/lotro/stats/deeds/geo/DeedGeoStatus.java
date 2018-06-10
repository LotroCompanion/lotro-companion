package delta.games.lotro.stats.deeds.geo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Status for the geographic part of a deed.
 * @author DAM
 */
public class DeedGeoStatus
{
  private Map<Integer,DeedGeoPointStatus> _pointStatuses;

  /**
   * Constructor.
   */
  public DeedGeoStatus()
  {
    _pointStatuses=new HashMap<Integer,DeedGeoPointStatus>();
  }

  /**
   * Get the status for a point.
   * @param pointId Point identifier.
   * @param createIfNeeded <code>true</code> to create a new status if it is
   * not found.
   * @return A status or <code>null</code>.
   */
  public DeedGeoPointStatus getStatus(int pointId, boolean createIfNeeded)
  {
    Integer key=Integer.valueOf(pointId);
    DeedGeoPointStatus status=_pointStatuses.get(key);
    if ((status==null) && (createIfNeeded))
    {
      status=new DeedGeoPointStatus(pointId);
      _pointStatuses.put(key,status);
    }
    return status;
  }

  /**
   * Get all points statuses, sorted by point ID.
   * @return A list of point status.
   */
  public List<DeedGeoPointStatus> getPointStatuses()
  {
    List<Integer> ids=new ArrayList<Integer>(_pointStatuses.keySet());
    Collections.sort(ids);
    List<DeedGeoPointStatus> ret=new ArrayList<DeedGeoPointStatus>();
    for(Integer id : ids)
    {
      DeedGeoPointStatus status=getStatus(id.intValue(),false);
      ret.add(status);
    }
    return ret;
  }

  /**
   * Remove all stored data.
   */
  public void clear()
  {
    _pointStatuses.clear();
  }

  @Override
  public String toString()
  {
    return "Geo status: "+_pointStatuses;
  }
}
