package delta.games.lotro.gui.character.status.achievables.aggregator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.character.status.achievables.edition.AchievableGeoStatusManager;
import delta.games.lotro.character.status.achievables.edition.AchievableStatusGeoItem;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.lore.maps.MapDescription;
import delta.games.lotro.lore.quests.Achievable;

/**
 * Computes the maps to use to display the points of several achievables.
 * @author DAM
 */
public class AggregatedGeoItemsManager
{
  private Map<Integer,AggregatedGeoItemsMap> _mapsWithId;
  private List<AggregatedGeoItemsMap> _mapsWithNoId;

  /**
   * Constructor.
   */
  public AggregatedGeoItemsManager()
  {
    _mapsWithId=new HashMap<Integer,AggregatedGeoItemsMap>();
    _mapsWithNoId=new ArrayList<AggregatedGeoItemsMap>();
  }

  /**
   * Add an achievable status.
   * @param status Status to use.
   */
  public void addAchievableStatus(AchievableStatus status)
  {
    Achievable achievable=status.getAchievable();
    AchievableGeoStatusManager geoStatusManager=new AchievableGeoStatusManager(status,null);
    List<AchievableStatusGeoItem> points=geoStatusManager.getPoints();
    List<MapDescription> maps=achievable.getMaps();

    for(AchievableStatusGeoItem point : points)
    {
      MapDescription map=maps.get(point.getPoint().getMapIndex());
      Integer mapId=map.getMapId();
      AggregatedGeoItemsMap aggregatedMap=null;
      if (mapId!=null)
      {
        aggregatedMap=_mapsWithId.get(mapId);
        if (aggregatedMap==null)
        {
          aggregatedMap=new AggregatedGeoItemsMap(map);
          _mapsWithId.put(mapId,aggregatedMap);
        }
      }
      else
      {
        aggregatedMap=new AggregatedGeoItemsMap(map);
        _mapsWithNoId.add(aggregatedMap);
      }
      aggregatedMap.addPoint(point);
    }
  }

  /**
   * Get the maps to use.
   * @return A list of maps.
   */
  public List<AggregatedGeoItemsMap> getMaps()
  {
    List<AggregatedGeoItemsMap> ret=new ArrayList<AggregatedGeoItemsMap>();
    ret.addAll(_mapsWithId.values());
    Collections.sort(ret,new NamedComparator());
    ret.addAll(_mapsWithNoId);
    return ret;
  }
}
