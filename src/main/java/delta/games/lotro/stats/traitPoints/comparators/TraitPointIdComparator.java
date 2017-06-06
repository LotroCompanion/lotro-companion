package delta.games.lotro.stats.traitPoints.comparators;

import java.util.Comparator;

import delta.games.lotro.stats.traitPoints.TraitPoint;

/**
 * Comparator for trait points, using their label.
 * @author DAM
 */
public class TraitPointIdComparator implements Comparator<TraitPoint>
{
  public int compare(TraitPoint tp1, TraitPoint tp2)
  {
    String label1=tp1.getId();
    String label2=tp2.getId();
    if (label1!=null)
    {
      if (label2!=null)
      {
        return label1.compareTo(label2);
      }
      return 1;
    }
    return (label2!=null)?-1:0;
  }
}
