package delta.games.lotro.gui.stats.deeds;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.text.dates.DateCodec;
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.games.lotro.maps.data.GeoPoint;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.utils.DateFormat;

/**
 * Controller for the UI gadgets of a single deed geo point.
 * @author DAM
 */
public class DeedGeoPointStatusGadgetsController
{
  // Data
  private Marker _marker;
  // Controllers
  private CheckboxController _completed;
  private DateEditionController _completionDate;

  /**
   * Constructor.
   * @param marker Marker to show.
   */
  public DeedGeoPointStatusGadgetsController(Marker marker)
  {
    _marker=marker;
    build();
  }

  /**
   * Get the identifier of the managed point.
   * @return a point identifier.
   */
  public int getPointID()
  {
    return _marker.getId();
  }

  /**
   * Get the 'completed' checkbox.
   * @return a checkbox controller.
   */
  public CheckboxController getCheckbox()
  {
    return _completed;
  }

  /**
   * Get the 'completion date' controller.
   * @return a date edition controller
   */
  public DateEditionController getDateEditor()
  {
    return _completionDate;
  }

  private void build()
  {
    // Completed
    String label=buildLabel();
    _completed=new CheckboxController(label);
    ActionListener l=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        setCompleted(_completed.isSelected());
      }
    };
    _completed.getCheckbox().addActionListener(l);
    // Completion date
    DateCodec codec=DateFormat.getDateTimeCodec();
    _completionDate=new DateEditionController(codec);
  }

  private String buildLabel()
  {
    String name=_marker.getLabel();
    GeoPoint point=_marker.getPosition();
    String pointStr=point.asString();
    return name+" ("+pointStr+")";
  }

  private void setCompleted(boolean completed)
  {
    _completionDate.setState(completed,completed);
  }


  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _marker=null;
    if (_completed!=null)
    {
      _completed.dispose();
      _completed=null;
    }
    if (_completionDate!=null)
    {
      _completionDate.dispose();
      _completionDate=null;
    }
  }
}
