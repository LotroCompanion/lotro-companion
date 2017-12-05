package delta.games.lotro.utils.gui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

/**
 * Custom fonts manager.
 * @author DAM
 */
public class FontsManager
{
  private static final Logger LOGGER=Logger.getLogger(FontsManager.class);

  private static final String FONT = "/resources/gui/fonts/firstv2.ttf";

  private static FontsManager instance=new FontsManager();

  private Font _font;

  /**
   * Get the sole instance of this class.
   * @return the fonts manager.
   */
  public static FontsManager getInstance()
  {
    return instance;
  }

  private FontsManager()
  {
    loadFont();
  }

  private void loadFont()
  {
    try {
      URL ufont=FontsManager.class.getResource(FONT);
      URLConnection con = ufont.openConnection();
      con.connect();
      InputStream is = con.getInputStream();
      Font f = Font.createFont(Font.TRUETYPE_FONT, is);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(f);
      String s = f.getFamily();
      //System.out.println(s);
      _font = new Font(s, Font.PLAIN, 12);
    }
    catch(Exception e)
    {
      LOGGER.warn("Could not load font "+FONT,e);
    }
  }

  /**
   * Get the custom font.
   * @return a font.
   */
  public Font getFont()
  {
    return _font;
  }
}
