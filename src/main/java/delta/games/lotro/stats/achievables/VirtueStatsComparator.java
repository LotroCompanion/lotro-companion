package delta.games.lotro.stats.achievables;

import java.util.Comparator;

import delta.games.lotro.character.virtues.VirtueKeyComparator;

/**
 * Comparator for virtue stats, using natural order of virtue identifiers.
 * @author DAM
 */
public class VirtueStatsComparator implements Comparator<VirtueStatsFromAchievables>
{
  private VirtueKeyComparator _virtueComparator=new VirtueKeyComparator();

  @Override
  public int compare(VirtueStatsFromAchievables o1, VirtueStatsFromAchievables o2)
  {
    return _virtueComparator.compare(o1.getVirtue(),o2.getVirtue());
  }
}
