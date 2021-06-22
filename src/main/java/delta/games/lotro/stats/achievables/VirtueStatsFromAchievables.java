package delta.games.lotro.stats.achievables;

import delta.games.lotro.character.virtues.VirtueDescription;

/**
 * Statistics for a single virtue.
 * @author DAM
 */
public class VirtueStatsFromAchievables
{
  private VirtueDescription _virtue;
  private int _points;
  private int _achievablesCount;

  /**
   * Constructor.
   * @param virtue Managed virtue.
   */
  public VirtueStatsFromAchievables(VirtueDescription virtue)
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
   * Add some virtue points for a single achievable.
   * @param points Points to add.
   */
  public void add(int points)
  {
    _points+=points;
    _achievablesCount++;
  }

  /**
   * Get the total virtue points earned for this virtue
   * by the registered achievables.
   * @return A points count.
   */
  public int getPoints()
  {
    return _points;
  }

  /**
   * Get the number of achievables used to build the associated virtue.
   * @return A count.
   */
  public int getAchievablesCount()
  {
    return _achievablesCount;
  }
}
