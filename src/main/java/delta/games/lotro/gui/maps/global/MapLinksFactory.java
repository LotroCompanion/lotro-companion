package delta.games.lotro.gui.maps.global;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.lore.maps.Area;
import delta.games.lotro.lore.maps.ParchmentMap;
import delta.games.lotro.lore.maps.ParchmentMapsManager;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.links.LinksManager;
import delta.games.lotro.maps.data.links.MapLink;
import delta.games.lotro.utils.maps.Maps;

/**
 * Map links factory.
 * @author DAM
 */
public class MapLinksFactory
{
  /**
   * Build the links list for a map.
   * @param mapId Map identifier (dungeon ID or parchment map ID).
   * @return A possibly empty but never <code>null</code> list of links.
   */
  public List<MapLink> getLinks(int mapId)
  {
    ParchmentMapsManager parchmentMapsMgr=ParchmentMapsManager.getInstance();
    ParchmentMap parchmentMap=parchmentMapsMgr.getMapById(mapId);
    if (parchmentMap!=null)
    {
      return getParchmentMapLinks(parchmentMap);
    }
    return getDungeonLinks(mapId);
  }

  private List<MapLink> getDungeonLinks(int dungeonId)
  {
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    LinksManager linksManager=mapsManager.getLinksManager();
    List<MapLink> links=linksManager.getLinks(dungeonId,0);
    return links;
  }

  private List<MapLink> getParchmentMapLinks(ParchmentMap parchmentMap)
  {
    int parchmentMapId=parchmentMap.getIdentifier();
    List<MapLink> links=new ArrayList<MapLink>();
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    LinksManager linksManager=mapsManager.getLinksManager();
    links.addAll(linksManager.getLinks(parchmentMapId,0));
    links.addAll(linksManager.getLinks(parchmentMapId,2));
    List<Area> areas=parchmentMap.getAreas();
    for(Area area : areas)
    {
      int areaId=area.getIdentifier();
      links.addAll(linksManager.getLinks(areaId,0));
      links.addAll(linksManager.getLinks(areaId,2));
    }
    return links;
  }
}
