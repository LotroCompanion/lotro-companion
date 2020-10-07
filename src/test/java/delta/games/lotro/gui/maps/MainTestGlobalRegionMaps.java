package delta.games.lotro.gui.maps;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import delta.games.lotro.lore.maps.ParchmentMap;
import delta.games.lotro.lore.maps.ParchmentMapsManager;
import delta.games.lotro.maps.data.GeoBox;
import delta.games.lotro.maps.data.GeoPoint;
import delta.games.lotro.maps.data.GeoReference;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.maps.data.view.BoundedZoomFilter;
import delta.games.lotro.maps.data.view.ZoomFilter;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.maps.ui.constraints.MapBoundsConstraint;
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
      int mapRegion=parchmentMap.getRegion();
      if (mapRegion!=_region)
      {
        continue;
      }
      GeoreferencedBasemap basemap=basemapsManager.getMapById(parchmentMap.getIdentifier());
      GeoReference geoRef=basemap.getGeoReference();
      if (geoRef.getGeo2PixelFactor()<2)
      {
        continue;
      }
      GeoBox mapBoundingBox=basemap.getBoundingBox();
      if (globalBBox==null)
      {
        globalBBox=new GeoBox(mapBoundingBox.getMin(),mapBoundingBox.getMax());
      }
      else
      {
        globalBBox.extend(mapBoundingBox);
      }
      BasemapLayer basemapLayer=new BasemapLayer();
      basemapLayer.setMap(basemap);
      canvas.addLayer(basemapLayer);
    }
    GeoPoint start=new GeoPoint(globalBBox.getMin().getLongitude(),globalBBox.getMax().getLatitude());
    float geo2Pixel=computeZoom(MAX_SIZE,globalBBox);
    GeoReference viewRef=new GeoReference(start,geo2Pixel);
    Dimension fitSize=getFitSize(geo2Pixel,globalBBox);
    panel.setViewSize(fitSize);
    canvas.setViewReference(viewRef);
    // Constraints
    MapBoundsConstraint constraints=new MapBoundsConstraint(canvas,globalBBox);
    panel.getCanvas().setMapConstraint(constraints);
    ZoomFilter zoomFilter=new BoundedZoomFilter(Float.valueOf(geo2Pixel),Float.valueOf(geo2Pixel*16));
    panel.getCanvas().setZoomFilter(zoomFilter);

    return panel;
  }

  private Dimension getFitSize(float geo2Pixel, GeoBox geoBounds)
  {
    float deltaLon=geoBounds.getMax().getLongitude()-geoBounds.getMin().getLongitude();
    float deltaLat=geoBounds.getMax().getLatitude()-geoBounds.getMin().getLatitude();
    int width=(int)(deltaLon*geo2Pixel);
    int height=(int)(deltaLat*geo2Pixel);
    return new Dimension(width,height);
  }

  private float computeZoom(Dimension pixelMaxSize, GeoBox geoBounds)
  {
    float deltaLon=geoBounds.getMax().getLongitude()-geoBounds.getMin().getLongitude();
    float deltaLat=geoBounds.getMax().getLatitude()-geoBounds.getMin().getLatitude();

    float xGeo2Pixel=pixelMaxSize.width/deltaLon;
    float yGeo2Pixel=pixelMaxSize.height/deltaLat;
    return Math.min(xGeo2Pixel,yGeo2Pixel);
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
