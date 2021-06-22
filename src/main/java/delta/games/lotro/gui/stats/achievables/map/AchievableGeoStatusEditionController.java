package delta.games.lotro.gui.stats.achievables.map;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.achievables.edition.AchievableGeoStatusManager;

/**
 * Controller for a panel to edit an achievable geographic status.
 * @author DAM
 */
public class AchievableGeoStatusEditionController
{
  private WindowController _parent;
  private AchievableGeoStatusManager _mgr;
  private GeoAchievableMapWindowController _mapController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param geoStatusManager Geo status manager.
   */
  public AchievableGeoStatusEditionController(WindowController parent, AchievableGeoStatusManager geoStatusManager)
  {
    _parent=parent;
    _mgr=geoStatusManager;
  }

  /**
   * Update UI from data.
   */
  public void updateUi()
  {
    _mgr.updateManagersFromStatus();
    // Maps
    if (_mapController!=null)
    {
      _mapController.updateUi();
    }
  }

  /**
   * Show maps.
   */
  public void showMaps()
  {
    _mapController=new GeoAchievableMapWindowController(_parent,_mgr);
    _mapController.updateUi();
    Window window=_mapController.getWindow();
    WindowAdapter l=new WindowAdapter()
    {
      public void windowClosed(WindowEvent e)
      {
        _mapController=null;
      }
    };
    window.addWindowListener(l);
    _mapController.show();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
    if (_mgr!=null)
    {
      _mgr.dispose();
      _mgr=null;
    }
    if (_mapController!=null)
    {
      _mapController.dispose();
      _mapController=null;
    }
  }
}
