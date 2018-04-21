package delta.games.lotro.stats.deeds;

import delta.games.lotro.lore.reputation.Faction;

/**
 * Deed statistics for a single faction.
 * @author DAM
 */
public class FactionStatsFromDeeds
{
  private Faction _faction;
  private int _points;
  private int _deedsCount;

  /**
   * Constructor.
   * @param faction Managed faction.
   */
  public FactionStatsFromDeeds(Faction faction)
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
   * Add a reputation amount for a single deed.
   * @param amount Amount to add.
   */
  public void add(int amount)
  {
    _points+=amount;
    _deedsCount++;
  }

  /**
   * Get the total reputation points earned for this faction
   * by the registered deeds.
   * @return A points count.
   */
  public int getPoints()
  {
    return _points;
  }

  /**
   * Get the number of deeds used to build this reputation.
   * @return A deeds count.
   */
  public int getDeedsCount()
  {
    return _deedsCount;
  }
}
