package delta.games.lotro.gui.stats.deeds.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import delta.games.lotro.character.achievables.AchievableElementState;
import delta.games.lotro.character.achievables.ObjectiveConditionStatus;
import delta.games.lotro.gui.stats.deeds.form.AchievableStatusBusinessRules;
import delta.games.lotro.lore.quests.geo.AchievableGeoPoint;
import delta.games.lotro.lore.quests.objectives.ObjectiveCondition;

/**
 * Manages the status of a single achievable condition.
 * @author DAM
 */
public class AchievableConditionStatusManager
{
  private static final Logger LOGGER=Logger.getLogger(AchievableConditionStatusManager.class);

  private ObjectiveConditionStatus _status;
  private List<AchievableStatusGeoItem> _items;
  private AchievableStatusBusinessRules _rules;

  /**
   * Constructor.
   * @param status Condition status to manage.
   * @param rules Business rules.
   */
  public AchievableConditionStatusManager(ObjectiveConditionStatus status, AchievableStatusBusinessRules rules)
  {
    _status=status;
    _rules=rules;
    buildItems();
    updateItemsFromStatus();
  }

  /**
   * Get the managed points.
   * @return the managed points.
   */
  public List<AchievableStatusGeoItem> getItems()
  {
    return _items;
  }

  private void buildItems()
  {
    _items=new ArrayList<AchievableStatusGeoItem>();
    ObjectiveCondition condition=_status.getCondition();
    List<AchievableGeoPoint> points=condition.getPoints();
    for(AchievableGeoPoint point : points)
    {
      AchievableStatusGeoItem item=new AchievableStatusGeoItem(point);
      _items.add(item);
    }
  }

  /**
   * Handle a request to change the completion state of an item.
   * @param itemToUpdate Item to update.
   * @param completed New completion state.
   */
  public void handlePointChange(AchievableStatusGeoItem itemToUpdate, boolean completed)
  {
    int index=_items.indexOf(itemToUpdate);
    if (index==-1)
    {
      return;
    }
    ObjectiveCondition objectiveCondition=_status.getCondition();
    int count=objectiveCondition.getCount();
    if (count==1)
    {
      // 'completed' status has to be set on all points with the same key or no key
      String key=itemToUpdate.getPoint().getKey();
      for(AchievableStatusGeoItem currentItem : _items)
      {
        String currentKey=currentItem.getPoint().getKey();
        if ((currentKey==null) || (key.equals(currentKey)))
        {
          currentItem.setCompleted(completed);
        }
      }
    }
    itemToUpdate.setCompleted(completed);
  }

  /**
   * Update the points from the current condition status.
   */
  public void updateItemsFromStatus()
  {
    AchievableElementState state=_status.getState();
    if ((state==AchievableElementState.COMPLETED) || (state==AchievableElementState.UNDEFINED))
    {
      boolean completed=(state==AchievableElementState.COMPLETED);
      for(AchievableStatusGeoItem item : _items)
      {
        item.setCompleted(completed);
      }
    }
    else if (state==AchievableElementState.UNDERWAY)
    {
      List<String> keys=_status.getKeys();
      for(AchievableStatusGeoItem item : _items)
      {
        if (keys!=null)
        {
          AchievableGeoPoint point=item.getPoint();
          String key=point.getKey();
          if (key!=null)
          {
            boolean completed=keys.contains(key);
            item.setCompleted(completed);
          }
        }
        else
        {
          item.setCompleted(false);
        }
      }
    }
    else
    {
      LOGGER.warn("Unmanaged achievable condition state: "+state);
    }
  }

  /**
   * Get the managed objective condition status.
   * @return an objective condition status.
   */
  public ObjectiveConditionStatus getStatus()
  {
    return _status;
  }

  /**
   * Update the condition status from the current status items.
   */
  public void updateStatusFromItems()
  {
    ObjectiveCondition condition=_status.getCondition();
    int expectedCount=condition.getCount();
    int completedCount=0;
    for(AchievableStatusGeoItem item : _items)
    {
      if (item.isCompleted())
      {
        completedCount++;
      }
    }
    _status.clearKeys();
    if (completedCount>=expectedCount)
    {
      _rules.setConditionState(AchievableElementState.COMPLETED,_status);
    }
    else
    {
      Set<String> keys=new HashSet<String>();
      for(AchievableStatusGeoItem item : _items)
      {
        String key=item.getPoint().getKey();
        if (key!=null)
        {
          if (item.isCompleted())
          {
            keys.add(key);
          }
        }
      }
      for(String key : keys)
      {
        _status.addKey(key);
      }
      _rules.setConditionState(AchievableElementState.UNDERWAY,_status);
      _rules.setConditionCount(Integer.valueOf(completedCount),_status);
    }
  }
}
