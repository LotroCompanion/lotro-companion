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
  /**
   * Default font.
   */
  public static final Font DEFAULT_FONT=new Font(Font.DIALOG,Font.BOLD,12);

  /**
   * Position of text inside the icon.
   * @author DAM
   */
  public enum Position
  {
    /**
     * Bottom right.
     */
    BOTTOM_RIGHT,
    /**
     * Top left.
     */
    TOP_LEFT
  }

  private ImageIcon _icon;
  private String _text;
  private Color _color;
  private Position _position;

  /**
   * Constructor.
   * @param icon Embedded icon.
   * @param text Text to display.
   * @param color Color to use for text.
   */
  public IconWithText(ImageIcon icon, String text, Color color)
  {
    _icon=icon;
    _text=text;
    _color=color;
    _position=Position.BOTTOM_RIGHT;
  }

  /**
   * Set position to use.
   * @param position Position to use.
   */
  public void setPosition(Position position)
  {
    _position=position;
  }

  public void paintIcon(Component c, Graphics g, int x, int y)
  {
    _icon.paintIcon(c,g,x,y);

    if (_text.length()>0)
    {
      Font font=DEFAULT_FONT;
      g.setFont(font);
      FontMetrics metrics=g.getFontMetrics(font);
      Rectangle2D r=metrics.getStringBounds(_text,g);

      int dx;
      int dy;
      if (_position==Position.BOTTOM_RIGHT)
      {
        dx = (int)(getIconWidth() - r.getWidth()) - 2;
        dy = getIconHeight() - metrics.getDescent();
      }
      else
      {
        dx = 5;
        dy = metrics.getAscent() + 1;
      }
      dx+=x;
      dy+=y;

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
