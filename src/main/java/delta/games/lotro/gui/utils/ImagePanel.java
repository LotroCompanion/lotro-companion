package delta.games.lotro.gui.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * Panel that shows an image.
 * @author DAM
 */
public class ImagePanel extends JPanel
{
  private Image img;

  /**
   * Constructor.
   * @param image Image to display.
   */
  public ImagePanel(Image image)
  {
    this.img = image;
    Dimension size = new Dimension(image.getWidth(null), image.getHeight(null));
    setPreferredSize(size);
    setMinimumSize(size);
    setMaximumSize(size);
    setSize(size);
    setLayout(null);
  }

  @Override
  public void paintComponent(Graphics g)
  {
    Color background=getBackground();
    if (background!=null)
    {
      g.setColor(getBackground());
      g.fillRect(0, 0, getWidth(),getHeight());
    }
    g.drawImage(img, 0, 0, null);
  }
}