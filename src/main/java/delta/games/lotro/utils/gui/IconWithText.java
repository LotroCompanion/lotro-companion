package delta.games.lotro.utils.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Icon with text display on bottom right.
 * @author DAM
 */
public class IconWithText implements Icon
{
  private ImageIcon _icon;
  private Font _font;
  private String _text;
  private Color _color;

  /**
   * Constructor.
   * @param icon Embedded icon.
   * @param font Font to use for text.
   * @param text Text to display.
   * @param color Color to use for text.
   */
  public IconWithText(ImageIcon icon, Font font, String text, Color color)
  {
    _icon=icon;
    _font=font;
    _text=text;
    _color=color;
  }

  public void paintIcon(Component c, Graphics g, int x, int y)
  {
    _icon.paintIcon(c,g,x,y);
    FontMetrics metrics=g.getFontMetrics(_font);
    Rectangle2D r=metrics.getStringBounds(_text,g);

    int dx = (int)(getIconWidth() - r.getWidth()) - 1;
    int dy = getIconHeight() - metrics.getDescent() - 1;
    g.setColor(_color);
    g.drawString(_text, dx, dy);
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
