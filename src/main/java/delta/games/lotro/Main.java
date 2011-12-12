package delta.games.lotro;

import delta.games.lotro.gui.MainFrameController;

/**
 * Main for LOTRO companion.
 * @author DAM
 */
public class Main
{
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    MainFrameController controller=new MainFrameController();
    //NewToonDialogController controller=new NewToonDialogController();
    controller.show();
  }
}
