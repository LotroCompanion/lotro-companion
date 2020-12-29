package delta.games.lotro.gui.stats.deeds.map;

import delta.games.lotro.lore.quests.geo.AchievableGeoPoint;

/**
 * Status of a single geographic point in an achievable.
 * @author DAM
 */
public class AchievableStatusGeoItem
{
  private AchievableGeoPoint _point;
  private String _label;
  private boolean _completed;

  /**
   * Constructor.
   * @param point Point to use.
   * @param label Label to use.
   */
  public AchievableStatusGeoItem(AchievableGeoPoint point, String label)
  {
    _point=point;
    _label=label;
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
   * Get the associated (condition) label.
   * @return the associated label.
   */
  public String getLabel()
  {
    return _label;
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
