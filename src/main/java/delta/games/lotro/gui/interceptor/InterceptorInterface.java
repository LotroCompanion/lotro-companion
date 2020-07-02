package delta.games.lotro.gui.interceptor;

import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;

/**
 * Utility methods related to the 'interceptor/synchronizer' feature.
 * @author DAM
 */
public class InterceptorInterface
{
  /**
   * Check if the 'interceptor' feature is present or not.
   * @return <code>true</code> if it is, <code>false</code> otherwise.
   */
  public static boolean checkInterceptorPresence()
  {
    String className="delta.games.lotro.interceptor.data.LotroPacket";
    Class<?> clazz=null;
    try
    {
      clazz=Class.forName(className);
    }
    catch(Exception e)
    {
      // Ignored
    }
    return clazz!=null;
  }

  /**
   * Invoke the synchronizer UI.
   * @param windowsManager Parent window manager.
   * @param parent Parent window.
   */
  public static void doSynchronizer(WindowsManager windowsManager, WindowController parent)
  {
    WindowController controller=windowsManager.getWindow(InterceptorDialogController.IDENTIFIER);
    if (controller==null)
    {
      controller=new InterceptorDialogController(parent);
      windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  /*
  private static WindowController buildInterceptorDialog(WindowController parent)
  {
    try
    {
      Class<?> clazz=Class.forName("")
    }
  }
  */
}
