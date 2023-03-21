package delta.games.lotro.gui.character.status.achievables.map;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.achievables.edition.AchievableGeoStatusManager;

/**
 * Controller for a panel to edit an achievable geographic status.
 * @author DAM
 */
public class AchievableGeoStatusEditionController
{
  private WindowController _parent;
  private AchievableGeoStatusManager _mgr;
  private GeoAchievableMapWindowController _mapController;
  private boolean _editable;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param geoStatusManager Geo status manager.
   * @param editable Editable or not.
   */
  public AchievableGeoStatusEditionController(WindowController parent, AchievableGeoStatusManager geoStatusManager, boolean editable)
  {
    _parent=parent;
    _mgr=geoStatusManager;
    _editable=editable;
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
    if (_mapController!=null)
    {
      return;
    }
    _mapController=new GeoAchievableMapWindowController(_parent,_mgr,_editable);
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
