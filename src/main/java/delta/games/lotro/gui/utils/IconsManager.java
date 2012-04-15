package delta.games.lotro.gui.utils;

import java.net.URL;
import java.util.HashMap;

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
  private static final HashMap<String,ImageIcon> _icons=new HashMap<String,ImageIcon>();

  private static URL getIconURL(String iconPath)
  {
    URL imageURL=IconsManager.class.getResource(iconPath);
    return imageURL;
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
        URL iconURL=getIconURL(iconPath);
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
}
