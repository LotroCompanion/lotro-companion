package delta.games.lotro.stats.deeds.statistics;

import java.util.Date;

import delta.games.lotro.lore.quests.Achievable;

/**
 * Title event.
 * @author DAM
 */
public class TitleEvent
{
  private Long _date;
  private String _title;
  private Achievable _achievable;

  /**
   * Constructor.
   * @param title Targeted title.
   * @param date Date of acquisition.
   * @param achievable Associated achievable.
   */
  public TitleEvent(String title, Long date, Achievable achievable)
  {
    _title=title;
    _date=date;
    _achievable=achievable;
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
   * Get the source achievable.
   * @return an achievable.
   */
  public Achievable getAchievable()
  {
    return _achievable;
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
    sb.append(_achievable.getName());
    return _title;
  }
}
