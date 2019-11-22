package delta.games.lotro.stats.traitPoints;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Test class for trait points status.
 * @author DAM
 */
public class TestTraitPointsStatus extends TestCase
{
  /**
   * Test the computation of the number of trait points given from the level.
   */
  public void testTraitPointsFromLevel()
  {
    int[] levels={6, 7, 8, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 120, 124, 125, 129, 130};
    int[] expected={0, 1, 1, 49, 50, 50, 50, 51, 51, 51, 52, 52, 52, 53, 53, 54, 54, 55, 55, 56};
    Assert.assertEquals(levels.length,expected.length);
    int nbItems=Math.min(levels.length,expected.length);
    for(int i=0;i<nbItems;i++)
    {
      int nbPoints=TraitPointsStatus.getTraitPointsFromLevel(levels[i]);
      Assert.assertEquals(expected[i],nbPoints);
    }
  }
}
