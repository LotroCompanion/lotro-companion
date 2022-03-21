package delta.games.lotro.gui.character.status.achievables.aggregator;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.status.achievables.edition.AchievableStatusGeoItem;
import delta.games.lotro.common.Named;
import delta.games.lotro.gui.maps.MapUtils;
import delta.games.lotro.lore.maps.MapDescription;

/**
 * A map that contains an aggregation of achievable geo points.
 * @author DAM
 */
public class AggregatedGeoItemsMap implements Named
{
  private MapDescription _map;
  private String _title;
  private List<AchievableStatusGeoItem> _points;

  /**
   * Constructor.
   * @param map Targeted map.
   */
  public AggregatedGeoItemsMap(MapDescription map)
  {
    _map=map;
    _title=MapUtils.getMapTitle(map);
    _points=new ArrayList<AchievableStatusGeoItem>();
  }

  @Override
  public String getName()
  {
    return _title;
  }

  /**
   * Get the targeted map.
   * @return A map description.
   */
  public MapDescription getMap()
  {
    return _map;
  }

  /**
   * Add a point to show.
   * @param point Point to add.
   */
  public void addPoint(AchievableStatusGeoItem point)
  {
    _points.add(point);
  }

  /**
   * Get the managed points.
   * @return a list of points.
   */
  public List<AchievableStatusGeoItem> getPoints()
  {
    return _points;
  }
}
