package delta.games.lotro.stats.deeds;

import delta.games.lotro.character.virtues.VirtueDescription;

/**
 * Deed statistics for a single virtue.
 * @author DAM
 */
public class VirtueStatsFromDeeds
{
  private VirtueDescription _virtue;
  private int _points;
  private int _deedsCount;

  /**
   * Constructor.
   * @param virtue Managed virtue.
   */
  public VirtueStatsFromDeeds(VirtueDescription virtue)
  {
    _virtue=virtue;
  }

  /**
   * Get the managed virtue.
   * @return a virtue.
   */
  public VirtueDescription getVirtue()
  {
    return _virtue;
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
