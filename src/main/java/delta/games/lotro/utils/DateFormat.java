package delta.games.lotro.utils;

import java.util.Date;

import delta.common.ui.swing.text.dates.DateCodec;

/**
 * Date format utils.
 * @author DAM
 */
public class DateFormat
{
  private static DateCodec _codec=new DateCodec()
  {
    public Long parseDate(String dateStr)
    {
      Date date=Formats.parseDate(dateStr);
      return (date!=null)?Long.valueOf(date.getTime()):null;
    }

    public String formatDate(Long date)
    {
      if (date!=null)
      {
        return Formats.getDateTimeString(new Date(date.longValue()));
      }
      return "";
    }
  };

  /**
   * Get the date/time codec.
   * @return the date/time codec.
   */
  public static DateCodec getDateTimeCodec()
  {
    return _codec;
  }
}
