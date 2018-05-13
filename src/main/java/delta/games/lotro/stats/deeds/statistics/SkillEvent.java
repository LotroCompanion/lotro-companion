package delta.games.lotro.stats.deeds.statistics;

import java.util.Date;

import delta.games.lotro.lore.deeds.DeedDescription;

/**
 * Skill event.
 * @author DAM
 */
public class SkillEvent
{
  private Long _date;
  private String _skill;
  private DeedDescription _deed;

  /**
   * Constructor.
   * @param skill Targeted skill.
   * @param date Date of acquisition.
   * @param deed Associated deed.
   */
  public SkillEvent(String skill, Long date, DeedDescription deed)
  {
    _skill=skill;
    _date=date;
    _deed=deed;
  }

  /**
   * Get the acquisition date for the managed skill.
   * @return A timestamp or <code>null</code>.
   */
  public Long getDate()
  {
    return _date;
  }

  /**
   * Get the targeted skill.
   * @return A skill.
   */
  public String getSkill()
  {
    return _skill;
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
    sb.append(_skill);
    if (_date!=null)
    {
      sb.append(" (");
      sb.append(new Date(_date.longValue()));
      sb.append(')');
    }
    sb.append(" from ");
    sb.append(_deed.getName());
    return _skill;
  }
}
