package delta.games.lotro.stats.deeds;

import java.util.Comparator;

/**
 * Comparator for deed statuses, using their date.
 * @author DAM
 */
public class DeedStatusDateComparator implements Comparator<DeedStatus>
{
  @Override
  public int compare(DeedStatus o1, DeedStatus o2)
  {
    Long date1=o1.getCompletionDate();
    Long date2=o2.getCompletionDate();
    if (date1==null)
    {
      return (date2!=null)?-1:0;
    }
    if (date2==null)
    {
      return 1;
    }
    return date1.compareTo(date2);
  }
}
