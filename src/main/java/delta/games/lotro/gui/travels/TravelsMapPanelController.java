package delta.games.lotro.gui.travels;

import java.awt.image.BufferedImage;

import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.travels.DiscoveredDestinations;
import delta.games.lotro.character.status.travels.io.DiscoveredDestinationsIo;
import delta.games.lotro.lore.travels.map.TravelsMap;
import delta.games.lotro.lore.travels.map.io.TravelsMapIO;
import delta.games.lotro.maps.data.GeoBox;
import delta.games.lotro.maps.data.GeoPoint;
import delta.games.lotro.maps.data.GeoReference;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.maps.ui.MapUiUtils;
import delta.games.lotro.maps.ui.layers.BasemapLayer;
import delta.games.lotro.maps.ui.layers.Layer;
import delta.games.lotro.maps.ui.layers.MarkersLayer;
import delta.games.lotro.maps.ui.layers.basemap.BasemapImageProvider;

/**
 * Controller for a panel that shows a travels map.
 * @author DAM
 */
public class TravelsMapPanelController extends AbstractPanelController
{
  private CharacterFile _file;
  private MapPanelController _mapController;
  private TravelsMap _map;
  private BufferedImage _mapImage;

  /**
   * Constructor.
   * @param file Character file to use.
   */
  public TravelsMapPanelController(CharacterFile file)
  {
    _file=file;
    loadImages();
    _map=TravelsMapIO.loadTravelsMap();
    _mapController=buildMapController();
    setPanel(_mapController.getPanel());
  }

  private void loadImages()
  {
    _mapImage=IconsManager.getImage("/misc/travelsMap/travels.png");
  }

  private MapPanelController buildMapController()
  {
    MapPanelController ret=new MapPanelController();
    // Basemap
    BasemapLayer basemapLayer=buildMapLayer();
    MapCanvas canvas=ret.getCanvas();
    GeoPoint origin=new GeoPoint(0,0);
    GeoPoint bounds=new GeoPoint(_mapImage.getWidth(),-_mapImage.getHeight());
    GeoReference reference=new GeoReference(origin,1);
    canvas.setViewReference(reference);
    canvas.addLayer(basemapLayer);
    // Labels
    Layer labelsLayer=new TravelsMapLabelsLayer(_map);
    canvas.addLayer(labelsLayer);
    // Markers
    DiscoveredDestinations destinations=getDestinations();
    TravelNodesLayerBuilder b=new TravelNodesLayerBuilder(_map,destinations);
    MarkersLayer nodesLayer=b.build();
    canvas.addLayer(nodesLayer);
    GeoBox box=new GeoBox(origin,bounds);
    MapUiUtils.configureConstraints(canvas,box,0.5f);
    return ret;
  }

  private DiscoveredDestinations getDestinations()
  {
    return DiscoveredDestinationsIo.load(_file);
  }

  private BasemapLayer buildMapLayer()
  {
    BasemapLayer layer=new BasemapLayer();
    BasemapImageProvider imageProvider=buildImageProvider();
    layer.setBasemapImageProvider(imageProvider);
    GeoreferencedBasemap basemap=buildBasemap();
    layer.setMap(basemap);
    return layer;
  }

  private GeoreferencedBasemap buildBasemap()
  {
    GeoReference reference=new GeoReference(new GeoPoint(0,0),1);
    GeoreferencedBasemap ret=new GeoreferencedBasemap(0,"Travels Map",reference);
    return ret;
  }

  private BasemapImageProvider buildImageProvider()
  {
    BasemapImageProvider ret=new BasemapImageProvider()
    {
      @Override
      public BufferedImage getImage(GeoreferencedBasemap basemap)
      {
        return _mapImage;
      }
    };
    return ret;
  }

}
