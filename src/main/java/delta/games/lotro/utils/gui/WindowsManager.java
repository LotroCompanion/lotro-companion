package delta.games.lotro.utils.gui;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collection;
import java.util.HashMap;

/**
 * Manages a set of windows.
 * @author DAM
 */
public class WindowsManager
{
  private HashMap<String,WindowController> _controllers;

  /**
   * Constructor.
   */
  public WindowsManager()
  {
    _controllers=new HashMap<String,WindowController>();
  }

  /**
   * Register a new window controller.
   * @param controller Window controller to register.
   */
  public void registerWindow(WindowController controller)
  {
    String id=controller.getWindowIdentifier();
    if ((id!=null) && (id.length()>0))
    {
      Window window=controller.getWindow();
      if (window!=null)
      {
        WindowListener wl=new WindowTracker(id);
        window.addWindowListener(wl);
        _controllers.put(id,controller);
      }
    }
  }

  /**
   * Tracks window closings.
   * @author DAM
   */
  public class WindowTracker extends WindowAdapter
  {
    private String _id;

    /**
     * Constructor.
     * @param id Window identifier.
     */
    public WindowTracker(String id)
    {
      _id=id;
    }

    public void windowClosed(WindowEvent e)
    {
      Window w=e.getWindow();
      w.removeWindowListener(this);
      _controllers.remove(_id);
    }
  }

  /**
   * Get the window controller for a given identifier.
   * @param identifier Identifier to search.
   * @return A window controller or <code>null</code> if not found.
   */
  public WindowController getWindow(String identifier)
  {
    WindowController controller=_controllers.get(identifier);
    return controller;
  }

  /**
   * Dispose all managed controllers.
   */
  public void disposeAll()
  {
    Collection<WindowController> controllers=_controllers.values();
    WindowController[] ctrls=controllers.toArray(new WindowController[controllers.size()]);
    for(WindowController controller : ctrls)
    {
      controller.dispose();
    }
  }
}
