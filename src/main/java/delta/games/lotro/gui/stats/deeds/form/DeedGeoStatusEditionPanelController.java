package delta.games.lotro.gui.stats.deeds.form;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;

import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.character.achievables.AchievableStatusGeoItem;
import delta.games.lotro.character.achievables.AchievableStatusManager;
import delta.games.lotro.gui.stats.deeds.map.GeoAchievableMapWindowController;

/**
 * Controller for a panel to edit a deed geographic status.
 * @author DAM
 */
public class DeedGeoStatusEditionPanelController
{
  private WindowController _parent;
  private AchievableStatusManager _mgr;
  private List<DeedGeoPointStatusGadgetsController> _gadgets;
  private GeoAchievableMapWindowController _mapController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to edit.
   */
  public DeedGeoStatusEditionPanelController(WindowController parent, AchievableStatus status)
  {
    _parent=parent;
    _gadgets=new ArrayList<DeedGeoPointStatusGadgetsController>();
    _mgr=new AchievableStatusManager(status);
    init();
  }

  private void init()
  {
    List<AchievableStatusGeoItem> points=_mgr.getPoints();
    for(final AchievableStatusGeoItem point : points)
    {
      final DeedGeoPointStatusGadgetsController gadgets=new DeedGeoPointStatusGadgetsController(point);
      _gadgets.add(gadgets);
      // Add a listener on the checkbox to update the map
      final JCheckBox checkbox=gadgets.getCheckbox().getCheckbox();
      ActionListener l=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          boolean completed=checkbox.isSelected();
          handlePointChange(point,completed);
        }
      };
      checkbox.addActionListener(l);
    }
  }

  private void handlePointChange(AchievableStatusGeoItem point, boolean completed)
  {
    _mgr.handlePointChange(point,completed);
    _mgr.updateStatusFromManagers();
    _mgr.updateManagersFromStatus();
    updateUiFromStatus();
  }

  private void updateUiFromStatus()
  {
    // Checkboxes
    setStatusData();
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
   * Get the gadgets controllers for the managed points.
   * @return a list of gadget controllers.
   */
  public List<DeedGeoPointStatusGadgetsController> getGadgets()
  {
    return _gadgets;
  }

  /**
   * Set the status data to show.
   */
  public void setStatusData()
  {
    List<AchievableStatusGeoItem> statusPoints=_mgr.getPoints();
    for(AchievableStatusGeoItem statusPoint : statusPoints)
    {
      DeedGeoPointStatusGadgetsController gadgets=getGadgets(statusPoint);
      boolean isCompleted=statusPoint.isCompleted(); 
      final CheckboxController checkbox=gadgets.getCheckbox();
      checkbox.setSelected(isCompleted);
      // TODO Handle date per point?
      /*
      Long completionDate=pointStatus.getCompletionDate();
      DateEditionController dateEditor=gadgets.getDateEditor();
      dateEditor.setDate(completionDate);
      */
    }
  }

  private DeedGeoPointStatusGadgetsController getGadgets(AchievableStatusGeoItem statusPoint)
  {
    int index=_mgr.getPoints().indexOf(statusPoint);
    return _gadgets.get(index);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
    if (_gadgets!=null)
    {
      for(DeedGeoPointStatusGadgetsController gadgets : _gadgets)
      {
        gadgets.dispose();
      }
      _gadgets.clear();
      _gadgets=null;
    }
    if (_mapController!=null)
    {
      _mapController.dispose();
      _mapController=null;
    }
  }
}
