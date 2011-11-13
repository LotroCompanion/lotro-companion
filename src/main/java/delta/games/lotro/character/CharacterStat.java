package delta.games.lotro.character;

/**
 * Represents a LOTRO character stat value.
 * @author DAM
 */
public class CharacterStat
{
  /**
   * A LOTRO character stat.
   * @author DAM
   */
  public enum STAT
  {
    /**
     * Morale.
     */
    MORALE("Morale"),
    /**
     * Power.
     */
    POWER("Power"),
    /**
     * Armour.
     */
    ARMOUR("Armour"),
    /**
     * Might.
     */
    MIGHT("Might"),
    /**
     * Agility.
     */
    AGILITY("Agility"),
    /**
     * Vitality.
     */
    VITALITY("Vitality"),
    /**
     * Will.
     */
    WILL("Will"),
    /**
     * Fate.
     */
    FATE("Fate"),
    /**
     * Critical hit.
     */
    CRITICAL_HIT("Critical hit"),
    /**
     * Finesse.
     */
    FINESSE("Finesse"),
    /**
     * Block.
     */
    BLOCK("Block"),
    /**
     * Parry.
     */
    PARRY("Parry"),
    /**
     * Evade.
     */
    EVADE("Evade"),
    /**
     * Resistance.
     */
    RESISTANCE("Resistance"),
    /**
     * Critical avoidance.
     */
    CRITICAL_AVOID("Critical avoidance"),
    /**
     * Physical mitigation.
     */
    PHYSICAL_MITIGATION("Physical mitigation"),
    /**
     * Tactical mitigation.
     */
    TACTICAL_MITIGATION("Tactical mitigation");
    
    private String _name;

    private STAT(String name)
    {
      _name=name;
    }
    
    /**
     * Get the name of this stat.
     * @return a stat name.
     */
    public String getName()
    {
      return _name;
    }
    
  }
  
  private STAT _stat;
  private Integer _value;

  /**
   * Constructor.
   * @param stat Associated stat.
   */
  public CharacterStat(STAT stat)
  {
    _stat=stat;
  }

  /**
   * Get the associated stat.
   * @return the associated stat.
   */
  public STAT getStat()
  {
    return _stat;
  }
  
  /**
   * Get the value of this stat.
   * @return A stat value or <code>null</code> if undefined.
   */
  public Integer getValue()
  {
    return _value;
  }

  /**
   * Set the value of this stat.
   * @param value Value to set (<code>null</code> means undefined).
   */
  public void setValue(Integer value)
  {
    _value=value;
  }

  @Override
  public String toString()
  {
    return _stat+": "+((_value!=null)?_value:"N/A");
  }
}
