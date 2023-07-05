package delta.games.lotro.gui.maps.resources;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.maps.DatHeightMapImageProvider;
import delta.games.lotro.gui.maps.DatRadarImageProvider;
import delta.games.lotro.gui.maps.RadarMapLayer;
import delta.games.lotro.gui.maps.basemap.DatBasemapImageProvider;
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
import delta.games.lotro.maps.ui.MapUiUtils;
import delta.games.lotro.maps.ui.MarkerIconProvider;
import delta.games.lotro.maps.ui.layers.BasemapLayer;
import delta.games.lotro.maps.ui.layers.MarkersLayer;
import delta.games.lotro.maps.ui.layers.SimpleMarkersProvider;
import delta.games.lotro.maps.ui.layers.radar.RadarImageProvider;
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
  private MarkersLayer _markersLayer;
  private SimpleMarkersProvider _markersProvider;

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
      DatBasemapImageProvider imageProvider=new DatBasemapImageProvider(facade);
      basemapLayer.setBasemapImageProvider(imageProvider);
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

    // Satellite map
    RadarImageProvider provider=new DatRadarImageProvider(facade);
    RadarMapLayer radarLayer=new RadarMapLayer(RadarMapLayer.SATELLITE_MAP,1,provider);
    canvas.addLayer(radarLayer);
    radarLayer.setRegion(region);
    // Height map
    RadarImageProvider heightMapProvider=new DatHeightMapImageProvider(facade);
    RadarMapLayer heightMapLayer=new RadarMapLayer(RadarMapLayer.HEIGHT_MAP,1,heightMapProvider);
    heightMapLayer.setVisible(false);
    canvas.addLayer(heightMapLayer);
    heightMapLayer.setRegion(region);

    // Markers
    MarkerIconProvider iconsProvider=new ResourceIconProvider();
    _markersProvider=new SimpleMarkersProvider();
    List<Marker> markers=findMarkers();
    _markersProvider.setMarkers(markers);
    _markersLayer=new MarkersLayer(iconsProvider,_markersProvider);
    canvas.addLayer(_markersLayer);
    return panel;
  }

  /**
   * Set the markers filter.
   * @param filter Filter to set. 
   */
  public void setFilter(Filter<Marker> filter)
  {
    _markersLayer.setFilter(filter);
  }

  /**
   * Get the item identifiers for the markers in the map.
   * @return a list of item identifiers.
   */
  public List<Integer> getItems()
  {
    Set<Integer> itemIds=new HashSet<Integer>();
    List<Marker> markers=_markersProvider.getMarkers();
    for(Marker marker : markers)
    {
      itemIds.add(Integer.valueOf(marker.getDid()));
    }
    return new ArrayList<Integer>(itemIds);
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
        List<Item> items=_mapDescriptor.getItems();
        for(Item item : items)
        {
          int itemId=item.getIdentifier();
          List<Marker> markers=markersFinder.findMarkersForDid(itemId,areaId);
          ret.addAll(markers);
        }
      }
    }
    DungeonsManager dungeonsMgr=DungeonsManager.getInstance();
    Dungeon dungeon=dungeonsMgr.getDungeonById(_mapId);
    if (dungeon!=null)
    {
      List<Item> items=_mapDescriptor.getItems();
      for(Item item : items)
      {
        int itemId=item.getIdentifier();
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
    _markersLayer=null;
    _markersProvider=null;
  }
}
