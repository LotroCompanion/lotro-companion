package delta.games.lotro.gui.character.status.achievables;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.status.achievables.edition.AchievableStatusGeoItem;
import delta.games.lotro.lore.maps.MapDescription;

/**
 * @author dm
 */
public class MarkersMapDescriptor
{
  private MapDescription _map;
  private List<AchievableStatusGeoItem> _points;

  public MarkersMapDescriptor(MapDescription map)
  {
    _map=map;
    _points=new ArrayList<AchievableStatusGeoItem>();
  }

  public MapDescription getMap()
  {
    return _map;
  }

  public void addPoint(AchievableStatusGeoItem point)
  {
    _points.add(point);
  }

  public List<AchievableStatusGeoItem> getPoints()
  {
    return _points;
  }
}
