package delta.games.lotro.gui.stats.deeds.form;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.character.achievables.AchievableStatusGeoItem;
import delta.games.lotro.character.achievables.AchievableStatusManager;
import delta.games.lotro.gui.stats.deeds.map.GeoAchievableMapWindowController;

/**
 * Controller for a panel to edit a deed geographic status.
 * @author DAM
 */
public class AchievableGeoStatusEditionController
{
  private WindowController _parent;
  private AchievableStatusManager _mgr;
  private GeoAchievableMapWindowController _mapController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to edit.
   */
  public AchievableGeoStatusEditionController(WindowController parent, AchievableStatus status)
  {
    _parent=parent;
    _mgr=new AchievableStatusManager(status);
  }

  /**
   * Handle a point state change.
   * @param point Targeted point.
   * @param completed New state.
   */
  public void handlePointChange(AchievableStatusGeoItem point, boolean completed)
  {
    _mgr.handlePointChange(point,completed);
    _mgr.updateStatusFromManagers();
    _mgr.updateManagersFromStatus();
    updateUiFromStatus();
  }

  private void updateUiFromStatus()
  {
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
    _mapController=new GeoAchievableMapWindowController(_parent,_mgr.getPoints());
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
    if (_mapController!=null)
    {
      _mapController.dispose();
      _mapController=null;
    }
  }
}
