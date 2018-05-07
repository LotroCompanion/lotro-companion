package delta.games.lotro.stats.deeds;

import java.util.Comparator;

import delta.games.lotro.common.VirtueId;

/**
 * Comparator for virtue stats, using natural order of virtue identifiers.
 * @author DAM
 */
public class VirtueStatsComparator implements Comparator<VirtueStatsFromDeeds>
{
  @Override
  public int compare(VirtueStatsFromDeeds o1, VirtueStatsFromDeeds o2)
  {
    VirtueId virtue1=o1.getVirtueId();
    VirtueId virtue2=o2.getVirtueId();
    return virtue1.ordinal()-virtue2.ordinal();
  }
}
