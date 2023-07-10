package delta.games.lotro.gui.lore.items.legendary2.traceries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatProvider;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.lore.items.Item;
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

  /**
   * Get the stats used by the given traceries.
   * @param traceries List of traceries to use.
   * @return A list of stats.
   */
  public static List<StatDescription> getStats(List<Tracery> traceries)
  {
    Set<StatDescription> values=new HashSet<StatDescription>();
    for(Tracery tracery : traceries)
    {
      Item item=tracery.getItem();
      StatsProvider statsProvider=item.getStatsProvider();
      int nbProviders=statsProvider.getNumberOfStatProviders();
      for(int i=0;i<nbProviders;i++)
      {
        StatProvider statProvider=statsProvider.getStatProvider(i);
        StatDescription statDescription=statProvider.getStat();
        values.add(statDescription);
      }
    }
    List<StatDescription> ret=new ArrayList<StatDescription>(values);
    Collections.sort(ret,new NamedComparator());
    return ret;
  }
}
