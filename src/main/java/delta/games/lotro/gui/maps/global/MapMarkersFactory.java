package delta.games.lotro.gui.maps.global;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.lore.maps.Area;
import delta.games.lotro.lore.maps.ParchmentMap;
import delta.games.lotro.lore.maps.ParchmentMapsManager;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.data.markers.MarkersFinder;
import delta.games.lotro.utils.maps.Maps;

/**
 * Markers factory.
 * @author DAM
 */
public class MapMarkersFactory
{
  /**
   * Build the markers list for a map.
   * @param mapId Map identifier (dungeon ID or parchment map ID).
   * @return A possibly empty but never <code>null</code> list of markers.
   */
  public List<Marker> getMarkers(int mapId)
  {
    ParchmentMapsManager parchmentMapsMgr=ParchmentMapsManager.getInstance();
    ParchmentMap parchmentMap=parchmentMapsMgr.getMapById(mapId);
    if (parchmentMap!=null)
    {
      return getParchmentMapMarkers(parchmentMap);
    }
    return getDungeonMarkers(mapId);
  }

  private List<Marker> getDungeonMarkers(int dungeonId)
  {
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    MarkersFinder finder=mapsManager.getMarkersFinder();
    List<Marker> markers=finder.findMarkers(dungeonId,0);
    return markers;
  }

  private List<Marker> getParchmentMapMarkers(ParchmentMap parchmentMap)
  {
    List<Marker> markers=new ArrayList<Marker>();
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    MarkersFinder finder=mapsManager.getMarkersFinder();
    List<Area> areas=parchmentMap.getAreas();
    for(Area area : areas)
    {
      int areaId=area.getIdentifier();
      List<Marker> areaMarkers=finder.findMarkers(areaId,0);
      markers.addAll(areaMarkers);
    }
    return markers;
  }
}
