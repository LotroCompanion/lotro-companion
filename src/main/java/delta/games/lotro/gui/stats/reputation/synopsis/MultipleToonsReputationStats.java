package delta.games.lotro.gui.stats.reputation.synopsis;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.reputation.ReputationData;
import delta.games.lotro.stats.MultipleToonsStats;

/**
 * Reputation data for several characters.
 * @author DAM
 */
public class MultipleToonsReputationStats extends MultipleToonsStats<ReputationData>
{
  @Override
  protected ReputationData loadToonStats(CharacterFile toon)
  {
    ReputationData reputation=toon.getReputation();
    return reputation;
  }
}
