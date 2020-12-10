package delta.games.lotro.gui.maps.global;

import java.util.List;

import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.maps.data.links.MapLink;
import delta.games.lotro.utils.maps.Maps;

/**
 * Test the map links factory.
 * @author DAM
 */
public class MainTestMapLinksFactory
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    // Simple test class to show links that are map-internal.
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    GeoreferencedBasemapsManager basemapsMgr=mapsManager.getBasemapsManager();
    for(GeoreferencedBasemap basemap : basemapsMgr.getBasemaps())
    {
      int mapId=basemap.getIdentifier();
      List<MapLink> links=new MapLinksFactory().getLinks(mapId);
      for(MapLink link : links)
      {
        if (link.getTargetMapKey()==mapId)
        {
          //if (link.getParentId()!=mapId)
          {
            System.out.println("Link to same map: "+link+" with different parent map");
          }
        }
      }
    }
  }
}
