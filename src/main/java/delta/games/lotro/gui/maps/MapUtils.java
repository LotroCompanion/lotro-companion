package delta.games.lotro.gui.maps;

import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.layers.radar.RadarImageProvider;
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
    // Radar map
    // TODO Do not use radar map for dungeon maps
    setupRadarMapLayer(controller.getMapCanvas());
    return controller;
  }

  private static void setupRadarMapLayer(MapCanvas canvas)
  {
    DataFacade facade=new DataFacade();
    RadarImageProvider provider=new DatRadarImageProvider(facade);
    // TODO Update region
    RadarMapLayer radarMap=new RadarMapLayer(1,provider);
    canvas.addLayer(radarMap);
  }
}
