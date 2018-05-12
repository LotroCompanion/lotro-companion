package delta.games.lotro.stats.deeds;

import java.util.List;

import delta.games.lotro.character.reputation.FactionLevelStatus;
import delta.games.lotro.character.reputation.FactionStatus;
import delta.games.lotro.character.reputation.ReputationStatus;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionLevel;
import delta.games.lotro.lore.reputation.FactionsRegistry;

/**
 * Methods to synchronize the reputation status and the deeds status for a single character.
 * @author DAM
 */
public class SyncDeedsStatusAndReputationStatus
{
  /**
   * Update the deeds status to reflect the reputation status.
   * @param repStatus Source reputation status.
   * @param deedsStatus Target deeds status.
   */
  public static void syncDeedsStatus(ReputationStatus repStatus, DeedsStatusManager deedsStatus)
  {
    List<Faction> factions=FactionsRegistry.getInstance().getAll();
    for(Faction faction : factions)
    {
      FactionStatus factionStatus=repStatus.getFactionStatus(faction);
      if (factionStatus!=null)
      {
        FactionLevel[] levels=faction.getLevels();
        for(FactionLevel level : levels)
        {
          String deedKey=level.getDeedKey();
          if (deedKey!=null)
          {
            boolean completed=false;
            FactionLevelStatus levelStatus=factionStatus.getStatusForLevel(level);
            if (levelStatus!=null)
            {
              completed=levelStatus.isCompleted();
            }
            if (completed)
            {
              DeedStatus deedStatus=deedsStatus.get(deedKey,true);
              deedStatus.setCompleted(Boolean.TRUE);
              //System.out.println("Set deed "+deedKey+" to completed!");
              long date=levelStatus.getCompletionDate();
              if (date!=0)
              {
                deedStatus.setCompletionDate(Long.valueOf(date));
              }
            }
            else
            {
              DeedStatus deedStatus=deedsStatus.get(deedKey,false);
              if (deedStatus!=null)
              {
                deedStatus.setCompleted(Boolean.FALSE);
                //System.out.println("Set deed "+deedKey+" to NOT completed!");
              }
            }
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
  public static void syncReputationStatus(DeedsStatusManager deedsStatus, ReputationStatus repStatus)
  {
    List<Faction> factions=FactionsRegistry.getInstance().getAll();
    for(Faction faction : factions)
    {
      boolean hasDeedKeys=false;
      FactionStatus factionStatus=repStatus.getOrCreateFactionStat(faction);
      FactionLevel[] levels=faction.getLevels();
      for(FactionLevel level : levels)
      {
        String deedKey=level.getDeedKey();
        if (deedKey!=null)
        {
          hasDeedKeys=true;
          boolean completed=false;
          Long date=null;
          DeedStatus deedStatus=deedsStatus.get(deedKey,false);
          if ((deedStatus!=null) && (deedStatus.isCompleted()==Boolean.TRUE))
          {
            completed=true;
            date=deedStatus.getCompletionDate();
          }
          FactionLevelStatus factionLevelStatus=factionStatus.getStatusForLevel(level);
          factionLevelStatus.setCompleted(completed);
          if (completed)
          {
            factionLevelStatus.setAcquiredXP(level.getRequiredXp());
          }
          else
          {
            factionLevelStatus.setCompletionDate(0);
          }
          if (date!=null)
          {
            factionLevelStatus.setCompletionDate(date.longValue());
          }
        }
      }
      if (hasDeedKeys)
      {
        factionStatus.updateCurrentLevel();
      }
    }
  }
}
