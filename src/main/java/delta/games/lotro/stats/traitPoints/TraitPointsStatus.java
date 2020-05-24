package delta.games.lotro.stats.traitPoints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.common.progression.ProgressionsManager;
import delta.games.lotro.utils.maths.Progression;

/**
 * Maintains the status of trait points acquisition for a single character.
 * @author DAM
 */
public class TraitPointsStatus
{
  private Set<String> _acquiredTraitPointsIds;

  /**
   * Constructor.
   */
  public TraitPointsStatus()
  {
    _acquiredTraitPointsIds=new HashSet<String>();
  }

  /**
   * Get an ordered list of all acquired trait points.
   * @return A list of trait point identifiers.
   */
  public List<String> getAcquiredTraitPointsIds()
  {
    List<String> ret=new ArrayList<String>(_acquiredTraitPointsIds);
    Collections.sort(ret);
    return ret;
  }

  /**
   * Indicates if the trait point identified by <code>id</code> has been acquired or not.
   * @param id Identifier of the targeted trait point.
   * @return <code>true</code> if acquired, <code>false</code> otherwise.
   */
  public boolean isAcquired(String id)
  {
    return _acquiredTraitPointsIds.contains(id);
  }

  /**
   * Get the number acquired trait points.
   * @param characterLevel Level of the character.
   * @return A points count.
   */
  public int getPointsCount(int characterLevel) 
  {
    int pointsFromLevel=getTraitPointsFromLevel(characterLevel);
    int acquiredPoints=_acquiredTraitPointsIds.size();
    int total=pointsFromLevel+acquiredPoints;
    return total;
  }

  /**
   * Get the number of trait points for a character level.
   * @param characterLevel Level to use.
   * @return A points count.
   */
  public static int getTraitPointsFromLevel(int characterLevel)
  {
    // TODO: Avoid hard coded progression ID
    Progression progression=ProgressionsManager.getInstance().getProgression(1879271247);
    Float value=progression.getValue(characterLevel);
    return value!=null?value.intValue():0;
  }

  /**
   * Set the status of a single trait point.
   * @param id Identifier of the trait point.
   * @param acquired Status to set.
   */
  public void setStatus(String id, boolean acquired)
  {
    if (acquired)
    {
      _acquiredTraitPointsIds.add(id);
    }
    else
    {
      _acquiredTraitPointsIds.remove(id);
    }
  }

  @Override
  public String toString()
  {
    return _acquiredTraitPointsIds.toString();
  }
}
