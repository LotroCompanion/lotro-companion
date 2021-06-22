package delta.games.lotro.stats.achievables;

import delta.games.lotro.lore.reputation.Faction;

/**
 * Statistics for a single faction.
 * @author DAM
 */
public class FactionStatsFromAchievables
{
  private Faction _faction;
  private int _points;
  private int _achievablesCount;

  /**
   * Constructor.
   * @param faction Managed faction.
   */
  public FactionStatsFromAchievables(Faction faction)
  {
    _faction=faction;
  }

  /**
   * Get the managed faction.
   * @return a faction.
   */
  public Faction getFaction()
  {
    return _faction;
  }

  /**
   * Add a reputation amount for a single achievable.
   * @param amount Amount to add.
   */
  public void add(int amount)
  {
    _points+=amount;
    _achievablesCount++;
  }

  /**
   * Get the total reputation points earned for this faction
   * by the registered achievables.
   * @return A points count.
   */
  public int getPoints()
  {
    return _points;
  }

  /**
   * Get the number of achievables used to build this reputation.
   * @return A count.
   */
  public int getAchievablesCount()
  {
    return _achievablesCount;
  }
}
