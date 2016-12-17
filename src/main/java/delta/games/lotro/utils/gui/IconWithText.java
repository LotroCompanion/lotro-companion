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

    if (_text.length()>0)
    {
      FontMetrics metrics=g.getFontMetrics(_font);
      Rectangle2D r=metrics.getStringBounds(_text,g);

      int dx = (int)(getIconWidth() - r.getWidth()) - 2;
      int dy = getIconHeight() - metrics.getDescent();

      g.setColor(Color.BLACK);
      for(int i=dx-1;i<=dx+1;i++)
      {
        for(int j=dy-1;j<=dy+1;j++)
        {
          if ((i!=dx) || (j!=dy))
          {
            g.drawString(_text, i, j);
          }
        }
      }
      g.setColor(_color);
      g.drawString(_text, dx, dy);
    }
  }

  /**
   * Set text to display.
   * @param text Text to display.
   */
  public void setText(String text)
  {
    if (!_text.equals(text))
    {
      _text=text;
    }
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
