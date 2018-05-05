package delta.games.lotro.stats.deeds;

import delta.games.lotro.common.VirtueId;

/**
 * Deed statistics for a single virtue.
 * @author DAM
 */
public class VirtueStatsFromDeeds
{
  private VirtueId _virtueId;
  private int _points;
  private int _deedsCount;

  /**
   * Constructor.
   * @param virtueId Managed virtue.
   */
  public VirtueStatsFromDeeds(VirtueId virtueId)
  {
    _virtueId=virtueId;
  }

  /**
   * Get the managed virtue.
   * @return a virtue.
   */
  public VirtueId getVirtueId()
  {
    return _virtueId;
  }

  /**
   * Add some virtue points for a single deed.
   * @param points Points to add.
   */
  public void add(int points)
  {
    _points+=points;
    _deedsCount++;
  }

  /**
   * Get the total virtue points earned for this virtue
   * by the registered deeds.
   * @return A points count.
   */
  public int getPoints()
  {
    return _points;
  }

  /**
   * Get the number of deeds used to build the associated virtue.
   * @return A deeds count.
   */
  public int getDeedsCount()
  {
    return _deedsCount;
  }
}
