package delta.games.lotro.region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import delta.common.utils.text.EndOfLine;

/**
 * Region.
 * A region is made of areas.
 * @author DAM
 */
public class Region
{
  private String _identifier;
  private String _name;
  private HashMap<String,Area> _byIdentifier;
  private List<Area> _areas;

  /**
   * Constructor.
   * @param identifier Internal identifier.
   * @param name Region's name.
   */
  public Region(String identifier, String name)
  {
    _identifier=identifier;
    _name=name;
    _areas=new ArrayList<Area>();
    _byIdentifier=new HashMap<String,Area>();
  }
  
  /**
   * Get the internal identifier.
   * @return the internal identifier.
   */
  public String getIdentifier()
  {
    return _identifier;
  }

  /**
   * Get the region's name.
   * @return the region's name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Add an area in this region.
   * @param area Area to add.
   */
  public void addArea(Area area)
  {
    if (area!=null)
    {
      String identifier=area.getIdentifier();
      if (_byIdentifier.get(identifier)==null)
      {
        _areas.add(area);
        _byIdentifier.put(identifier,area);
      }
    }
  }

  /**
   * Get all the areas of this region.
   * @return an array of areas.
   */
  public Area[] getAreas()
  {
    Area[] ret=new Area[_areas.size()];
    return _areas.toArray(ret);
  }

  /**
   * Dump the contents of this region as a string.
   * @return A readable string.
   */
  public String dump()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Region: ").append(_name);
    sb.append(" (").append(_identifier).append(')');
    sb.append(EndOfLine.NATIVE_EOL);
    for(Area area : _areas)
    {
      String areaName=area.getName();
      String areaIdentifier=area.getIdentifier();
      sb.append("\tArea: ").append(areaName);
      sb.append(" (").append(areaIdentifier).append(')');
      sb.append(EndOfLine.NATIVE_EOL);
    }
    return sb.toString();
  }

  @Override
  public String toString()
  {
    return _name;
  }
}
