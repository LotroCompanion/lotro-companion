package delta.games.lotro.gui.stats.deeds;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.text.dates.DateCodec;
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.games.lotro.character.achievables.AchievableStatusGeoItem;
import delta.games.lotro.lore.quests.geo.AchievableGeoPoint;
import delta.games.lotro.maps.data.GeoPoint;
import delta.games.lotro.utils.DateFormat;

/**
 * Controller for the UI gadgets of a single deed geo point.
 * @author DAM
 */
public class DeedGeoPointStatusGadgetsController
{
  // Data
  private AchievableStatusGeoItem _point;
  // Controllers
  private CheckboxController _completed;
  private DateEditionController _completionDate;

  /**
   * Constructor.
   * @param point Point to show.
   */
  public DeedGeoPointStatusGadgetsController(AchievableStatusGeoItem point)
  {
    _point=point;
    build();
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
    AchievableGeoPoint point=_point.getPoint();
    String name=point.getLabel();
    Point2D.Float position=point.getLonLat();
    GeoPoint geoPoint=new GeoPoint(position.x,position.y);
    String pointStr=geoPoint.asString();
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
    _point=null;
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
