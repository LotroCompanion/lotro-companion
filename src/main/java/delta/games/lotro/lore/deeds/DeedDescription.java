package delta.games.lotro.lore.deeds;

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.common.Rewards;

/**
 * LOTRO deed description.
 * @author DAM
 */
public class DeedDescription
{
  /**
   * Quest type.
   * @author DAM
   */
  public enum TYPE
  {
    /**
     * Class deed.
     */
    CLASS,
    /**
     * Exploration deed.
     */
    EXPLORER,
    /**
     * Lore-related deed.
     */
    LORE,
    /**
     * Racial deed.
     */
    RACE,
    /**
     * Reputation deed.
     */
    REPUTATION,
    /**
     * Slayer deed.
     */
    SLAYER,
    /**
     * Event deed.
     */
    EVENT
  }

  private int _identifier;
  private String _key;
  private String _name;
  private TYPE _type;
  private String _class;
  private Integer _minLevel;
  private String _description;
  private String _objectives;
  private Rewards _rewards;

  /**
   * Constructor.
   */
  public DeedDescription()
  {
    _identifier=0;
    _key=null;
    _name="";
    _type=TYPE.SLAYER;
    _class=null;
    _minLevel=null;
    _description="";
    _objectives="";
    _rewards=new Rewards();
  }

  /**
   * Get the identifier of this deed.
   * @return the identifier of this deed.
   */
  public int getIdentifier()
  {
    return _identifier;
  }

  /**
   * Set the identifier of this deed.
   * @param identifier the identifier to set.
   */
  public void setIdentifier(int identifier)
  {
    _identifier=identifier;
  }

  /**
   * Get the key of this deed.
   * @return the key of this deed.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Set the key of this deed.
   * @param key the key to set.
   */
  public void setKey(String key)
  {
    _key=key;
  }

  /**
   * Get the name of this deed.
   * @return the name of this deed.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Set the name of this deed.
   * @param name the name to set.
   */
  public void setName(String name)
  {
    _name=name;
  }

  /**
   * Get the type of this deed.
   * @return the type of this deed.
   */
  public TYPE getType()
  {
    return _type;
  }

  /**
   * Set the type of this deed. 
   * @param type the type to set.
   */
  public void setType(TYPE type)
  {
    _type=type;
  }

  /**
   * Get the class name of this deed.
   * @return the class name of this deed.
   */
  public String getClassName()
  {
    return _class;
  }

  /**
   * Set the class name of this deed.
   * @param className the class name to set.
   */
  public void setClassName(String className)
  {
    _class=className;
  }

  /**
   * Get the minimum level of this deed.
   * @return the minimum level of this deed.
   */
  public Integer getMinLevel()
  {
    return _minLevel;
  }

  /**
   * Set the minimum level of this deed. 
   * @param minLevel the minimum level to set.
   */
  public void setMinLevel(Integer minLevel)
  {
    _minLevel=minLevel;
  }

  /**
   * Get the description of this deed.
   * @return the description of this deed.
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * Set the description of this deed.
   * @param description the description to set.
   */
  public void setDescription(String description)
  {
    _description=description;
  }

  /**
   * Get the objectives of this deed.
   * @return the objectives of this deed.
   */
  public String getObjectives()
  {
    return _objectives;
  }

  /**
   * Set the objectives of this deed.
   * @param objectives the objectives to set.
   */
  public void setObjectives(String objectives)
  {
    _objectives=objectives;
  }

  /**
   * Get the rewards for this deed.
   * @return the rewards.
   */
  public Rewards getRewards()
  {
    return _rewards;
  }

  /**
   * Dump the contents of this deed as a string.
   * @return A readable string.
   */
  public String dump()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Name: ").append(_name);
    if (_identifier!=0)
    {
      sb.append(" (");
      sb.append(_identifier);
      sb.append(')');
    }
    if (_key!=null)
    {
      sb.append(" (");
      sb.append(_key);
      sb.append(')');
    }
    if (_type!=null)
    {
      sb.append(" (");
      sb.append(_type);
      if (_class!=null)
      {
        sb.append(" ");
        sb.append(_class);
      }
      sb.append(')');
    }
    if (_minLevel!=null)
    {
      sb.append(" level=");
      sb.append(_minLevel);
    }
    if ((_description!=null) && (_description.length()>0))
    {
      sb.append(EndOfLine.NATIVE_EOL);
      sb.append("Description: ").append(_description);
    }
    if (_objectives.length()>0)
    {
      sb.append(EndOfLine.NATIVE_EOL);
      sb.append("Objectives: ").append(_objectives);
    }
    sb.append(EndOfLine.NATIVE_EOL);
    sb.append("Rewards: ").append(_rewards);
    return sb.toString();
  }

  @Override
  public String toString()
  {
    return _name;
  }
}
