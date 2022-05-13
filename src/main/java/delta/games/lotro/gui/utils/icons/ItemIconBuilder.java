package delta.games.lotro.gui.utils.icons;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.WeakHashMap;

import delta.common.ui.swing.icons.IconsManager;
import delta.games.lotro.utils.IconsUtils;

/**
 * Builder for item icons.
 * @author DAM
 */
public class ItemIconBuilder
{
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
        //System.out.println("Loaded into cache: "+iconName);
        _icons.put(iconName,ret);
      }
    }
    else
    {
      //System.out.println("Successful cache: "+iconName);
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
      // Foreground-Background
      return new String[]{paths[1],paths[0]};
    }
    else if (paths.length==3)
    {
      // Foreground-Background-Shadow
      return new String[]{paths[1],paths[2],paths[0]};
    }
    else if (paths.length==4)
    {
      // Foreground-Background-Shadow-Underlay
      return new String[]{paths[1],paths[3],paths[2],paths[0]};
    }
    return new String[0];
  }

  private BufferedImage buildImage(String[] imageIds)
  {
    BufferedImage[] images=new BufferedImage[imageIds.length];
    int nbImages=imageIds.length;
    int width=0;
    int height=0;
    for(int i=0;i<nbImages;i++)
    {
      if ("0".equals(imageIds[i]))
      {
        continue;
      }
      String iconPath=IconsUtils.buildItemIcon(imageIds[i]);
      images[i]=IconsManager.getImage(iconPath);
      if (images[i]!=null)
      {
        int newWidth=images[i].getWidth();
        if (newWidth>width)
        {
          width=newWidth;
        }
        int newHeight=images[i].getHeight();
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
    for(BufferedImage image : images)
    {
      if (image!=null)
      {
        g.drawImage(image,0,0,null);
      }
    }
    return combined;
  }
}
