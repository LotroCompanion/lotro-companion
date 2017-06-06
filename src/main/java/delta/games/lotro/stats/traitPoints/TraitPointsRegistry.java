package delta.games.lotro.stats.traitPoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.common.CharacterClass;

/**
 * Registry for trait points.
 * @author DAM
 */
public class TraitPointsRegistry
{
  private static final Logger LOGGER=Logger.getLogger(TraitPointsRegistry.class);

  private HashMap<String,TraitPoint> _traitPoints;

  /**
   * Constructor.
   */
  public TraitPointsRegistry()
  {
    _traitPoints=new HashMap<String,TraitPoint>();
  }

  /**
   * Register a new trait point.
   * @param point Point to register.
   */
  public void registerTraitPoint(TraitPoint point)
  {
    String id=point.getId();
    TraitPoint old=_traitPoints.put(id,point);
    if (old!=null)
    {
      LOGGER.warn("Duplicate trait point ID: "+old);
    }
  }

  /**
   * Get all registered trait points.
   * @return A list of trait point (unspecified order).
   */
  public List<TraitPoint> getAll()
  {
    return new ArrayList<TraitPoint>(_traitPoints.values());
  }

  /**
   * Get all trait points for a given class.
   * @param requiredClass Character class to use.
   * @return A list of trait point (unspecified order).
   */
  public List<TraitPoint> getPointsForClass(CharacterClass requiredClass)
  {
    List<TraitPoint> points=new ArrayList<TraitPoint>();
    for(TraitPoint point : _traitPoints.values())
    {
      CharacterClass traitRequiredClass=point.getRequiredClass();
      if ((traitRequiredClass==null) || (traitRequiredClass==requiredClass))
      {
        points.add(point);
      }
    }
    return points;
  }
}
