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
   * Get the radar image for the given landblock.
   * @param region Region.
   * @param blockX Landblock X.
   * @param blockY Landblock Y.
   * @return a buffered image or <code>null</code> if not found.
   */
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
    //int maxH=0;
    //int minH=40000;
    for(int y=0;y<32;y++)
    {
      for(int x=0;x<32;x++)
      {
        int h=map.getRawHeightAt(x,y);
        //int c = (int)(Math.log(((16 * h) % 65535) + 1)/Math.log(1.045));
        /*
        int c;
        if (h<10000) c=0;
        else if (h>45000) c=255;
        else c=((h-10000)*255)/(45000-10000);
        //int c=h/128;
        c=255-c;
        */
        int c = (int)(Math.log(((16 * h) % 65535) + 1)/Math.log(1.045));
        byte color=(byte)c;
        int fullX=x;
        int fullY=(31-y);
        int offset=((width*fullY)+fullX)*3;
        b[offset]=color;
        b[offset+1]=color;
        b[offset+2]=color;
        //if (h>maxH) maxH=h;
        //if (h<minH) minH=h;
      }
    }
    //System.out.println("Min="+minH+",max="+maxH);
    DataBuffer buffer = new DataBufferByte(b, b.length);
    WritableRaster raster = Raster.createInterleavedRaster(buffer, width, height, 3 * width, 3, new int[] {0, 1, 2}, (Point)null);
    ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE); 
    BufferedImage image = new BufferedImage(cm, raster, true, null);
    return image;
  }
}
