package delta.games.lotro.gui.maps;

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
    List<MapLink> links=getParchmentMapLinks(mapId);
    if (links==null)
    {
      links=getDungeonLinks(mapId);
    }
    return links;
  }

  private List<MapLink> getDungeonLinks(int dungeonId)
  {
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    LinksManager linksManager=mapsManager.getLinksManager();
    List<MapLink> links=linksManager.getLinks(dungeonId,0);
    return links;
  }

  private List<MapLink> getParchmentMapLinks(int parchmentMapId)
  {
    ParchmentMapsManager parchmentMapsMgr=ParchmentMapsManager.getInstance();
    ParchmentMap parchmentMap=parchmentMapsMgr.getMapById(parchmentMapId);
    if (parchmentMap==null)
    {
      return null;
    }
    List<MapLink> links=new ArrayList<MapLink>();
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    LinksManager linksManager=mapsManager.getLinksManager();
    List<MapLink> mapLinks=linksManager.getLinks(parchmentMapId,0);
    links.addAll(mapLinks);
    List<Area> areas=parchmentMap.getAreas();
    for(Area area : areas)
    {
      int areaId=area.getIdentifier();
      List<MapLink> areaLinks=linksManager.getLinks(areaId,0);
      links.addAll(areaLinks);
    }
    return links;
  }
}
