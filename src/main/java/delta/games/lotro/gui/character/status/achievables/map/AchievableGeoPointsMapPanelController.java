package delta.games.lotro.gui.character.status.achievables.map;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.area.AbstractAreaController;
import delta.common.ui.swing.area.AreaController;
import delta.games.lotro.character.status.achievables.edition.AchievableStatusGeoItem;
import delta.games.lotro.character.status.achievables.edition.GeoPointChangeListener;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.character.status.achievables.form.AchievableStatusUtils;
import delta.games.lotro.gui.maps.MapPanelConfigurator;
import delta.games.lotro.gui.maps.MapUtils;
import delta.games.lotro.lore.geo.GeoBoundingBox;
import delta.games.lotro.lore.maps.MapDescription;
import delta.games.lotro.lore.quests.geo.AchievableGeoPoint;
import delta.games.lotro.lore.quests.objectives.ObjectiveCondition;
import delta.games.lotro.maps.data.GeoBox;
import delta.games.lotro.maps.data.GeoPoint;
import delta.games.lotro.maps.data.MapPoint;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.maps.ui.MapUiUtils;
import delta.games.lotro.maps.ui.controllers.SelectionListener;
import delta.games.lotro.maps.ui.layers.MarkersLayer;
import delta.games.lotro.maps.ui.layers.SimpleMarkersProvider;
import delta.games.lotro.maps.ui.selection.SelectionManager;
import delta.games.lotro.utils.dat.DatInterface;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for a panel to show a single map for the geo points of a single achievable.
 * @author DAM
 */
public class AchievableGeoPointsMapPanelController extends AbstractAreaController
{
  // Data
  private MapDescription _map;
  private List<AchievableStatusGeoItem> _points;
  private List<Marker> _markers;
  private boolean _editable;
  // Controllers
  private MapPanelController _mapPanel;
  // UI
  private CompletedOrNotMarkerIconProvider _iconProvider;
  private GeoPointChangeListener _listener;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param map Description of map to show.
   * @param points Points to show.
   * @param editable Editable or not.
   * @param listener Listener for point state changes.
   */
  public AchievableGeoPointsMapPanelController(AreaController parent, MapDescription map, List<AchievableStatusGeoItem> points, boolean editable, GeoPointChangeListener listener)
  {
    super(parent);
    _map=map;
    _points=points;
    _editable=editable;
    _listener=listener;
    _markers=buildMarkers(points);
    initMapPanel();
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

  private List<Marker> buildMarkers(List<AchievableStatusGeoItem> points)
  {
    int id=0;
    AchievableStatusUtils utils=new AchievableStatusUtils(this);
    List<Marker> ret=new ArrayList<Marker>();
    for(AchievableStatusGeoItem item : points)
    {
      AchievableGeoPoint point=item.getPoint();
      Marker marker=new Marker();
      marker.setId(id);
      marker.setDid(id+1);
      // Label
      ObjectiveCondition condition=item.getCondition();
      String label=utils.getConditionLabel(condition);
      marker.setLabel(label);
      // Position
      Point2D.Float position=point.getLonLat();
      GeoPoint geoPoint=new GeoPoint(position.x,position.y);
      marker.setPosition(geoPoint);
      ret.add(marker);
      id++;
    }
    return ret;
  }

  private GeoBox getMapBoundingBox()
  {
    GeoBoundingBox box=_map.getBoundingBox();
    if (box!=null)
    {
      GeoPoint p1=new GeoPoint(box.getMin().x,box.getMin().y);
      GeoPoint p2=new GeoPoint(box.getMax().x,box.getMax().y);
      return new GeoBox(p1,p2);
    }
    Integer mapId=_map.getMapId();
    if (mapId!=null)
    {
      MapsManager mapsManager=Maps.getMaps().getMapsManager();
      GeoreferencedBasemapsManager basemapsManager=mapsManager.getBasemapsManager();
      GeoreferencedBasemap basemap=basemapsManager.getMapById(mapId.intValue());
      return basemap.getBoundingBox();
    }
    return null;
  }

  private void initMapPanel()
  {
    // Build and configure map panel
    _mapPanel=new MapPanelController();
    // Configure map
    DataFacade facade=DatInterface.getInstance().getFacade();
    MapPanelConfigurator.configureCanvas(facade,_mapPanel,_map);
    GeoBox box=getMapBoundingBox();
    MapUiUtils.configureMapPanel(_mapPanel,box,new Dimension(768,576));
    MapCanvas canvas=_mapPanel.getCanvas();
    _iconProvider=new CompletedOrNotMarkerIconProvider();
    SimpleMarkersProvider markersProvider=new SimpleMarkersProvider();
    markersProvider.setMarkers(_markers);
    MarkersLayer custom=new MarkersLayer(_iconProvider,markersProvider);
    canvas.addLayer(custom);
    if (_editable)
    {
      SelectionManager selectionMgr=_mapPanel.getSelectionManager();
      SelectionListener listener=new SelectionListener()
      {
        @Override
        public boolean handleSelection(MapPoint point)
        {
          int index=_markers.indexOf(point);
          if (index!=-1)
          {
            AchievableStatusGeoItem geoPoint=_points.get(index);
            togglePoint(geoPoint);
            return true;
          }
          return false;
        }
      };
      selectionMgr.addListener(listener);
    }
  }

  private void togglePoint(AchievableStatusGeoItem geoPoint)
  {
    boolean isCompleted=geoPoint.isCompleted();
    _listener.handlePointChange(geoPoint,!isCompleted);
  }

  /**
   * Update UI.
   */
  public void updateUi()
  {
    int index=0;
    for(AchievableStatusGeoItem point : _points)
    {
      boolean completed=point.isCompleted();
      _iconProvider.setCompleted(index,completed);
      index++;
    }
    _mapPanel.getCanvas().repaint();
  }

  /**
   * Release all managed resources.
   */
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
    // UI
    _iconProvider=null;
    _listener=null;
  }
}
