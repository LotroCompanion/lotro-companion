package delta.games.lotro.gui.maps;

import delta.games.lotro.lore.maps.MapDescription;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.utils.maps.Maps;

/**
 * Utility methods related to maps.
 * @author DAM
 */
public class MapUtils
{
  /**
   * Get the title to use for a map.
   * @param map Map description.
   * @return A title.
   */
  public static String getMapTitle(MapDescription map)
  {
    String title=null;
    Integer mapId=map.getMapId();
    if (mapId!=null)
    {
      MapsManager mapsManager=Maps.getMaps().getMapsManager();
      GeoreferencedBasemapsManager basemapsManager=mapsManager.getBasemapsManager();
      GeoreferencedBasemap basemap=basemapsManager.getMapById(mapId.intValue());
      if (basemap!=null)
      {
        title=basemap.getName();
      }
    }
    if (title==null)
    {
      title="Landscape"; // I18n
    }
    return title;
  }
}
