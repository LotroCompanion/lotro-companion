package delta.games.lotro.lore.warbands;

import delta.games.lotro.common.SIZE;

/**
 * Definition of a warband.
 * @author DAM
 */
public class WarbandDefinition
{
  private String _name;
  private Integer _level;
  private Integer _morale;
  private String _region;
  private String _description;
  private SIZE _size;
  
  /**
   * Constructor.
   */
  public WarbandDefinition()
  {
    _name="";
    _level=null;
    _morale=null;
    _region="";
    _description="";
    _size=SIZE.SOLO;
  }

  /**
   * Get the warband name.
   * @return a name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Set the warband name.
   * @param name the name to set.
   */
  public void setName(String name)
  {
    _name=name;
  }

  /**
   * Get the warband level.
   * @return A level or <code>null</code> if not known.
   */
  public Integer getLevel()
  {
    return _level;
  }

  /**
   * Set the level of this warband.
   * @param level the level to set.
   */
  public void setLevel(Integer level)
  {
    _level=level;
  }

  /**
   * Get the warband morale.
   * @return A morale value or <code>null</code> if not known.
   */
  public Integer getMorale()
  {
    return _morale;
  }

  /**
   * Set the morale of this warband.
   * @param morale the morale to set.
   */
  public void setMorale(Integer morale)
  {
    _morale=morale;
  }

  /**
   * Get the region of this warband.
   * @return A region.
   */
  public String getRegion()
  {
    return _region;
  }

  /**
   * Set the region of this warband.
   * @param region the region to set.
   */
  public void setRegion(String region)
  {
    _region=region;
  }

  /**
   * Get the description of this warband.
   * @return A description.
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * Set the description of this warband.
   * @param description the description to set.
   */
  public void setDescription(String description)
  {
    _description=description;
  }

  /**
   * Get the size of this warband.
   * @return A size.
   */
  public SIZE getSize()
  {
    return _size;
  }

  /**
   * Set the size of this warband.
   * @param size the size to set.
   */
  public void setSize(SIZE size)
  {
    _size=size;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder(_name);
    if (_level!=null) {
      sb.append(" (level: ").append(_level.intValue()).append(')');
    }
    if (_morale!=null) {
      sb.append(" (morale: ").append(_morale.intValue()).append(')');
    }
    if (_region!=null) {
      sb.append(" (region: ").append(_region).append(')');
    }
    if (_size!=null) {
      sb.append(" (_size: ").append(_size).append(')');
    }
    if (_description!=null) {
      sb.append(": ").append(_description);
    }
    return sb.toString();
  }
}
