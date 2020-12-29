package delta.games.lotro.gui.stats.deeds.map;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.achievables.AchievableObjectiveStatus;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.character.achievables.ObjectiveConditionStatus;

/**
 * Achievable geo status manager.
 * @author DAM
 */
public class AchievableGeoStatusManager implements GeoPointChangeListener
{
  private GeoPointChangeListener _listener;
  private List<AchievableConditionStatusManager> _conditionsMgr;

  /**
   * Constructor.
   * @param status Status to use.
   * @param listener Listener for point state changes.
   */
  public AchievableGeoStatusManager(AchievableStatus status, GeoPointChangeListener listener)
  {
    _listener=listener;
    buildManagers(status);
  }

  private void buildManagers(AchievableStatus status)
  {
    _conditionsMgr=new ArrayList<AchievableConditionStatusManager>();
    List<AchievableObjectiveStatus> objectiveStatuses=status.getObjectiveStatuses();
    for(AchievableObjectiveStatus objectiveStatus : objectiveStatuses)
    {
      List<ObjectiveConditionStatus> conditionStatuses=objectiveStatus.getConditionStatuses();
      for(ObjectiveConditionStatus conditionStatus : conditionStatuses)
      {
        AchievableConditionStatusManager statusMgr=new AchievableConditionStatusManager(conditionStatus);
        _conditionsMgr.add(statusMgr);
      }
    }
  }

  /**
   * Get the points to edit.
   * @return a list of points.
   */
  public List<AchievableStatusGeoItem> getPoints()
  {
    List<AchievableStatusGeoItem> items=new ArrayList<AchievableStatusGeoItem>();
    for(AchievableConditionStatusManager conditionStatusMgr : _conditionsMgr)
    {
      items.addAll(conditionStatusMgr.getItems());
    }
    return items;
  }

  private AchievableConditionStatusManager getManager(AchievableStatusGeoItem point)
  {
    for(AchievableConditionStatusManager conditionStatusMgr : _conditionsMgr)
    {
      if (conditionStatusMgr.getItems().contains(point))
      {
        return conditionStatusMgr;
      }
    }
    return null;
  }

  /**
   * Update the conditions from the current objective status.
   */
  public void updateManagersFromStatus()
  {
    for(AchievableConditionStatusManager conditionStatusMgr : _conditionsMgr)
    {
      conditionStatusMgr.updateItemsFromStatus();
    }
  }

  @Override
  public void handlePointChange(AchievableStatusGeoItem point, boolean completed)
  {
    // Find manager
    AchievableConditionStatusManager statusManager=getManager(point);
    // Handle point change
    statusManager.handlePointChange(point,completed);
    // Update states
    statusManager.updateStatusFromItems();
    // Listener
    _listener.handlePointChange(point,completed);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _listener=null;
  }
}
