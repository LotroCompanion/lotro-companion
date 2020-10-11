package delta.games.lotro.gui.maps.instances;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.dat.loaders.PositionDecoder;
import delta.games.lotro.gui.maps.DatRadarImageProvider;
import delta.games.lotro.gui.maps.MapUiUtils;
import delta.games.lotro.gui.maps.RadarMapLayer;
import delta.games.lotro.lore.geo.BlockReference;
import delta.games.lotro.lore.instances.InstanceMapDescription;
import delta.games.lotro.lore.instances.PrivateEncounter;
import delta.games.lotro.lore.maps.ParchmentMap;
import delta.games.lotro.lore.maps.ParchmentMapsManager;
import delta.games.lotro.maps.data.GeoBox;
import delta.games.lotro.maps.data.GeoPoint;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.maps.data.categories.CategoriesManager;
import delta.games.lotro.maps.data.markers.MarkersFinder;
import delta.games.lotro.maps.ui.DefaultMarkerIconsProvider;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.maps.ui.MarkerIconProvider;
import delta.games.lotro.maps.ui.layers.BasemapLayer;
import delta.games.lotro.maps.ui.layers.MarkersLayer;
import delta.games.lotro.maps.ui.layers.SimpleMarkersProvider;
import delta.games.lotro.maps.ui.layers.radar.RadarImageProvider;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for an instance map panel.
 * @author DAM
 */
public class InstanceMapPanelController
{
  private static final Dimension MAX_SIZE=new Dimension(1024,768);

  private PrivateEncounter _privateEncounter;
  private InstanceMapDescription _mapDescription;
  private MapPanelController _mapPanel;

  /**
   * Constructor.
   * @param facade Data facade.
   * @param privateEncounter Private encounter to use.
   * @param mapDescription Map description for this private encounter.
   */
  public InstanceMapPanelController(DataFacade facade, PrivateEncounter privateEncounter, InstanceMapDescription mapDescription)
  {
    _privateEncounter=privateEncounter;
    _mapDescription=mapDescription;
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
    Integer mapId=_mapDescription.getMapId();
    if (mapId!=null)
    {
      GeoreferencedBasemapsManager basemapsManager=mapsManager.getBasemapsManager();
      GeoreferencedBasemap basemap=basemapsManager.getMapById(mapId.intValue());
      if (basemap!=null)
      {
        BasemapLayer basemapLayer=new BasemapLayer();
        basemapLayer.setMap(basemap);
        canvas.addLayer(basemapLayer);
        //canvas.setViewReference(basemap.getGeoReference());
        MapUiUtils.configureMapPanel(panel,basemap.getBoundingBox(),MAX_SIZE);
        ParchmentMapsManager parchmentMapsMgr=ParchmentMapsManager.getInstance();
        ParchmentMap parchmentMap=parchmentMapsMgr.getMapById(mapId.intValue());
        if (parchmentMap!=null)
        {
          region=parchmentMap.getRegion();
        }
      }
    }
    else
    {
      GeoBox box=buildBoundingBox();
      MapUiUtils.configureMapPanel(panel,box,MAX_SIZE);
      region=_mapDescription.getBlocks().get(0).getRegion();
    }

    // Radar map?
    RadarImageProvider provider=new DatRadarImageProvider(facade);
    RadarMapLayer radarLayer=new RadarMapLayer(1,provider);
    canvas.addLayer(radarLayer);
    radarLayer.setRegion(region);

    // Markers
    CategoriesManager categoriesManager=mapsManager.getCategories();
    MarkerIconProvider iconsProvider=new DefaultMarkerIconsProvider(categoriesManager);
    SimpleMarkersProvider markersProvider=new SimpleMarkersProvider();
    List<Marker> markers=findMarkers();
    markersProvider.setMarkers(markers);
    MarkersLayer markersLayer=new MarkersLayer(iconsProvider,markersProvider);
    canvas.addLayer(markersLayer);
    return panel;
  }

  private GeoBox buildBoundingBox()
  {
    List<BlockReference> blocks=_mapDescription.getBlocks();
    GeoBox box=null;
    for(BlockReference block : blocks)
    {
      GeoBox blockBox=buildBoxForBlock(block);
      if (box==null)
      {
        box=blockBox;
      }
      else
      {
        box.extend(blockBox);
      }
    }
    return box;
  }

  private GeoBox buildBoxForBlock(BlockReference block)
  {
    int blockX=block.getBlockX();
    int blockY=block.getBlockY();
    float[] startLatLon=PositionDecoder.decodePosition(blockX,blockY,0,PositionDecoder.LANDBLOCK_SIZE);
    GeoPoint landBlockStart=new GeoPoint(startLatLon[0],startLatLon[1]);
    float[] endLatLon=PositionDecoder.decodePosition(blockX+1,blockY-1,0,PositionDecoder.LANDBLOCK_SIZE);
    GeoPoint landBlockEnd=new GeoPoint(endLatLon[0],endLatLon[1]);
    return new GeoBox(landBlockStart,landBlockEnd);
  }

  private List<Marker> findMarkers()
  {
    List<Marker> ret=new ArrayList<Marker>();
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    MarkersFinder markersFinder=mapsManager.getMarkersFinder();
    int contentLayer=_privateEncounter.getContentLayerId();
    /*
    List<Marker> markers=markersFinder.findMarkersForContentLayer(contentLayer);
    ret.addAll(markers);
    */
    List<Integer> zones=_mapDescription.getZoneIds();
    for(Integer zone : zones)
    {
      List<Marker> markers=markersFinder.findMarkers(zone.intValue(),contentLayer);
      ret.addAll(markers);
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _privateEncounter=null;
    _mapDescription=null;
    if (_mapPanel!=null)
    {
      _mapPanel.dispose();
      _mapPanel=null;
    }
  }
}
