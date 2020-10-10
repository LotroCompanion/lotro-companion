package delta.games.lotro.gui.maps;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import delta.games.lotro.lore.maps.ParchmentMap;
import delta.games.lotro.lore.maps.ParchmentMapsManager;
import delta.games.lotro.maps.data.GeoBox;
import delta.games.lotro.maps.data.GeoReference;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.maps.ui.layers.BasemapLayer;
import delta.games.lotro.utils.maps.Maps;

/**
 * A panel to show all parchment maps in a region.
 * @author DAM
 */
public class MainTestGlobalRegionMaps
{
  private static final Dimension MAX_SIZE=new Dimension(1024,768);

  private int _region;

  /**
   * Constructor.
   * @param region Region to use.
   */
  public MainTestGlobalRegionMaps(int region)
  {
    _region=region;
  }

  /**
   * Build the map panel controller.
   * @return a map panel controller.
   */
  public MapPanelController buildPanel()
  {
    GeoBox globalBBox=null;
    MapPanelController panel=new MapPanelController();
    MapCanvas canvas=panel.getCanvas();
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    GeoreferencedBasemapsManager basemapsManager=mapsManager.getBasemapsManager();
    ParchmentMapsManager parchmentMapsManager=ParchmentMapsManager.getInstance();
    for(ParchmentMap parchmentMap : parchmentMapsManager.getParchmentMaps())
    {
      // Check region
      int mapRegion=parchmentMap.getRegion();
      if (mapRegion!=_region)
      {
        continue;
      }
      // Skip basemaps with no geographic reference (region maps)
      GeoreferencedBasemap basemap=basemapsManager.getMapById(parchmentMap.getIdentifier());
      GeoReference geoRef=basemap.getGeoReference();
      if (geoRef.getGeo2PixelFactor()<2)
      {
        continue;
      }
      // Bounding box management
      GeoBox mapBoundingBox=basemap.getBoundingBox();
      if (globalBBox==null)
      {
        // Initialize bounding box
        globalBBox=new GeoBox(mapBoundingBox.getMin(),mapBoundingBox.getMax());
      }
      else
      {
        // Extend current bounding box
        globalBBox.extend(mapBoundingBox);
      }
      // Add a basemap layer
      BasemapLayer basemapLayer=new BasemapLayer();
      basemapLayer.setMap(basemap);
      canvas.addLayer(basemapLayer);
    }
    MapUiUtils.configureMapPanel(panel,globalBBox,MAX_SIZE);

    return panel;
  }

  /**
   * Main method for this POC.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    for(int region=1;region<=4;region++)
    {
      MainTestGlobalRegionMaps globalMapPanelCtrl=new MainTestGlobalRegionMaps(region);
      MapPanelController mapPanelCtrl=globalMapPanelCtrl.buildPanel();
      JFrame f=new JFrame();
      Component mapPanel=mapPanelCtrl.getLayers();
      f.getContentPane().add(mapPanel,BorderLayout.CENTER);
      f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      f.pack();
      f.setResizable(false);
      f.setVisible(true);
    }
  }
}
