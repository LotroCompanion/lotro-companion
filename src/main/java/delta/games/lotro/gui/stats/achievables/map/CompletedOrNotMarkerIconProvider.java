package delta.games.lotro.gui.stats.achievables.map;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import delta.common.ui.swing.icons.IconsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.ui.MarkerIconProvider;

/**
 * 'completed or not' marker icons provider.
 * @author DAM
 */
public class CompletedOrNotMarkerIconProvider implements MarkerIconProvider
{
  private HashMap<Boolean,BufferedImage> _markerIcons;
  private Set<Integer> _completedPoints;

  /**
   * Constructor.
   */
  public CompletedOrNotMarkerIconProvider()
  {
    _markerIcons=new HashMap<Boolean,BufferedImage>();
    BufferedImage completed=IconsManager.getImage("/resources/gui/icons/check32.png");
    _markerIcons.put(Boolean.TRUE,completed);
    BufferedImage notCompleted=IconsManager.getImage("/resources/gui/icons/delete32.png");
    _markerIcons.put(Boolean.FALSE,notCompleted);
    _completedPoints=new HashSet<Integer>();
  }

  @Override
  public BufferedImage getImage(Marker marker)
  {
    boolean completed=isCompleted(marker);
    return _markerIcons.get(Boolean.valueOf(completed));
  }

  /**
   * Set the completion status of a point.
   * @param pointId Identifier of the targeted point.
   * @param completed Completion status to set.
   */
  public void setCompleted(int pointId, boolean completed)
  {
    Integer pointIdKey=Integer.valueOf(pointId);
    if (completed)
    {
      _completedPoints.add(pointIdKey);
    }
    else
    {
      _completedPoints.remove(pointIdKey);
    }
  }

  private boolean isCompleted(Marker marker)
  {
    int id=marker.getId();
    return _completedPoints.contains(Integer.valueOf(id));
  }
}
