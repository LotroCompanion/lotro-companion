package delta.games.lotro.stats.achievables.statistics;

import java.util.Date;

import delta.games.lotro.lore.quests.Achievable;

/**
 * Trait event.
 * @author DAM
 */
public class TraitEvent
{
  private Long _date;
  private String _trait;
  private Achievable _achievable;

  /**
   * Constructor.
   * @param trait Targeted trait.
   * @param date Date of acquisition.
   * @param achievable Associated achievable.
   */
  public TraitEvent(String trait, Long date, Achievable achievable)
  {
    _trait=trait;
    _date=date;
    _achievable=achievable;
  }

  /**
   * Get the acquisition date for the managed trait.
   * @return A timestamp or <code>null</code>.
   */
  public Long getDate()
  {
    return _date;
  }

  /**
   * Get the targeted trait.
   * @return A trait.
   */
  public String getTrait()
  {
    return _trait;
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
    sb.append(_trait);
    if (_date!=null)
    {
      sb.append(" (");
      sb.append(new Date(_date.longValue()));
      sb.append(')');
    }
    sb.append(" from ");
    sb.append(_achievable.getName());
    return _trait;
  }
}
