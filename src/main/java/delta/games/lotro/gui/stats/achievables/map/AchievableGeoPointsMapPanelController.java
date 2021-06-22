package delta.games.lotro.gui.stats.achievables.map;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.achievables.edition.AchievableStatusGeoItem;
import delta.games.lotro.character.achievables.edition.GeoPointChangeListener;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.maps.basemap.DatBasemapImageProvider;
import delta.games.lotro.gui.stats.achievables.form.AchievableStatusUtils;
import delta.games.lotro.lore.quests.geo.AchievableGeoPoint;
import delta.games.lotro.lore.quests.objectives.ObjectiveCondition;
import delta.games.lotro.maps.data.GeoPoint;
import delta.games.lotro.maps.data.MapPoint;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.ui.BasemapPanelController;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.maps.ui.controllers.SelectionListener;
import delta.games.lotro.maps.ui.layers.BasemapLayer;
import delta.games.lotro.maps.ui.layers.MarkersLayer;
import delta.games.lotro.maps.ui.layers.SimpleMarkersProvider;
import delta.games.lotro.maps.ui.selection.SelectionManager;
import delta.games.lotro.utils.dat.DatInterface;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for a panel to show a single map for the geo points of a single achievable.
 * @author DAM
 */
public class AchievableGeoPointsMapPanelController
{
  private int _mapId;
  private BasemapPanelController _panel;
  private CompletedOrNotMarkerIconProvider _iconProvider;
  private List<AchievableStatusGeoItem> _points;
  private List<Marker> _markers;
  private GeoPointChangeListener _listener;

  /**
   * Constructor.
   * @param mapId Identifier of the map to show.
   * @param points Points to show.
   * @param listener Listener for point state changes.
   */
  public AchievableGeoPointsMapPanelController(int mapId, List<AchievableStatusGeoItem> points, GeoPointChangeListener listener)
  {
    _mapId=mapId;
    _points=points;
    _listener=listener;
    _markers=buildMarkers(points);
    initMapPanel();
  }

  /**
   * Get the identifier of the managed map.
   * @return a map identifier.
   */
  public int getMapId()
  {
    return _mapId;
  }

  /**
   * Get the managed map component.
   * @return  the managed map component.
   */
  public Component getMapComponent()
  {
    return _panel.getComponent();
  }

  private List<Marker> buildMarkers(List<AchievableStatusGeoItem> points)
  {
    int id=0;
    List<Marker> ret=new ArrayList<Marker>();
    for(AchievableStatusGeoItem item : points)
    {
      AchievableGeoPoint point=item.getPoint();
      Marker marker=new Marker();
      marker.setId(id);
      marker.setDid(id+1);
      // Label
      ObjectiveCondition condition=item.getCondition();
      String label=AchievableStatusUtils.getConditionLabel(condition);
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

  private void initMapPanel()
  {
    // Build and configure map panel
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    _panel=new BasemapPanelController(mapsManager.getBasemapsManager());
    // Basemap layer
    BasemapLayer basemapLayer=_panel.getBasemapLayer();
    DataFacade facade=DatInterface.getInstance().getFacade();
    DatBasemapImageProvider imageProvider=new DatBasemapImageProvider(facade);
    basemapLayer.setBasemapImageProvider(imageProvider);
    _panel.setMap(_mapId);
    _panel.setMaxSize(new Dimension(768,576));
    MapCanvas canvas=_panel.getCanvas();
    _iconProvider=new CompletedOrNotMarkerIconProvider();
    SimpleMarkersProvider markersProvider=new SimpleMarkersProvider();
    markersProvider.setMarkers(_markers);
    MarkersLayer custom=new MarkersLayer(_iconProvider,markersProvider);
    canvas.addLayer(custom);
    MapPanelController mapPanel=_panel.getMapPanelController();
    SelectionManager selectionMgr=mapPanel.getSelectionManager();
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
    _panel.getCanvas().repaint();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.dispose();
      _panel=null;
    }
    _iconProvider=null;
  }
}
