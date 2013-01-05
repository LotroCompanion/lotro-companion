package delta.games.lotro.lore.warbands;

import delta.games.lotro.common.SIZE;

/**
 * Filter for warband definitions.
 * @author DAM
 */
public class WarbandFilter
{
  private String _region;
  private SIZE _size;
  private Integer _minLevel;

  /**
   * Constructor.
   */
  public WarbandFilter()
  {
    _region=null;
    _size=null;
    _minLevel=null;
  }

  /**
   * Get the region of selected items.
   * @return A region or <code>null</code> for no filter.
   */
  public String getRegion()
  {
    return _region;
  }
  
  /**
   * Set the region of selected items.
   * @param region A region or <code>null</code>.
   */
  public void setRegion(String region)
  {
    _region=region;
  }

  /**
   * Get the size filter.
   * @return A size filter or <code>null</code> for no filter.
   */
  public SIZE getSize()
  {
    return _size;
  }

  /**
   * Set the size of selected items.
   * @param size A size or <code>null</code>.
   */
  public void setSize(SIZE size)
  {
    _size=size;
  }

  /**
   * Get the minimum level of selected items.
   * @return A level or <code>null</code> for no filter.
   */
  public Integer getMinLevel()
  {
    return _minLevel;
  }
  
  /**
   * Set the minimum level of selected items.
   * @param minLevel A minimum level or <code>null</code>.
   */
  public void setMinLevel(Integer minLevel)
  {
    _minLevel=minLevel;
  }

  /**
   * Filter a log item.
   * @param warband Item to test.
   * @return <code>true</code> if it passes the filter, <code>false</code> otherwise.
   */
  public boolean filterItem(WarbandDefinition warband)
  {
    boolean ret=true;
    if (_region!=null)
    {
      String region=warband.getRegion();
      ret=_region.equals(region);
      if (!ret) return false;
    }
    if (_minLevel!=null)
    {
      Integer level=warband.getLevel();
      ret=(level==null)||(level.intValue()>=_minLevel.intValue());
      if (!ret) return false;
    }
    if (_size!=null)
    {
      SIZE size=warband.getSize();
      ret=size==_size;
      if (!ret) return false;
    }
    return ret;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Warband filter: ");
    if (_region!=null)
    {
      sb.append(" Region: ").append(_region);
    }
    if (_size!=null)
    {
      sb.append(" Size: ").append(_size);
    }
    if (_minLevel!=null)
    {
      sb.append(" Minimum level: ").append(_minLevel);
    }
    return sb.toString();
  }
}
