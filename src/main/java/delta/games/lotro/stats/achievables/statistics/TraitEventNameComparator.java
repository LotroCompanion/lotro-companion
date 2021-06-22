package delta.games.lotro.stats.achievables.statistics;

import java.util.Comparator;

/**
 * Comparator for TraitEvent that uses the trait name.
 * @author DAM
 */
public class TraitEventNameComparator implements Comparator<TraitEvent>
{
  @Override
  public int compare(TraitEvent o1, TraitEvent o2)
  {
    String trait1=o1.getTrait();
    String trait2=o2.getTrait();
    return trait1.compareTo(trait2);
  }
}
