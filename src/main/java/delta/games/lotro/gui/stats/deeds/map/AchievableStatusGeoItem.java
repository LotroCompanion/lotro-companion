package delta.games.lotro.gui.stats.deeds.map;

import delta.games.lotro.lore.quests.geo.AchievableGeoPoint;

/**
 * Status of a single geographic point in an achievable.
 * @author DAM
 */
public class AchievableStatusGeoItem
{
  private AchievableGeoPoint _point;
  private boolean _completed;

  /**
   * Constructor.
   * @param point Point to use.
   */
  public AchievableStatusGeoItem(AchievableGeoPoint point)
  {
    _point=point;
  }

  /**
   * Get the managed point.
   * @return the managed point.
   */
  public AchievableGeoPoint getPoint()
  {
    return _point;
  }

  /**
   * Indicates if the managed point is completed or not.
   * @return <code>true</code> if it is, <code>false</code> otherwise.
   */
  public boolean isCompleted()
  {
    return _completed;
  }

  /**
   * Set the 'completed' state of the managed point.
   * @param completed State to set.
   */
  public void setCompleted(boolean completed)
  {
    _completed=completed;
  }
}
