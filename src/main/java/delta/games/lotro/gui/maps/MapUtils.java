package delta.games.lotro.gui.maps;

import java.util.List;

import delta.common.utils.NumericTools;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.MapWindowController;
import delta.games.lotro.maps.ui.layers.radar.RadarImageProvider;
import delta.games.lotro.maps.ui.navigation.NavigationListener;
import delta.games.lotro.utils.maps.Maps;

/**
 * Map utilities.
 * @author DAM
 */
public class MapUtils
{
  /**
   * Build a map window controller.
   * @return the new map window controller.
   */
  public static MapWindowController buildMapWindow()
  {
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    final MapWindowController controller=new MapWindowController(mapsManager);
    NavigationListener listener=new NavigationListener()
    {
      @Override
      public void mapChangeRequest(String key)
      {
        updateMarkers(controller,key);
      }
    };
    controller.getNavigationSupport().getNavigationListeners().addListener(listener);
    // Radar map
    // TODO Do not use radar map for dungeon maps
    setupRadarMapLayer(controller.getMapCanvas());
    return controller;
  }

  private static void updateMarkers(MapWindowController mapController, String mapKey)
  {
    int mapId=NumericTools.parseInt(mapKey,0);
    List<Marker> markers=new MapMarkersFactory().getMarkers(mapId);
    mapController.setMarkers(markers);
  }

  private static void setupRadarMapLayer(MapCanvas canvas)
  {
    DataFacade facade=new DataFacade();
    RadarImageProvider provider=new DatRadarImageProvider(facade);
    // TODO Update region
    RadarMapLayer radarMap=new RadarMapLayer(canvas,1,provider);
    canvas.addLayer(radarMap);
  }
}
