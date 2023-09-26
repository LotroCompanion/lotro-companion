package delta.games.lotro.gui.utils.icons;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;

import delta.common.ui.swing.icons.IconsManager;
import delta.games.lotro.utils.IconsUtils;

/**
 * Builder for item icons.
 * @author DAM
 */
public class ItemIconBuilder
{
  private static final Logger LOGGER=Logger.getLogger(ItemIconBuilder.class);
  private static final WeakHashMap<String,BufferedImage> _icons=new WeakHashMap<String,BufferedImage>();

  /**
   * Get the buffered image that represents an item.
   * @param iconName Item icon name.
   * @return A buffered image or <code>null</code>
   */
  public BufferedImage getItemIcon(String iconName)
  {
    BufferedImage ret=_icons.get(iconName);
    if (ret==null)
    {
      ret=buildImage(iconName);
      if (ret!=null)
      {
        if (LOGGER.isDebugEnabled())
        {
          LOGGER.debug("Loaded into cache: "+iconName);
        }
        _icons.put(iconName,ret);
      }
    }
    else
    {
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug("Successful cache hit: "+iconName);
      }
    }
    return ret;
  }

  private BufferedImage buildImage(String iconName)
  {
    int index=iconName.indexOf('-');
    if (index==-1)
    {
      String iconPath=IconsUtils.buildItemIcon(iconName);
      return IconsManager.getImage(iconPath);
    }
    String[] imageIds=buildImageIds(iconName);
    return buildImage(imageIds);
  }

  private String[] buildImageIds(String iconName)
  {
    String[] paths=iconName.split("-");
    if (paths.length==2)
    {
      // Background-Foreground
      return new String[]{paths[1],paths[0]};
    }
    else if (paths.length==3)
    {
      // Background-Shadow-Foreground
      return new String[]{paths[1],paths[2],paths[0]};
    }
    else if (paths.length==4)
    {
      // Background-Underlay-Shadow-Foreground
      return new String[]{paths[1],paths[3],paths[2],paths[0]};
    }
    return new String[0];
  }

  private BufferedImage buildImage(String[] imageIds)
  {
    BufferedImage[] images=new BufferedImage[imageIds.length];
    int nbImages=imageIds.length;
    for(int i=0;i<nbImages;i++)
    {
      if ("0".equals(imageIds[i]))
      {
        continue;
      }
      String iconPath=IconsUtils.buildItemIcon(imageIds[i]);
      images[i]=IconsManager.getImage(iconPath);
    }
    return buildImage(images);
  }

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
