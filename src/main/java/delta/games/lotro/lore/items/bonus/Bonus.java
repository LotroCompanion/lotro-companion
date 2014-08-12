package delta.games.lotro.lore.items.bonus;

import delta.games.lotro.lore.items.bonus.BonusType.VALUE_CLASS;

/**
 * A single bonus on an item or an items set.
 * @author DAM
 */
public class Bonus
{
  /**
   * Bonus occurrence.
   * @author DAM
   */
  public enum BONUS_OCCURRENCE
  {
    /**
     * Always.
     */
    ALWAYS,
    /**
     * On use.
     */
    ON_USE
  }
  private BonusType _type;
  private Object _value;
  private BONUS_OCCURRENCE _occurrence; 
  private String _durationStr;

  /**
   * Constructor.
   * @param type Bonus type.
   * @param occurrence Bonus occurrence.
   */
  public Bonus(BonusType type, BONUS_OCCURRENCE occurrence)
  {
    _type=type;
    _occurrence=occurrence;
  }

  /**
   * Get the bonus type.
   * @return the bonus type.
   */
  public BonusType getBonusType()
  {
    return _type;
  }
  
  /**
   * Get the bonus value.
   * @return A value or <code>null</code>.
   * The actual value type depends on the bonus type.
   */
  public Object getValue()
  {
    return _value;
  }

  /**
   * Get the bonus occurrence.
   * @return the bonus occurrence.
   */
  public BONUS_OCCURRENCE getBonusOccurrence()
  {
    return _occurrence;
  }

  /**
   * Get the bonus duration as a string.
   * @return a duration string.
   */
  public String getDuration()
  {
    return _durationStr;
  }

  /**
   * Set the bonus value.
   * @param value Value to set.
   */
  public void setValue(Object value)
  {
    _value=value;
  }
  
  /**
   * Set the duration string.
   * @param durationStr Duration to set.
   */
  public void setDuration(String durationStr)
  {
    _durationStr=durationStr;
  }
  
  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append(_type.getLabel());
    sb.append(": ");
    sb.append(_value);
    if (_type.getValueClass()==VALUE_CLASS.PERCENTAGE) sb.append('%');
    if (_occurrence!=BONUS_OCCURRENCE.ALWAYS)
    {
      sb.append(", ");
      sb.append(_occurrence);
      if (_durationStr!=null)
      {
        sb.append(", ");
        sb.append(_durationStr);
      }
    }
    return sb.toString();
  }
}
