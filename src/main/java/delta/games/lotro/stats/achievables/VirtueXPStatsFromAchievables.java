package delta.games.lotro.stats.achievables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import delta.games.lotro.lore.quests.Achievable;

/**
 * Virtue XP statistics.
 * @author DAM
 */
public class VirtueXPStatsFromAchievables
{
  private int _totalVirtueXP;
  private int _totalCompletions;
  private Map<Integer,VirtueXPStatsFromAchievable> _mapByAchievable;

  /**
   * Constructor.
   */
  public VirtueXPStatsFromAchievables()
  {
    _mapByAchievable=new HashMap<Integer,VirtueXPStatsFromAchievable>();
  }

  /**
   * Register an achievable.
   * @param achievable Achievable.
   * @param virtueXP Virtue XP reward.
   * @param completions Completions count.
   */
  public void addAchievable(Achievable achievable, int virtueXP, int completions)
  {
    VirtueXPStatsFromAchievable entry=new VirtueXPStatsFromAchievable(achievable);
    entry.setCompletions(completions,virtueXP);
    _mapByAchievable.put(Integer.valueOf(achievable.getIdentifier()),entry);
    _totalVirtueXP+=entry.getPoints();
    _totalCompletions+=completions;
  }

  /**
   * Get the number of registered achievables.
   * @return A count.
   */
  public int getEntriesCount()
  {
    return _mapByAchievable.size();
  }

  /**
   * Clear all data.
   */
  public void clear()
  {
    _mapByAchievable.clear();
    _totalVirtueXP=0;
    _totalCompletions=0;
  }

  /**
   * Get the total virtue XP.
   * @return a virtue XP count.
   */
  public int getTotalVirtueXP()
  {
    return _totalVirtueXP;
  }

  /**
   * Get the total completions count.
   * @return a count.
   */
  public int getTotalCompletions()
  {
    return _totalCompletions;
  }

  /**
   * Get managed entries, sorted by total virtue XP points.
   * @return A list of managed entries.
   */
  public List<VirtueXPStatsFromAchievable> getEntries()
  {
    List<VirtueXPStatsFromAchievable> ret=new ArrayList<VirtueXPStatsFromAchievable>(_mapByAchievable.values());
    Collections.sort(ret,Collections.reverseOrder(new VirtueXPStatsFromAchievableTotalPointsComparator()));
    return ret;
  }
}
