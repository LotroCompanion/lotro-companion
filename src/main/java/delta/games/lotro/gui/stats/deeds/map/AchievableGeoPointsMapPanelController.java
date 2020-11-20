package delta.games.lotro.gui.stats.deeds.map;

import java.awt.Component;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.achievables.AchievableStatusGeoItem;
import delta.games.lotro.lore.quests.geo.AchievableGeoPoint;
import delta.games.lotro.maps.data.GeoPoint;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.ui.BasemapPanelController;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.layers.MarkersLayer;
import delta.games.lotro.maps.ui.layers.SimpleMarkersProvider;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for a panel toshow a single map for the geo points of a single achievable.
 * @author DAM
 */
public class AchievableGeoPointsMapPanelController
{
  private int _mapId;
  private BasemapPanelController _panel;
  private CompletedOrNotMarkerIconProvider _iconProvider;
  private List<AchievableStatusGeoItem> _points;
  private List<Marker> _markers;

  /**
   * Constructor.
   * @param mapId Identifier of the map to show.
   * @param points Points to show.
   */
  public AchievableGeoPointsMapPanelController(int mapId, List<AchievableStatusGeoItem> points)
  {
    _mapId=mapId;
    _points=points;
    _markers=buildMarkers(points);
    initMapPanel();
  }

  /**
   * Ge the identifuer of the managed map.
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
    _panel.setMap(_mapId);
    MapCanvas canvas=_panel.getCanvas();
    _iconProvider=new CompletedOrNotMarkerIconProvider();
    SimpleMarkersProvider markersProvider=new SimpleMarkersProvider();
    markersProvider.setMarkers(_markers);
    MarkersLayer custom=new MarkersLayer(_iconProvider,markersProvider);
    canvas.addLayer(custom);
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
