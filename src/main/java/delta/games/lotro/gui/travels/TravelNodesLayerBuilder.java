package delta.games.lotro.gui.travels;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.status.travels.DiscoveredDestinations;
import delta.games.lotro.lore.travels.TravelDestination;
import delta.games.lotro.lore.travels.TravelNode;
import delta.games.lotro.lore.travels.map.TravelsMap;
import delta.games.lotro.lore.travels.map.TravelsMapNode;
import delta.games.lotro.maps.data.GeoPoint;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.ui.layers.MarkersLayer;
import delta.games.lotro.maps.ui.layers.SimpleMarkersProvider;

/**
 * Builder for the travel nodes layer.
 * @author DAM
 */
public class TravelNodesLayerBuilder
{
  private TravelsMap _map;
  private DiscoveredDestinations _destinations;

  /**
   * Constructor.
   * @param map Travels map.
   * @param destinations Known destinations.
   */
  public TravelNodesLayerBuilder(TravelsMap map, DiscoveredDestinations destinations)
  {
    _map=map;
    _destinations=destinations;
  }

  /**
   * Build the markers layer for the travel nodes.
   * @return a new layer.
   */
  public MarkersLayer build()
  {
    TravelNodesIconProvider iconsProvider=new TravelNodesIconProvider();
    SimpleMarkersProvider markersProvider=new SimpleMarkersProvider();
    List<Marker> markers=new ArrayList<Marker>();
    int id=0;
    for(TravelsMapNode uiNode : _map.getNodes())
    {
      Marker marker=buildMarker(uiNode);
      marker.setId(id);
      markers.add(marker);
      id++;
    }
    markersProvider.setMarkers(markers);
    MarkersLayer ret=new MarkersLayer(iconsProvider,markersProvider);
    return ret;
  }

  private boolean isKnown(TravelNode node)
  {
    TravelDestination destination=node.getMainLocation();
    return _destinations.isKnown(destination);
  }

  private Marker buildMarker(TravelsMapNode uiNode)
  {
    Marker marker=new Marker();
    TravelNode node=uiNode.getNpc().getNode();
    int code;
    boolean isKnown=isKnown(node);
    boolean capital=uiNode.isCapital();
    if (isKnown)
    {
      code=capital?TravelNodeMarkerConstants.CAPITAL:TravelNodeMarkerConstants.NORMAL;
    }
    else
    {
      code=capital?TravelNodeMarkerConstants.CAPITAL_EMPTY:TravelNodeMarkerConstants.NORMAL_EMPTY;
    }
    marker.setCategoryCode(code);
    Dimension uiPosition=uiNode.getUIPosition();
    int width=capital?28:24;
    GeoPoint position=new GeoPoint(uiPosition.width+width/2,-(uiPosition.height+(width/2)));
    marker.setPosition(position);
    String label=uiNode.getTooltip();
    marker.setLabel(label);
    int did=uiNode.getNpc().getNode().getIdentifier();
    marker.setDid(did);
    return marker;
  }
}
