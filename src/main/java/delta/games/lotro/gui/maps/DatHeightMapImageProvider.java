package delta.games.lotro.gui.maps;

import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.dat.data.geo.HeightMap;
import delta.games.lotro.dat.loaders.HeightMapDataLoader;
import delta.games.lotro.maps.ui.layers.radar.RadarImageProvider;

/**
 * Provider for height map images.
 * @author DAM
 */
public class DatHeightMapImageProvider implements RadarImageProvider
{
  private HeightMapDataLoader _loader;

  /**
   * Constructor.
   * @param facade Data facade.
   */
  public DatHeightMapImageProvider(DataFacade facade)
  {
    _loader=new HeightMapDataLoader(facade);
  }

  /**
   * Get the height map image for the given landblock.
   * @param region Region.
   * @param blockX Landblock X.
   * @param blockY Landblock Y.
   * @return a buffered image or <code>null</code> if not found.
   */
  @Override
  public BufferedImage getImage(int region, int blockX, int blockY)
  {
    BufferedImage ret=null;
    HeightMap map=_loader.loadHeightMapData(region,blockX,blockY);
    if (map!=null)
    {
      ret=buildCellImage(map);
    }
    return ret;
  }

  private BufferedImage buildCellImage(HeightMap map)
  {
    int width=32;
    int height=32;
    byte[] b=new byte[width*height*3];
    // Decode data
    for(int y=0;y<32;y++)
    {
      for(int x=0;x<32;x++)
      {
        int h=map.getRawHeightAt(x,y);
        int c=getColorFromHeightUsingLog(h);
        byte color=(byte)c;
        int fullX=x;
        int fullY=(31-y);
        int offset=((width*fullY)+fullX)*3;
        b[offset]=color;
        b[offset+1]=color;
        b[offset+2]=color;
      }
    }
    DataBuffer buffer = new DataBufferByte(b, b.length);
    WritableRaster raster = Raster.createInterleavedRaster(buffer, width, height, 3 * width, 3, new int[] {0, 1, 2}, (Point)null);
    ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
    BufferedImage image = new BufferedImage(cm, raster, true, null);
    return image;
  }

  private static final int MIN_HEIGHT=10000;
  private static final int MAX_HEIGHT=45000;

  private int getColorFromHeightUsingLog(int h)
  {
    int c = (int)(Math.log(((16 * h) % 65535) + 1.0)/Math.log(1.045));
    return c;
  }

  int getColorFromHeightUsingLinear(int h)
  {
    int c;
    if (h<MIN_HEIGHT) c=0;
    else if (h>MAX_HEIGHT) c=255;
    else c=((h-MIN_HEIGHT)*255)/(MAX_HEIGHT-MIN_HEIGHT);
    // White=255 is low, Black=0 is high, so we need to flip the color value:
    c=255-c; 
    return c;
  }
}
