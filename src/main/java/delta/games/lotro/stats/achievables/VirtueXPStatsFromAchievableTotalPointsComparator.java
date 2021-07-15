package delta.games.lotro.stats.achievables;

import java.util.Comparator;

/**
 * Comparator for virtue XP stats using their total virtue XP points.
 * @author DAM
 */
public class VirtueXPStatsFromAchievableTotalPointsComparator implements Comparator<VirtueXPStatsFromAchievable>
{
  @Override
  public int compare(VirtueXPStatsFromAchievable o1, VirtueXPStatsFromAchievable o2)
  {
    return Integer.compare(o1.getPoints(),o2.getPoints());
  }
}
