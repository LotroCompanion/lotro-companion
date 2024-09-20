package delta.games.lotro.stats.deeds;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.reputation.FactionLevelStatus;
import delta.games.lotro.character.status.reputation.FactionStatus;
import delta.games.lotro.character.status.reputation.ReputationStatus;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionLevel;
import delta.games.lotro.lore.reputation.FactionsRegistry;

/**
 * Methods to synchronize the reputation status and the deeds status for a single character.
 * @author DAM
 */
public class SyncDeedsStatusAndReputationStatus
{
  private static final Logger LOGGER=LoggerFactory.getLogger(SyncDeedsStatusAndReputationStatus.class);

  /**
   * Update the deeds status to reflect the reputation status.
   * @param repStatus Source reputation status.
   * @param deedsStatus Target deeds status.
   */
  public static void syncDeedsStatus(ReputationStatus repStatus, AchievablesStatusManager deedsStatus)
  {
    List<Faction> factions=FactionsRegistry.getInstance().getAll();
    for(Faction faction : factions)
    {
      FactionStatus factionStatus=repStatus.getFactionStatus(faction);
      if (factionStatus==null)
      {
        continue;
      }
      FactionLevel currentLevel=factionStatus.getFactionLevel();
      if (currentLevel==null)
      {
        continue;
      }
      FactionLevel[] levels=faction.getLevels();
      for(FactionLevel level : levels)
      {
        String deedKey=level.getDeedKey();
        if (deedKey==null)
        {
          continue;
        }
        DeedDescription deed=DeedsManager.getInstance().getDeed(deedKey);
        if (deed==null)
        {
          continue;
        }
        boolean completed=factionStatus.isCompleted(level);
        if (completed)
        {
          AchievableStatus deedStatus=deedsStatus.get(deed,true);
          deedStatus.setCompleted(true);
          if (LOGGER.isDebugEnabled())
          {
            LOGGER.debug("Set deed "+deedKey+" to completed!");
          }
          FactionLevelStatus levelStatus=factionStatus.getStatusForLevel(level);
          long date=levelStatus.getCompletionDate();
          if (date!=0)
          {
            deedStatus.setCompletionDate(Long.valueOf(date));
          }
        }
        else
        {
          AchievableStatus deedStatus=deedsStatus.get(deed,false);
          if (deedStatus!=null)
          {
            deedStatus.setCompleted(false);
          }
        }
      }
    }
  }

  /**
   * Update the reputation status to reflect the deeds status.
   * @param deedsStatus Source deeds status.
   * @param repStatus Target reputation status.
   */
  public static void syncReputationStatus(AchievablesStatusManager deedsStatus, ReputationStatus repStatus)
  {
    List<Faction> factions=FactionsRegistry.getInstance().getAll();
    for(Faction faction : factions)
    {
      FactionStatus factionStatus=repStatus.getOrCreateFactionStat(faction);
      FactionLevel[] levels=faction.getLevels();
      for(FactionLevel level : levels)
      {
        String deedKey=level.getDeedKey();
        if (deedKey==null)
        {
          continue;
        }
        DeedDescription deed=DeedsManager.getInstance().getDeed(deedKey);
        if (deed==null)
        {
          continue;
        }
        Long date=null;
        AchievableStatus deedStatus=deedsStatus.get(deed,false);
        if ((deedStatus!=null) && (deedStatus.isCompleted()))
        {
          date=deedStatus.getCompletionDate();
        }
        if (date!=null)
        {
          FactionLevelStatus factionLevelStatus=factionStatus.getStatusForLevel(level);
          factionLevelStatus.setCompletionDate(date.longValue());
        }
      }
    }
  }
}
