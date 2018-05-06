package delta.games.lotro.stats.deeds.statistics;

import java.util.Date;

import delta.games.lotro.lore.deeds.DeedDescription;

/**
 * Emote event.
 * @author DAM
 */
public class EmoteEvent
{
  private Long _date;
  private String _emote;
  private DeedDescription _deed;

  /**
   * Constructor.
   * @param emote Targeted emote.
   * @param date Date of acquisition.
   * @param deed Associated deed.
   */
  public EmoteEvent(String emote, Long date, DeedDescription deed)
  {
    _emote=emote;
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
   * Get the targeted emote.
   * @return An emote.
   */
  public String getEmote()
  {
    return _emote;
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
    sb.append(_emote);
    if (_date!=null)
    {
      sb.append(" (");
      sb.append(new Date(_date.longValue()));
      sb.append(')');
    }
    sb.append(" from ");
    sb.append(_deed.getName());
    return _emote;
  }
}
