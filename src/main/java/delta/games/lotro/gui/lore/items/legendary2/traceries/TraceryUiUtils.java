package delta.games.lotro.gui.lore.items.legendary2.traceries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.lore.items.legendary2.Tracery;

/**
 * Utility methods for traceries-related UIs.
 * @author DAM
 */
public class TraceryUiUtils
{
  /**
   * Get the tiers used by the given traceries.
   * @param traceries List of traceries to use.
   * @return A list of tiers.
   */
  public static List<Integer> getTiers(List<Tracery> traceries)
  {
    Set<Integer> values=new HashSet<Integer>();
    for(Tracery tracery : traceries)
    {
      Integer tier=tracery.getTier();
      if (tier!=null)
      {
        values.add(tier);
      }
    }
    List<Integer> ret=new ArrayList<Integer>(values);
    Collections.sort(ret);
    return ret;
  }
}
