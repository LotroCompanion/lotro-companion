package delta.games.lotro.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Formats.
 * @author DAM
 */
public class Formats
{
  /**
   * Date pattern.
   */
  public static String DATE_PATTERN="dd/MM/yyyy";

  private static SimpleDateFormat _dateFormatter=new SimpleDateFormat(DATE_PATTERN);

  /**
   * Get the dates formatter.
   * @return a dates formatter.
   */
  public static SimpleDateFormat getDateFormatter()
  {
    if (_dateFormatter==null)
    {
      _dateFormatter=new SimpleDateFormat(DATE_PATTERN);
      _dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    return _dateFormatter;
  }

  /**
   * Format a date.
   * @param date Date to format.
   * @return A string or <code>null</code> if <code>date</code> is <code>null</code>.
   */
  public static String getDateString(Date date)
  {
    String ret=null;
    if (date!=null)
    {
      ret=_dateFormatter.format(date);
    }
    return ret;
  }
  /**
   * Format a date.
   * @param date Date to format.
   * @return A string or <code>null</code> if <code>date</code> is <code>null</code>.
   */
  public static String getDateString(Long date)
  {
    String ret=null;
    if (date!=null)
    {
      ret=getDateString(new Date(date.longValue()));
    }
    return ret;
  }
}
