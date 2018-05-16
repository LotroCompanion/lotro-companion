package delta.games.lotro.utils.charts;

import java.util.Comparator;

/**
 * Comparator for dated curve items, using their date.
 * @author DAM
 */
public class DatedCurveItemDateComparator implements Comparator<DatedCurveItem<?>>
{
  @Override
  public int compare(DatedCurveItem<?> o1, DatedCurveItem<?> o2)
  {
    Long date1=o1.getDate();
    Long date2=o2.getDate();
    return date1.compareTo(date2);
  }
}
