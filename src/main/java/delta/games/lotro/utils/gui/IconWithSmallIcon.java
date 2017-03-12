package delta.games.lotro.utils.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Icon with text display on bottom right.
 * @author DAM
 */
public class IconWithSmallIcon implements Icon
{
  private ImageIcon _icon;
  private Image _smallIcon;

  /**
   * Constructor.
   * @param icon Embedded icon.
   * @param smallIcon Embedded icon.
   */
  public IconWithSmallIcon(ImageIcon icon, Image smallIcon)
  {
    _icon=icon;
    _smallIcon=smallIcon;
  }

  public void paintIcon(Component c, Graphics g, int x, int y)
  {
    _icon.paintIcon(c,g,x,y);

    if (_smallIcon!=null)
    {
      int dx = getIconWidth() - _smallIcon.getWidth(null);
      int dy = getIconHeight() - _smallIcon.getHeight(null);

      g.drawImage(_smallIcon,dx,dy,null);
    }
  }

  /**
   * Set small icon to display.
   * @param smallIcon Text to display.
   */
  public void setSmallIcon(Image smallIcon)
  {
    _smallIcon=smallIcon;
  }

  public int getIconWidth()
  {
    return _icon.getIconWidth();
  }

  public int getIconHeight()
  {
    return _icon.getIconHeight();
  }
}
