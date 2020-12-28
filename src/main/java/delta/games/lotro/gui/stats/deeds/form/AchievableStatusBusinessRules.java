package delta.games.lotro.gui.stats.deeds.form;

import delta.games.lotro.character.achievables.AchievableElementState;
import delta.games.lotro.character.achievables.AchievableObjectiveStatus;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.character.achievables.ObjectiveConditionStatus;
import delta.games.lotro.lore.quests.objectives.ObjectiveCondition;

/**
 * Implementation of business rules for achievable statuses.
 * @author DAM
 */
public class AchievableStatusBusinessRules
{
  /**
   * Set the state of an achiveable.
   * @param newState New state.
   * @param status Status to update.
   */
  public void setAchievableState(AchievableElementState newState, AchievableStatus status)
  {
    if (status.getState()==newState)
    {
      return;
    }
    status.setState(newState);
    if (newState==AchievableElementState.UNDEFINED)
    {
      for(AchievableObjectiveStatus objectiveStatus : status.getObjectiveStatuses())
      {
        setObjectiveState(AchievableElementState.UNDEFINED,objectiveStatus);
      }
    }
    else if (newState==AchievableElementState.UNDERWAY)
    {
      AchievableObjectiveStatus firstStatus=status.getObjectiveStatus(1);
      if (firstStatus!=null)
      {
        if (firstStatus.getState()==AchievableElementState.UNDEFINED)
        {
          setObjectiveState(AchievableElementState.UNDERWAY,firstStatus);
        }
      }
    }
    else if (newState==AchievableElementState.COMPLETED)
    {
      for(AchievableObjectiveStatus objectiveStatus : status.getObjectiveStatuses())
      {
        setObjectiveState(AchievableElementState.COMPLETED,objectiveStatus);
      }
    }
  }

  /**
   * Set the state of an objective.
   * @param newState New state.
   * @param status Status to update.
   */
  public void setObjectiveState(AchievableElementState newState, AchievableObjectiveStatus status)
  {
    if (status.getState()==newState)
    {
      return;
    }
    status.setState(newState);
    if (newState==AchievableElementState.UNDEFINED)
    {
      // Conditions are undefined
      for(ObjectiveConditionStatus conditionStatus : status.getConditionStatuses())
      {
        setConditionState(AchievableElementState.UNDEFINED,conditionStatus);
      }
      // Next objectives (if any) are undefined
      AchievableStatus parentStatus=status.getParentStatus();
      AchievableObjectiveStatus nextStatus=parentStatus.getNextStatus(status);
      while (nextStatus!=null)
      {
        setObjectiveState(AchievableElementState.UNDEFINED,nextStatus);
        nextStatus=parentStatus.getNextStatus(nextStatus);
      }
      // Previous is underway, at best
      AchievableObjectiveStatus previousStatus=status.getParentStatus().getPreviousStatus(status);
      if (previousStatus!=null)
      {
        if (previousStatus.getState()==AchievableElementState.COMPLETED)
        {
          setObjectiveState(AchievableElementState.UNDERWAY,previousStatus);
        }
      }
    }
    else if ((newState==AchievableElementState.UNDERWAY) || (newState==AchievableElementState.COMPLETED))
    {
      // Parent is underway, at least
      AchievableStatus parentStatus=status.getParentStatus();
      if (parentStatus.getState()==AchievableElementState.UNDEFINED)
      {
        setAchievableState(AchievableElementState.UNDERWAY,parentStatus);
      }
      // Conditions are underway, at least
      for(ObjectiveConditionStatus conditionStatus : status.getConditionStatuses())
      {
        if (conditionStatus.getState()==AchievableElementState.UNDEFINED)
        {
          setConditionState(AchievableElementState.UNDERWAY,conditionStatus);
        }
      }
      // Previous objectives are completed
      AchievableObjectiveStatus previousStatus=parentStatus.getPreviousStatus(status);
      while (previousStatus!=null)
      {
        setObjectiveState(AchievableElementState.COMPLETED,previousStatus);
        previousStatus=parentStatus.getPreviousStatus(previousStatus);
      }
      // Additional rules for underway state
      if (newState==AchievableElementState.UNDERWAY)
      {
        // Parent cannot be completed
        if (parentStatus.getState()==AchievableElementState.COMPLETED)
        {
          setAchievableState(AchievableElementState.UNDERWAY,parentStatus);
        }
      }
      // Additional rules for completed state
      if (newState==AchievableElementState.COMPLETED)
      {
        // Conditions are completed
        for(ObjectiveConditionStatus conditionStatus : status.getConditionStatuses())
        {
          setConditionState(AchievableElementState.COMPLETED,conditionStatus);
        }
        // Next objective (if any) is at least underway
        AchievableObjectiveStatus nextStatus=status.getParentStatus().getNextStatus(status);
        if (nextStatus!=null)
        {
          if (nextStatus.getState()==AchievableElementState.UNDEFINED)
          {
            setObjectiveState(AchievableElementState.UNDERWAY,nextStatus);
          }
        }
        // If all brothers are completed, then the parent objective is completed
        if (parentStatus.areObjectivesCompleted())
        {
          setAchievableState(AchievableElementState.COMPLETED,parentStatus);
        }
      }
    }
  }

  /**
   * Set the state of a condition.
   * @param newState New state.
   * @param status Status to update.
   */
  public void setConditionState(AchievableElementState newState, ObjectiveConditionStatus status)
  {
    if (status.getState()==newState)
    {
      return;
    }
    status.setState(newState);
    if (newState==AchievableElementState.UNDERWAY)
    {
      // Parent is underway
      setObjectiveState(AchievableElementState.UNDERWAY,status.getParentStatus());
    }
    else if (newState==AchievableElementState.COMPLETED)
    {
      // Update count
      ObjectiveCondition condition=status.getCondition();
      int count=condition.getCount();
      status.setCount(Integer.valueOf(count));
      // Parent is at least underway
      AchievableObjectiveStatus parent=status.getParentStatus();
      if (parent.getState()==AchievableElementState.UNDEFINED)
      {
        setObjectiveState(AchievableElementState.UNDERWAY,parent);
      }
      // If all brothers are completed, then the parent objective is completed
      if (parent.areConditionsCompleted())
      {
        setObjectiveState(AchievableElementState.COMPLETED,parent);
      }
    }
    else if (newState==AchievableElementState.UNDEFINED)
    {
      // Update count
      status.setCount(Integer.valueOf(0));
    }
  }

  /**
   * Set the count of a condition.
   * @param newCount New count.
   * @param status Status to update.
   */
  public void setConditionCount(Integer newCount, ObjectiveConditionStatus status)
  {
    ObjectiveCondition condition=status.getCondition();
    int count=condition.getCount();
    if (newCount!=null)
    {
      if (newCount.intValue()==count)
      {
        setConditionState(AchievableElementState.COMPLETED,status);
      }
      else
      {
        if ((newCount.intValue()>0) && (status.getState()==AchievableElementState.UNDEFINED))
        {
          setConditionState(AchievableElementState.UNDERWAY,status);
        }
        if (status.getState()==AchievableElementState.COMPLETED)
        {
          setConditionState(AchievableElementState.UNDERWAY,status);
        }
      }
    }
  }
}
