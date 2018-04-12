package delta.games.lotro.stats.deeds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Storage for all deed statuses for a single character.
 * @author DAM
 */
public class DeedsStatusManager
{
  private Map<String,DeedStatus> _status;

  /**
   * Constructor.
   */
  public DeedsStatusManager()
  {
    _status=new HashMap<String,DeedStatus>();
  }

  /**
   * Get a deed status.
   * @param deedKey Key of the targeted deed.
   * @param createIfNecessary Indicates if the status shall be created if it
   * does not exist.
   * @return A deed status or <code>null</code>.
   */
  public DeedStatus get(String deedKey, boolean createIfNecessary)
  {
    DeedStatus ret=_status.get(deedKey);
    if ((ret==null) && (createIfNecessary))
    {
      ret=new DeedStatus(deedKey);
      _status.put(deedKey,ret);
    }
    return ret;
  }

  /**
   * Get all managed deed statuses.
   * @return A list of deed statuses, ordered by deed key.
   */
  public List<DeedStatus> getAll()
  {
    List<String> deedKeys=new ArrayList<String>(_status.keySet());
    Collections.sort(deedKeys);
    List<DeedStatus> ret=new ArrayList<DeedStatus>();
    for(String deedKey : deedKeys)
    {
      ret.add(_status.get(deedKey));
    }
    return ret;
  }
}
