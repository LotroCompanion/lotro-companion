package delta.games.lotro.gui.maps.resources;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.maps.DatRadarImageProvider;
import delta.games.lotro.gui.maps.MapUiUtils;
import delta.games.lotro.gui.maps.RadarMapLayer;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.maps.Area;
import delta.games.lotro.lore.maps.Dungeon;
import delta.games.lotro.lore.maps.DungeonsManager;
import delta.games.lotro.lore.maps.ParchmentMap;
import delta.games.lotro.lore.maps.ParchmentMapsManager;
import delta.games.lotro.lore.maps.resources.ResourcesMapDescriptor;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.maps.data.markers.MarkersFinder;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.maps.ui.MarkerIconProvider;
import delta.games.lotro.maps.ui.layers.BasemapLayer;
import delta.games.lotro.maps.ui.layers.MarkersLayer;
import delta.games.lotro.maps.ui.layers.SimpleMarkersProvider;
import delta.games.lotro.maps.ui.layers.radar.RadarImageProvider;
import delta.games.lotro.utils.Proxy;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for a resources map panel.
 * @author DAM
 */
public class ResourcesMapPanelController
{
  private static final Dimension MAX_SIZE=new Dimension(1024,768);

  private ResourcesMapDescriptor _mapDescriptor;
  private int _mapId;
  private MapPanelController _mapPanel;

  /**
   * Constructor.
   * @param facade Data facade.
   * @param mapDescriptor Map descriptor to use.
   * @param mapId Basemap identifier.
   */
  public ResourcesMapPanelController(DataFacade facade, ResourcesMapDescriptor mapDescriptor, int mapId)
  {
    _mapDescriptor=mapDescriptor;
    _mapId=mapId;
    _mapPanel=buildPanel(facade);
  }

  /**
   * Get the managed map panel controller.
   * @return the managed map panel controller.
   */
  public MapPanelController getMapPanelController()
  {
    return _mapPanel;
  }

  /**
   * Build the map panel.
   * @param facade Data facade.
   * @return the new map panel controller.
   */
  private MapPanelController buildPanel(DataFacade facade)
  {
    MapPanelController panel=new MapPanelController();
    MapCanvas canvas=panel.getCanvas();
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    int region=0;
    // Basemap
    GeoreferencedBasemapsManager basemapsManager=mapsManager.getBasemapsManager();
    GeoreferencedBasemap basemap=basemapsManager.getMapById(_mapId);
    if (basemap!=null)
    {
      BasemapLayer basemapLayer=new BasemapLayer();
      basemapLayer.setMap(basemap);
      canvas.addLayer(basemapLayer);
      MapUiUtils.configureMapPanel(panel,basemap.getBoundingBox(),MAX_SIZE);
      ParchmentMapsManager parchmentMapsMgr=ParchmentMapsManager.getInstance();
      ParchmentMap parchmentMap=parchmentMapsMgr.getMapById(_mapId);
      if (parchmentMap!=null)
      {
        region=parchmentMap.getRegion();
      }
    }

    // Radar map?
    RadarImageProvider provider=new DatRadarImageProvider(facade);
    RadarMapLayer radarLayer=new RadarMapLayer(1,provider);
    canvas.addLayer(radarLayer);
    radarLayer.setRegion(region);

    // Markers
    MarkerIconProvider iconsProvider=new ResourceIconProvider();
    SimpleMarkersProvider markersProvider=new SimpleMarkersProvider();
    List<Marker> markers=findMarkers();
    markersProvider.setMarkers(markers);
    MarkersLayer markersLayer=new MarkersLayer(iconsProvider,markersProvider);
    canvas.addLayer(markersLayer);
    return panel;
  }

  private List<Marker> findMarkers()
  {
    List<Marker> ret=new ArrayList<Marker>();
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    MarkersFinder markersFinder=mapsManager.getMarkersFinder();
    ParchmentMapsManager parchmentMapsMgr=ParchmentMapsManager.getInstance();
    ParchmentMap parchmentMap=parchmentMapsMgr.getMapById(_mapId);
    if (parchmentMap!=null)
    {
      List<Area> areas=parchmentMap.getAreas();
      for(Area area : areas)
      {
        int areaId=area.getIdentifier();
        List<Proxy<Item>> items=_mapDescriptor.getItems();
        for(Proxy<Item> item : items)
        {
          int itemId=item.getId();
          List<Marker> markers=markersFinder.findMarkersForDid(itemId,areaId);
          ret.addAll(markers);
        }
      }
    }
    DungeonsManager dungeonsMgr=DungeonsManager.getInstance();
    Dungeon dungeon=dungeonsMgr.getDungeonById(_mapId);
    if (dungeon!=null)
    {
      List<Proxy<Item>> items=_mapDescriptor.getItems();
      for(Proxy<Item> item : items)
      {
        int itemId=item.getId();
        List<Marker> markers=markersFinder.findMarkersForDid(itemId,_mapId);
        ret.addAll(markers);
      }
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _mapDescriptor=null;
    if (_mapPanel!=null)
    {
      _mapPanel.dispose();
      _mapPanel=null;
    }
  }
}
