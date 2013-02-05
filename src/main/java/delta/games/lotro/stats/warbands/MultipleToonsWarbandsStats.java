package delta.games.lotro.stats.warbands;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.stats.MultipleToonsStats;

/**
 * Warbands statistics for several toons.
 * @author DAM
 */
public class MultipleToonsWarbandsStats extends MultipleToonsStats<WarbandsStats>
{
  protected WarbandsStats loadToonStats(CharacterFile toon)
  {
    CharacterLog log=toon.getLastCharacterLog();
    WarbandsStats stats=new WarbandsStats(log);
    return stats;
  }
}
