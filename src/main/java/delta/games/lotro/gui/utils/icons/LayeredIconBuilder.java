package delta.games.lotro.gui.utils.icons;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Builder for layered images/icons.
 * @author DAM
 */
public class LayeredIconBuilder
{
  /**
   * Build an image from image layers.
   * @param layers Layers to use.
   * @return A new image.
   */
  public static BufferedImage buildImage(BufferedImage[] layers)
  {
    int nbImages=layers.length;
    int width=0;
    int height=0;
    for(int i=0;i<nbImages;i++)
    {
      if (layers[i]!=null)
      {
        int newWidth=layers[i].getWidth();
        if (newWidth>width)
        {
          width=newWidth;
        }
        int newHeight=layers[i].getHeight();
        if (newHeight>height)
        {
          height=newHeight;
        }
      }
    }
    // Create the new image
    BufferedImage combined=new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    // Paint all images, preserving the alpha channels
    Graphics g=combined.getGraphics();
    for(BufferedImage layer : layers)
    {
      if (layer!=null)
      {
        g.drawImage(layer,0,0,null);
      }
    }
    return combined;
  }
}
