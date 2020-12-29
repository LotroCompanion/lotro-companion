package delta.games.lotro.gui.stats.deeds.map;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.character.achievables.AchievableElementState;
import delta.games.lotro.character.achievables.AchievableObjectiveStatus;
import delta.games.lotro.character.achievables.AchievableStatus;

/**
 * Manages the status of a single achievable.
 * @author DAM
 */
public class AchievableStatusManager
{
  private static final Logger LOGGER=Logger.getLogger(AchievableStatusManager.class);

  private AchievableStatus _status;
  private List<AchievableObjectiveStatusManager> _managers;
  
  /**
   * Constructor.
   * @param status Achievable status to manage.
   */
  public AchievableStatusManager(AchievableStatus status)
  {
    _status=status;
    buildManagers();
    updateManagersFromStatus();
  }

  /**
   * Get the condition status managers.
   * @return the condition status managers.
   */
  public List<AchievableObjectiveStatusManager> getManagers()
  {
    return _managers;
  }

  private void buildManagers()
  {
    _managers=new ArrayList<AchievableObjectiveStatusManager>();
    List<AchievableObjectiveStatus> objectiveStatuses=_status.getObjectiveStatuses();
    for(AchievableObjectiveStatus objectiveStatus : objectiveStatuses)
    {
      AchievableObjectiveStatusManager statusMgr=new AchievableObjectiveStatusManager(objectiveStatus);
      _managers.add(statusMgr);
    }
  }

  /**
   * Get the points to edit.
   * @return a list of points.
   */
  public List<AchievableStatusGeoItem> getPoints()
  {
    List<AchievableStatusGeoItem> items=new ArrayList<AchievableStatusGeoItem>();
    for(AchievableObjectiveStatusManager statusMgr : _managers)
    {
      for(AchievableConditionStatusManager conditionStatusMgr : statusMgr.getManagers())
      {
        items.addAll(conditionStatusMgr.getItems());
      }
    }
    return items;
  }

  /**
   * Handle a request to change the completion state of an item.
   * @param itemToUpdate Item to update.
   * @param completed New completion state.
   */
  public void handlePointChange(AchievableStatusGeoItem itemToUpdate, boolean completed)
  {
    for(AchievableObjectiveStatusManager manager : _managers)
    {
      manager.handlePointChange(itemToUpdate,completed);
    }
  }

  /**
   * Update the objectives from the current achievable status.
   */
  public void updateManagersFromStatus()
  {
    AchievableElementState state=_status.getState();
    if ((state==AchievableElementState.COMPLETED) || (state==AchievableElementState.UNDEFINED))
    {
      List<AchievableObjectiveStatus> objectiveStatuses=_status.getObjectiveStatuses();
      int nbObjectives=objectiveStatuses.size();
      for(int i=0;i<nbObjectives;i++)
      {
        AchievableObjectiveStatus objectiveStatus=_status.getObjectiveStatus(i+1);
        objectiveStatus.setState(state);
        _managers.get(i).updateManagersFromStatus();
      }
    }
    else if (state==AchievableElementState.UNDERWAY)
    {
      for(AchievableObjectiveStatusManager manager : _managers)
      {
        manager.updateManagersFromStatus();
      }
    }
    else
    {
      LOGGER.warn("Unmanaged achievable state: "+state);
    }
  }

  /**
   * Update the achievable status from the current objective managers status.
   */
  public void updateStatusFromManagers()
  {
    boolean allCompleted=true;
    boolean allNotCompleted=true;
    // Update status of conditions
    for(AchievableObjectiveStatusManager manager : _managers)
    {
      manager.updateStatusFromManagers();
      AchievableObjectiveStatus objectiveStatus=manager.getStatus();
      AchievableElementState state=objectiveStatus.getState();
      if (state!=AchievableElementState.COMPLETED) allCompleted=false;
      if (state!=AchievableElementState.UNDEFINED) allNotCompleted=false;
    }
    // Update state...
    if (allCompleted) _status.setState(AchievableElementState.COMPLETED);
    else if (allNotCompleted) _status.setState(AchievableElementState.UNDEFINED);
    else _status.setState(AchievableElementState.UNDERWAY);
  }
}
