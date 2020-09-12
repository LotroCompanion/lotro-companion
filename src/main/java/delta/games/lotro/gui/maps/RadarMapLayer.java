package delta.games.lotro.gui.maps;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import delta.games.lotro.dat.data.DatPosition;
import delta.games.lotro.dat.loaders.PositionDecoder;
import delta.games.lotro.maps.data.GeoBox;
import delta.games.lotro.maps.data.GeoPoint;
import delta.games.lotro.maps.data.GeoReference;
import delta.games.lotro.maps.ui.MapView;
import delta.games.lotro.maps.ui.layers.RasterLayer;
import delta.games.lotro.maps.ui.layers.radar.RadarImageCache;
import delta.games.lotro.maps.ui.layers.radar.RadarImageProvider;

/**
 * Layer to display the radar map.
 * @author DAM
 */
public class RadarMapLayer implements RasterLayer
{
  private MapView _view;
  private int _region;
  private RadarImageCache _cache;

  /**
   * Constructor.
   * @param region to show
   * @param view Map view.
   * @param provider Radar images provider.
   */
  public RadarMapLayer(MapView view, int region, RadarImageProvider provider)
  {
    _view=view;
    _region=region;
    _cache=new RadarImageCache(provider);
  }

  /**
   * Set the region to use.
   * @param region Region code.
   */
  public void setRegion(int region)
  {
    _region=region;
  }

  @Override
  public int getPriority()
  {
    return 0;
  }

  @Override
  public void paintLayer(Graphics g)
  {
    GeoReference viewReference=_view.getViewReference();
    GeoPoint startPoint=viewReference.getStart();

    // Compute block of the view start point
    DatPosition startPos=PositionDecoder.fromLatLon(startPoint.getLongitude(),startPoint.getLatitude());
    final int startBlockX=startPos.getBlockX();
    final int startBlockY=startPos.getBlockY();

    // Compute lon/lat position of the south/west point of this block
    float[] startLatLon=PositionDecoder.decodePosition(startBlockX,startBlockY,0,PositionDecoder.LANDBLOCK_SIZE);
    GeoPoint landBlockStart=new GeoPoint(startLatLon[0],startLatLon[1]);
    // Compute the pixel of this position
    Dimension startPixel=viewReference.geo2pixel(landBlockStart);

    // Compute the number of images to display horizontally and vertically
    // - degrees for each cell
    float deltaDegrees=PositionDecoder.LANDBLOCK_SIZE*PositionDecoder.OP_TO_MP;
    // - geographic bounds of the view
    GeoBox bounds=_view.getGeoBounds();
    // - number of images horizonzally
    float deltaLon=bounds.getMax().getLongitude()-bounds.getMin().getLongitude();
    int nbImagesX=(int)(deltaLon/deltaDegrees)+2;
    if (nbImagesX>20) return;
    // - number of images vertically
    float deltaLat=bounds.getMax().getLatitude()-bounds.getMin().getLatitude();
    int nbImagesY=(int)(deltaLat/deltaDegrees)+2;
    if (nbImagesY>20) return;

    //System.out.println("Images X="+nbImagesX+", images Y="+nbImagesY);
    // Compute the size of images in pixels 
    float imgSize=deltaDegrees*viewReference.getGeo2PixelFactor();

    // Iterate vertically
    int startY=startPixel.height;
    int currentBlockY=startBlockY;
    for(int yCellIndex=0;yCellIndex<nbImagesY;yCellIndex++)
    {
      // Iterate horizontally
      int startX=startPixel.width;
      int currentBlockX=startBlockX;
      for(int xCellIndex=0;xCellIndex<nbImagesX;xCellIndex++)
      {
        BufferedImage img=_cache.getImage(_region,currentBlockX,currentBlockY);
        if (img!=null)
        {
          g.drawImage(img,startX,startY,(int)(startX+imgSize),(int)(startY+imgSize),0,0,img.getWidth(),img.getHeight(),null);
        }
        else
        {
          // If image not found, do not draw anything. The parchment/dungeon map is drawn below
        }
        currentBlockX++;
        startX+=imgSize;
      }
      currentBlockY--;
      startY+=imgSize;
    }
  }
}
