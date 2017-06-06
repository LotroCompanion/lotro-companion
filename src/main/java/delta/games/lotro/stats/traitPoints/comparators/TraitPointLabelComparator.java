package delta.games.lotro.stats.traitPoints.comparators;

import java.util.Comparator;

import delta.games.lotro.stats.traitPoints.TraitPoint;

/**
 * Comparator for trait points, using their label.
 * @author DAM
 */
public class TraitPointLabelComparator implements Comparator<TraitPoint>
{
  public int compare(TraitPoint tp1, TraitPoint tp2)
  {
    String label1=tp1.getLabel();
    String label2=tp2.getLabel();
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
