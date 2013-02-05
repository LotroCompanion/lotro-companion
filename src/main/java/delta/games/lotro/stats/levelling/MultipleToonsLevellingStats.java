package delta.games.lotro.stats.levelling;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.stats.MultipleToonsStats;

/**
 * Levelling statistics for several toons.
 * @author DAM
 */
public class MultipleToonsLevellingStats extends MultipleToonsStats<LevellingStats>
{
  @Override
  protected LevellingStats loadToonStats(CharacterFile toon)
  {
    LevellingStats toonStats=null;
    CharacterLog log=toon.getLastCharacterLog();
    if (log!=null)
    {
      toonStats=new LevellingStats(log);
    }
    return toonStats;
  }
}
