package delta.games.lotro.stats.deeds.statistics;

import java.util.Date;

import delta.games.lotro.lore.deeds.DeedDescription;

/**
 * Trait event.
 * @author DAM
 */
public class TraitEvent
{
  private Long _date;
  private String _trait;
  private DeedDescription _deed;

  /**
   * Constructor.
   * @param trait Targeted trait.
   * @param date Date of acquisition.
   * @param deed Associated deed.
   */
  public TraitEvent(String trait, Long date, DeedDescription deed)
  {
    _trait=trait;
    _date=date;
    _deed=deed;
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
    sb.append(_trait);
    if (_date!=null)
    {
      sb.append(" (");
      sb.append(new Date(_date.longValue()));
      sb.append(')');
    }
    sb.append(" from ");
    sb.append(_deed.getName());
    return _trait;
  }
}
