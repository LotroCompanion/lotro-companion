package delta.games.lotro.gui.stats.deeds.map;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.character.achievables.AchievableElementState;
import delta.games.lotro.character.achievables.AchievableObjectiveStatus;
import delta.games.lotro.character.achievables.AchievableStatusBusinessRules;
import delta.games.lotro.character.achievables.ObjectiveConditionStatus;

/**
 * Manages the status of a single objective.
 * @author DAM
 */
public class AchievableObjectiveStatusManager
{
  private static final Logger LOGGER=Logger.getLogger(AchievableObjectiveStatusManager.class);

  private AchievableObjectiveStatus _status;
  private List<AchievableConditionStatusManager> _managers;

  /**
   * Constructor.
   * @param status Objective status to manage.
   */
  public AchievableObjectiveStatusManager(AchievableObjectiveStatus status)
  {
    _status=status;
    buildManagers();
    updateManagersFromStatus();
  }

  /**
   * Get the condition status managers.
   * @return the condition status managers.
   */
  public List<AchievableConditionStatusManager> getManagers()
  {
    return _managers;
  }

  private void buildManagers()
  {
    _managers=new ArrayList<AchievableConditionStatusManager>();
    List<ObjectiveConditionStatus> conditionStatuses=_status.getConditionStatuses();
    for(ObjectiveConditionStatus conditionStatus : conditionStatuses)
    {
      AchievableConditionStatusManager statusMgr=new AchievableConditionStatusManager(conditionStatus);
      _managers.add(statusMgr);
    }
  }

  /**
   * Handle a request to change the completion state of an item.
   * @param itemToUpdate Item to update.
   * @param completed New completion state.
   */
  public void handlePointChange(AchievableStatusGeoItem itemToUpdate, boolean completed)
  {
    for(AchievableConditionStatusManager manager : _managers)
    {
      manager.handlePointChange(itemToUpdate,completed);
    }
  }

  /**
   * Update the conditions from the current objective status.
   */
  public void updateManagersFromStatus()
  {
    AchievableElementState state=_status.getState();
    if ((state==AchievableElementState.COMPLETED) || (state==AchievableElementState.UNDEFINED))
    {
      List<ObjectiveConditionStatus> conditionStatuses=_status.getConditionStatuses();
      int nbConditions=conditionStatuses.size();
      for(int i=0;i<nbConditions;i++)
      {
        ObjectiveConditionStatus conditionStatus=_status.getConditionStatus(i);
        conditionStatus.setState(state);
        _managers.get(i).updateItemsFromStatus();
      }
    }
    else if (state==AchievableElementState.UNDERWAY)
    {
      for(AchievableConditionStatusManager manager : _managers)
      {
        manager.updateItemsFromStatus();
      }
    }
    else
    {
      LOGGER.warn("Unmanaged achievable objective state: "+state);
    }
  }

  /**
   * Get the managed objective status.
   * @return an objective status.
   */
  public AchievableObjectiveStatus getStatus()
  {
    return _status;
  }

  /**
   * Update the objective status from the current condition managers status.
   */
  public void updateStatusFromManagers()
  {
    boolean allCompleted=true;
    boolean allNotCompleted=true;
    // Update status of conditions
    for(AchievableConditionStatusManager manager : _managers)
    {
      manager.updateStatusFromItems();
      ObjectiveConditionStatus objectiveConditionStatus=manager.getStatus();
      AchievableElementState state=objectiveConditionStatus.getState();
      if (state!=AchievableElementState.COMPLETED) allCompleted=false;
      if (state!=AchievableElementState.UNDEFINED) allNotCompleted=false;
    }
    // Update state...
    if (allCompleted)
    {
      AchievableStatusBusinessRules.setObjectiveState(AchievableElementState.COMPLETED,_status);
    }
    else if (allNotCompleted)
    {
      AchievableStatusBusinessRules.setObjectiveState(AchievableElementState.UNDEFINED,_status);
    }
    else
    {
      AchievableStatusBusinessRules.setObjectiveState(AchievableElementState.UNDERWAY,_status);
    }
  }
}
