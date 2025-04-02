package delta.games.lotro.stats.deeds.statistics.curves;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.character.status.achievables.AchievableStatusDateComparator;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.io.DeedsStatusIo;
import delta.games.lotro.common.rewards.ItemReward;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.gui.character.status.curves.DatedCurveProvider;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.utils.charts.DatedCurve;

/** 
 * Builds dated curves from deeds statistics.
 * @author DAM
 */
public class DeedCurvesBuilder implements DatedCurveProvider<CharacterFile>
{
  private int getValueFromRewards(DeedDescription deed, int itemIdToUse)
  {
    int value=0;
    Rewards rewards=deed.getRewards();
    List<ItemReward> itemRewards=rewards.getRewardElementsOfClass(ItemReward.class);
    for(ItemReward itemReward : itemRewards)
    {
      Item rewardedItem=itemReward.getItem();
      int itemId=rewardedItem.getIdentifier();
      int itemsCount=itemReward.getQuantity();
      if (itemId==itemIdToUse)
      {
        value+=itemsCount;
      }
    }
    return value;
  }

  private int getValue(DeedDescription deed, int itemId)
  {
    if (itemId==0)
    {
      // LP
      return deed.getRewards().getLotroPoints();
    }
    return getValueFromRewards(deed,itemId);
  }

  @Override
  public DatedCurve<?> getCurve(CharacterFile toon)
  {
    int type=0; // type could be WellKnownItems.MARK or WellKnownItems.MEDALLION
    String name=toon.getName();
    DatedCurve<Integer> curve=new DatedCurve<Integer>(name);
    AchievablesStatusManager deedsStatus=DeedsStatusIo.load(toon);
    List<AchievableStatus> datedStatuses=getDeedsStatus(deedsStatus);
    DeedsManager deeds=DeedsManager.getInstance();
    int totalValue=0;
    Long currentDate=null;
    for(AchievableStatus datedStatus : datedStatuses)
    {
      int deedId=datedStatus.getAchievableId();
      DeedDescription deed=deeds.getDeed(deedId);
      if (deed!=null)
      {
        int value=getValue(deed,type);
        if (value!=0)
        {
          totalValue+=value;
          Long deedDate=datedStatus.getCompletionDate();
          if (!deedDate.equals(currentDate))
          {
            curve.addValue(deedDate.longValue(),Integer.valueOf(totalValue));
            currentDate=deedDate;
          }
        }
      }
    }
    return curve;
  }

  private List<AchievableStatus> getDeedsStatus(AchievablesStatusManager deedsStatus)
  {
    List<AchievableStatus> all=deedsStatus.getAll();
    List<AchievableStatus> datedStatuses=new ArrayList<AchievableStatus>();
    for(AchievableStatus status : all)
    {
      if (status.getCompletionDate()!=null)
      {
        datedStatuses.add(status);
      }
    }
    Collections.sort(datedStatuses,new AchievableStatusDateComparator());
    return datedStatuses;
  }
}
