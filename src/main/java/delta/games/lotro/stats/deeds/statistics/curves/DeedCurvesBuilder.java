package delta.games.lotro.stats.deeds.statistics.curves;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.character.status.achievables.AchievableStatusDateComparator;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.io.DeedsStatusIo;
import delta.games.lotro.gui.character.status.curves.DatedCurveProvider;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.utils.charts.DatedCurve;

/**
 * Builds dated curves from deeds statistics.
 * @author DAM
 */
public class DeedCurvesBuilder implements DatedCurveProvider<CharacterFile>
{
  @Override
  public DatedCurve<?> getCurve(CharacterFile toon)
  {
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
        int value=deed.getRewards().getLotroPoints();
        /*
        int value=0;
        Rewards rewards=deed.getRewards();
        ObjectsSet objects=rewards.getObjects();
        int nbItems=objects.getNbObjectItems();
        for(int i=0;i<nbItems;i++)
        {
          Proxy<Item> itemReward=objects.getItem(i);
          int itemId=itemReward.getId();
          int itemsCount=objects.getQuantity(i);
          // Marks
          if (itemId==WellKnownItems.MARK)
          {
            value+=itemsCount;
          }
          // Medallions
          if (itemId==WellKnownItems.MEDALLION)
          {
            value+=itemsCount;
          }
        }
        */
        if (value!=0)
        {
          totalValue+=value;
          Long deedDate=datedStatus.getCompletionDate();
          if (!deedDate.equals(currentDate))
          {
            curve.addValue(deedDate.longValue(),Integer.valueOf(totalValue));
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
