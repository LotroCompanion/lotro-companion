package delta.games.lotro.gui.utils;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * Panel that shows an image.
 * @author DAM
 */
public class BackgroundPanel extends JPanel
{
  private Image _image;

  /**
   * Constructor.
   * @param image Image to display.
   * @param layout Layout manager.
   */
  public BackgroundPanel(Image image, LayoutManager layout)
  {
    _image=image;
    setLayout(layout);
  }

  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    int width=getWidth();
    int height=getHeight();
    int imageWidth=_image.getWidth(null);
    int imageHeight=_image.getHeight(null);
    if ((imageHeight==-1) || (imageWidth==-1))
    {
      return;
    }
    int x=0;
    while (x<width) {
      int y=0;
      while (y<height) {
        g.drawImage(_image,x,y,null);
        y+=imageHeight;
      }
      x+=imageWidth;
    }
  }
}