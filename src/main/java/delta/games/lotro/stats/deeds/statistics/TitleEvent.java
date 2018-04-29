package delta.games.lotro.stats.deeds.statistics;

import java.util.Date;

import delta.games.lotro.lore.deeds.DeedDescription;

/**
 * Title event.
 * @author DAM
 */
public class TitleEvent
{
  private Long _date;
  private String _title;
  private DeedDescription _deed;

  /**
   * Constructor.
   * @param title Targeted title.
   * @param date Date of acquisition.
   * @param deed Associated deed.
   */
  public TitleEvent(String title, Long date, DeedDescription deed)
  {
    _title=title;
    _date=date;
    _deed=deed;
  }

  /**
   * Get the acquisition date for the managed title.
   * @return A timestamp or <code>null</code>.
   */
  public Long getDate()
  {
    return _date;
  }

  /**
   * Get the targeted title.
   * @return A title.
   */
  public String getTitle()
  {
    return _title;
  }

  /**
   * Get the source deed.
   * @return a deed.
   */
  public DeedDescription getDeed()
  {
    return _deed;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append(_title);
    if (_date!=null)
    {
      sb.append(" (");
      sb.append(new Date(_date.longValue()));
      sb.append(')');
    }
    sb.append(" from ");
    sb.append(_deed.getName());
    return _title;
  }
}
