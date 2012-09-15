package delta.games.lotro;

import java.util.Locale;

import javax.swing.JFrame;

import delta.games.lotro.gui.main.MainFrameController;

/**
 * Main for LOTRO companion.
 * @author DAM
 */
public class Main
{
  /**
   * Main method of LOTRO companion.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    Locale.setDefault(Locale.US);
    MainFrameController controller=new MainFrameController();
    JFrame frame=controller.getFrame();
    frame.setVisible(true);
  }
}
