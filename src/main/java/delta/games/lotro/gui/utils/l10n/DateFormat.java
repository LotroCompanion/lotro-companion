package delta.games.lotro.gui.utils.l10n;

import java.util.Date;

import delta.common.ui.swing.text.dates.DateCodec;
import delta.games.lotro.utils.Formats;
import delta.games.lotro.utils.l10n.LocalizedFormats;
import delta.games.lotro.utils.l10n.dates.DateFormatSpecification;

/**
 * Date format utils.
 * @author DAM
 */
public class DateFormat
{
  private static final DateCodec DATE_TIME_CODEC=new DateCodec()
  {
    @Override
    public Long parseDate(String dateStr)
    {
      return parseDateTime(dateStr,true);
    }

    @Override
    public String formatDate(Long date)
    {
      return DateFormat.formatDateTime(date);
    }
  };

  /**
   * Get the date/time codec.
   * @return the date/time codec.
   */
  public static DateCodec getDateTimeCodec()
  {
    return DATE_TIME_CODEC;
  }

  private static String formatDateTime(Long date)
  {
    if (date!=null)
    {
      return Formats.getDateTimeString(new Date(date.longValue()));
    }
    return "";
  }

  private static Long parseDateTime(String dateStr, boolean strict)
  {
    if (dateStr==null)
    {
      return null;
    }
    if (strict)
    {
      DateFormatSpecification spec=LocalizedFormats.getDateTimeFormatSpecification();
      int minLength=spec.getMinLength();
      int maxLength=spec.getMaxLength();
      int length=dateStr.length();
      if ((length<minLength) || (length>maxLength))
      {
        return null;
      }
    }
    Date date=Formats.parseDate(dateStr);
    return (date!=null)?Long.valueOf(date.getTime()):null;
  }

  private static final DateCodec DATE_CODEC=new DateCodec()
  {
    @Override
    public Long parseDate(String dateStr)
    {
      return parseDateString(dateStr,true);
    }

    @Override
    public String formatDate(Long date)
    {
      return DateFormat.formatDate(date);
    }
  };

  /**
   * Get the date codec.
   * @return the date codec.
   */
  public static DateCodec getDateCodec()
  {
    return DATE_CODEC;
  }

  private static String formatDate(Long date)
  {
    if (date!=null)
    {
      return Formats.getDateString(new Date(date.longValue()));
    }
    return "";
  }

  private static Long parseDateString(String dateStr, boolean strict)
  {
    if (dateStr==null)
    {
      return null;
    }
    if (strict)
    {
      DateFormatSpecification spec=LocalizedFormats.getDateFormatSpecification();
      int minLength=spec.getMinLength();
      int maxLength=spec.getMaxLength();
      int length=dateStr.length();
      if ((length<minLength) || (length>maxLength))
      {
        return null;
      }
    }
    Date date=Formats.parseDate(dateStr);
    return (date!=null)?Long.valueOf(date.getTime()):null;
  }
}
