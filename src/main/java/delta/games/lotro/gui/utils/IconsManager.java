package delta.games.lotro.gui.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.WeakHashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import delta.games.lotro.utils.LotroLoggers;

/**
 * Icons manager.
 * @author DAM
 */
public class IconsManager
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();
  private static final WeakHashMap<String,ImageIcon> _icons=new WeakHashMap<String,ImageIcon>();

  private static URL getImageURL(String iconPath)
  {
    URL imageURL=IconsManager.class.getResource(iconPath);
    return imageURL;
  }

  /**
   * Get an image.
   * @param path Image path in the classpath.
   * @return An image or <code>null</code> if not found.
   */
  public static BufferedImage getImage(String path)
  {
    BufferedImage img=null;
    if (path!=null)
    {
      URL url=getImageURL(path);
      if (url!=null)
      {
        try
        {
          img = ImageIO.read(url);
        }
        catch(IOException ioe)
        {
          _logger.error("Image loading error for ["+path+"]",ioe);
        }
      }
      else
      {
        _logger.error("Image not found: "+path);
      }
    }
    return img;
  }

  /**
   * Get an icon by name.
   * @param iconPath Icon path.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getIcon(String iconPath)
  {
    ImageIcon icon=null;
    if (iconPath!=null)
    {
      icon=_icons.get(iconPath);
      if (icon==null)
      {
        URL iconURL=getImageURL(iconPath);
        if (iconURL!=null)
        {
          icon=new ImageIcon(iconURL);
          _icons.put(iconPath,icon);
        }
        else
        {
          _logger.error("Icon not found: "+iconPath);
        }
      }
    }
    return icon;
  }

  /**
   * Get the icon for an item.
   * @param iconId Foreground icon ID.
   * @param backgroundId Background icon ID.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getItemIcon(String iconId, String backgroundId)
  {
    String path="/icons/"+iconId+"-"+backgroundId+".png";
    return getIcon(path);
  }
}
