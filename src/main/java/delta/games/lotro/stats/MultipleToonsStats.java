package delta.games.lotro.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import delta.games.lotro.character.CharacterFile;

/**
 * Statistics for several toons.
 * @param <T> Stats type.
 * @author DAM
 */
public abstract class MultipleToonsStats<T>
{
  private List<CharacterFile> _toons;
  private HashMap<String,T> _stats;

  /**
   * Constructor.
   */
  public MultipleToonsStats()
  {
    _toons=new ArrayList<CharacterFile>();
    _stats=new HashMap<String,T>();
  }

  /**
   * Get the number of managed toons.
   * @return A number of toons.
   */
  public int getNumberOfToons()
  {
    return _toons.size();
  }

  /**
   * Add a new toon.
   * @param toon Toon to add.
   */
  public void addToon(CharacterFile toon)
  {
    if (toon!=null)
    {
      _toons.add(toon);
      T stats=loadToonStats(toon);
      if (stats!=null)
      {
        String id=toon.getIdentifier();
        _stats.put(id,stats);
      }
    }
  }

  /**
   * Get a list of all the managed toons.
   * @return A possibly empty list of toons.
   */
  public List<CharacterFile> getToonsList()
  {
    List<CharacterFile> ret=new ArrayList<CharacterFile>();
    ret.addAll(_toons);
    return ret;
  }

  /**
   * Remove a toon.
   * @param toon Toon to remove.
   */
  public void removeToon(CharacterFile toon)
  {
    String toonID=toon.getIdentifier();
    _stats.remove(toonID);
    _toons.remove(toon);
  }

  /**
   * Get the statistics for a toon.
   * @param toonIdentifier Toon identifier.
   * @return A statistics instance or <code>null</code> if not found.
   */
  public T getStatsForToon(String toonIdentifier)
  {
    T stats=_stats.get(toonIdentifier);
    return stats;
  }

  protected abstract T loadToonStats(CharacterFile toon);
}
