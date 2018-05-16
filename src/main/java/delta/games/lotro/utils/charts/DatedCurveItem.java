package delta.games.lotro.utils.charts;

import java.util.Date;

/**
 * Item in a dated curve.
 * @author DAM
 * @param <VALUE> Type of managed values.
 */
public class DatedCurveItem<VALUE>
{
  private Long _date;
  private VALUE _value;

  /**
   * Constructor.
   * @param date Date.
   * @param value Value.
   */
  public DatedCurveItem(long date, VALUE value)
  {
    _date=Long.valueOf(date);
    _value=value;
  }

  /**
   * Get the date of this item.
   * @return a date as a Long.
   */
  public Long getDate()
  {
    return _date;
  }

  /**
   * Get the value of this date.
   * @return a value.
   */
  public VALUE getValue()
  {
    return _value;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append(_date);
    sb.append(" (");
    sb.append(new Date(_date.longValue()));
    sb.append(") => ");
    sb.append(_value);
    return sb.toString();
  }
}
