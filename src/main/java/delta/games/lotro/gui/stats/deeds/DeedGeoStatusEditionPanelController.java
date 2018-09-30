package delta.games.lotro.gui.stats.deeds;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;

import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.stats.deeds.map.GeoDeedMapWindowController;
import delta.games.lotro.lore.deeds.geo.DeedGeoData;
import delta.games.lotro.lore.deeds.geo.DeedGeoPoint;
import delta.games.lotro.maps.data.MapBundle;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.data.MarkersManager;
import delta.games.lotro.stats.deeds.geo.DeedGeoPointStatus;
import delta.games.lotro.stats.deeds.geo.DeedGeoStatus;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for a panel to edit a deed geographic status.
 * @author DAM
 */
public class DeedGeoStatusEditionPanelController
{
  private WindowController _parent;
  private DeedGeoData _geoData;
  private List<DeedGeoPointStatusGadgetsController> _gadgets;
  private GeoDeedMapWindowController _mapController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param geoData Geographic data of the deed.
   */
  public DeedGeoStatusEditionPanelController(WindowController parent,DeedGeoData geoData)
  {
    _parent=parent;
    _geoData=geoData;
    _gadgets=new ArrayList<DeedGeoPointStatusGadgetsController>();
    init();
  }

  private void init()
  {
    List<DeedGeoPoint> points=_geoData.getPoints();
    for(DeedGeoPoint point : points)
    {
      Marker marker=getMarker(point);
      final DeedGeoPointStatusGadgetsController gadgets=new DeedGeoPointStatusGadgetsController(marker);
      _gadgets.add(gadgets);
      // Add a listener on the checkbox to update the map
      final JCheckBox checkbox=gadgets.getCheckbox().getCheckbox();
      final int pointId=marker.getId();
      ActionListener l=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          boolean completed=checkbox.isSelected();
          updatePoint(pointId,completed);
        }
      };
      checkbox.addActionListener(l);
    }
  }

  private void updatePoint(int pointId, boolean completed)
  {
    if (_mapController!=null)
    {
      _mapController.setPointStatus(pointId,completed);
    }
  }

  /**
   * Show map.
   */
  public void showMap()
  {
    String mapKey=null;
    List<Marker> markers=new ArrayList<Marker>();
    List<DeedGeoPoint> points=_geoData.getPoints();
    for(DeedGeoPoint point : points)
    {
      Marker marker=getMarker(point);
      markers.add(marker);
      mapKey=point.getMapKey();
    }
    _mapController=new GeoDeedMapWindowController(_parent,mapKey,markers);
    // Update points status
    int nbPoints=points.size();
    for(int i=0;i<nbPoints;i++)
    {
      DeedGeoPointStatusGadgetsController gadgets=_gadgets.get(i);
      JCheckBox checkbox=gadgets.getCheckbox().getCheckbox();
      boolean completed=checkbox.isSelected();
      Marker marker=markers.get(i);
      int pointId=marker.getId();
      updatePoint(pointId,completed);
    }
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

  private Marker getMarker(DeedGeoPoint point)
  {
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    String mapKey=point.getMapKey();
    MapBundle map=mapsManager.getMapByKey(mapKey);
    MarkersManager markers=map.getData();
    int pointID=point.getPointId();
    Marker marker=markers.getPoint(pointID);
    return marker;
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
   * @param status Status data to show.
   */
  public void setStatusData(DeedGeoStatus status)
  {
    List<DeedGeoPointStatus> pointStatuses=status.getPointStatuses();
    for(DeedGeoPointStatus pointStatus : pointStatuses)
    {
      final int pointId=pointStatus.getPointId();
      DeedGeoPointStatusGadgetsController gadgets=getGadgets(pointId);
      Boolean isCompleted=pointStatus.isCompleted(); 
      final CheckboxController checkbox=gadgets.getCheckbox();
      checkbox.setSelected(isCompleted==Boolean.TRUE);
      Long completionDate=pointStatus.getCompletionDate();
      DateEditionController dateEditor=gadgets.getDateEditor();
      dateEditor.setDate(completionDate);
    }
  }

  /**
   * Get the status data from the UI contents.
   * @param status Storage for the status data.
   */
  public void updateGeoStatus(DeedGeoStatus status)
  {
    status.clear();
    for(DeedGeoPointStatusGadgetsController gadgets : _gadgets)
    {
      int pointID=gadgets.getPointID();
      boolean completed=gadgets.getCheckbox().isSelected();
      Long completionDate=gadgets.getDateEditor().getDate();
      DeedGeoPointStatus pointStatus=status.getStatus(pointID,true);
      pointStatus.setCompleted(Boolean.valueOf(completed));
      pointStatus.setCompletionDate(completionDate);
    }
  }

  private DeedGeoPointStatusGadgetsController getGadgets(int pointId)
  {
    DeedGeoPointStatusGadgetsController ret=null;
    for(DeedGeoPointStatusGadgetsController gadget : _gadgets)
    {
      if (gadget.getPointID()==pointId)
      {
        ret=gadget;
        break;
      }
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
    _geoData=null;
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
