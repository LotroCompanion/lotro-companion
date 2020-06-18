package delta.games.lotro.stats.traitPoints;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.common.CharacterClass;

/**
 * Registry for trait points.
 * @author DAM
 */
public class TraitPointsRegistry
{
  private List<TraitPoint> _traitPoints;

  /**
   * Constructor.
   */
  public TraitPointsRegistry()
  {
    _traitPoints=new ArrayList<TraitPoint>();
  }

  /**
   * Register a new trait point.
   * @param point Point to register.
   */
  public void registerTraitPoint(TraitPoint point)
  {
    _traitPoints.add(point);
  }

  /**
   * Get all registered trait points.
   * @return A list of trait points.
   */
  public List<TraitPoint> getAll()
  {
    return new ArrayList<TraitPoint>(_traitPoints);
  }

  /**
   * Get all trait points for a given class.
   * @param requiredClass Character class to use.
   * @return A list of trait point (unspecified order).
   */
  public List<TraitPoint> getPointsForClass(CharacterClass requiredClass)
  {
    List<TraitPoint> points=new ArrayList<TraitPoint>();
    for(TraitPoint point : _traitPoints)
    {
      boolean enabled=point.isEnabledForClass(requiredClass);
      if (enabled)
      {
        points.add(point);
      }
    }
    return points;
  }
}
