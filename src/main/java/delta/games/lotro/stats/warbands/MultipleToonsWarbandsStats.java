package delta.games.lotro.stats.warbands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;

/**
 * Warband statistics for several toons.
 * @author DAM
 */
public class MultipleToonsWarbandsStats
{
  private List<CharacterFile> _toons;
  private HashMap<String,WarbandsStats> _stats;

  /**
   * Constructor.
   */
  public MultipleToonsWarbandsStats()
  {
    _toons=new ArrayList<CharacterFile>();
    _stats=new HashMap<String,WarbandsStats>();
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
      WarbandsStats stats=loadToonStats(toon);
      if (stats!=null)
      {
        String id=toon.getIdentifier();
        _stats.put(id,stats);
      }
    }
  }

  /**
   * Get all the managed toons.
   * @return A possibly array of toons.
   */
  public CharacterFile[] getToons()
  {
    return _toons.toArray(new CharacterFile[_toons.size()]);
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
   * Get the warbands statistics for a toon.
   * @param toonIdentifier Toon identifier.
   * @return A warbands statistics instance or <code>null</code> if not found.
   */
  public WarbandsStats getStatsForToon(String toonIdentifier)
  {
    WarbandsStats stats=_stats.get(toonIdentifier);
    return stats;
  }

  private WarbandsStats loadToonStats(CharacterFile toon)
  {
    CharacterLog log=toon.getLastCharacterLog();
    WarbandsStats stats=new WarbandsStats(log);
    return stats;
  }
}
