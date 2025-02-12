package delta.games.lotro.gui.character.status.housing.map;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.area.AbstractAreaController;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.housing.HouseContents;
import delta.games.lotro.character.status.housing.HousingItem;
import delta.games.lotro.common.geo.Position;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.maps.DatRadarImageProvider;
import delta.games.lotro.gui.maps.MapUtils;
import delta.games.lotro.gui.maps.MarkerSelectionListener;
import delta.games.lotro.gui.maps.RadarMapLayer;
import delta.games.lotro.gui.maps.basemap.DatBasemapImageProvider;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.maps.MapDescription;
import delta.games.lotro.lore.maps.ParchmentMap;
import delta.games.lotro.lore.maps.ParchmentMapsManager;
import delta.games.lotro.maps.data.GeoPoint;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.maps.data.categories.CategoriesConstants;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.maps.ui.MapUiUtils;
import delta.games.lotro.maps.ui.MarkerIconProvider;
import delta.games.lotro.maps.ui.layers.BasemapLayer;
import delta.games.lotro.maps.ui.layers.MarkersLayer;
import delta.games.lotro.maps.ui.layers.SimpleMarkersProvider;
import delta.games.lotro.maps.ui.layers.radar.RadarImageProvider;
import delta.games.lotro.maps.ui.selection.SelectionManager;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for a panel to show a single map for the items in a house.
 * @author DAM
 */
public class HouseMapPanelController extends AbstractAreaController
{
  private static final Dimension MAX_SIZE=new Dimension(1024,768);

  // Data
  private MapDescription _map;
  private List<HousingItem> _points;
  private List<Marker> _markers;
  // Controllers
  private MapPanelController _mapPanel;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param facade Facade.
   * @param contents Contents to show.
   */
  public HouseMapPanelController(AreaController parent, DataFacade facade, HouseContents contents)
  {
    super(parent);
    initData(contents);
    _points=contents.getItems();
    _markers=buildMarkers();
    _mapPanel=buildPanel(facade);
  }

  private void initData(HouseContents contents)
  {
    int zoneID=contents.getZoneID();
    MapDescription map=HousingMapsUtils.buildMapDescription(zoneID);
    _map=map;
  }

  /**
   * Get the managed map component.
   * @return  the managed map component.
   */
  public Component getMapComponent()
  {
    return _mapPanel.getLayers();
  }

  /**
   * Get the title for the managed map.
   * @return a title.
   */
  public String getMapTitle()
  {
    return MapUtils.getMapTitle(_map);
  }

  private List<Marker> buildMarkers()
  {
    int id=0;
    List<Marker> ret=new ArrayList<Marker>();
    for(HousingItem housingItem : _points)
    {
      Position position=housingItem.getPosition();
      Marker marker=new Marker();
      marker.setId(id);
      Item item=housingItem.getItem();
      int did=item.getIdentifier();
      marker.setDid(did);
      marker.setCategoryCode(CategoriesConstants.ITEM);
      // Label
      String label=item.getName();
      marker.setLabel(label);
      // Position
      GeoPoint geoPoint=new GeoPoint(position.getLongitude(),position.getLatitude());
      marker.setPosition(geoPoint);
      ret.add(marker);
      id++;
    }
    return ret;
  }

  /**
   * Build the map panel.
   * @param facade Data facade.
   * @return the new map panel controller.
   */
  private MapPanelController buildPanel(DataFacade facade)
  {
    MapPanelController panel=new MapPanelController();
    // Setup selection manager
    SelectionManager selectionMgr=panel.getSelectionManager();
    WindowController parent=getWindowController();
    selectionMgr.addListener(new MarkerSelectionListener(parent));
    MapCanvas canvas=panel.getCanvas();
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    int region=0;
    // Basemap
    int mapId=_map.getMapId().intValue();
    GeoreferencedBasemapsManager basemapsManager=mapsManager.getBasemapsManager();
    GeoreferencedBasemap basemap=basemapsManager.getMapById(mapId);
    if (basemap!=null)
    {
      BasemapLayer basemapLayer=new BasemapLayer();
      DatBasemapImageProvider imageProvider=new DatBasemapImageProvider(facade);
      basemapLayer.setBasemapImageProvider(imageProvider);
      basemapLayer.setMap(basemap);
      canvas.addLayer(basemapLayer);
      MapUiUtils.configureMapPanel(panel,basemap.getBoundingBox(),MAX_SIZE);
      ParchmentMapsManager parchmentMapsMgr=ParchmentMapsManager.getInstance();
      ParchmentMap parchmentMap=parchmentMapsMgr.getMapById(mapId);
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

    // Offsets
    HouseItemOffsetLayer offsetsLayer=new HouseItemOffsetLayer();
    offsetsLayer.setItems(_points);
    canvas.addLayer(offsetsLayer);
    // Markers
    MarkerIconProvider iconsProvider=new ItemIconProvider();
    SimpleMarkersProvider markersProvider=new SimpleMarkersProvider();
    markersProvider.setMarkers(_markers);
    MarkersLayer markersLayer=new MarkersLayer(iconsProvider,markersProvider);
    canvas.addLayer(markersLayer);
    return panel;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _map=null;
    _points=null;
    _markers=null;
    // Controllers
    if (_mapPanel!=null)
    {
      _mapPanel.dispose();
      _mapPanel=null;
    }
  }
}
